package com.example.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 延迟加载的基本Fragment
 * @author 李晓军
 */
public abstract class BaseFragment extends Fragment {

	protected boolean isVisible;
	protected boolean isFirst = true;
	protected View view;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
        }
	}
	
	protected void onVisible(){  
        lazyLoad();  
    }

	//懒加载
	protected abstract void lazyLoad();
	
}
