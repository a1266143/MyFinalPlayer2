package com.example.adapter;

import java.util.List;

import com.example.bean.Song;
import com.example.Activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentCategoryMoreAdapter extends BaseAdapter {

	private List<Song> list;
	private Context context;
	private MoreBtnClicked interfaceMoreBtnClicked;
	public FragmentCategoryMoreAdapter(List<Song> list,Context context,MoreBtnClicked interfaceMoreBtnClicked){
		this.list = list;
		this.context = context;
		this.interfaceMoreBtnClicked = interfaceMoreBtnClicked;
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
	public View getView(final int arg0, View view, ViewGroup arg2) {
		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(view==null){
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listitem_fragmentcategorymore, null);
			holder.tv1 = (TextView) view.findViewById(R.id.listitem_fragmentcategorymore_tv1);
			holder.tv2 = (TextView) view.findViewById(R.id.listitem_fragmentcategorymore_tv2);
			holder.btn = (ImageButton) view.findViewById(R.id.listitem_fragmentcategorymore_morebtn);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		//设置歌曲名称
		holder.tv1.setText(list.get(arg0).getTitle());
		//设置歌手名称
		holder.tv2.setText(list.get(arg0).getAuthor());
		//设置“更多”按钮监听器
		holder.btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				interfaceMoreBtnClicked.clicked(arg0);
			}
		});
		return view;
	}
	
	private class ViewHolder{
		TextView tv1,tv2;
		ImageButton btn;
	}

	/**
	 * 定义回调接口，将更多按钮的回调事件交给外部类处理
	 */
	public interface MoreBtnClicked{
		void clicked(int position);
	}

}
