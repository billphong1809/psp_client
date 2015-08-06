package phong.android.com.adapter;

import java.util.ArrayList;

import phong.android.com.entity.Recent;
import phong.android.com.entity.UserLogin;
import phong.android.com.fragment.FragmentFavorite;
import phong.android.com.fragment.FragmentPeople;
import phong.android.com.fragment.FragmentRecent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterTabsPager extends FragmentPagerAdapter {

	Context mContext;
	FragmentFavorite fragmentFavorite;
	FragmentPeople fragmentPeopple;
	FragmentRecent fragmentRecent;

	public AdapterTabsPager(FragmentManager fm, FragmentRecent fragmentRecent,
			FragmentFavorite fragmentFavorite, FragmentPeople fragmentPeopple) {
		super(fm);
		this.fragmentRecent = fragmentRecent;
		this.fragmentFavorite = fragmentFavorite;
		this.fragmentPeopple = fragmentPeopple;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			return fragmentRecent;

		case 1:
			return fragmentFavorite;

		case 2:
			return fragmentPeopple;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	public void updateRecentFragment(Recent r) {
		this.fragmentRecent.updateList();
		notifyDataSetChanged();
	}

	public void updateRecent() {
		this.fragmentRecent.updateList();
		notifyDataSetChanged();
	}

	public void updatePeopleFragment(ArrayList<UserLogin> arr) {
		this.fragmentPeopple.updateList(arr);
		notifyDataSetChanged();
	}

}
