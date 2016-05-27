package com.example.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by 李晓军 on 2016/5/24.
 * 此类封装了相关Volley操作
 */
public class VolleyUtils {

    private RequestQueue queue;
    private Context context;
    public VolleyUtils(RequestQueue queue,Context context){
        this.queue = queue;
        this.context = context;
    }

    /**
     * 新建一个request
     * 加入队列
     */
    public void newRequest(String url, final OnResponseListener listener){
        queue.cancelAll(context);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                listener.response(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.error(volleyError);
            }
        });
        queue.add(request);
    }

    /**
     * 取消请求
     */
    public void cancleRequest(Context context){
        queue.cancelAll(context);
    }

    /**
     * 回调接口
     * 传递参数给外部类
     */
    public interface OnResponseListener{
        void response(JSONObject jsonObject);
        void error(VolleyError volleyError);
    }
}
