package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adapter.FragmentLocalAdapter;
import com.example.Activity.MainActivity;
import com.example.Activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Fragment_Local extends Fragment {
	private List<Map<String, Integer>> list;
	private ListView listview;
	private FragmentLocalAdapter adapter;
	private View footerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_local, null);
		// 初始化list
		init();
		// 找view
		findview(view);
		// 设置adapter或者listener
		setAdapterAndListener();
		return view;
	}

	/**
	 * 设置adapter或者listener
	 */
	public void setAdapterAndListener() {
		//添加footerview
		listview.addFooterView(footerView,null,false);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				itemClick(arg2);
			}
		});
	}

	/**
	 * 按钮点击
	 * @param position 点击的position
	 */
	public void itemClick(int position) {
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		//加入回退栈
		transaction.addToBackStack(MainActivity.BACKSTACK);
		//设置动画
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		switch (position) {
		//点击本地音乐
		case 0:
			Fragment_LocalList fl = new Fragment_LocalList();
			transaction.replace(R.id.activity_main_frameLayout, fl);
			break;
		//点击最近播放
		case 1:
			Fragment_Recently fr = new Fragment_Recently();
			transaction.replace(R.id.activity_main_frameLayout, fr);
			break;
		//点击下载管理
		case 2:
			Fragment_Download fd = new Fragment_Download();
			transaction.replace(R.id.activity_main_frameLayout, fd);
			break;
		}
		transaction.commit();
	}

	/**
	 * 找到相应view
	 * 
	 * @param view
	 */
	public void findview(View view) {
		listview = (ListView) view.findViewById(R.id.fragment_local_listview);
		footerView = getActivity().getLayoutInflater().inflate(R.layout.footerview_localmusiclistview, null);
	}

	/**
	 * 初始化List
	 */
	public void init() {
		list = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("title", R.string.localmusic);
		map.put("imageResourse", R.drawable.localmusicicon);
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		map2.put("title", R.string.recentplay);
		map2.put("imageResourse", R.drawable.recentplay);
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		map3.put("title", R.string.downloadmanager);
		map3.put("imageResourse", R.drawable.download);
		list.add(map);
		list.add(map2);
		list.add(map3);
		adapter = new FragmentLocalAdapter(getActivity(), list);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
