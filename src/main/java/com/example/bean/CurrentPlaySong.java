package com.example.bean;

import java.io.Serializable;

/**
 * Created by 李晓军 on 2016/5/24.
 */
public class CurrentPlaySong implements Serializable{
    private String songName;
    private String artistName;
    private String songPicRadio;
    private String lrcLink;
    private String songLink;

    public CurrentPlaySong(String songName, String artistName, String songPicRadio, String lrcLink, String songLink) {
        this.songName = songName;
        this.artistName = artistName;
        this.songPicRadio = songPicRadio;
        this.lrcLink = lrcLink;
        this.songLink = songLink;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSongPicRadio(String songPicRadio) {
        this.songPicRadio = songPicRadio;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getSongName() {

        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongPicRadio() {
        return songPicRadio;
    }

    public String getLrcLink() {
        return lrcLink;
    }

    public String getSongLink() {
        return songLink;
    }
}
