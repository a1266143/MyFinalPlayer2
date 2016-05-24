package com.example.listener;

import com.example.pppppp.R;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageButton;

public class MyPageChangeListener implements OnPageChangeListener {

	private Context context;
	private ImageButton localmusicBtn,onlinemusicBtn;
	

	public MyPageChangeListener(Context context, ImageButton localmusicBtn,
			ImageButton onlinemusicBtn) {
		super();
		this.context = context;
		this.localmusicBtn = localmusicBtn;
		this.onlinemusicBtn = onlinemusicBtn;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		//本地音乐
		case 0:
			localmusicBtn.setImageResource(R.drawable.localmusic_selected);
			onlinemusicBtn.setImageResource(R.drawable.onlinemusic);
			break;
			//在线音乐
		case 1:
			localmusicBtn.setImageResource(R.drawable.localmusic);
			onlinemusicBtn.setImageResource(R.drawable.onlinemusic_selected);
			break;
		}
	}

}
