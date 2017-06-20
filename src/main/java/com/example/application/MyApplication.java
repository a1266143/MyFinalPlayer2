package com.example.application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.service.MainService;
import com.example.service.MyBinder;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

public class MyApplication extends Application {

	// 初始化volley
	private RequestQueue mRequestQueue;
	//从Service传过来的Binder
	private MyBinder myBinder;
	@Override
	public void onCreate() {
		super.onCreate();
		bindMyService();
		mRequestQueue = Volley.newRequestQueue(this);
	}

	/**
	 * 绑定service
	 */
	private void bindMyService() {

		Intent service = new Intent(this, MainService.class);
		ServiceConnection conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				Toast.makeText(MyApplication.this, "服务已断开", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder ibinder) {
				//获取从service传过来的binder对象
				myBinder = (MyBinder) ibinder;
			}
		};
		// 绑定服务
		bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	/*
	 * 返回全局的RequestQueue对象 主要是进行网络操作
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue != null)
			return mRequestQueue;
		else
			return null;
	}
	
	

}
