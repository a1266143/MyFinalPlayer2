package com.example.pppppp;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.FragmentLocalListAdapter.MenuClick;
import com.example.adapter.MyFragmentPagerAdapter;
import com.example.application.MyApplication;
import com.example.bean.CurrentPlaySong;
import com.example.bean.LocalMusic;
import com.example.fragment.DialogFragment_Menu;
import com.example.fragment.Fragment_Local;
import com.example.fragment.Fragment_Online;
import com.example.fragment.Fragment_Search;
import com.example.listener.MyPageChangeListener;
import com.example.service.MainService;
import com.example.utils.Utils;

public class MainActivity extends BaseActivity implements OnClickListener,MenuClick {

	private ImageButton searchBtn,listBtn,playBtn,nextSongBtn,localmusicBtn,onlineMusicBtn;
	private ViewPager mViewPager;
	private MyFragmentPagerAdapter adapter;
	private RelativeLayout bottombar;
	private TextView tv_title,tv_author;

	private ServiceConnection conn;

	//binder
	private MainService.MyBinder myBinder;

	public static final String BACKSTACK = "MyBackStack";

	private MyReceiver receiver;

	/**
	 * 广播接收器，负责接收从Service传递过来的值
	 */
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String title = bundle.getString("title");
			String author = bundle.getString("author");
			tv_title.setText(title);
			tv_author.setText(author);
			playBtn.setImageResource(R.drawable.pause);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void init() {
		Fragment mFragment_Local = new Fragment_Local();
		Fragment mFragment_Online = new Fragment_Online();
		ArrayList<Fragment> arr = new ArrayList<Fragment>();
		arr.add(mFragment_Local);
		arr.add(mFragment_Online);
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), arr);
		//注册广播接收器
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(MainService.ACTION_CHANGEUI);
		registerReceiver(receiver,filter);
		//绑定service
		bindMyService();
	}

	/**
	 * 绑定service
	 */
	private void bindMyService() {

		Intent service = new Intent(this, MainService.class);
		conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				Utils.showToast(MainActivity.this,"与Service的连接已断开");
			}

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder ibinder) {
				//获取从service传过来的binder对象
				myBinder = (MainService.MyBinder) ibinder;
				CurrentPlaySong song = myBinder.getCurrentSong();
				MediaPlayer player = myBinder.getPlayer();
				setState(song.getSongName(),song.getAuthor(),player);
			}
		};
		// 绑定服务
		bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 设置歌名和歌手
	 * @param title
	 * @param author
     */
	private void setState(String title, String author, MediaPlayer player){
		tv_title.setText(title);
		tv_author.setText(author);
		if(player.isPlaying())
			playBtn.setImageResource(R.drawable.pause);
		else
			playBtn.setImageResource(R.drawable.playbtn);
	}



	@Override
	public void findview() {
		bottombar = (RelativeLayout) findViewById(R.id.activity_main_bottombar_re);
		searchBtn = (ImageButton) findViewById(R.id.activity_main_searchbtn);
		listBtn = (ImageButton) findViewById(R.id.activity_main_listbtn);
		playBtn = (ImageButton) findViewById(R.id.activity_main_playbtn);
		nextSongBtn = (ImageButton) findViewById(R.id.activity_main_nextsong);
		mViewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
		localmusicBtn = (ImageButton) findViewById(R.id.activity_main_localmusicbtn);
		onlineMusicBtn = (ImageButton) findViewById(R.id.activity_main_onlinemusicbtn);
		tv_title = (TextView) findViewById(R.id.activity_main_songname);
		tv_author = (TextView) findViewById(R.id.activity_main_authorname);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setListener() {
		searchBtn.setOnClickListener(this);
		listBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		nextSongBtn.setOnClickListener(this);
		localmusicBtn.setOnClickListener(this);
		onlineMusicBtn.setOnClickListener(this);
		bottombar.setOnClickListener(this);
		//设置viewpager的适配器
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener(this,localmusicBtn,onlineMusicBtn));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//本地音乐按钮
		case R.id.activity_main_localmusicbtn:
			//设置状态以及viewpager
			setViewPager(R.id.activity_main_localmusicbtn);
			break;
			//在线音乐按钮
		case R.id.activity_main_onlinemusicbtn:
			//设置状态以及viewpager
			setViewPager(R.id.activity_main_onlinemusicbtn);
			break;
		//搜索按钮
		case R.id.activity_main_searchbtn:
			goToSearch();
			break;
			//列表按钮
		case R.id.activity_main_listbtn:
			Utils.showToast(this, "你点击了列表按钮");
			break;
			//播放按钮
		case R.id.activity_main_playbtn:
			Utils.showToast(this, "你点击了播放按钮");
			break;
			//下一首按钮
		case R.id.activity_main_nextsong:
			Utils.showToast(this, "你点击了下一首按钮");
			break;

		case R.id.activity_main_bottombar_re:
			Intent i = new Intent(this,PlayActivity.class);
			startActivity(i);
			break;
		}
	}
	
	
	/**
	 * 通过按钮标识设置viewpager以及button的状态
	 * @param id
	 */
	public void setViewPager(int id){
		switch (id) {
		case R.id.activity_main_localmusicbtn:
			localmusicBtn.setImageResource(R.drawable.localmusic_selected);
			onlineMusicBtn.setImageResource(R.drawable.onlinemusic);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.activity_main_onlinemusicbtn:
			localmusicBtn.setImageResource(R.drawable.localmusic);
			onlineMusicBtn.setImageResource(R.drawable.onlinemusic_selected);
			mViewPager.setCurrentItem(1);
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public int getColor() {
		return android.R.color.white;
	}
	
	/**
	 * 搜索界面
	 */ 
	public void goToSearch(){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.activity_main_frameLayout, new Fragment_Search());
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.addToBackStack(BACKSTACK);
		transaction.commit();
	}

	/**
	 * 本地音乐列表菜单按钮被按
	 */
	@Override
	public void onclick(int pos,List<LocalMusic> list) {
		DialogFragment_Menu menu = DialogFragment_Menu.newInstance(list.get(pos).getSongName());
		menu.show(getSupportFragmentManager(), "本地音乐菜单");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(conn!=null){
			unbindService(conn);
			conn = null;
		}

		unregisterReceiver(receiver);
	}
	
	

}
