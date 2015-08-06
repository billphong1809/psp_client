package phong.android.com.fragment;

import java.util.ArrayList;

import phong.android.com.ActivityChatAllTabs;
import phong.android.com.ActivityChatMain;
import phong.android.com.R;
import phong.android.com.adapter.AdapterPeopleListView;
import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataSingleton;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class FragmentPeople extends Fragment {

	public static final String TAG = ActivityChatAllTabs.class.getSimpleName();
	private ListView lvPeople;
	private ArrayList<UserLogin> arrUsers = new ArrayList<UserLogin>();
	private AdapterPeopleListView adapterPeople;
	public ActivityChatAllTabs mContext;

	public FragmentPeople(ActivityChatAllTabs mContext) {
		this.mContext = mContext;
		this.arrUsers = DataSingleton.getArrUserLogin();
//		for (UserLogin u : arrUsers) {
//			Log.e(TAG, u.getUsersName()
//					+ DataSingleton.getArrUserLogin().size());
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_people, null);

		lvPeople = (ListView) rootView.findViewById(R.id.lvPeople);

		adapterPeople = new AdapterPeopleListView(getActivity(), arrUsers);

		lvPeople.setAdapter(adapterPeople);

		adapterPeople.notifyDataSetChanged();

		lvPeople.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						ActivityChatMain.class);
				Bundle bundle = new Bundle();
				bundle.putString("username", arrUsers.get(position)
						.getUsersName());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		return rootView;
	}

	public ArrayList<UserLogin> getmArray() {
		return arrUsers;
	}

	public void updateList(ArrayList<UserLogin> arr) {
		this.arrUsers = arr;
		this.adapterPeople.notifyDataSetChanged();
	}
}
