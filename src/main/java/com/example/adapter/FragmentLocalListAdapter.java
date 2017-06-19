package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.LocalMusic;
import com.example.Activity.R;

public class FragmentLocalListAdapter extends BaseAdapter {

	private Context context;
	private List<LocalMusic> list;
	private MenuClick mMenuClick;
	public FragmentLocalListAdapter(Context context, List<LocalMusic> list,MenuClick mMenuClick) {
		super();
		this.context = context;
		this.list = list;
		this.mMenuClick = mMenuClick;
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
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(view == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_fragment_locallist_listview, null);
			viewHolder.name = (TextView) view.findViewById(R.id.listitem_fragment_locallist_songname);
			viewHolder.author = (TextView) view.findViewById(R.id.listitem_fragment_locallist_author);
			viewHolder.localiv = (ImageView) view.findViewById(R.id.listitem_fragment_locallist_localmobile);
			viewHolder.more = (ImageButton) view.findViewById(R.id.listitem_fragment_locallist_morebtn);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.name.setText(list.get(position).getSongName());
		viewHolder.author.setText(list.get(position).getAuthor()+" - "+list.get(position).getAlbum());
		viewHolder.localiv.setImageResource(R.drawable.mobile);
		viewHolder.more.setImageResource(R.drawable.more);
		viewHolder.more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将点击相关信息传出去
				mMenuClick.onclick(position,list);
			}
		});
		return view;
	}
	
	class ViewHolder{
		TextView name,author;
		ImageButton more;
		ImageView localiv;
	}
	
	public interface MenuClick{
		public void onclick(int pos,List<LocalMusic> list);
	}

}
