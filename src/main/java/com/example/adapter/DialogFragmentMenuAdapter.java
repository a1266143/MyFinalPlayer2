package com.example.adapter;

import java.util.List;
import java.util.Map;

import com.example.pppppp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogFragmentMenuAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Integer>> list;
	
	
	
	public DialogFragmentMenuAdapter(Context context,
			List<Map<String, Integer>> list) {
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
			arg1 = inflater.inflate(R.layout.listitem_dialogfragment_menu, null);
			viewHolder.iv = (ImageView) arg1.findViewById(R.id.listitem_dialogfragment_menu_imageview);
			viewHolder.tv = (TextView) arg1.findViewById(R.id.listitem_dialogfragment_menu_textview);
			arg1.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.iv.setImageResource(list.get(arg0).get("image"));
		viewHolder.tv.setText(list.get(arg0).get("text"));
		return arg1;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}
