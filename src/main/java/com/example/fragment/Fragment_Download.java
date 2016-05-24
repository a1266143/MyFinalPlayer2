package com.example.fragment;

import com.example.pppppp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Fragment_Download extends Fragment {

	private ImageButton returnBtn;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_download, null);
		findview(view);
		return view;
	}
	
	private void findview(View view){
		returnBtn = (ImageButton) view.findViewById(R.id.fragment_download_return);
		returnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goBack();
			}
		});
	}
	
	//关闭此Fragment，弹出栈
	private void goBack(){
		getActivity().getSupportFragmentManager().popBackStack();
	}
	
}
