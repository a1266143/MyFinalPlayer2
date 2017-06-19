package com.example.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.bean.Singer;
import com.example.Activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOnlineSingerAdapter extends BaseAdapter {

	private List<Singer> list;
	private Context context;
	public FragmentOnlineSingerAdapter(Context context,List<Singer> list){
		this.list = list;
		this.context = context;
	}
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
	public View getView(int position, View view, ViewGroup viewgroup) {
		ViewHolder viewholder = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(view == null){
			viewholder = new ViewHolder();
			view = inflater.inflate(R.layout.listitem_fragment_online_singer, null);
			viewholder.iv = (ImageView) view.findViewById(R.id.listitem_fragment_online_singer_iv);
			viewholder.tv = (TextView) view.findViewById(R.id.listitem_fragment_online_singer_tv);
			view.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) view.getTag();
		}
		//载入图片
		Glide.with(context).load(list.get(position).getPictureUrl()).into(viewholder.iv);
		//设置歌手名称
		viewholder.tv.setText(list.get(position).getName());
		return view;
	}
	
	private class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}
