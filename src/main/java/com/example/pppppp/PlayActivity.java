package com.example.pppppp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bean.CurrentOnlineSong;
import com.example.service.MainService;
import com.example.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlayActivity extends Activity {

    private RelativeLayout parentRelativeLayout;
    private ImageView backgroundImage;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTran();
        setContentView(R.layout.activity_play);
        init();
        findview();
        setListener();
        bindMyService();
    }

    private void init(){
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
            private void init(IBinder ibinder){
                //获取从service传过来的binder对象
                myBinder = (MainService.MyBinder) ibinder;
                //song = myBinder.getCurrentSong();
                player = myBinder.getPlayer();
                isLocalMusic = myBinder.getIsLocalMusic();
                if(isLocalMusic){
                    //localMusicList = myBinder.getLocalMusicList();
                }else{
                   // onlineMusicList = myBinder.getOnlineMusicList();
                    mCurrentOnlineSong = myBinder.getCurrentOnlineSong();
                    Glide.with(PlayActivity.this).load(mCurrentOnlineSong.getSongPicRadio()).into(backgroundImage);

                }
            }
        };
        // 绑定服务
        bindService(service, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn;
    private MainService.MyBinder myBinder;
    private MediaPlayer player;
    private boolean isLocalMusic;
    private CurrentOnlineSong mCurrentOnlineSong;

    private void findview(){
        parentRelativeLayout = (RelativeLayout) findViewById(R.id.activity_play_re);
        backgroundImage = (ImageView) findViewById(R.id.activity_play_image);
        backBtn = (ImageButton) findViewById(R.id.activity_play_back);
        //设置margintop
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentRelativeLayout.getLayoutParams();
        params.topMargin = Utils.getStatusBarHeight();
        parentRelativeLayout.setLayoutParams(params);
    }

    private void setListener(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.this.finish();
            }
        });
    }


    /**
     * 设置状态栏透明
     */
    private void setTran(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
