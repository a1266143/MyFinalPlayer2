package com.example.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.example.bean.Singer;
import com.example.bean.Song;

public class AnaJson {
	/**
	 * 根据网络歌单类型解析json数据
	 * @param type
	 * @return
	 */
	public ArrayList<Song> anaJson(JSONObject jo,int type){
		ArrayList<Song> arr = null;
		switch (type) {
		// 新歌
		case NetWork.TYPE_NEWSONG:
			//arr = realAnaNewSong(jo);
			break;
			//热歌
		case NetWork.TYPE_HOTSONG:

			break;
			//kty榜
		case NetWork.TYPE_KTV:

			break;
			//叱咤风云榜
		case NetWork.TYPE_WIND:

			break;
			//billboard榜
		case NetWork.TYPE_BILLBOARD:

			break;
			//摇滚榜
		case NetWork.TYPE_ROCK:

			break;
			//影视金曲榜
		case NetWork.TYPE_MOVIE:

			break;
			//hito榜
		case NetWork.TYPE_HITO:

			break;
			//华语金曲榜
		case NetWork.TYPE_CHINESE:

			break;
			//欧美金曲榜
		case NetWork.TYPE_EUROPE:

			break;
			//经典老歌
		case NetWork.TYPE_OLDSONG:
			
			break;
			//情歌对唱榜
		case NetWork.TYPE_LOVESONG:

			break;
			//网络歌曲榜
		case NetWork.TYPE_NETSONG:

			break;

		default:
			break;
		}
		return arr;
	}
	
	//获取各个类别的json并解析
	@SuppressLint("UseValueOf")
	public ArrayList<HashMap<String,String>> getCategoryList(List<HashMap<Integer,String>> arr){
		ArrayList<HashMap<String,String>> arrList = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<arr.size();i++){
			String jsonStr = "";
			try {
				for(int j=0;j<arr.size();j++){
					HashMap<Integer, String> jsonStrMap = arr.get(j);
					if(jsonStrMap.containsKey(i)){
						jsonStr = jsonStrMap.get(i);
						break;
					}
				}
				//解析String为Json
				JSONObject obj = new JSONObject(jsonStr);
				JSONArray array = obj.getJSONArray("song_list");
				HashMap<String,String> map = new HashMap<String, String>();
				for(int j=0;j<array.length();j++){
					//最多只解析三个
					if(j>2)
						break;
					//给map赋值
					JSONObject jo = array.getJSONObject(j);
					String title = jo.getString("title");
					String author = jo.getString("author");
						if(j==0){

							map.put("first", title+"---"+author);
						}
						if(j==1){
							map.put("second", title+"---"+author);
						}
						if(j==2){
							map.put("third", title+"---"+author);
						}
				}
				//将map加入list
				arrList.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arrList;
	}
	
	/**
	 * 解析歌手json对象
	 */
	public static ArrayList<Singer> anaSingerJson(String response){
		ArrayList<Singer> arr = new ArrayList<Singer>();
		try {
			JSONObject obj = new JSONObject(response);
			JSONArray array = obj.getJSONArray("artist");
			for(int i=0;i<array.length();i++){
				JSONObject jsonobj = array.getJSONObject(i);
				String name = jsonobj.getString("name");
				String tinguid = jsonobj.getString("ting_uid");
				String pictureUrl = jsonobj.getString("avatar_big");
				String artistId = jsonobj.getString("artist_id");
				//新建歌手bean
				Singer singer = new Singer();
				singer.setName(name);
				singer.setTinguid(tinguid);
				singer.setPictureUrl(pictureUrl);
				singer.setArtistId(artistId);
				//将歌手bean加入集合中
				arr.add(singer);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	
	/**
	 * 解析列表
	 * @return
	 */
	public static ArrayList<Song> AnaSong(String response){
		ArrayList<Song> arr = new ArrayList<Song>();
		try {
			JSONObject jsonobject = new JSONObject(response);
			JSONArray array = jsonobject.getJSONArray("song_list");
			for(int i=0;i<array.length();i++){
				JSONObject jo = array.getJSONObject(i);
				Song song = new Song();
				song.setArtistid(jo.getString("artist_id"));
				//song.setLrclink(jo.getString("lrclink"));
				song.setSongid(jo.getString("song_id"));
				song.setTitle(jo.getString("title"));
				song.setTinguid(jo.getString("ting_uid"));
				song.setAuthor(jo.getString("author"));
				//song.setHeadImage("pic_big");
				arr.add(song);
			}
			return arr;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析搜索的歌曲的json
	 */
	public static ArrayList<Song> anaSearchSong(JSONObject object){
		ArrayList<Song> list = null;
		try {
			int error_code = object.getInt("error_code");
			if(error_code == 22000){
				list = new ArrayList<>();
				JSONObject result = object.getJSONObject("result");
				JSONObject song_info = result.getJSONObject("song_info");
				JSONArray song_list = song_info.getJSONArray("song_list");
				for(int i=0;i<song_list.length();i++){
					Song song = new Song();
					JSONObject songObject = song_list.getJSONObject(i);
					song.setTitle(songObject.getString("title"));
					song.setAuthor(songObject.getString("author"));
					song.setSongid(songObject.getString("song_id"));
					song.setTinguid(songObject.getString("ting_uid"));
					song.setArtistid(songObject.getString("artist_id"));
					list.add(song);
				}
				return list;
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
