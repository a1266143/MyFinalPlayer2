package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.adapter.FragmentCategoryMoreAdapter;
import com.example.bean.MyObject;
import com.example.bean.Song;
import com.example.pppppp.R;
import com.example.proxy.AnaJson;
import com.example.service.MainService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Online_Category_More extends Fragment implements FragmentCategoryMoreAdapter.MoreBtnClicked {

	private String mArgument;
	public static final String ARGUMENT = "argument";
	public static final String IMAGEURL = "imageurl";
	
	private SharedPreferences preference;
	private SharedPreferences.Editor editor;
	private String json;
	private ArrayList<Song> list = new ArrayList<Song>();
	private ListView listview;
	private BaseAdapter adapter;
	private ImageButton returnBtn;
	private TextView titleTextView;

	private final String NEWSONG = "新歌榜";
	private final String HOTSONG = "热歌榜";
	private final String KTV = "KTV热歌";
	private final String BILLBOARD = "billboard";
	private final String ROCK = "摇滚榜";
	private final String YINGSHI = "影视金曲";
	private final String HITO = "hito";
	private final String HUAYU = "华语金曲";
	private final String OUMEI = "欧美金曲";
	private final String OLD = "经典老歌";
	private final String QINGGE = "情歌对唱";
	private final String NET = "网络歌曲";

	private String imageurl;
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle !=null){
			mArgument = bundle.getString(ARGUMENT);
			imageurl = bundle.getString(IMAGEURL);
		}
		preference = getActivity().getSharedPreferences("category", Context.MODE_PRIVATE);
		//获取sharedpreference中的json字符串
		json = preference.getString(mArgument, "");
		if(!json.equals("")) {
			list = AnaJson.AnaSong(json);
		}
		//KTV单独解析
		/*if(mArgument.equals(Fragment_Online_Category.KTV)){
			if(!json.equals("")){
				list = AnaJson.AnaSong(json);
			}
		}
		//billboard单独解析
		else if(mArgument.equals(Fragment_Online_Category.BILLBOARD)){
			if(!json.equals("")){
				list = AnaJson.AnaSong(json);
			}
		}
		//HITO榜单独解析json
		else if(mArgument.equals(Fragment_Online_Category.HITO)){
			if(!json.equals("")){
				list = AnaJson.AnaSong(json);
			}
		}else{

			}
		}*/
	}

	/**
	 * 根据mArgument获取title的值
	 * @param title
	 * @return
     */
	private String getTitle(String title){

		if(title.equals(Fragment_Online_Category.NEWSONG)){
			return this.NEWSONG;
		}else if(title.equals(Fragment_Online_Category.HOTSONG)){
			return this.HOTSONG;
		}else if(title.equals(Fragment_Online_Category.KTV)){
			return this.KTV;
		}
		else if(title.equals(Fragment_Online_Category.BILLBOARD)){
			return this.BILLBOARD;
		}
		else if(title.equals(Fragment_Online_Category.ROCK)){
			return this.ROCK;
		}
		else if(title.equals(Fragment_Online_Category.YINGSHI)){
			return this.YINGSHI;
		}
		else if(title.equals(Fragment_Online_Category.HITO)){
			return this.HITO;
		}
		else if(title.equals(Fragment_Online_Category.HUAYU)){
			return this.HUAYU;
		}
		else if(title.equals(Fragment_Online_Category.OUMEI)){
			return this.OUMEI;
		}
		else if(title.equals(Fragment_Online_Category.OLD)){
			return this.OLD;
		}
		else if(title.equals(Fragment_Online_Category.QINGGE)){
			return this.QINGGE;
		}
		else{
			return this.NET;
		}
	}
	
	/**
	 * 传入需要的参数，设置给arguments
	 */
	public static Fragment_Online_Category_More newInstance(MyObject obj){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, obj.argument);
		bundle.putString(IMAGEURL,obj.imageURL);
		Fragment_Online_Category_More moreFragment = new Fragment_Online_Category_More();
		moreFragment.setArguments(bundle);
		return moreFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online_category_more, null);
		findviewsetAdapter(view);
		return view;
	}

	/**
	 * 找view，设置adapter
	 */
	private void findviewsetAdapter(View view){
		returnBtn = (ImageButton) view.findViewById(R.id.fragment_online_category_more_return);
		titleTextView = (TextView) view.findViewById(R.id.fragment_online_category_more_title);
		listview = (ListView) view.findViewById(R.id.fragment_online_category_more_listview);
		adapter = new FragmentCategoryMoreAdapter(list,getActivity(),this);
		//设置标题
		titleTextView.setText(getTitle(mArgument));
		//添加headerview
		addHaederView();
		listview.setAdapter(adapter);
		//返回按钮的监听器
		returnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				itemClicked(arg2-1);
			}
		});
		setEmptyView();
	}

	/**
	 * 添加headerview
	 */
	private void addHaederView(){
		View headerview = getActivity().getLayoutInflater().inflate(R.layout.onlinemusic_headerview,null);
		ImageView headimage = (ImageView) headerview.findViewById(R.id.onlinemusic_headerview_imageview);
		//网络获取图片并设置
		Glide.with(this).load(imageurl).into(headimage);
		//添加headerview，并让其不可点击
		listview.addHeaderView(headerview);

	}

	/**
	 * list为空时设置空view	 * 发送广播给前台,将歌名和歌手传递给前台，通知其修改界面

	 */
	private void setEmptyView(){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View emptyView = inflater.inflate(R.layout.emptyview_online, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
		emptyView.setVisibility(View.GONE);
		/*emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});*/
		((ViewGroup)listview.getParent()).addView(emptyView); 
		listview.setEmptyView(emptyView);
	}


	/**
	 * listview的item被点击
	 * @param position
     */
	private void itemClicked(int position){
		Intent intent = new Intent(getActivity(), MainService.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("isLocalMusicList",false);
		bundle.putSerializable("List",list);
		bundle.putInt("position",position);
		intent.putExtras(bundle);
		getActivity().startService(intent);
	}
	/**
	 * 更多按钮被点击的时候
	 * @param position
     */
	@Override
	public void clicked(int position) {
		Toast.makeText(getActivity(),"你点击了"+list.get(position).getTitle()+"的更多按钮",Toast.LENGTH_SHORT).show();
	}


}
