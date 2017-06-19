package com.example.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.bean.CurrentOnlineSong;
import com.example.service.MainService;
import com.example.utils.Utils;
import com.kogitune.activity_transition.ActivityTransition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

public class PlayActivity extends Activity {

    public static String SONG_IMAGE = "";

    private ServiceConnection conn;
    private MainService.MyBinder myBinder;
    private MediaPlayer player;
    private boolean isLocalMusic;
    private CurrentOnlineSong mCurrentOnlineSong;

    @BindView(R.id.activity_play_ivHead)
    ImageView headImageView;
    @BindView(R.id.activity_play_re)
    RelativeLayout parentRelativeLayout;
    @BindView(R.id.activity_play_image)
    ImageView backgroundImage;
    @BindView(R.id.activity_play_back)
    ImageButton backBtn;

    @OnClick(R.id.activity_play_back)
    void back(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTran(this);
        setContentView(R.layout.activity_play);
        init(savedInstanceState);
        bindMyService();
    }

    private void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentRelativeLayout.getLayoutParams();
        params.topMargin = Utils.getStatusBarHeight();
        parentRelativeLayout.setLayoutParams(params);
        ActivityTransition.with(getIntent()).to(findViewById(R.id.activity_play_ivHead)).start(savedInstanceState);
        if (!TextUtils.isEmpty(PlayActivity.SONG_IMAGE))
            Glide.with(PlayActivity.this).load(PlayActivity.SONG_IMAGE).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    headImageView.setImageBitmap(bitmap);
                    Blurry.with(getApplicationContext()).radius(100).animate(3000).from(bitmap).into(backgroundImage);
                }
            });
    }

    /**
     * 绑定service
     */
    private void bindMyService() {
        Intent service = new Intent(this, MainService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                //Utils.showToast(MainActivity.this,"与Service的连接已断开");
            }

            @Override
            public void onServiceConnected(ComponentName arg0, IBinder ibinder) {
                init(ibinder);
                //setState(song.getSongName(),song.getAuthor(),player);
            }

            /**
             * 在bindService成功的时候初始化各个变量
             * @param ibinder
             */
            private void init(IBinder ibinder) {
                //获取从service传过来的binder对象
                myBinder = (MainService.MyBinder) ibinder;
                //song = myBinder.getCurrentSong();
                player = myBinder.getPlayer();
                isLocalMusic = myBinder.getIsLocalMusic();
                if (isLocalMusic) {
                    //localMusicList = myBinder.getLocalMusicList();
                } else {
                    // onlineMusicList = myBinder.getOnlineMusicList();
                    mCurrentOnlineSong = myBinder.getCurrentOnlineSong();
                    //Glide.with(PlayActivity.this).load(mCurrentOnlineSong.getSongPicRadio()).into(headImageView);
                }
            }
        };
        // 绑定服务
        bindService(service, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
    }
}
