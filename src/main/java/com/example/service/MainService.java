package com.example.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.Activity.R;
import com.example.Dagger.Component.DaggerServiceComponent;
import com.example.Dagger.Module.ServiceModule;
import com.example.application.MyApplication;
import com.example.bean.CurrentPlaySong;
import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.utils.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

public class MainService extends Service {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    @Named("LocalMusic")
    ArrayList<LocalMusic> localMusicList;
    @Inject
    @Named("Song")
    ArrayList<Song> onlineMusicList;
    @Inject
    RequestQueue queue;
    @Inject
    VolleyUtils volleyUtils;
    @Inject
    NotificationCompat.Builder builder;
    @Inject
    RemoteViews mRemoteViews;
    @Inject
    MyBinder binder;
    @Inject
    @Named("nextPendingIntent")
    PendingIntent nextPendingIntent;
    @Inject
    @Named("playOrPause")
    PendingIntent playOrPausePendingIntent;
    @Inject
    @Named("close")
    PendingIntent closePendingIntent;

    private boolean isLocalMusicList = true;
    private int position;
    private CurrentPlaySong mCurrentPlaySong;
    //通知栏TAG
    private static int TAG_NOTIFICATION= 1;

    class CloseBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            stopForeground(true);
            mediaPlayer.pause();
        }
    }

    class NextBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            playNextSong();
        }
    }

    class PlayOrPauseBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.pause();
            else
                mediaPlayer.start();
            changeNotification();
            sendBroadCastToUI();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerServiceComponent.builder().serviceModule(new ServiceModule((MyApplication) getApplication(), this)).build().inject(this);
        registerReceiver(new NextBroadCastReceiver(),new IntentFilter(MainService.ACTION_NEXT));
        registerReceiver(new PlayOrPauseBroadCastReceiver(),new IntentFilter(MainService.ACTION_PLAYORPAUSE));
        registerReceiver(new CloseBroadCastReceiver(),new IntentFilter(MainService.ACTION_CLOSE));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        isLocalMusicList = bundle.getBoolean("isLocalMusicList");
        position = bundle.getInt("position");
        if (isLocalMusicList) {
            localMusicList = (ArrayList<LocalMusic>) bundle.getSerializable("List");
            playLocalMusic();
            sendBroadCastToUI();
        } else {
            onlineMusicList = (ArrayList<Song>) bundle.getSerializable("List");
            getOnlineMusicInfo(onlineMusicList.get(position).getSongid());
        }
        changeNotification();
        setListener();
        return Service.START_REDELIVER_INTENT;
    }

    private void setListener() {
        //设置播放完成监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });
        //设置缓冲监听器
        /*mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {

			}
		});*/
    }

    /**
     * 播放本地音乐
     */
    private void playLocalMusic() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(localMusicList.get(position).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            setCurrentPlaySong(localMusicList.get(position).getSongName(), localMusicList.get(position).getAuthor(),"","","");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放下一曲
     */
    public void playNextSong() {
        if (isLocalMusicList) {
            if (position == (localMusicList.size() - 1)) {
                position = 0;
            } else {
                position++;
            }
            playLocalMusic();
            sendBroadCastToUI();
        } else {
            if (position == (onlineMusicList.size() - 1)) {
                position = 0;
            } else {
                position++;
            }
            if (onlineMusicList.size() != 0) {
                getOnlineMusicInfo(onlineMusicList.get(position).getSongid());
            }

        }
    }


    /**
     * 获得网络歌曲相关信息
     */
    private void getOnlineMusicInfo(String songid) {
        mediaPlayer.reset();
        String requestURL = URL + songid;
        volleyUtils.cancleRequest(getApplicationContext());
        volleyUtils.newRequest(requestURL, new VolleyUtils.OnResponseListener() {
            @Override
            public void response(JSONObject jsonObject) {
                try {
                    JSONObject object = jsonObject.getJSONObject("data");
                    JSONArray array = object.getJSONArray("songList");
                    JSONObject object1 = array.getJSONObject(0);
                    setCurrentPlaySong(object1.optString("songName"), object1.optString("artistName"), object1.optString("songPicRadio"), object1.optString("lrcLink"),
                            object1.getString("songLink"));
                    //播放网络音乐
                    playOnlineMusic(mCurrentPlaySong.getSongLink());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            @Override
            public void error(VolleyError volleyError) {
                Log.e("VolleyError", "网络出现问题" + volleyError.getMessage());
            }
        });
    }

    /**
     * 播放网络音乐
     *
     * @param url 网络音乐的播放地址
     */
    private void playOnlineMusic(String url) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            //异步准备，防止阻塞页面
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    sendBroadCastToUI();
                    mRemoteViews.setImageViewResource(R.id.notification_layout_btnPlayOrPause,mediaPlayer.isPlaying()?R.drawable.pause:R.drawable.playbtn);
                    builder.setContent(mRemoteViews);
                    startForeground(TAG_NOTIFICATION,builder.build());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "网络出现问题");
        }
    }

    private void setCurrentPlaySong(String songName, String author,String songPicRadio,String lrcLink,String songLink) {
        mCurrentPlaySong = new CurrentPlaySong(songName, author,songPicRadio,lrcLink,songLink);
    }

    /**
     * 发送广播给前台,将歌名和歌手传递给前台，通知其修改界面
     */
    private void sendBroadCastToUI() {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("title",isLocalMusicList?localMusicList.get(position).getSongName():onlineMusicList.get(position).getTitle());
        b.putString("author",isLocalMusicList?localMusicList.get(position).getAuthor():onlineMusicList.get(position).getAuthor());
        b.putBoolean("isLocalMusic",isLocalMusicList?true:false);
        b.putSerializable("currentOnlineSong",mCurrentPlaySong);
        intent.putExtras(b);
        intent.setAction(MainService.ACTION_CHANGEUI);
        sendBroadcast(intent);
        changeNotification();
    }

    public void changeNotification(){
        if(isLocalMusicList)
            mRemoteViews.setImageViewResource(R.id.notification_layout_ivHeadImage,R.drawable.cd);
        mRemoteViews.setTextViewText(R.id.notification_layout_tvSong,isLocalMusicList?localMusicList.get(position).getSongName():onlineMusicList.get(position).getTitle());
        mRemoteViews.setTextViewText(R.id.notification_layout_tvSinger,isLocalMusicList?localMusicList.get(position).getAuthor():onlineMusicList.get(position).getAuthor());
        mRemoteViews.setImageViewResource(R.id.notification_layout_btnPlayOrPause,mediaPlayer.isPlaying()?R.drawable.pause:R.drawable.playbtn);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_layout_btnNext,nextPendingIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_layout_btnPlayOrPause,playOrPausePendingIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_layout_btnClose,closePendingIntent);
        builder.setContent(mRemoteViews);
        startForeground(TAG_NOTIFICATION,builder.build());
        if(!TextUtils.isEmpty(mCurrentPlaySong.getSongPicRadio()))
        Glide.with(MainService.this).load(mCurrentPlaySong.getSongPicRadio()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                mRemoteViews.setImageViewBitmap(R.id.notification_layout_ivHeadImage,bitmap);
                builder.setContent(mRemoteViews);
                startForeground(1,builder.build());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
        binder = null;
        queue.cancelAll(null);
    }

    public boolean getIsLocalMusicList() {
        return isLocalMusicList;
    }

    public ArrayList<LocalMusic> getLocalMusicList() {
        return localMusicList;
    }

    public ArrayList<Song> getOnlineMusicList() {
        return onlineMusicList;
    }

    public int getPosition() {
        return position;
    }

    public static final String ACTION_CHANGEUI = "changeUI";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PLAYORPAUSE = "playorpause";
    public static final String ACTION_CLOSE = "close";
    //具体页面的url
    public static final String URL = "http://music.baidu.com/data/music/links?songIds=";

    public CurrentPlaySong getCurrentSong() {
        if (mCurrentPlaySong != null)
            return mCurrentPlaySong;
        else {
            mCurrentPlaySong = new CurrentPlaySong("", "","","","");
            return mCurrentPlaySong;
        }
    }

    //传递给客户端MediaPlayer对象
    public MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    //获得当前播放的列表类型
    public boolean getIsLocalMusic() {
        return isLocalMusicList;
    }

    //返回当前播放的网络歌曲
    public CurrentPlaySong getCurrentOnlineSong() {
        if (mCurrentPlaySong != null)
            return mCurrentPlaySong;
        return null;
    }

}
