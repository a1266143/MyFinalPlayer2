package com.example.proxy;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.application.MyApplication;

/**
 * 处理网络请求的类
 * 
 * @author 李晓军
 * 
 */
public class NetWork {
	/**
	 * 榜单基础url
	 */
	public static final String URL_LIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&method=baidu.ting.billboard.billList&type=";
	// 新歌榜
	public static final int TYPE_NEWSONG = 1;
	// 热歌榜
	public static final int TYPE_HOTSONG = 2;
	// ktv热歌榜
	public static final int TYPE_KTV = 6;
	// 叱咤风云榜
	public static final int TYPE_WIND = 7;
	// billboard榜
	public static final int TYPE_BILLBOARD = 8;
	// 摇滚榜
	public static final int TYPE_ROCK = 11;
	// 影视金曲榜
	public static final int TYPE_MOVIE = 14;
	// Hito中文榜
	public static final int TYPE_HITO = 18;
	// 华语金曲榜
	public static final int TYPE_CHINESE = 20;
	// 欧美金曲榜
	public static final int TYPE_EUROPE = 21;
	//经典老歌
	public static final int TYPE_OLDSONG = 22;
	// 情歌对唱榜
	public static final int TYPE_LOVESONG = 23;
	// 网络歌曲榜
	public static final int TYPE_NETSONG = 25;

	/**
	 * 获取json数据
	 * 
	 * @param url
	 * @param application
	 */
	public static void NetForJson(final int TYPE, MyApplication application,
			final CallBack_Net mCallBack_Net) {
		String url = URL_LIST + TYPE;
		JsonObjectRequest jr = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// 将此对象传回给fragment
						mCallBack_Net.getJSONObject(response,TYPE);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// 将错误返回给fragment
						mCallBack_Net.onErrorResponse(error);
					}
				});
		// 将此任务加入队列
		application.getRequestQueue().add(jr);
	}

	public interface CallBack_Net {
		// 回调此方法将网络获取的JSONObject回调给某个类
		void getJSONObject(JSONObject response,int type);
		// 回调此方法说明网络连接失败
		void onErrorResponse(VolleyError error);
	}
}
