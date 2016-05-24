package com.example.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

import com.example.bean.Album;
import com.example.bean.LocalMusic;

public class LocalMusicUtils {

	//不能设置为静态变量
	private ArrayList<LocalMusic> localMusicList;
	
	/**
	 * 外部存储卡上的音频文件URI
	 */
	public static final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	
	//歌名
	private static final String SONGNAME = MediaStore.Audio.Media.TITLE;
	//歌手
	private static final String AUTHOR = MediaStore.Audio.Media.ARTIST;
	//路径
	private static final String PATH = MediaStore.Audio.Media.DATA;
	//时长
	private static final String DURATION = MediaStore.Audio.Media.DURATION;
	//大小
	private static final String SIZE = MediaStore.Audio.Media.SIZE;
	//专辑名
	private static final String ALBUM = MediaStore.Audio.AudioColumns.ALBUM;
	
	/**
	 * 获取本地音乐列表
	 */
	public ArrayList<LocalMusic> getLocalMusicArr(Context context){
		if(localMusicList!=null)
			localMusicList.clear();
		localMusicList = new ArrayList<LocalMusic>();
		Cursor cursor = context.getContentResolver().query(URI, null, null, null, null);
		while(cursor.moveToNext()){
			LocalMusic localMusic = new LocalMusic();
			localMusic.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(SONGNAME)));
			localMusic.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)));
			localMusic.setPath(cursor.getString(cursor.getColumnIndexOrThrow(PATH)));
			localMusic.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DURATION)));
			localMusic.setSize(cursor.getString(cursor.getColumnIndexOrThrow(SIZE)));
			localMusic.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(ALBUM)));
			if(cursor.getInt(cursor.getColumnIndexOrThrow(Media.DURATION))>60000&&cursor.getInt(cursor.getColumnIndexOrThrow(Media.IS_MUSIC))==1&&
					!(cursor.getString(cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME))).endsWith(".amr"))
				localMusicList.add(localMusic);
		}
		if(!cursor.isClosed())
			cursor.close();
		return localMusicList;
	}
	
	/**
	 * 获取本地音乐专辑列表
	 */
	public List<Album> getLocalMusicAlbum(Context context){
		//利用Set集合，不会重复数据
		Set<String> set = new HashSet<String>();
		for(LocalMusic localMusic:getLocalMusicArr(context)){
			set.add(localMusic.getAlbum());
		}
		//专辑名称列表
		List<String> list = new ArrayList<String>(set);
		getNumFromAlbum(context, "原来我是第三者");
		List<Album> albumList = new ArrayList<Album>();
		for(String s:list){
			Album mAlbum = new Album();
			mAlbum.setAlbumName(s);
			mAlbum.setAlbumNumber(""+getNumFromAlbum(context, s));
			albumList.add(mAlbum);
		}
		return albumList;
	}
	
	/**
	 * 通过专辑名称返回歌曲数量
	 */
	public int getNumFromAlbum(Context context,String albumName){
		//查询的列数,第三个参数是列名称，第四个参数（如果第三个参数有？占位符）将替代占位符
		Cursor cursor = context.getContentResolver().query(URI, null, Media.ALBUM+"=?" , new String[]{albumName}, null);
		int i=0;
		while(cursor.moveToNext()){
			i += 1;
		}
		if(!cursor.isClosed())
			cursor.close();
		return i;
	}
	
	/**
	 * 获取本地总共几首音乐
	 * @param context
	 * @return
	 */
	public int getArrSize(Context context){
		return getLocalMusicArr(context).size();
	}
	
	/**
	 * 返回localList表
	 */
	public ArrayList<LocalMusic> getArr(){
		if(localMusicList!=null)
			return localMusicList;
		return null;
	}
}
