package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bean.LocalMusic;
import com.example.bean.Song;
import com.example.Activity.MainActivity;
import com.example.Activity.R;
import com.example.service.MainService;

import java.util.ArrayList;


public class Fragment_List extends Fragment {
    //本列表菜单是否打开
    public static boolean isOpen;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "isLocalMusic";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private ArrayList<LocalMusic> localMusicList;
    private ArrayList<Song> onlineMusicList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_List newInstance(boolean isLocalMusic, ArrayList<LocalMusic> localMusicList,ArrayList<Song> onlineMusicList) {
        Fragment_List fragment = new Fragment_List();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1,isLocalMusic);
        args.putSerializable(ARG_PARAM2,localMusicList);
        args.putSerializable(ARG_PARAM3,onlineMusicList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isOpen = true;
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private ListView listview;
    private RelativeLayout re;
    private Button closeBtn;
    private MainService mainService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__list,null);
        init();
        findview(view);
        setAdapter();
        setListener();
        setScrolltoPosition();
        setPlayingIcon();
        return view;
    }

    private void init(){
        MainActivity activity = (MainActivity) getActivity();
        //获取service对象
        mainService = activity.getMyBinder().getService();
    }

    private void findview(View view){
        listview = (ListView) view.findViewById(R.id.fragment_list_listview);
        re = (RelativeLayout) view.findViewById(R.id.fragment_list_re);
        closeBtn = (Button) view.findViewById(R.id.fragment_list_shutdown);
    }

    private void setListener(){
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭当前fragment
                getFragmentManager().popBackStack();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(position);
            }
        });
    }

    /**
     * listview被点击
     * @param position
     */
    private void itemClick(int position){
        Intent intent = new Intent(getActivity(), MainService.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLocalMusicList",mainService.getIsLocalMusicList());
        if(mainService.getIsLocalMusicList())
            bundle.putSerializable("List",mainService.getLocalMusicList());
        else
            bundle.putSerializable("List",mainService.getOnlineMusicList());
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }


    /**
     * 设置listview的Adapter
     */
    private void setAdapter(){

        if(mainService.getIsLocalMusicList()){
            localMusicList = mainService.getLocalMusicList();
            if(localMusicList.size()!=0){
                BaseAdapter adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return localMusicList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return localMusicList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder = null;
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        if(convertView==null){
                            viewHolder = new ViewHolder();
                            convertView = inflater.inflate(R.layout.listitem_fragmentcategorymore,null);
                            TextView tv_songName = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv1);
                            TextView tv_author = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv2);
                            ImageButton moreBtn = (ImageButton) convertView.findViewById(R.id.listitem_fragmentcategorymore_morebtn);
                            ImageView playingicon = (ImageView) convertView.findViewById(R.id.listitem_fragmentcategorymore_playingicon);
                            viewHolder.tv_songName = tv_songName;
                            viewHolder.tv_author = tv_author;
                            viewHolder.tv_songName.setTextColor(Color.WHITE);
                            viewHolder.moreBtn = moreBtn;
                            //viewHolder.playingicon = playingicon;
                            convertView.setTag(viewHolder);
                        }else{
                            viewHolder = (ViewHolder) convertView.getTag();
                        }
                        viewHolder.tv_songName.setText(localMusicList.get(position).getSongName());
                        viewHolder.tv_author.setText(localMusicList.get(position).getAuthor());
                        viewHolder.moreBtn.setVisibility(View.GONE);
                        return convertView;
                    }

                    class ViewHolder{
                        TextView tv_songName,tv_author;
                        ImageButton moreBtn;
                        ImageView playingicon;
                    }
                };
                listview.setAdapter(adapter);
            }
        }else{
            onlineMusicList = mainService.getOnlineMusicList();
            if(onlineMusicList.size()!=0){
                BaseAdapter adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return onlineMusicList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return onlineMusicList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder = null;
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        if(convertView==null){
                            viewHolder = new ViewHolder();
                            convertView = inflater.inflate(R.layout.listitem_fragmentcategorymore,null);
                            TextView tv_songName = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv1);
                            TextView tv_author = (TextView) convertView.findViewById(R.id.listitem_fragmentcategorymore_tv2);
                            ImageButton moreBtn = (ImageButton) convertView.findViewById(R.id.listitem_fragmentcategorymore_morebtn);
                            viewHolder.tv_songName = tv_songName;
                            viewHolder.tv_author = tv_author;
                            viewHolder.tv_songName.setTextColor(Color.WHITE);
                            viewHolder.moreBtn = moreBtn;
                            convertView.setTag(viewHolder);
                        }else{
                            viewHolder = (ViewHolder) convertView.getTag();
                        }
                        viewHolder.tv_songName.setText(onlineMusicList.get(position).getTitle());
                        viewHolder.tv_author.setText(onlineMusicList.get(position).getAuthor());
                        viewHolder.moreBtn.setVisibility(View.GONE);
                        return convertView;
                    }

                    class ViewHolder{
                        TextView tv_songName,tv_author;
                        ImageButton moreBtn;
                    }
                };
                listview.setAdapter(adapter);
            }

        }
    }

    private void setScrolltoPosition(){
        listview.setSelection(mainService.getPosition());
    }

    private void setPlayingIcon(){
        //listview.getChildAt(mainService.getPosition() - listview.getFirstVisiblePosition()).findViewById(R.id.listitem_fragmentcategorymore_playingicon).setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isOpen = false;
    }

}
