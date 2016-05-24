package com.example.bean;

import java.io.Serializable;

public class SuperSong implements Serializable{
	protected String title;
	protected String author;
	//播放歌曲的唯一songid
	protected String songid;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	
}
