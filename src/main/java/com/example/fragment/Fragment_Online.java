package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.application.MyApplication;
import com.example.customview.NoScrollGridView;
import com.example.customview.NoScrollListView;
import com.example.pppppp.R;
import com.example.proxy.NetWork.CallBack_Net;
import com.example.utils.Utils;

public class Fragment_Online extends Fragment implements CallBack_Net {

	//private SwipeRefreshLayout mSwipeRefreshLayout;
	private NoScrollListView mNoScrollListView;
	private NoScrollGridView mNoScrollGridView;
	private MyApplication mMyApplication;
	
	private ViewPager mViewPager;
	private List<Fragment> list;

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 获取全局的application
		 */
		mMyApplication = (MyApplication) getActivity().getApplication();
		/**
		 * 进行网络操作,先将新歌榜获取
		 */
		//NetWork.NetForJson(NetWork.TYPE_NEWSONG, mMyApplication, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online, null);
		init();
		findview(view);
		setViewPagerAdapter();
		// 显示下拉刷新图标
		//showSwipeRefreshIcon();
		/*
		 * view.findViewById(R.id.btn).setOnClickListener(new OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(View arg0) { FragmentTransaction tran =
		 * getActivity().getSupportFragmentManager().beginTransaction();
		 * tran.addToBackStack(null);
		 * tran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		 * tran.replace(R.id.activity_main_frameLayout, new Fragment_Three());
		 * tran.commit(); } });
		 */
		// 1.获取网络数据
		return view;
	}
	
	//初始化
	private void init(){
		list = new ArrayList<Fragment>();
		Fragment categoryFragment = new Fragment_Online_Category();
		Fragment singerFragment = new Fragment_Online_Singer();
		list.add(categoryFragment);
		list.add(singerFragment);
	}

	// 找view
	private void findview(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.fragment_online_viewpager);
		//mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_online_swiperefreshlayout);
		/*mNoScrollListView = (NoScrollListView) view
				.findViewById(R.id.fragment_online_noscrolllistview);
		mNoScrollGridView = (NoScrollGridView) view
				.findViewById(R.id.fragment_online_noscrollgridview);*/
		//为listview设置headerview
		//setHeaderView();
	}
	
	//设置viewpager的adapter
	private void setViewPagerAdapter(){
		FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			
			@Override
			public int getCount() {
				return list.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};
		mViewPager.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 将队列中的所有请求全部移除
		removeRequestFromQueue();
	}

	/**
	 * 获取网络数据，返回的jsonobject对象
	 */
	@Override
	public void getJSONObject(JSONObject response, int type) {
		// 1.将下拉图标取消
		//cancleRefresh();
		// 2.根据type解析歌曲
	}

	/**
	 * 网络错误或者其他~~
	 */
	@Override
	public void onErrorResponse(VolleyError error) {
		// 将下拉图标取消
		//cancleRefresh();
		// 将队列中的所有请求全部移除
		removeRequestFromQueue();
		Utils.showToast(getActivity(), "网络出现问题，请稍后再试！");
	}

	/*
	 * 取消下拉刷新图标
	 */
	/*private void cancleRefresh() {
		if (mSwipeRefreshLayout.isRefreshing())
			mSwipeRefreshLayout.setRefreshing(false);
	}*/

	/**
	 * 设置显示下拉刷新图标
	 */
	/*private void showSwipeRefreshIcon() {
		// 设置刚开始就直接显示下拉
		mSwipeRefreshLayout.post(new Runnable() {

			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(true);
			}
		});
	}*/

	/**
	 * 将requestqueue中的请求全部移除
	 */
	public void removeRequestFromQueue() {
		// 如果此Fragment销毁了，将volley的未完成的网络操作全部取消
		if (mMyApplication != null)
			mMyApplication.getRequestQueue().cancelAll(getActivity());
	}
	
	/**
	 * 为listview添加headerview
	 */
	public void setHeaderView(){
		
	}
	
}
