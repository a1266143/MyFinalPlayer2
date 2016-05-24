package com.example.pppppp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(getColor());
		init();
		findview();
		setListener();
	}

	/**
	 * 初始化方法
	 */
	public void init(){}
	
	/**
	 * 设置状态栏的颜色
	 * @return
	 */
	public abstract int getColor();
	
	/**
	 * 设置contentview
	 */
	public abstract void setContentView();
		
	
	/**
	 * 找view
	 */
	public abstract void findview();
	
	/**
	 * 设置监听器
	 */
	public abstract void setListener();
	
	/**
	 * 设置沉浸式状态栏
	 * @param on
	 */
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	
}
