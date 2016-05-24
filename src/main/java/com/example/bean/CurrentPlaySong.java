package com.example.bean;

/**
 * Created by 李晓军 on 2016/5/24.
 */
public class CurrentPlaySong {
    private String songName;
    private String author;

    public CurrentPlaySong(String songName, String author) {
        this.songName = songName;
        this.author = author;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
