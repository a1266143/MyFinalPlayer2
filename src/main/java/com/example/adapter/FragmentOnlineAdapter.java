package com.example.adapter;

import java.util.List;

import com.example.bean.HomeList;
import com.example.pppppp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOnlineAdapter extends BaseAdapter {

	private Context context;
	private List<HomeList> list;
	
	
	public FragmentOnlineAdapter(Context context, List<HomeList> list) {
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
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(view == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_fragment_online_listview, null);
			viewHolder.iv = (ImageView) view.findViewById(R.id.listitem_fragment_online_listview_imageview);
			viewHolder.t1 = (TextView) view.findViewById(R.id.textview_song1);
			viewHolder.t2 = (TextView) view.findViewById(R.id.textview_song2);
			viewHolder.t3 = (TextView) view.findViewById(R.id.textview_song3);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		return null;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView t1,t2,t3;
	}

}
