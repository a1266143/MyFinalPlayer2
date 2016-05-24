package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adapter.FragmentOnlineCategoryAdapter;
import com.example.application.MyApplication;
import com.example.bean.MyObject;
import com.example.pppppp.MainActivity;
import com.example.pppppp.R;
import com.example.proxy.AnaJson;
import com.example.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络歌曲界面的类别Fragment
 * 
 * @author 李晓军
 * 
 */
public class Fragment_Online_Category extends BaseFragment {

	private List<HashMap<String, String>> list;
	private BaseAdapter adapter;
	private ListView listview;
	private boolean isPrepared;
	private SharedPreferences preference;
	private SharedPreferences.Editor editor;
	// 存储每个json数据的list
	private List<HashMap<Integer, String>> everyCategoryList = new ArrayList<HashMap<Integer, String>>();

	// 记录response回调次数
	private int responseTime;

	private RequestQueue queue;

	public static final int TYPE_NEWSONG = 1;
	public static final int TYPE_HOTSONG = 2;
	public static final int TYPE_KTV = 6;
	public static final int TYPE_BILLBOARD = 8;
	public static final int TYPE_ROCK = 11;
	public static final int TYPE_YINGSHI = 14;
	public static final int TYPE_HITO = 18;
	public static final int TYPE_HUAYU = 20;
	public static final int TYPE_OUMEI = 21;
	public static final int TYPE_OLD = 22;
	public static final int TYPE_QINGGE = 23;
	public static final int TYPE_NET = 25;

	public static final int NEWSONG_INDEX = 0;
	public static final int HOTSONG_INDEX = 1;
	public static final int KTV_INDEX = 2;
	public static final int BILLBOARD_INDEX = 3;
	public static final int ROCK_INDEX = 4;
	public static final int YINGSHI_INDEX = 5;
	public static final int HITO_INDEX = 6;
	public static final int HUAYU_INDEX = 7;
	public static final int OUMEI_INDEX = 8;
	public static final int OLD_INDEX = 9;
	public static final int QINGGE_INDEX = 10;
	public static final int NET_INDEX = 11;

	// 存入SharedPreference的名称
	public static final String NEWSONG = "newsong";
	public static final String HOTSONG = "hotsong";
	public static final String KTV = "ktv";
	public static final String BILLBOARD = "billboard";
	public static final String ROCK = "rock";
	public static final String YINGSHI = "yingshi";
	public static final String HITO = "hito";
	public static final String HUAYU = "huayu";
	public static final String OUMEI = "oumei";
	public static final String OLD = "old";
	public static final String QINGGE = "qingge";
	public static final String NET = "net";
	// 类别地址
	private static final String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&method=baidu.ting.billboard.billList&type=";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication application = (MyApplication) getActivity()
				.getApplication();
		// 获取Application中的RequestQueue对象
		queue = application.getRequestQueue();
		list = new ArrayList<HashMap<String, String>>();
	}

	@Override

	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_online_category, null);
		isPrepared = true;
		lazyLoad();
		return view;
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || !isFirst)
			return;
		isFirst = false;
		// 如果是wifi联网，就每次联网获取数据
		if (Utils.isWifi(getActivity())) {
			getNetDataAndSave();
		} 
		//否则直接获取SharedPreference中的数据
		else {
			ReadSharedPreference();
			Toast.makeText(getActivity(), "没联网，读取SharedPreference中的数据", Toast.LENGTH_LONG).show();
		}

	}


	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			listview.setAdapter(adapter);
		}
	};

	// 解析数据并给当前list赋值
	private void anaDataAndEva() {
		final AnaJson a = new AnaJson();
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = new ArrayList<HashMap<String, String>>();
				// 将当前list赋值，传递给listview显示
				list = a.getCategoryList(everyCategoryList);
				// 设置listview
				listview = (ListView) view
						.findViewById(R.id.fragment_online_category_listview);
				adapter = new FragmentOnlineCategoryAdapter(getActivity(), list);
				handler.sendEmptyMessage(0x123);
				// 设置被点击监听器
				setListener();
			}
		}).start();
	}

	// 给listview设置监听器
	private void setListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				dosomething(arg2);
			}
		});
	}

	// 点击item后做的事情
	private void dosomething(int position) {
		String choice = "";
		String imagechoice = "";
		switch (position) {
		case 0:
			choice = NEWSONG;
			imagechoice = preference.getString("newsongimage","");
			break;
		case 1:
			choice = HOTSONG;
			imagechoice = preference.getString("hotsongimage","");
			break;
		case 2:
			choice = KTV;
			imagechoice = preference.getString("ktvimage","");
			break;
		case 3:
			choice = BILLBOARD;
			imagechoice = preference.getString("billboardimage","");
			break;
		case 4:
			choice = ROCK;
			imagechoice = preference.getString("rockimage","");
			break;
		case 5:
			choice = YINGSHI;
			imagechoice = preference.getString("yingshiimage","");
			break;
		case 6:
			choice = HITO;
			imagechoice = preference.getString("hitoimage","");
			break;
		case 7:
			choice = HUAYU;
			imagechoice = preference.getString("huayuimage","");
			break;
		case 8:
			choice = OUMEI;
			imagechoice = preference.getString("oumeiimage","");
			break;
		case 9:
			choice = OLD;
			imagechoice = preference.getString("oldimage","");
			break;
		case 10:
			choice = QINGGE;
			imagechoice = preference.getString("qinggeimage","");
			break;
		case 11:
			choice = NET;
			imagechoice = preference.getString("netimage","");
			break;

		default:
			break;
		}
		MyObject objj = new MyObject();
		objj.argument = choice;
		objj.imageURL = imagechoice;
		Fragment_Online_Category_More f = Fragment_Online_Category_More
				.newInstance(objj);
		FragmentTransaction tran = getActivity().getSupportFragmentManager()
				.beginTransaction();
		tran.replace(R.id.activity_main_frameLayout2, f);
		tran.addToBackStack(MainActivity.BACKSTACK);
		tran.commit();
	}

	/**
	 *  获取网络数据并存储
	 */
	private void getNetDataAndSave() {
		// 打开preference
		preference = getActivity().getSharedPreferences("category",
				Context.MODE_PRIVATE);
		// 写入SharedPreference.Editor对象
		editor = preference.edit();
		MyErrorListener errorListener = new MyErrorListener();
		//建立请求数据的StringRequest
		StringRequest request1 = new StringRequest(url + TYPE_NEWSONG,
				new ResponseListener(NEWSONG_INDEX), errorListener);
		StringRequest request2 = new StringRequest(url + TYPE_HOTSONG,
				new ResponseListener(HOTSONG_INDEX), errorListener);
		StringRequest request3 = new StringRequest(url + TYPE_KTV,
				new ResponseListener(KTV_INDEX), errorListener);
		StringRequest request4 = new StringRequest(url + TYPE_BILLBOARD,
				new ResponseListener(BILLBOARD_INDEX), errorListener);
		StringRequest request5 = new StringRequest(url + TYPE_ROCK,
				new ResponseListener(ROCK_INDEX), errorListener);
		StringRequest request6 = new StringRequest(url + TYPE_YINGSHI,
				new ResponseListener(YINGSHI_INDEX), errorListener);
		StringRequest request7 = new StringRequest(url + TYPE_HITO,
				new ResponseListener(HITO_INDEX), errorListener);
		StringRequest request8 = new StringRequest(url + TYPE_HUAYU,
				new ResponseListener(HUAYU_INDEX), errorListener);
		StringRequest request9 = new StringRequest(url + TYPE_OUMEI,
				new ResponseListener(OUMEI_INDEX), errorListener);
		StringRequest request10 = new StringRequest(url + TYPE_OLD,
				new ResponseListener(OLD_INDEX), errorListener);
		StringRequest request11 = new StringRequest(url + TYPE_QINGGE,
				new ResponseListener(QINGGE_INDEX), errorListener);
		StringRequest request12 = new StringRequest(url + TYPE_NET,
				new ResponseListener(NET_INDEX), errorListener);
		// 将所有请求加入网络请求队列中
		queue.add(request1);
		queue.add(request2);
		queue.add(request3);
		queue.add(request4);
		queue.add(request5);
		queue.add(request6);
		queue.add(request7);
		queue.add(request8);
		queue.add(request9);
		queue.add(request10);
		queue.add(request11);
		queue.add(request12);
	}

	// 返回的数据监听器
	private class ResponseListener implements Listener<String> {
		private int type;

		// 构造方法
		public ResponseListener(int type) {
			this.type = type;
		}

		@Override
		public void onResponse(String response) {

			switch (type) {
			case NEWSONG_INDEX:
				editor.putString(NEWSONG, response);
				editor.putString("newsongimage",anaTitleImage(response));
				break;
			case HOTSONG_INDEX:
				editor.putString(HOTSONG, response);
				editor.putString("hotsongimage",anaTitleImage(response));
				break;
			case KTV_INDEX:
				editor.putString(KTV, response);
				editor.putString("ktvimage",anaTitleImage(response));
				break;
			case BILLBOARD_INDEX:
				editor.putString(BILLBOARD, response);
				editor.putString("billboardimage",anaTitleImage(response));
				break;
			case ROCK_INDEX:
				editor.putString(ROCK, response);
				editor.putString("rockimage",anaTitleImage(response));
				break;
			case YINGSHI_INDEX:
				editor.putString(YINGSHI, response);
				editor.putString("yingshiimage",anaTitleImage(response));
				break;
			case HITO_INDEX:
				editor.putString(HITO, response);
				editor.putString("hitoimage",anaTitleImage(response));
				break;
			case HUAYU_INDEX:
				editor.putString(HUAYU, response);
				editor.putString("huayuimage",anaTitleImage(response));
				break;
			case OUMEI_INDEX:
				editor.putString(OUMEI, response);
				editor.putString("oumeiimage",anaTitleImage(response));
				break;
			case OLD_INDEX:
				editor.putString(OLD, response);
				editor.putString("oldimage",anaTitleImage(response));
				break;
			case QINGGE_INDEX:
				editor.putString(QINGGE, response);
				editor.putString("qinggeimage",anaTitleImage(response));
				break;
			case NET_INDEX:
				editor.putString(NET, response);
				editor.putString("netimage",anaTitleImage(response));
				break;
			}

			HashMap<Integer, String> map = new HashMap<Integer, String>();
			map.put(type, response);
			everyCategoryList.add(map);
			responseTime++;
			// 如果所有的请求都执行完毕了
			if (responseTime == 12) {
				// 记得提交
				editor.commit();
				anaDataAndEva();
				responseTime = 0;
			}
		}

	}

	/**
	 * 将榜单的图片解析出来
	 */
	private String anaTitleImage(String response){
		try {
			JSONObject obj = new JSONObject(response);
			JSONObject billboard = obj.getJSONObject("billboard");
			String imageURL = billboard.getString("pic_s640");
			return imageURL;
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}


	/**
	 * 读取SharedPreference中的数据，
	 * 并为everyCategoryList赋值
	 */
	private void ReadSharedPreference(){
		// 打开preference
		preference = getActivity().getSharedPreferences("category",
				Context.MODE_PRIVATE);
		HashMap<Integer,String> map1 = new HashMap<Integer, String>();
		HashMap<Integer,String> map2 = new HashMap<Integer, String>();
		HashMap<Integer,String> map3 = new HashMap<Integer, String>();
		HashMap<Integer,String> map4 = new HashMap<Integer, String>();
		HashMap<Integer,String> map5 = new HashMap<Integer, String>();
		HashMap<Integer,String> map6 = new HashMap<Integer, String>();
		HashMap<Integer,String> map7 = new HashMap<Integer, String>();
		HashMap<Integer,String> map8 = new HashMap<Integer, String>();
		HashMap<Integer,String> map9 = new HashMap<Integer, String>();
		HashMap<Integer,String> map10 = new HashMap<Integer, String>();
		HashMap<Integer,String> map11 = new HashMap<Integer, String>();
		HashMap<Integer,String> map12 = new HashMap<Integer, String>();

		map1.put(NEWSONG_INDEX,preference.getString(NEWSONG,""));
		map2.put(HOTSONG_INDEX,preference.getString(HOTSONG,""));
		map3.put(KTV_INDEX,preference.getString(KTV,""));
		map4.put(BILLBOARD_INDEX,preference.getString(BILLBOARD,""));
		map5.put(ROCK_INDEX,preference.getString(ROCK,""));
		map6.put(YINGSHI_INDEX,preference.getString(YINGSHI,""));
		map7.put(HITO_INDEX,preference.getString(HITO,""));
		map8.put(HUAYU_INDEX,preference.getString(HUAYU,""));
		map9.put(OUMEI_INDEX,preference.getString(OUMEI,""));
		map10.put(OLD_INDEX,preference.getString(OLD,""));
		map11.put(QINGGE_INDEX,preference.getString(QINGGE,""));
		map12.put(NET_INDEX,preference.getString(NET,""));

		everyCategoryList.add(map1);
		everyCategoryList.add(map2);
		everyCategoryList.add(map3);
		everyCategoryList.add(map4);
		everyCategoryList.add(map5);
		everyCategoryList.add(map6);
		everyCategoryList.add(map7);
		everyCategoryList.add(map8);
		everyCategoryList.add(map9);
		everyCategoryList.add(map10);
		everyCategoryList.add(map11);
		everyCategoryList.add(map12);

		anaDataAndEva();
	}

	// 网络错误的listener监听器
	private class MyErrorListener implements
			com.android.volley.Response.ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 结束所有请求队列中的请求
		queue.cancelAll(this);
	}

}
