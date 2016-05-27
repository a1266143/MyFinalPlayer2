package com.example.bean;

import java.io.Serializable;

public class Song extends SuperSong{
	//根据artistid获取具体列表歌曲
	private String artistid;
	private String songid;
	//根据tinguid获取歌手歌曲
	private String tinguid;
	//歌手头像图片地址
	//private String headImage;
	
	/*public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}*/
	public String getArtistid() {
		return artistid;
	}
	public void setArtistid(String artistid) {
		this.artistid = artistid;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTinguid() {
		return tinguid;
	}
	public void setTinguid(String tinguid) {
		this.tinguid = tinguid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
}
