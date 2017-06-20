package com.example.service;

import android.os.Binder;

/**
 * 用于Service与其他绑定的组件通信的Binder
 * 创建人 xiaojun
 * 创建时间 2017/6/20-10:10
 */

public class MyBinder extends Binder {
    private MainService service;

    public MyBinder(MainService service){
        this.service = service;
    }

    //将此service实例传给客户端
    public MainService getService() {
        return service;
    }
}
