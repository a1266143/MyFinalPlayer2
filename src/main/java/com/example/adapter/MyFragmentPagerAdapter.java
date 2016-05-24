package com.example.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mList;
	
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> mList) {
		super(fm);
		this.mList = mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

}
