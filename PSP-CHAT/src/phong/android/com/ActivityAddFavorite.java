package phong.android.com;

import java.util.ArrayList;

import phong.android.com.adapter.AdapterAddFriendFavorite;
import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataSingleton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAddFavorite extends Activity {

	public static final String TAG = ActivityAddFavorite.class.getSimpleName();
	private ListView lvFriendFavorite;
	private ArrayList<UserLogin> arrUsers = new ArrayList<UserLogin>();
	private AdapterAddFriendFavorite adapterPeople;
	public UserLogin me;
	public UserLogin friend;
	CheckBox cb;

	public ActivityAddFavorite() {
		this.arrUsers = DataSingleton.getArrUserLogin();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_activity_add_favorite);

		lvFriendFavorite = (ListView) findViewById(R.id.lvFriendFavorite);

		adapterPeople = new AdapterAddFriendFavorite(ActivityAddFavorite.this,
				arrUsers);

		lvFriendFavorite.setAdapter(adapterPeople);

		adapterPeople.notifyDataSetChanged();

		lvFriendFavorite
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						showCustomDialogSelectAdd(position);

						return false;
					}
				});

	}

	@SuppressLint("InflateParams")
	private void showCustomDialogSelectAdd(final int posititon) {

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_select_add_friend, null,
				false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog.findViewById(R.id.tvTitleSelect);

		TextView txtTitleSelect = (TextView) dialog
				.findViewById(R.id.tvAddFriendDialog);
		txtTitle.setText("PLEASE SELECT ");
		txtTitleSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCustomDialogCheckAdd(posititon);
				dialog.cancel();

			}
		});

		dialog.show();

	}

	public boolean isFavorite(UserLogin userLogin) {
		for (UserLogin p : DataSingleton.getArrFavorite()) {
			if (userLogin.getUsersName().equals(p.getUsersName()))
				return true;
		}
		return false;
	}

	@SuppressLint("InflateParams")
	public void showCustomDialogCheckAdd(final int position) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_check_add_friend, null,
				false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog.findViewById(R.id.tvTileCheckAdd);

		txtTitle.setText(" CONFIRM ");

		TextView txtTitleFriendAdd = (TextView) dialog
				.findViewById(R.id.tvCheckAdd);

		txtTitleFriendAdd.setText("ARE YOU SURE ? ");

		Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_check_add);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("SEND_DATA_ADD_FAVORITE");
				Bundle bundle = new Bundle();
				bundle.putString("username", arrUsers.get(position)
						.getUsersName());
				bundle.putString("avatar", arrUsers.get(position).getAvatar());
				Log.e(TAG, arrUsers.get(position).getUsersName());
				intent.putExtras(bundle);
				sendBroadcast(intent);
				Toast.makeText(
						getApplicationContext(),
						arrUsers.get(position).getUsersName()
								+ " ADD FRIEND SUCCESS ! ", Toast.LENGTH_SHORT)
						.show();

				dialog.cancel();

			}
		});
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_check_add);
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
