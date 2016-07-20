package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adapter.FragmentOnlineSingerAdapter;
import com.example.application.MyApplication;
import com.example.bean.Singer;
import com.example.listener.MyScrollListener;
import com.example.pppppp.MainActivity;
import com.example.pppppp.R;
import com.example.proxy.AnaJson;
import com.example.utils.Utils;
import com.example.utils.VolleyUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Fragment_Online_Singer extends BaseFragment {

	private boolean isPrepared;

	private ProgressBar pb;
	private GridView gridview;
	private BaseAdapter adapter;
	private int offset = 0;
	private int limit = 50;
	private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=2.4.0&method=baidu.ting.artist.get72HotArtist&format=json&order=1&offset=";
	private RequestQueue queue;

	private Handler handler = new Handler();
	//下拉菜单
	private com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout mSwipyRefreshLayout;

	private ArrayList<Singer> list;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_online_singer, null);
		findviewsetadapter(view);
		setRefreshLayoutListener();
		isPrepared = true;
		return view;
	}
	
	//找view
	private void findviewsetadapter(View view){
		gridview = (GridView) view.findViewById(R.id.fragment_online_singer_gridview);
		pb = (ProgressBar) view.findViewById(R.id.fragment_online_singer_pb);
		MyApplication application = (MyApplication) getActivity().getApplication();
		queue = application.getRequestQueue();
		mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.fragment_online_singer_refreshLayout);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
	}

	/**
	 * 给RefreshLayout添加监听器
	 */
	private void setRefreshLayoutListener(){
		mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				//如果是上刷新
				if(direction==SwipyRefreshLayoutDirection.TOP){
					downPullAction();
				}else{
					upPullAction();
				}
			}
		});
	}

	/**
	 * 下拉刷新方法
	 */
	private void downPullAction(){
		offset = 0;
		limit = 50;
		getJson();
	}

	/**
	 * 上拉加载更多
	 */
	private void upPullAction(){
		offset = offset + 50;
		loadMore();
	}

	/**
	 * 连接网络获取数据加载更多
	 */
	private void loadMore(){
		String newUrl =  url+
				offset+"&limit="+limit;
		StringRequest request = new StringRequest(newUrl, new Listener<String>() {
			@Override
			public void onResponse(String s) {
				ArrayList<Singer> moreList = AnaJson.anaSingerJson(s);
				Fragment_Online_Singer.this.list.addAll(moreList);
				adapter.notifyDataSetChanged();
				if(mSwipyRefreshLayout.isRefreshing())
					mSwipyRefreshLayout.setRefreshing(false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});
		queue.add(request);
	}

	//获取json数据
	private void getJson(){
		String newUrl = url+
				offset+"&limit="+limit;
		//新建请求对象
		StringRequest singerRequest = new StringRequest(newUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				//解析json对象
				list = AnaJson.anaSingerJson(response);
				//设置数据
				setAdapter();
				//设置Listener
				setListener();
				if(mSwipyRefreshLayout.isRefreshing())
					mSwipyRefreshLayout.setRefreshing(false);
				handler.post(new Runnable() {
					@Override
					public void run() {
						pb.setVisibility(View.GONE);
					}
				});

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
		queue.add(singerRequest);
	}

	//设置adapter适配器
	private void setAdapter(){
		adapter = new FragmentOnlineSingerAdapter(getActivity(), list);
		gridview.setAdapter(adapter);
	}

    /**
     * 设置监听器
     */
	private void setListener(){
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_Singer mFragment_Singer = Fragment_Singer.newInstance(list,position);
                Utils.openNewFragment(tran,R.id.activity_main_frameLayout,mFragment_Singer);
			}
		});
	}

	@Override
	protected void lazyLoad() {
		if(!isPrepared||!isVisible||!isFirst)
			return;
		//下面执行数据加载
		isFirst = false;
		getJson();
	}

}
