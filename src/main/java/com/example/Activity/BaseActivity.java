package com.example.Activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.utils.Utils;

/**
 * 基础Activity
 */
public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setTran(this);
		setContentView();
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
