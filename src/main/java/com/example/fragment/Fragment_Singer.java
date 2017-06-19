package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.application.MyApplication;
import com.example.bean.Singer;
import com.example.bean.Song;
import com.example.Activity.R;
import com.example.service.MainService;
import com.example.utils.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Singer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Singer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Singer> list;
    private ArrayList<Song> songList;
    private int position;

    private BaseAdapter adapter;


    public Fragment_Singer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_Singer.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Singer newInstance(ArrayList<Singer> list, int position) {
        Fragment_Singer fragment = new Fragment_Singer();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,list);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.list = (ArrayList<Singer>) getArguments().getSerializable(ARG_PARAM1);
            this.position = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__singer,container,false);
        findview(view);
        initValue();
        return view;
    }

    private ImageButton returnBtn;
    private TextView tv_songname;
    private ListView listview;
    private void findview(View view){
        this.returnBtn = (ImageButton) view.findViewById(R.id.fragment_singer_returnbtn);
        this.tv_songname = (TextView) view.findViewById(R.id.fragment_singer_tvname);
        this.listview = (ListView) view.findViewById(R.id.fragment_singer_listview);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private View headerview;
    private VolleyUtils volleyUtils;
    /**
     * 初始化
     */
    private void initValue(){
        this.tv_songname.setText(list.get(position).getName());
        MyApplication application = (MyApplication) getActivity().getApplication();
        volleyUtils = new VolleyUtils(application.getRequestQueue(),getActivity());
        connectNet4Data();
        headerview = getActivity().getLayoutInflater().inflate(R.layout.onlinemusic_headerview,null);
        ImageView imageView = (ImageView) headerview.findViewById(R.id.onlinemusic_headerview_imageview);
        //载入网络图片
        Glide.with(this).load(list.get(position).getPictureUrl()).into(imageView);
        listview.addHeaderView(headerview);

    }

    /**
     * 连接网络获取数据
     */
    private void connectNet4Data(){
        String newUrl = URL + list.get(position).getTinguid();
        volleyUtils.newRequest(newUrl, new VolleyUtils.OnResponseListener() {
            @Override
            public void response(JSONObject jsonObject) {
                //解析数据
                songList = anaData(jsonObject);
                setListAdapter();
                setListener();
            }

            @Override
            public void error(VolleyError volleyError) {

            }
        });
    }

    /**
     * 解析数据
     * @param jsonObject
     */
    private ArrayList<Song> anaData(JSONObject jsonObject){
        ArrayList<Song> arr = new ArrayList<>();
        try {
            JSONArray songList = jsonObject.getJSONArray("songlist");
            for(int i=0;i<songList.length();i++){
                Song song = new Song();
                JSONObject object = songList.getJSONObject(i);
                song.setTitle(object.getString("title"));
                song.setAuthor(object.getString("author"));
                song.setArtistid(object.getString("artist_id"));
                song.setTinguid(object.getString("ting_uid"));
                song.setSongid(object.getString("song_id"));
                arr.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return arr;
        }
        return arr;

    }

    private void setListAdapter(){
        MyBaseAdapter adapter = new MyBaseAdapter();
        listview.setAdapter(adapter);
    }

    private void setListener(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int arg0, long id) {
                Intent intent = new Intent(getActivity(), MainService.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLocalMusicList",false);
                bundle.putSerializable("List",songList);
                bundle.putInt("position",arg0-1);
                intent.putExtras(bundle);
                getActivity().startService(intent);
            }
        });
    }

    /**
     * 列表适配器
     */
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public Object getItem(int position) {
            return songList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.listitem_fragmentcategorymore,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_songname = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv1);
                viewHolder.tv_author = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv2);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_songname.setText(songList.get(position).getTitle());
            viewHolder.tv_author.setText(songList.get(position).getAuthor());
            return convertView;
        }

        class ViewHolder{
            TextView tv_songname,tv_author;
        }
    }


    //具体歌手歌曲URL
    public static final String URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=2.4.0&method=baidu.ting.artist.getSongList&format=json&order=2&tinguid=";

}
