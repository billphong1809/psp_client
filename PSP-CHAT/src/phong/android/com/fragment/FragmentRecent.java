package phong.android.com.fragment;

import java.util.ArrayList;
import java.util.List;

import phong.android.com.ActivityChatAllTabs;
import phong.android.com.R;
import phong.android.com.adapter.AdapterRecentListView;
import phong.android.com.entity.Recent;
import phong.android.com.utilities.DataSingleton;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class FragmentRecent extends Fragment {

	public static final String TAG = ActivityChatAllTabs.class.getSimpleName();
	private ListView lvRecent;
	private List<Recent> arrPeople = new ArrayList<Recent>();
	private AdapterRecentListView adapterPeople;
	private ProgressDialog pDialog;
	public Handler myHandler;
	ActivityChatAllTabs mContext;

	public FragmentRecent(ActivityChatAllTabs mContext) {
		this.mContext = mContext;
		this.arrPeople = DataSingleton.getArrRecent();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View rootView = inflater.inflate(R.layout.fragment_recent, null);
		lvRecent = (ListView) rootView.findViewById(R.id.lvRecent);

		adapterPeople = new AdapterRecentListView(getActivity(), arrPeople);

		lvRecent.setAdapter(adapterPeople);

		return rootView;
	}

	public void updateList() {
		this.arrPeople = DataSingleton.getArrRecent();
		if (adapterPeople == null) {
		} else {
			this.adapterPeople.notifyDataSetChanged();
		}

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
