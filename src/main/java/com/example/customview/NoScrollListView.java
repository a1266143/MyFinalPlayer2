package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoScrollListView extends ListView {

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context) {
		super(context);
	}

	/**
	 * 重写listview让其失去滑动性
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
	
	

}
