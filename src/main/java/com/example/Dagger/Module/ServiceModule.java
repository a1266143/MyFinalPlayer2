package com.example.Dagger.Module;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.example.Activity.MainActivity;
import com.example.Activity.R;
import com.example.application.MyApplication;
import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.service.MainService;
import com.example.service.MyBinder;
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

    public ServiceModule(MyApplication application, Context context) {
        this.application = application;
        this.context = context;
    }

    @Named("LocalMusic")
    @Singleton
    @Provides
    public ArrayList<LocalMusic> providerLocalMusicList() {
        return new ArrayList<>();
    }

    @Named("Song")
    @Singleton
    @Provides
    public ArrayList<Song> providerOnlineMusicList() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    public MediaPlayer providerMediaPlayer() {
        return new MediaPlayer();
    }

    @Singleton
    @Provides
    public RequestQueue providerRequestQueue() {
        return application.getRequestQueue();
    }

    @Singleton
    @Provides
    public VolleyUtils providerVolleyUtils(RequestQueue queue) {
        return new VolleyUtils(queue, context);
    }

    @Singleton
    @Provides
    public NotificationCompat.Builder providerBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("title");
        builder.setContentText("contenttext");
        builder.setSmallIcon(R.drawable.app_notification);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon));
        builder.setColor(Color.parseColor("#FF0000"));
        /*Notification.InboxStyle inboxStyle =  new Notification.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
// Moves events into the expanded layout
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        builder.setStyle(inboxStyle);*/

        //builder.setTicker("Hello RemotesViews!");// 收到通知的时候用于显示于屏幕顶部通知栏的内容
        //builder.setSmallIcon(R.drawable.appicon);// 设置通知小图标,在下拉之前显示的图标
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon));// 落下后显示的图标
        //builder.setWhen(System.currentTimeMillis());
        //builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等
        //builder.setAutoCancel(true);// 自动取消
        //builder.setOnlyAlertOnce(true);// 只alert一次
        //builder.setDefaults(Notification.DEFAULT_ALL);

        //设置点击跳转到的Activity
        Intent intent = new Intent(context, MainActivity.class);
        //intent.setPackage(context.getPackageName());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        //设置自定义View
        //builder.setContent(mRemoteViews);
        return builder;
    }

    @Singleton
    @Provides
    public RemoteViews providerRemoteViews() {
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        return mRemoteViews;
    }

    @Singleton
    @Provides
    public MyBinder providerMyBinder(MainService service) {
        return new MyBinder(service);
    }

    @Singleton
    @Provides
    public MainService providerMainService() {
        return (MainService) context;
    }

    @Singleton
    @Named("nextPendingIntent")
    @Provides
    public PendingIntent providerNextPendingIntent() {
        Intent intent = new Intent(MainService.ACTION_NEXT);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Singleton
    @Named("playOrPause")
    @Provides
    public PendingIntent providerPlayOrPausePendingIntent() {
        Intent intent = new Intent(MainService.ACTION_PLAYORPAUSE);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Singleton
    @Named("close")
    @Provides
    public PendingIntent providerClosePendingIntent() {
        Intent intent = new Intent(MainService.ACTION_CLOSE);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}
