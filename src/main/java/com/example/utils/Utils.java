package com.example.utils;

import com.example.Activity.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 工具类
 * 
 * @author 李晓军
 * 
 */
public class Utils {
	
	/**
	 * 判断是否是wifi联网
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {  
	    ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
	    if (activeNetInfo != null  
	            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	        return true;  
	    }  
	    return false;  
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param s
	 */
	public static void showToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 关闭输入法
	 */
	public static void closeInputMethod(Context context,EditText edt) {
		InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
		//imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
	/**
	 * 打开新的fragment
	 */
	public static void openNewFragment(FragmentTransaction tran,int res, Fragment fragment){
		tran.replace(res, fragment);
		tran.addToBackStack(null);
		tran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		tran.commit();
	}
	
	/**
	 * 以动画方式打开菜单fragment
	 */
	public static void openMenu(FragmentTransaction tran,int res,Fragment fragment){
		tran.setCustomAnimations(R.anim.enter, 0,0,R.anim.out1);
		tran.add(res, fragment);
		tran.addToBackStack(null);
		tran.commit();
	}

	/**
	 * 图片高斯模糊
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static Bitmap blurBitmap(Context context, Bitmap bitmap){
		//Let's create an empty bitmap with the same size of the bitmap we want to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		//Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(context);
		//Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		//Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
		//Set the radius of the blur
		blurScript.setRadius(25.f);
		//Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);
		//Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);
		//recycle the original bitmap
		bitmap.recycle();
		//After finishing everything, we destroy the Renderscript.
		rs.destroy();
		return outBitmap;
	}

	/**
	 * 获取通知栏的高度
	 * @return
     */
	public static int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(
				Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
	}

	/**
	 * 设置状态栏透明
	 */
	public static void setTran(Activity activity){
		activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
}
