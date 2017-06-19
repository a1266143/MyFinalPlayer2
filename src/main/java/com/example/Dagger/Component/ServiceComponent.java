package com.example.Dagger.Component;

import com.example.Dagger.Module.ServiceModule;
import com.example.service.MainService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 创建人 xiaojun
 * 创建时间 2017/6/19-15:07
 */
@Singleton
@Component(modules = ServiceModule.class)
public interface ServiceComponent {
    void inject(MainService service);
}
