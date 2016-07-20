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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adapter.FragmentLocalListAdapter.MenuClick;
import com.example.adapter.MyFragmentPagerAdapter;
import com.example.bean.CurrentOnlineSong;
import com.example.bean.CurrentPlaySong;
import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.fragment.DialogFragment_Menu;
import com.example.fragment.Fragment_List;
import com.example.fragment.Fragment_Local;
import com.example.fragment.Fragment_Online;
import com.example.fragment.Fragment_Search;
import com.example.listener.MyPageChangeListener;
import com.example.service.MainService;
import com.example.utils.Utils;

public class MainActivity extends BaseActivity implements OnClickListener,MenuClick {

	private ImageButton searchBtn,listBtn,playBtn,nextSongBtn,localmusicBtn,onlineMusicBtn;
	private ImageView mHeadImage;
	private ViewPager mViewPager;
	private MyFragmentPagerAdapter adapter;
	private RelativeLayout bottombar;
	private TextView tv_title,tv_author;

	private ServiceConnection conn;

	//binder
	private MainService.MyBinder myBinder;

	public static final String BACKSTACK = "MyBackStack";

	private MyReceiver receiver;

	private MediaPlayer player;

	private CurrentPlaySong song;

	private boolean isLocalMusic;

	private ArrayList<LocalMusic> localMusicList;

	private ArrayList<Song> onlineMusicList;

	private CurrentOnlineSong mCurrentOnlineSong;

	/**
	 * 广播接收器，负责接收从Service传递过来的值
	 */
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String title = bundle.getString("title");
			String author = bundle.getString("author");
			isLocalMusic = bundle.getBoolean("isLocalMusic");
			tv_title.setText(title);
			tv_author.setText(author);
			playBtn.setImageResource(R.drawable.pause);
			if(!isLocalMusic){
				mCurrentOnlineSong = (CurrentOnlineSong) bundle.getSerializable("currentOnlineSong");
				Glide.with(MainActivity.this).load(mCurrentOnlineSong.getSongPicRadio()).into(mHeadImage);
			}else{
				mHeadImage.setImageResource(R.drawable.headimage);
			}

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
				init(ibinder);
				setState(song.getSongName(),song.getAuthor(),player);
			}

			/**
			 * 在bindService成功的时候初始化各个变量
			 * @param ibinder
             */
			private void init(IBinder ibinder){
				//获取从service传过来的binder对象
				myBinder = (MainService.MyBinder) ibinder;
				song = myBinder.getCurrentSong();
				player = myBinder.getPlayer();
				isLocalMusic = myBinder.getIsLocalMusic();
				if(isLocalMusic){
					localMusicList = myBinder.getLocalMusicList();
				}else{
					onlineMusicList = myBinder.getOnlineMusicList();
					mCurrentOnlineSong = myBinder.getCurrentOnlineSong();
				}
			}
		};
		// 绑定服务
		bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 获得当前和Service绑定的Activity的binder
	 * 主要是给Fragment，不需要让Fragment再绑定一次
	 * @return
     */
	public MainService.MyBinder getMyBinder(){
		return myBinder;
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
		if(!isLocalMusic){
			Glide.with(this).load(mCurrentOnlineSong.getSongPicRadio()).into(mHeadImage);
		}
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
		mHeadImage = (ImageView) findViewById(R.id.activity_main_headimage);
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
			clickListBtn();
			break;
			//播放按钮
		case R.id.activity_main_playbtn:
			clickPlayBtn();
			break;
			//下一首按钮
		case R.id.activity_main_nextsong:
			clickNextBtn();
			break;

		case R.id.activity_main_bottombar_re:
			Intent i = new Intent(this,PlayActivity.class);
			startActivity(i);
			break;
		}
	}

	/**
	 * 点击下一首按钮
	 */
	private void clickNextBtn(){
		myBinder.getService().playNextSong();
	}

	/**
	 * 点击列表按钮
	 */
	private void clickListBtn(){
		if(!Fragment_List.isOpen)
			beginTransaction(R.id.activity_main_frameLayout,Fragment_List.newInstance(isLocalMusic,localMusicList,onlineMusicList));
	}

	/**
	 * 点击播放按钮
	 */
	private void clickPlayBtn(){
		if(player.isPlaying()){
			player.pause();
			playBtn.setImageResource(R.drawable.playbtn);
		}else{
			player.start();
			playBtn.setImageResource(R.drawable.pause);
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
		beginTransaction(R.id.activity_main_frameLayout,new Fragment_Search());
	}

	/**
	 * 开始Fragment事物
	 * @param layout
	 * @param fragment
     */
	private void beginTransaction(int layout,Fragment fragment){
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.add(layout,fragment);
		transaction = transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.addToBackStack(MainActivity.BACKSTACK);
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
