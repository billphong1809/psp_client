package phong.android.com.fragment;

import java.util.ArrayList;
import java.util.List;

import phong.android.com.ActivityAddFavorite;
import phong.android.com.ActivityChatAllTabs;
import phong.android.com.R;
import phong.android.com.adapter.AdapterFavoriteListView;
import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataSingleton;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class FragmentFavorite extends Fragment {

	public static final String TAG = ActivityChatAllTabs.class.getSimpleName();
	private ListView lvFavorite;
	private List<UserLogin> arrPeople = new ArrayList<UserLogin>();
	private AdapterFavoriteListView adapterPeople;
	public ActivityChatAllTabs mContext;
	private ImageView btnAdd;

	public FragmentFavorite(ActivityChatAllTabs context) {
		this.mContext = context;
		this.arrPeople = DataSingleton.getArrFavorite();
//		for (UserLogin u : arrPeople) {
//			Log.e(TAG + "00000000000000000000000000000000000", u.getAvatar()
//					+ arrPeople.size());
//			Log.e(TAG + "00000000000000000000000000000000000", u.getUsersName());
//		}
//		Log.e("0000000000000000000000000", "000000000000000");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater
				.inflate(R.layout.fragment_favorite, null);
		lvFavorite = (ListView) rootView.findViewById(R.id.lvFavorite);
		btnAdd = (ImageView) rootView.findViewById(R.id.imgAdd);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ActivityAddFavorite.class);
				startActivity(intent);
			}
		});
		adapterPeople = new AdapterFavoriteListView(getActivity(), arrPeople);
		lvFavorite.setAdapter(adapterPeople);

		lvFavorite.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				showCustomDialogSelectDelete(position);
				return false;
			}

		});
		adapterPeople.notifyDataSetChanged();
		return rootView;
	}

	public void updateList() {
		this.arrPeople = DataSingleton.getArrFavorite();
		if (adapterPeople != null)
			this.adapterPeople.notifyDataSetChanged();
	}

	private void showCustomDialogSelectDelete(final int posititon) {

		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater
				.inflate(R.layout.dialog_select_delete, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.tvTitleSelectDelete);

		TextView txtTitleSelect = (TextView) dialog
				.findViewById(R.id.tvDialogDelete);
		txtTitle.setText("PLEASE SELECT ");
		txtTitleSelect.setText("DELETE FRIEND");
		txtTitleSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCustomDialogCheckDelete(posititon);
				dialog.cancel();

			}
		});

		dialog.show();

	}

	@SuppressLint("InflateParams")
	public void showCustomDialogCheckDelete(final int position) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_check_delete, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.tvTileCheckDelete);

		txtTitle.setText(" CONFIRM ");

		TextView txtTitleFriendAdd = (TextView) dialog
				.findViewById(R.id.tvCheckDelete);

		txtTitleFriendAdd.setText("ARE YOU SURE ? ");

		Button btnOk = (Button) dialog.findViewById(R.id.btn_ok_check_delete);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent("DELETE_FAVORITE_FRIEND");
				Bundle bundle = new Bundle();
				bundle.putString("usersname", arrPeople.get(position)
						.getUsersName());

				intent.putExtras(bundle);

				getActivity().sendBroadcast(intent);

				Log.e(TAG + "4444444444444444444", arrPeople.get(position)
						.getUsersName());

				dialog.cancel();

			}
		});
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btn_cancel_check_delete);
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
