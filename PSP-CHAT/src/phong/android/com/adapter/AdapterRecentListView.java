package phong.android.com.adapter;

import java.util.List;

import phong.android.com.R;
import phong.android.com.entity.Recent;
import phong.android.com.utilities.AppController;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

@SuppressLint("InflateParams")
public class AdapterRecentListView extends BaseAdapter {

	private List<Recent> arrPeople;
	private LayoutInflater inflater;
	private Activity activity;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public AdapterRecentListView(Activity activity, List<Recent> list) {
		// TODO Auto-generated constructor stub
		this.arrPeople = list;
		this.activity = activity;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_item_fragment_recent,
					null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		NetworkImageView thumnaidAvatar = (NetworkImageView) convertView
				.findViewById(R.id.thumnailAvatarFriend);
		TextView tvUsername = (TextView) convertView
				.findViewById(R.id.tvUsernameRecent);
		TextView tvMessageRecent = (TextView) convertView
				.findViewById(R.id.tvMessageRecent);

		Recent people = arrPeople.get(position);

		thumnaidAvatar.setImageUrl(people.getUserLogin().getAvatar(),
				imageLoader);
		tvUsername.setText(people.getUserLogin().getUsersName());
		tvMessageRecent.setText(people.getUserLogin().getStatus());

		return convertView;
	}

}
