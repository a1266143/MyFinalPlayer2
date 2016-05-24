package com.example.fragment;

import com.example.pppppp.R;
import com.example.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class Fragment_Search extends Fragment {
	public static final String ARGUMENT = "argument";
	
	private EditText searchEditText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		if(b!=null){
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = 	inflater.inflate(R.layout.fragment_search, null);
		findview(view);
		return view;
	}
	
	public void findview(View view){
		view.findViewById(R.id.fragment_search_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//关闭当前fragment,将fragment弹出栈
				getActivity().getSupportFragmentManager().popBackStack();
				Utils.closeInputMethod(getActivity(),searchEditText);
			}
		});
		searchEditText = (EditText) view.findViewById(R.id.fragment_search_edittext);
	}

	/**
	 * 通过这个方法可以在新建这个fragment对象的同时
	 * 可以进行传值
	 * @param argument
	 * @return
	 */
	public static Fragment_Search newInstance(String argument){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);
		Fragment_Search mFragment_Three = new Fragment_Search();
		mFragment_Three.setArguments(bundle);
		return mFragment_Three;
	}
}
