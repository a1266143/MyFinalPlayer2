package com.example.fragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.example.application.MyApplication;
import com.example.bean.Song;
import com.example.Activity.R;
import com.example.proxy.AnaJson;
import com.example.service.MainService;
import com.example.utils.Utils;
import com.example.utils.VolleyUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Search extends Fragment {
	public static final String ARGUMENT = "argument";
	
	private EditText searchEditText;

	private RequestQueue queue;
	private VolleyUtils volleyUtils;
	private ArrayList<Song> list;
	private ListView listview;
	private BaseAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		Bundle b = getArguments();
		if(b!=null){
			
		}
	}

	private void init(){
		MyApplication application = (MyApplication) getActivity().getApplication();
		queue = application.getRequestQueue();
		volleyUtils = new VolleyUtils(queue,getActivity());
		list = new ArrayList<>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		findview(view);
		setListener();
		return view;
	}
	
	private void findview(View view){
		view.findViewById(R.id.fragment_search_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//关闭当前fragment,将fragment弹出栈
				getActivity().getSupportFragmentManager().popBackStack();
				Utils.closeInputMethod(getActivity(),searchEditText);
			}
		});
		this.searchEditText = (EditText) view.findViewById(R.id.fragment_search_edittext);
		this.listview = (ListView) view.findViewById(R.id.fragment_search_listview);
	}

	private void setListener(){
		this.searchEditText.addTextChangedListener(new MyTextWatcher());
	}

	/**
	 * EditText内容改变监听器
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(!s.toString().equals("")){
				//关键词联想
				wordAssociate(s.toString());
				listview.setVisibility(View.VISIBLE);
			}

			else
				listview.setVisibility(View.GONE);
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	private boolean isFirstGetData = true;
	/**
	 * 通过关键词获取json数据
	 * @param word
     */
	private void wordAssociate(String word){
		String url = Fragment_Search.URLSEARCH+word;
		volleyUtils.newRequest(url, new VolleyUtils.OnResponseListener() {
			@Override
			public void response(JSONObject jsonObject) {
				list = AnaJson.anaSearchSong(jsonObject);
				setListViewAdapter();
				setListListener();
			}

			@Override
			public void error(VolleyError volleyError) {
				Utils.showToast(getActivity(),volleyError.getMessage());
			}
		});
	}

	/**
	 * 设置ListView的Adapter
	 */
	private void setListViewAdapter(){
		if(isFirstGetData){
			if(this.list.size()!=0&&list!=null) {
				this.adapter = new BaseAdapter() {
					@Override
					public int getCount() {
						return list.size();
					}

					@Override
					public Object getItem(int position) {
						return list.get(position);
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						ViewHolder viewHolder = null;
						if (convertView == null) {
							viewHolder = new ViewHolder();
							LayoutInflater inflater = getActivity().getLayoutInflater();
							convertView = inflater.inflate(R.layout.listitem_fragmentcategorymore, null);
							viewHolder.tv_songName = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv1);
							viewHolder.tv_author = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv2);
							convertView.setTag(viewHolder);
						} else {
							viewHolder = (ViewHolder) convertView.getTag();
						}
						viewHolder.tv_songName.setText(list.get(position).getTitle());
						viewHolder.tv_author.setText(list.get(position).getAuthor());
						return convertView;
					}

					class ViewHolder {
						TextView tv_songName, tv_author;
					}
				};
				this.listview.setAdapter(adapter);
				isFirstGetData = false;
			}
		}else{
				this.adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 设置ListView的点击事件监听器
	 */
	private void setListListener(){
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				itemClicked(position);
			}
		});
	}

	/**
	 * 列表被点击
	 * @param position
     */
	private void itemClicked(int position){
		Intent intent = new Intent(getActivity(), MainService.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("isLocalMusicList",false);
		bundle.putSerializable("List",list);
		bundle.putInt("position",position);
		intent.putExtras(bundle);
		getActivity().startService(intent);
	}

	/**
	 * 通过这个方法可以在新建这个fragment对象的同时
	 * 可以进行传值
	 * @param argument
	 * @return
	 */
	public static Fragment_Search newInstance(String argument){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);
		Fragment_Search mFragment_Three = new Fragment_Search();
		mFragment_Three.setArguments(bundle);
		return mFragment_Three;
	}


	//搜索url
	public static final String URLSEARCH = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&method=baidu.ting.search.merge&page_no=1&page_size=30&query=";
}
