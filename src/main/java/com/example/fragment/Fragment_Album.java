package com.example.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.adapter.FragmentAlbumAdapter;
import com.example.bean.Album;
import com.example.pppppp.R;
import com.example.utils.LocalMusicUtils;

public class Fragment_Album extends BaseFragment {
	
	private List<Album> list;
	private BaseAdapter adapter;
	private ListView listview;
	
	private boolean isPrepared;
	
	private LocalMusicUtils musicUtils = new LocalMusicUtils();

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_album, null);
		isPrepared = true;
		lazyLoad();
		return view;
	}

	@Override
	protected void lazyLoad() {
		if(!isPrepared||!isFirst||!isVisible)
			return;
		listview = (ListView) view.findViewById(R.id.fragment_album_list);
		list = musicUtils.getLocalMusicAlbum(getActivity());
		adapter = new FragmentAlbumAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		isFirst = false;
	}


	
}
