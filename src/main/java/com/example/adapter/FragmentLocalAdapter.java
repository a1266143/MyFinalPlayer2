package com.example.adapter;

import java.util.List;
import java.util.Map;

import com.example.pppppp.R;
import com.example.utils.LocalMusicUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentLocalAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Integer>> list;
	
	public FragmentLocalAdapter(Context context,  List<Map<String,Integer>> list) {
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
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(view == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_fragment_local_listview, null);
			viewHolder.iv = (ImageView) view.findViewById(R.id.listitem_fragment_local_listview_imageview);
			viewHolder.tv = (TextView) view.findViewById(R.id.listitem_fragment_local_listview_tv);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		//如果是本地音乐，将本地音乐的数量标识出来
		if(arg0 == 0)
			viewHolder.tv.setText(context.getResources().getString((Integer) list.get(arg0).get("title"))+"（"+new LocalMusicUtils().getArrSize(context)+"）");
		else
			viewHolder.tv.setText(context.getResources().getString((Integer) list.get(arg0).get("title")));
		viewHolder.iv.setImageResource((Integer) list.get(arg0).get("imageResourse"));
		return view;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}
