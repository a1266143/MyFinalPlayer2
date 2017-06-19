package com.example.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.Activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOnlineCategoryAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private Context context;

	public FragmentOnlineCategoryAdapter(Context context,
			List<HashMap<String, String>> list) {
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
	public View getView(int position, View view, ViewGroup viewgroup) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.listitem_fragment_online_category,
					null);
			viewHolder.iv = (ImageView) view
					.findViewById(R.id.listitem_fragment_online_category_iv);
			viewHolder.firstSong = (TextView) view
					.findViewById(R.id.listitem_fragment_online_category_firstSong);
			viewHolder.secondSong = (TextView) view
					.findViewById(R.id.listitem_fragment_online_category_secondSong);
			viewHolder.thirdSong = (TextView) view
					.findViewById(R.id.listitem_fragment_online_category_thirdSong);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// 设置标题图片
		switch (position) {
		// 新歌
		case 0:
			viewHolder.iv.setImageResource(R.drawable.newsong);
			break;
		// 热歌
		case 1:
			viewHolder.iv.setImageResource(R.drawable.hotsong);
			break;
		// KTV
		case 2:
			viewHolder.iv.setImageResource(R.drawable.ktvsong);
			break;
		// billboard榜
		case 3:
			viewHolder.iv.setImageResource(R.drawable.billboard);
			break;
		// 摇滚榜
		case 4:
			viewHolder.iv.setImageResource(R.drawable.rock);
			break;
		// 影视金曲榜
		case 5:
			viewHolder.iv.setImageResource(R.drawable.yingshijinqu);
			break;
		// hito榜
		case 6:
			viewHolder.iv.setImageResource(R.drawable.hito);
			break;
		// 华语榜
		case 7:
			viewHolder.iv.setImageResource(R.drawable.huayu);
			break;
		// 欧美榜
		case 8:
			viewHolder.iv.setImageResource(R.drawable.oumei);
			break;
		// 经典老歌
		case 9:
			viewHolder.iv.setImageResource(R.drawable.oldsong);
			break;
		// 情歌对唱
		case 10:
			viewHolder.iv.setImageResource(R.drawable.qingge);
			break;
		// 网络歌曲
		case 11:
			viewHolder.iv.setImageResource(R.drawable.netsong);
			break;
		}
		viewHolder.firstSong.setText("1." + list.get(position).get("first"));
		viewHolder.secondSong.setText("2." + list.get(position).get("second"));
		viewHolder.thirdSong.setText("3." + list.get(position).get("third"));
		if(list.get(position).get("third")==null)
			viewHolder.thirdSong.setText("");
		return view;
	}

	private class ViewHolder {
		ImageView iv;
		TextView firstSong, secondSong, thirdSong;
	}

}
