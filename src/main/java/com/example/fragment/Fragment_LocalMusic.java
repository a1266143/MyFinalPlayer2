package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.FragmentLocalListAdapter;
import com.example.bean.LocalMusic;
import com.example.pppppp.MainActivity;
import com.example.pppppp.R;
import com.example.service.MainService;
import com.example.utils.LocalMusicUtils;
import com.example.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_LocalMusic extends BaseFragment {

	private ListView listview;
	private View headerview;
	private BaseAdapter adapter;
	private View view;
	private boolean isPrepared;
	private ArrayList<LocalMusic> list;
	private LocalMusicUtils musicUtils = new LocalMusicUtils();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_localmusic, null);
		headerview = inflater.inflate(R.layout.headerview_localmusiclistview,	null);
		TextView tv = (TextView) headerview.findViewById(R.id.headerview_localmusiclistview_playall);
		tv.setText("播放全部" + "（" + new LocalMusicUtils().getArrSize(getActivity())	+ "）");
		headerview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getActivity(), "你点击了播放全部", Toast.LENGTH_SHORT).show();
			}
		});
		list = musicUtils.getLocalMusicArr(getActivity());
		isPrepared = true;
		lazyLoad();
		return view;
	}

	@Override
	protected void lazyLoad() {
		if(!isPrepared||!isFirst||!isVisible)
			return;
		findAndSetAdapter();
		setListener();
		isFirst = false;
	}

	/**
	 * 设置Adapter
	 */
	private void findAndSetAdapter(){
		listview = (ListView) view.findViewById(R.id.fragment_localmusic_listview);
		listview.addHeaderView(headerview);
		adapter = new FragmentLocalListAdapter(getActivity(), list, (MainActivity)getActivity());
		listview.setAdapter(adapter);
	}

	/**
	 * 设置listview监听器
	 */
	private void setListener(){
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//因为有headerview，所以真实的position应该-1
				Intent intent = new Intent(getActivity(), MainService.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("isLocalMusicList",true);
				bundle.putSerializable("List",list);
				bundle.putInt("position",position-1);
				intent.putExtras(bundle);
				getActivity().startService(intent);
			}
		});
	}

}
