package com.example.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.example.application.MyApplication;
import com.example.bean.CurrentOnlineSong;
import com.example.bean.CurrentPlaySong;
import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.pppppp.R;
import com.example.utils.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainService extends Service {

	//接口对象
	private MyBinder binder;
	//音乐播放对象
	private MediaPlayer mediaPlayer;
	//当前播放的列表类型，本地列表和网络歌曲列表
	private boolean isLocalMusicList;
	//当前播放的position
	private int position;
	//本地音乐列表
	private ArrayList<LocalMusic> localMusicList;
	//网络音乐列表
	private ArrayList<Song> onlineMusicList;
	//Volley
	private RequestQueue queue;
	//VolleyUtils
	private VolleyUtils volleyUtils;
	//存储当前播放的网络音乐的Bean
	private CurrentOnlineSong currentOnlineSong;

	//存储当前正在播放的歌曲bean
	private CurrentPlaySong mCurrentPlaySong;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	/**
	 * 初始化
	 */
	private void init(){
		localMusicList = new ArrayList<LocalMusic>();
		onlineMusicList = new ArrayList<Song>();
		mediaPlayer = new MediaPlayer();
		binder = new MyBinder();
		//将播放器重置
		mediaPlayer.reset();
		MyApplication application = (MyApplication) getApplication();
		queue = application.getRequestQueue();
		volleyUtils = new VolleyUtils(queue,this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	//客户端与此服务的接口
	public class MyBinder extends Binder{
		
		//将此service实例传给客户端
		public MainService getService(){
			return MainService.this;
		}

		public CurrentPlaySong getCurrentSong(){
			if(mCurrentPlaySong!=null)
				return mCurrentPlaySong;
			else{
				mCurrentPlaySong = new CurrentPlaySong("","");
				return mCurrentPlaySong;
			}

		}
		
		//传递给客户端MediaPlayer对象
		public MediaPlayer getPlayer(){
			return mediaPlayer;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		//获取歌曲列表类型
		isLocalMusicList = bundle.getBoolean("isLocalMusicList");
		//获取position
		position = bundle.getInt("position");
		//根据列表类型，根据相应的类型给相应的列表赋值，并播放
		if(isLocalMusicList){
			localMusicList = (ArrayList<LocalMusic>) bundle.getSerializable("List");
			playLocalMusic();
		}else{
			onlineMusicList = (ArrayList<Song>) bundle.getSerializable("List");
			getOnlineMusicInfo(onlineMusicList.get(position).getSongid());
		}
		sendBroadCastToUI();
		//开启前台Service
		startForgroundService();
		return Service.START_REDELIVER_INTENT;
	}

	/**
	 * 开启前台Service
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void startForgroundService(){
		RemoteViews view = new RemoteViews(getPackageName(),R.layout.notification_layout);
		Notification notification = new Notification.Builder(this).build();
		notification.contentView = view;

		//PendingIntent pengdingIntent = PendingIntent.getActivity(this,2,)
		startForeground(1,notification);
	}

	/**
	 * 播放本地音乐
	 */
	private void playLocalMusic(){
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(localMusicList.get(position).getPath());
			mediaPlayer.prepare();
			mediaPlayer.start();
			setCurrentPlaySong(localMusicList.get(position).getSongName(),localMusicList.get(position).getAuthor());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得网络歌曲相关信息
	 */
	private void getOnlineMusicInfo(String songid){
		mediaPlayer.reset();
		String requestURL = URL + songid;
		volleyUtils.newRequest(requestURL, new VolleyUtils.OnResponseListener() {
			@Override
			public void response(JSONObject jsonObject) {
				try {
					JSONObject object = jsonObject.getJSONObject("data");
					JSONArray array = object.getJSONArray("songList");
					JSONObject object1 = array.getJSONObject(0);
					currentOnlineSong = new CurrentOnlineSong(object1.getString("songName"),object1.getString("artistName"),object1.getString("songPicRadio"),object1.getString("lrcLink"),
							object1.getString("songLink"));
					//播放网络音乐
					playOnlineMusic(currentOnlineSong.getSongLink());
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			}

			@Override
			public void error(VolleyError volleyError) {
				Log.e("VolleyError","网络出现问题"+volleyError.getMessage());
			}
		});
	}

	/**
	 * 播放网络音乐
	 * @param url 网络音乐的播放地址
     */
	private void playOnlineMusic(String url){
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(url);
			//异步准备，防止阻塞页面
			mediaPlayer.prepareAsync();
			setCurrentPlaySong(onlineMusicList.get(position).getTitle(),onlineMusicList.get(position).getAuthor());
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IOException","网络出现问题");
			return;
		}
	}

	private void setCurrentPlaySong(String songName,String author){
		mCurrentPlaySong = new CurrentPlaySong(songName,author);
	}


	/**
	 * 发送广播给前台,将歌名和歌手传递给前台，通知其修改界面
	 */
	private void sendBroadCastToUI(){
		Intent intent = new Intent();
		Bundle b = new Bundle();
		if(isLocalMusicList){
			b.putString("title",localMusicList.get(position).getSongName());
			b.putString("author",localMusicList.get(position).getAuthor());
		}else{
			b.putString("title",onlineMusicList.get(position).getTitle());
			b.putString("author",onlineMusicList.get(position).getAuthor());
		}
		intent.putExtras(b);
		intent.setAction(MainService.ACTION_CHANGEUI);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//置空对象
		mediaPlayer.release();
		mediaPlayer = null;
		binder = null;
		queue.cancelAll(null);
	}

	public static final String ACTION_CHANGEUI = "changeUI";
	//具体页面的url
	public static final String URL = "http://music.baidu.com/data/music/links?songIds=";



}
