package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.Album;
import com.example.Activity.R;

public class FragmentAlbumAdapter extends BaseAdapter {

	private Context context;
	private List<Album> list;
	
	
	
	public FragmentAlbumAdapter(Context context, List<Album> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(arg1==null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.listitem_fragment_album, null);
			viewHolder.iv = (ImageView) arg1.findViewById(R.id.listitem_fragment_album_imageview);
			viewHolder.album = (TextView) arg1.findViewById(R.id.listitem_fragment_albumName);
			viewHolder.albumnum = (TextView) arg1.findViewById(R.id.listitem_fragment_albumnum);
			arg1.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		//专辑图片
		viewHolder.iv.setImageResource(R.drawable.albumicon);
		//专辑名称
		viewHolder.album.setText(list.get(arg0).getAlbumName());
		//专辑数量
		viewHolder.albumnum.setText(list.get(arg0).getAlbumNumber()+"首");
		return arg1;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView album,albumnum;
	}

}
