package phong.android.com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import phong.android.com.adapter.AdapterTabsPager;
import phong.android.com.entity.MutiUsersAdd;
import phong.android.com.entity.Recent;
import phong.android.com.entity.UserLogin;
import phong.android.com.fragment.FragmentFavorite;
import phong.android.com.fragment.FragmentPeople;
import phong.android.com.fragment.FragmentRecent;
import phong.android.com.sqlites.DatabaseHandler;
import phong.android.com.utilities.DataSingleton;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

@SuppressLint({ "ResourceAsColor", "InflateParams" })
public class ActivityChatAllTabs extends FragmentActivity implements
		TabListener, OnNavigationListener {

	private ViewPager viewPaper;
	private AdapterTabsPager pagerAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "RECENT", "FAVORITE", "PEOPLE" };
	BroadcastReceiver broadcast;
	private FragmentPeople fragmentPeople;
	private FragmentFavorite fragmentFavorite;
	private FragmentRecent fragmentRecent;
	ArrayList<UserLogin> arr = new ArrayList<UserLogin>();
	private static final String TAG = ActivityChatAllTabs.class.getSimpleName();
	private String prefname = "my_data_chat";
	private UserLogin me;
	private UserLogin friend;
	public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";

	public Handler mHandler;
	public Thread handlerThread;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list_chat_friend);
		viewPaper = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setTitle("I am : " + DataSingleton.getMe().getUsersName());

		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		final DatabaseHandler dataHandler = new DatabaseHandler(this);
		ArrayList<UserLogin> arr = new ArrayList<UserLogin>();
		// List<UserLogin> arr2 = dataHandler.getAllUsers(DataSingleton.getMe()
		// .getUsersName());
		for (UserLogin userLogin : dataHandler.getAllUsers(DataSingleton
				.getMe().getUsersName())) {
			// Log.e(TAG + "222222222222222",
			// userLogin.getUsersName() + userLogin.getAvatar());
			arr.add(userLogin);
			// for (UserLogin u : arr) {
			// Log.e(TAG + "4444444444444444444444",
			// u.getAvatar() + arr.size());
			// }
		}
		DataSingleton.setArrFavorite(arr);
		// ArrayList<UserLogin> af = DataSingleton.getArrFavorite();
		// Log.e("xxx", "" + af.size());
		// Toast.makeText(getBaseContext(), "" + af.size(), Toast.LENGTH_LONG)
		// .show();
		DataSingleton.setArrRecent(new ArrayList<Recent>());

		fragmentRecent = new FragmentRecent(this);
		fragmentPeople = new FragmentPeople(this);
		fragmentFavorite = new FragmentFavorite(this);
		pagerAdapter = new AdapterTabsPager(getSupportFragmentManager(),
				fragmentRecent, fragmentFavorite, fragmentPeople);

		viewPaper.setAdapter(pagerAdapter);

		IntentFilter intentFillter = new IntentFilter();
		intentFillter.addAction("SEND_MESSAGE_TO_SERVER");
		intentFillter.addAction("SEND_DATA_ADD_FAVORITE");
		intentFillter.addAction("DELETE_FAVORITE_FRIEND");

		broadcast = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if (intent.getAction().equals("SEND_MESSAGE_TO_SERVER")) {
					try {
						String msg = intent.getStringExtra("msg");
						Log.e(TAG, msg);
						DataSingleton.sendToServer(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (intent.getAction().equals("SEND_DATA_ADD_FAVORITE")) {

					String usersname = intent.getStringExtra("username");
					String avatar = intent.getStringExtra("avatar");
					Log.e(TAG + "  |  " + avatar, avatar);

					UserLogin userlogin = DataSingleton.getUser(usersname);
					if (userlogin != null) {
						dataHandler.addUsers(DataSingleton.getMe()
								.getUsersName(), usersname, avatar);
						DataSingleton.addFavorite(userlogin);
					}
					pagerAdapter.notifyDataSetChanged();
				} else if (intent.getAction().equals("DELETE_FAVORITE_FRIEND")) {
					String usersname = intent.getStringExtra("usersname");
					Log.e(TAG + "  |  " + usersname, usersname);

					Log.e(TAG, usersname);
					dataHandler.deleteUsers(usersname);
				}
			}
		};
		registerReceiver(broadcast, intentFillter);
		mHandler = new Handler();
		handlerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ObjectInputStream streamFromServer = DataSingleton.getOis();
				try {
					while (true) {
						final String receivedData = (String) streamFromServer
								.readObject();

						final JSONObject jsonObject = new JSONObject(
								receivedData);
						String flag = jsonObject.getString("flag");

						Log.e(TAG + " | " + flag, flag);

						if (flag.equals("msg")) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {

									Log.e(TAG + " | " + receivedData,
											receivedData);
									handingMessageFromServer(receivedData);

								}
							});

						} else if (flag.equals("logout_success")) {
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {

										Intent intent = new Intent(
												ActivityChatAllTabs.this,
												ActivityFirstLogin.class);
										startActivity(intent);
										savingLoginPreferences(false);
										finish();
										String usersNameLogout = jsonObject
												.getString("usersName");
										Log.e(TAG + " | " + usersNameLogout,
												usersNameLogout);

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});

						} else if (flag.equals("failure")) {
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(),
											"PLEASE CHECK USERSNAME MY FRIEND",
											Toast.LENGTH_SHORT).show();

								}
							});

						} else if (flag.equals("add_success")) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									try {
										String me = jsonObject.getString("me");
										String friend = jsonObject
												.getString("friend");

										JSONObject jsonObjectMe = new JSONObject(
												me);
										JSONObject jsonObjectFriend = new JSONObject(
												friend);
										String meAdd = jsonObjectMe
												.getString("usersName");
										String friendAdd = jsonObjectFriend
												.getString("usersName");

										if (DataSingleton.getMe()
												.getUsersName()
												.equals(friendAdd)) {
											createNotificationMessageAdd(
													Calendar.getInstance()
															.getTimeInMillis(),
													meAdd);
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});

						} else if (flag.equals("done")) {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									try {
										String me = jsonObject.getString("me");
										String friend = jsonObject
												.getString("friend");
										JSONObject jsonObjectFriend = new JSONObject(
												friend);
										JSONObject jsonObjectMe = new JSONObject(
												me);
										String usersnameMe = jsonObjectMe
												.getString("usersName");
										String friendAdd = jsonObjectFriend
												.getString("usersName");
										Log.e("@@@@@@@@@@@@@@@@@@+9999999999999999999999999999999999999",
												friendAdd);
										if (DataSingleton.getMe()
												.getUsersName()
												.equals(friendAdd)) {
											showCustomDialogDone(usersnameMe);
										}
										// UserLogin usersLogin = new UserLogin(
										// friendAdd, null, null, null,
										// null);
										// DataSingleton.addPeople(usersLogin);
										// pagerAdapter.notifyDataSetChanged();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});

						} else if (flag.endsWith("change_success")) {
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									try {
										String vtLogin = jsonObject
												.getString("vtUsersLogin");
										Log.e(TAG, vtLogin);
										JSONArray jsonArray = new JSONArray(
												vtLogin);
										ArrayList<UserLogin> arrUsers = new ArrayList<UserLogin>();
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObjectArr = jsonArray
													.getJSONObject(i);
											String usersname = jsonObjectArr
													.getString("usersName");
											String avatar = jsonObjectArr
													.getString("avatar");
											String status = jsonObjectArr
													.getString("status");

											Log.e(TAG
													+ "zzzzzzzzzzzzzzzzzzzzzzzzzz",
													status);
											UserLogin usersLogin = new UserLogin(
													usersname, null, null,
													avatar, status);

											arrUsers.add(usersLogin);

										}

										DataSingleton.setArrUserLogin(arrUsers);
										updatePeopleList(arrUsers);
										pagerAdapter.notifyDataSetChanged();

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									Log.e(TAG
											+ "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr",
											receivedData.toString());

								}
							});

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		handlerThread.start();

		viewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcast);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPaper.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_friend:
			showCustomDialogAddFriend();
			return true;
		case R.id.change_status:
			showCustomDialogChangeStatus();
			return true;
		case R.id.logout:
			showCustomDialogConfirmLogout();
			return true;

		default:

			return super.onOptionsItemSelected(item);
		}
	}

	// HAM TRA VE GSON
	public Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;
	}

	// LOGOUT RA KHOI HE THONG

	public void logout() {

		UserLogin me = DataSingleton.getMe();

		try {
			UserLogin userLogin = new UserLogin(me.getUsersName(), null,
					"logout", null, null);

			String jsonLogout = getGson().toJson(userLogin);

			DataSingleton.sendToServer(jsonLogout);

			Log.e(TAG + " | " + jsonLogout, jsonLogout);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// LUU TRANG LOGIN

	public void savingLoginPreferences(boolean login) {
		SharedPreferences sp = getSharedPreferences(prefname, MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (login) {
			editor.putString("user", DataSingleton.getMe().getUsersName());
			editor.putString("pwd", DataSingleton.getMe().getPassWord());
			editor.putBoolean("login", true);
		} else {
			editor.clear();
		}
		editor.commit();
	}

	private void showCustomDialogConfirmLogout() {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_check_logout, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.tvTileCheckLogout);
		txtTitle.setText("LOGOUT");

		TextView txtTitleCheck = (TextView) dialog
				.findViewById(R.id.tvCheckLogout);
		txtTitleCheck.setText("ARE YOU SURE ? ");

		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok_check_logout);
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_check_logout);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logout();
				dialog.cancel();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		dialog.show();

	}

	private void showCustomDialogChangeStatus() {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater
				.inflate(R.layout.dialog_change_status, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.txt_dialog_title_status);
		txtTitle.setText("CHANGE STATUS");
		final EditText edText = (EditText) dialog.findViewById(R.id.edStatus);

		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok_status);
		Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_status);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String statusNew = edText.getText().toString();
				UserLogin usersLogin = new UserLogin(DataSingleton.getMe()
						.getUsersName(), null, "change_status", null, statusNew);

				String jsonNewStatus = getGson().toJson(usersLogin);

				DataSingleton.sendToServer(jsonNewStatus);
				Toast.makeText(getApplicationContext(), "PLEASE WAIT..",
						Toast.LENGTH_SHORT).show();

				dialog.cancel();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		dialog.show();

	}

	public void updatePeopleList(ArrayList<UserLogin> arr) {
		pagerAdapter.updatePeopleFragment(arr);
	}

	public void handingMessageFromServer(String receivedData) {

		try {
			JSONObject jsonObject = new JSONObject(receivedData);
			String msg = jsonObject.getString("message");
			String senderObjStr = jsonObject.getString("sender");
			JSONObject senderObj = new JSONObject(senderObjStr);
			String sender = senderObj.getString("usersName");
			String receivedObjStr = jsonObject.getString("received");
			JSONObject receivedObj = new JSONObject(receivedObjStr);
			String received = receivedObj.getString("usersName");
			Intent intent = new Intent("RECEIVED_FROM_SERVER");
			Bundle bundle = new Bundle();
			bundle.putString("msg", msg);
			bundle.putString("sender", sender);
			bundle.putString("received", received);

			intent.putExtras(bundle);

			sendBroadcast(intent);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			DataSingleton.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.finish();
	}

	// ---------------------------- 1 ADD FRIEND
	// ---------------------------------------------------------------HIEN THI
	// LEN DIALOG NHAP TEN NGUOI CAN ADD FRIEND

	private void showCustomDialogAddFriend() {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_add_friend, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.txt_dialog_title);
		txtTitle.setText("ADD FRIENDS");

		final EditText edInput = (EditText) dialog
				.findViewById(R.id.txt_dialog_message);
		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok_add);
		Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_add);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String inputAdd = edInput.getText().toString();
				me = new UserLogin(DataSingleton.getMe().getUsersName(), null,
						null, null, null);
				friend = new UserLogin(inputAdd, null, null, null, null);

				MutiUsersAdd mutiUserAdd = new MutiUsersAdd(me, friend,
						"add_friends");
				String jsonAddToServer = getGson().toJson(mutiUserAdd);

				DataSingleton.sendToServer(jsonAddToServer);
				Log.e(TAG + " | " + jsonAddToServer, jsonAddToServer);
				Toast.makeText(getApplicationContext(),
						"PLEASE WAIT " + inputAdd + " ACCEPT ! ",
						Toast.LENGTH_SHORT).show();

				dialog.cancel();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		dialog.show();

	}

	// 2 HIEN THI NOTIFICATION LEN CHO NGUOI BAN NHAN DUOC THONG BAO ADD FRIEND

	private void createNotificationMessageAdd(long when, String dataMeAll) {

		// String result[] = dataMeAll.split("[|]");
		// String dataMe = result[0];
		// // String avatar = result[1];
		// // String status = result[2];

		String notificationContent = "FROM PSP CHAT";
		String notificationTitle = "PSP NOTIFICATION ";

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_chat);
		int smallIcon = R.drawable.icon_chat;
		String notificationData = "DO YOU WANT ADD" + dataMeAll
				+ " TO MY FRIEND ? ";

		Intent intent = new Intent(getApplicationContext(),
				ActivityChatAllTabs.class);
		intent.putExtra(NOTIFICATION_DATA, notificationData);
		showCustomDialogConfirmAdd(dataMeAll);

		intent.setData(Uri.parse("content://" + when));
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder;
		builder = new Builder(getApplicationContext());
		builder.setWhen(when)
				.setContentText(notificationContent)
				.setContentTitle(notificationTitle)
				.setSmallIcon(smallIcon)
				.setAutoCancel(true)
				.setTicker(notificationTitle)
				.setLargeIcon(largeIcon)
				.setDefaults(
						Notification.DEFAULT_LIGHTS
								| Notification.DEFAULT_SOUND
								| Notification.DEFAULT_VIBRATE)
				.setContentIntent(pendingIntent);

		Notification notification = builder.build();

		notificationManager.notify((int) when, notification);
	}

	// HIEN THI DIALOG DE BAN XAC NHAN THONG TIN NGUOI ADD MINH LAM FRIEND

	private void showCustomDialogConfirmAdd(final String dataMeAll) {

		// String result[] = dataMeAll.split("[|]");
		// String dataMe = result[0];

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confirm_add_friend, null,
				false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.tvTitleCheckAddFriend);
		txtTitle.setText("ADD FRIEND");
		TextView txtTitle2 = (TextView) dialog
				.findViewById(R.id.tvCheckAddFriend);
		txtTitle2.setText("DO YOU WANT ADD " + dataMeAll + " TO MY FRIEND ? ");

		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok_check_add);
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_check_add);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// String avatarMe = DataSingleton.getMe().getAvatar();
				// String statusMe = DataSingleton.getMe().getStatus();
				me = new UserLogin(DataSingleton.getMe().getUsersName(), null,
						null, null, null);

				friend = new UserLogin(dataMeAll, null, null, null, null);

				MutiUsersAdd mutiAdd = new MutiUsersAdd(me, friend, "accept");

				String jsonAccept = getGson().toJson(mutiAdd);

				DataSingleton.sendToServer(jsonAccept);

				dialog.cancel();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		dialog.show();
	}

	// NGUOI GUI THONG TIN XAC NHAN ADD THANH CONG

	private void showCustomDialogDone(final String dataMe) {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confirm_add_done, null,
				false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.tvTitleCheckAddFriendDone);
		txtTitle.setText("ADD FRIEND");
		TextView txtTitle2 = (TextView) dialog
				.findViewById(R.id.tvCheckAddFriendDone);
		txtTitle2.setText(dataMe + " IS ACCEPTED TO MY FRIEND ? ");

		Button btnOK = (Button) dialog.findViewById(R.id.btn_ok_check_add_done);
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_check_add_done);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.cancel();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		dialog.show();

	}
}
