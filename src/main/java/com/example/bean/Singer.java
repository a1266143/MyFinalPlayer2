package com.example.bean;

/**
 * 存储歌手的类
 * @author 李晓军
 */
public class Singer {
	//歌手名称
	private String name;
	//根据此参数获得歌手的歌曲
	private String tinguid;
	//照片地址
	private String pictureUrl;
	//根据artistId获取具体的歌曲
	private String artistId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTinguid() {
		return tinguid;
	}
	public void setTinguid(String tinguid) {
		this.tinguid = tinguid;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getArtistId() {
		return artistId;
	}
	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}
	
}
