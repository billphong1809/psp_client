package phong.android.com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataSingleton;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityFirstLogin extends Activity implements OnClickListener {
	private Button btnLogin;
	public TextView tvLinkReg;
	private EditText edUsername;
	private EditText edPass;
	private ProgressDialog pDialog;
	private CheckBox chksaveaccount;
	private String prefname = "my_data";
	private Handler mHandler;
	private static final String TAG = ActivityFirstLogin.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_login);
		edUsername = (EditText) findViewById(R.id.username);
		edPass = (EditText) findViewById(R.id.password);

		chksaveaccount = (CheckBox) findViewById(R.id.chksaveacount);
		btnLogin = (Button) findViewById(R.id.btnLoginFirst);
		tvLinkReg = (TextView) findViewById(R.id.tvLinkReg);

		btnLogin.setOnClickListener(this);
		tvLinkReg.setOnClickListener(this);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		mHandler = new Handler();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLoginFirst:
			String usersname = edUsername.getText().toString();
			String pass = edPass.getText().toString();
			String flag = "login";
			UserLogin usersLogin = new UserLogin(usersname, pass, flag, null,
					null);
			String jsonLogin = getGson().toJson(usersLogin);
			Log.e(TAG, jsonLogin);
			LoginAsynTask loginAsynTask = new LoginAsynTask();
			loginAsynTask.execute(jsonLogin);

			break;
		case R.id.tvLinkReg:
			Intent i = new Intent(ActivityFirstLogin.this,
					ActivityRegistration.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}

	public Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;

	}

	class LoginAsynTask extends AsyncTask<String, Void, Void> {
		private Socket socket;
		public ObjectOutputStream streamToServer;
		public ObjectInputStream streamFromServer;
		public ArrayList<UserLogin> arrUserLogin = new ArrayList<UserLogin>();

		public void connnectToServer() {
			try {

				socket = new Socket("192.168.211.1", 1111);
				streamToServer = new ObjectOutputStream(
						socket.getOutputStream());
				streamFromServer = new ObjectInputStream(
						socket.getInputStream());

				DataSingleton.setOis(streamFromServer);
				DataSingleton.setOos(streamToServer);
				DataSingleton.setSocket(socket);

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(ActivityFirstLogin.this);
			pDialog.setMessage("Loading...");
			pDialog.show();
		}

		public void sendToServer(String obj) {
			try {
				streamToServer.writeObject(obj);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Client Write Error>>>>>>>>>>>>>" + e);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent i = new Intent(ActivityFirstLogin.this,
					ActivityChatAllTabs.class);
			startActivity(i);
			if (pDialog.isShowing())
				pDialog.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			connnectToServer();
			sendToServer(params[0]);
			try {
				final String receivedData = (String) streamFromServer
						.readObject();
				if (receivedData instanceof String) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							String message = (String) receivedData;

							String result[] = message.split("[|]");

							String messageUserLogin = result[1];
							String messageFriendOfUserLogin = result[0];
							Log.e(TAG, receivedData.toString());
							try {
								JSONObject jsonObjectUserLogin = new JSONObject(
										messageUserLogin);
								Log.e(TAG, messageUserLogin);
								String username = jsonObjectUserLogin
										.getString("usersName");
								UserLogin me = new UserLogin(username, null,
										null, null, null);
								DataSingleton.setMe(me);
								JSONObject jsonObject = new JSONObject(
										messageFriendOfUserLogin);
								Log.e(TAG, messageFriendOfUserLogin);
								String vtUserLogin = jsonObject
										.getString("vtUsersLogin");
								JSONArray jsonArray = new JSONArray(vtUserLogin);
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObjectArr = jsonArray
											.getJSONObject(i);
									String usersname = jsonObjectArr
											.getString("usersName");
									String avatar = jsonObjectArr
											.getString("avatar");
									String status = jsonObjectArr
											.getString("status");
									UserLogin usersLogin = new UserLogin(
											usersname, null, null, avatar,
											status);
									arrUserLogin.add(usersLogin);
									for (UserLogin u : arrUserLogin) {
										Log.e(TAG, u.getUsersName());
									}
								}
								// set people
								DataSingleton.setArrUserLogin(arrUserLogin);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Client Error" + e);
			}
			return null;

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		savingPreferences();
	}

	@Override
	protected void onResume() {

		super.onResume();
		restoringPreferences();
	}

	public void savingPreferences() {

		SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		String user = edUsername.getText().toString();
		String pwd = edPass.getText().toString();
		boolean bchk = chksaveaccount.isChecked();
		if (!bchk) {
			editor.clear();
		} else {
			editor.putString("user", user);
			editor.putString("pwd", pwd);
			editor.putBoolean("checked", bchk);
		}
		editor.commit();
	}

	public void restoringPreferences() {
		SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);

		boolean bchk = pre.getBoolean("checked", false);
		if (bchk) {
			String user = pre.getString("user", "");
			String pwd = pre.getString("pwd", "");
			edUsername.setText(user);
			edPass.setText(pwd);
		}
		chksaveaccount.setChecked(bchk);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

}
