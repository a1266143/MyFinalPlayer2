package com.example.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.Activity.MainActivity;
import com.example.Activity.R;
import com.example.Dagger.Component.DaggerServiceComponent;
import com.example.Dagger.Module.ServiceModule;
import com.example.application.MyApplication;
import com.example.bean.CurrentOnlineSong;
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
    Notification.Builder builder;
    @Inject
    RemoteViews mRemoteViews;

    MyBinder binder;
    private boolean isLocalMusicList = true;
    private int position;
    private CurrentOnlineSong currentOnlineSong;
    private CurrentPlaySong mCurrentPlaySong;
    //播放模式旗标
    private static final int PLAYLOOP = 1;
    private static final int SHUFFLE = 2;
    private static final int SINGLELOOP = 3;
    //播放模式，默认循环模式
    private int currentPlayModel = PLAYLOOP;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        DaggerServiceComponent.builder().serviceModule(new ServiceModule((MyApplication) getApplication(), getApplicationContext())).build().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 客户端与此服务的接口
     */
    public class MyBinder extends Binder {

        //将此service实例传给客户端
        public MainService getService() {
            return MainService.this;
        }

        public CurrentPlaySong getCurrentSong() {
            if (mCurrentPlaySong != null)
                return mCurrentPlaySong;
            else {
                mCurrentPlaySong = new CurrentPlaySong("", "");
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

        //获得网络音乐列表
        public ArrayList<Song> getOnlineMusicList() {
            return onlineMusicList;
        }

        //获得本地音乐列表
        public ArrayList<LocalMusic> getLocalMusicList() {
            return localMusicList;
        }

        //返回当前播放的网络歌曲
        public CurrentOnlineSong getCurrentOnlineSong() {
            if (currentOnlineSong != null)
                return currentOnlineSong;
            return null;
        }
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
        startForgroundService();
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
     * 开启前台Service
     */
    private void startForgroundService() {
        mRemoteViews.setImageViewResource(R.id.notification_layout_ivHeadImage, R.drawable.ic_launcher);
        mRemoteViews.setTextViewText(R.id.notification_layout_tvSong,onlineMusicList.get(position).getTitle());
        mRemoteViews.setTextViewText(R.id.notification_layout_tvSinger, onlineMusicList.get(position).getAuthor());
        builder.setContent(mRemoteViews);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        startForeground(1,notification);

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
            setCurrentPlaySong(localMusicList.get(position).getSongName(), localMusicList.get(position).getAuthor());
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
        volleyUtils.newRequest(requestURL, new VolleyUtils.OnResponseListener() {
            @Override
            public void response(JSONObject jsonObject) {
                try {
                    JSONObject object = jsonObject.getJSONObject("data");
                    JSONArray array = object.getJSONArray("songList");
                    JSONObject object1 = array.getJSONObject(0);
                    currentOnlineSong = new CurrentOnlineSong(object1.optString("songName"), object1.optString("artistName"), object1.optString("songPicRadio"), object1.optString("lrcLink"),
                            object1.getString("songLink"));

                    //播放网络音乐
                    playOnlineMusic(currentOnlineSong.getSongLink());
                    sendBroadCastToUI();
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
            setCurrentPlaySong(onlineMusicList.get(position).getTitle(), onlineMusicList.get(position).getAuthor());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", "网络出现问题");
            return;
        }
    }

    private void setCurrentPlaySong(String songName, String author) {
        mCurrentPlaySong = new CurrentPlaySong(songName, author);
    }


    /**
     * 发送广播给前台,将歌名和歌手传递给前台，通知其修改界面
     */
    private void sendBroadCastToUI() {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        if (isLocalMusicList) {
            b.putString("title", localMusicList.get(position).getSongName());
            b.putString("author", localMusicList.get(position).getAuthor());
            b.putBoolean("isLocalMusic", true);
        } else {
            b.putString("title", onlineMusicList.get(position).getTitle());
            b.putString("author", onlineMusicList.get(position).getAuthor());
            b.putBoolean("isLocalMusic", false);
            b.putSerializable("currentOnlineSong", currentOnlineSong);

            changeNotification();

        }
        intent.putExtras(b);
        intent.setAction(MainService.ACTION_CHANGEUI);
        sendBroadcast(intent);
    }

    private void changeNotification(){
        Glide.with(MainService.this).load(currentOnlineSong.getSongPicRadio()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                mRemoteViews.setImageViewBitmap(R.id.notification_layout_ivHeadImage,bitmap);
                builder.setTicker(currentOnlineSong.getSongName());
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
    //具体页面的url
    public static final String URL = "http://music.baidu.com/data/music/links?songIds=";


}
