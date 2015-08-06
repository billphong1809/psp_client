package phong.android.com.adapter;

import java.util.ArrayList;
import java.util.List;

import phong.android.com.R;
import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.AppController;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AdapterAddFriendFavorite extends BaseAdapter {

	private List<UserLogin> arrPeople;
	private LayoutInflater inflater;
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	public CheckBox cbCheckBox;
	public static ArrayList<Boolean> arrCheck = new ArrayList<Boolean>();

	public AdapterAddFriendFavorite(Activity activity, List<UserLogin> list) {
		this.activity = activity;
		this.arrPeople = list;
		for (int i = 0; i < arrPeople.size(); i++) {
			arrCheck.add(false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrPeople.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrPeople.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void resetCheck() {
		for (int i = 0; i < arrCheck.size(); i++) {
			arrCheck.set(i, false);
		}
	}

	public void checkAll() {
		Boolean c = !arrCheck.get(0);
		for (int i = 0; i < arrCheck.size(); i++) {
			arrCheck.set(i, c);
		}
	}

	public static ArrayList<Boolean> getArrCheck() {
		return arrCheck;
	}

	public void check(int p) {
		arrCheck.set(p, !arrCheck.get(p));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_item_add_friend, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		NetworkImageView thumnaidAvatar = (NetworkImageView) convertView
				.findViewById(R.id.thumnailAvatarFriendAdd);
		TextView tvUsername = (TextView) convertView
				.findViewById(R.id.tvUsernameAdd);

//		CheckBox cbCheckBox = (CheckBox) convertView.findViewById(R.id.cbAdd);

		UserLogin people = arrPeople.get(position);

		thumnaidAvatar.setImageUrl(people.getAvatar(), imageLoader);
		tvUsername.setText(people.getUsersName());
//		cbCheckBox.setChecked(arrCheck.get(position));

		return convertView;
	}

}
