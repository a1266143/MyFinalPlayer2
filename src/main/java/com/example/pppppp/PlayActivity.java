package com.example.pppppp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.utils.Utils;

public class PlayActivity extends Activity {

    private RelativeLayout parentRelativeLayout;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTran();
        setContentView(R.layout.activity_play);
        findview();
    }

    private void findview(){
        parentRelativeLayout = (RelativeLayout) findViewById(R.id.activity_play_re);
        backgroundImage = (ImageView) findViewById(R.id.activity_play_image);
        //设置margintop
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentRelativeLayout.getLayoutParams();
        params.topMargin = Utils.getStatusBarHeight();
        parentRelativeLayout.setLayoutParams(params);
    }


    /**
     * 设置状态栏透明
     */
    private void setTran(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
