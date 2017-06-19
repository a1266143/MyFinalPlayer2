package com.example.Dagger.Module;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.example.Activity.MainActivity;
import com.example.Activity.PlayActivity;
import com.example.Activity.R;
import com.example.application.MyApplication;
import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.utils.VolleyUtils;

import java.util.ArrayList;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 创建人 xiaojun
 * 创建时间 2017/6/19-14:57
 */
@Module
public class ServiceModule {

    MyApplication application;
    Context context;
    public ServiceModule(MyApplication application,Context context){
        this.application = application;
        this.context = context;
    }

    @Named("LocalMusic")
    @Singleton
    @Provides
    public ArrayList<LocalMusic> providerLocalMusicList() {
        return new ArrayList<LocalMusic>();
    }

    @Named("Song")
    @Singleton
    @Provides
    public ArrayList<Song> providerOnlineMusicList() {
        return new ArrayList<Song>();
    }

    @Singleton
    @Provides
    public MediaPlayer providerMediaPlayer(){
        return new MediaPlayer();
    }

    @Singleton
    @Provides
    public RequestQueue providerRequestQueue(){
        return application.getRequestQueue();
    }

    @Singleton
    @Provides
    public VolleyUtils providerVolleyUtils(RequestQueue queue){
        return new VolleyUtils(queue, context);
    }

    @Singleton
    @Provides
    public Notification.Builder providerBuilder(RemoteViews mRemoteViews){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker("Hello RemotesViews!");// 收到通知的时候用于显示于屏幕顶部通知栏的内容
        builder.setSmallIcon(R.drawable.appicon);// 设置通知小图标,在下拉之前显示的图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon));// 落下后显示的图标
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等
        //builder.setAutoCancel(true);// 自动取消
        //builder.setOnlyAlertOnce(true);// 只alert一次
        //builder.setDefaults(Notification.DEFAULT_ALL);

        //设置点击跳转到的Activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        //设置自定义View
        //builder.setContent(mRemoteViews);
        return builder;
    }

    @Singleton
    @Provides
    public RemoteViews providerRemoteViews(){
        RemoteViews mRemoteViews =  new RemoteViews(context.getPackageName(),R.layout.notification_layout);
        return mRemoteViews;
    }

}
