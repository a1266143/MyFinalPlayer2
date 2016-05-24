package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.pppppp.R;
import com.example.utils.Utils;

public class Fragment_LocalList extends Fragment{

	private ImageButton returnbtn,menubtn;
	private ViewPager viewPager;
	private List<Fragment> list;
	private FragmentPagerAdapter pagerAdapter;
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locallist, null);
		init();
		//找到对应的view
		findview(view);
		//设置adapter和监听器
		setAdapterAndListener();
		return view;
	}
	
	/**
	 * 初始化
	 */
	public void init(){
		//这里要传入getChildFragmentManager,不然会出问题
		pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
		list = new ArrayList<Fragment>();
		Fragment_LocalMusic music = new Fragment_LocalMusic();
		Fragment album = new Fragment_Album();
		list.add(music);
		list.add(album);
	}
	
	/**
	 * 设置监听器和适配器
	 */
	public void setAdapterAndListener(){
		
		//返回按钮
		returnbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//移除当前fragment，弹出栈
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		//菜单按钮
		menubtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utils.showToast(getActivity(), "你点击了菜单按钮");
			}
		});
		
		viewPager.setAdapter(pagerAdapter);
	}
	
	/**
	 * 找view
	 * @param view
	 */
	public void findview(View view){
		returnbtn = (ImageButton) view.findViewById(R.id.fragment_locallist_return);
		menubtn = (ImageButton) view.findViewById(R.id.fragment_locallist_menu);
		viewPager = (ViewPager) view.findViewById(R.id.fragment_locallist_viewpager);
	}

	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return 2;
		}
		
	}

	
}
