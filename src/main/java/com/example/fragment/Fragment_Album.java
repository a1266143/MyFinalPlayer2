package com.example.fragment;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.adapter.FragmentAlbumAdapter;
import com.example.bean.Album;
import com.example.Activity.R;
import com.example.utils.LocalMusicUtils;

public class Fragment_Album extends BaseFragment {
	
	private List<Album> list;
	private BaseAdapter adapter;
	private ListView listview;
	private ProgressBar pb;
	
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

	Handler handler = new Handler();
	@Override
	protected void lazyLoad() {
		if(!isPrepared||!isFirst||!isVisible)
			return;
		pb = (ProgressBar) view.findViewById(R.id.fragment_album_pb);
		listview = (ListView) view.findViewById(R.id.fragment_album_list);
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = musicUtils.getLocalMusicAlbum(getActivity());
				adapter = new FragmentAlbumAdapter(getActivity(), list);

				handler.post(new Runnable() {
					@Override
					public void run() {
						listview.setAdapter(adapter);
						pb.setVisibility(View.GONE);
					}
				});
				isFirst = false;
			}
		}).start();


	}


	
}
