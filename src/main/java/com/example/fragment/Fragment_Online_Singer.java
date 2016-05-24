package com.example.fragment;

import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adapter.FragmentOnlineSingerAdapter;
import com.example.application.MyApplication;
import com.example.bean.Singer;
import com.example.pppppp.R;
import com.example.proxy.AnaJson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class Fragment_Online_Singer extends BaseFragment {

	private boolean isPrepared;
	
	private GridView gridview;
	private BaseAdapter adapter;
	private int offset = 0;
	private int limit = 50;
	private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=2.4.0&method=baidu.ting.artist.get72HotArtist&format=json&order=1&";
	private String newUrl;
	private RequestQueue queue;
	
	private List<Singer> list;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_online_singer, null);
		findviewsetadapter(view);
		isPrepared = true;
		return view;
	}
	
	//找view
	private void findviewsetadapter(View view){
		gridview = (GridView) view.findViewById(R.id.fragment_online_singer_gridview);
		MyApplication application = (MyApplication) getActivity().getApplication();
		queue = application.getRequestQueue();
	}

	//获取json数据
	private void getJson(){
		newUrl = url+"offset="+offset+"&limit="+limit;
		//新建请求对象
		StringRequest singerRequest = new StringRequest(newUrl, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				//解析json对象
				list = AnaJson.anaSingerJson(response);
				//设置数据
				setAdapter();
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

	@Override
	protected void lazyLoad() {
		if(!isPrepared||!isVisible||!isFirst)
			return;
		//下面执行数据加载
		isFirst = false;
		getJson();
	}

}
