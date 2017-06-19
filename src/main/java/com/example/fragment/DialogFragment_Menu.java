package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.DialogFragmentMenuAdapter;
import com.example.Activity.R;

public class DialogFragment_Menu extends DialogFragment {

	// argument参数名
	public static final String ARGUMENT = "argument";
	private String mArgument;
	private ListView listview;
	private ArrayList<Map<String, Integer>> list;
	private TextView title;

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mArgument = bundle.getString(ARGUMENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// 将此dialog对话框设置为无标题
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialogfragment_menu, null);
		title = (TextView) view.findViewById(R.id.dialogfragment_menu_textview);
		title.setText(mArgument);
		listview = (ListView) view
				.findViewById(R.id.dialogfragment_menu_listview);
		list = new ArrayList<Map<String, Integer>>();
		// 将数据添加到list中
		addList();
		BaseAdapter adapter = new DialogFragmentMenuAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				//查询歌曲信息
				case 0:
					//将这个dialogfragment本身关掉
					DialogFragment_Menu.this.dismiss();
					break;
					//收藏此曲目
				case 1:

					break;
					//删除此曲目
				case 2:

					break;
				}
			}
		});
		return view;
	}

	/**
	 * 将菜单内容添加到list中
	 */
	public void addList() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("image", R.drawable.info);
		map.put("text", R.string.queryinfo);
		list.add(map);
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		map1.put("image", R.drawable.shoucang);
		map1.put("text", R.string.collect);
		list.add(map1);
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		map2.put("image", R.drawable.delete);
		map2.put("text", R.string.delete);
		list.add(map2);
	}

	/**
	 * 传入需要的参数，设置给argument
	 * 
	 * @param argument
	 * @return
	 */
	public static DialogFragment_Menu newInstance(String argument) {
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);
		DialogFragment_Menu menuFragment = new DialogFragment_Menu();
		menuFragment.setArguments(bundle);
		return menuFragment;
	}

}
