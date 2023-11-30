package com.example.tastelog;

import com.google.firebase.Timestamp;

public class Widget {
    private String uid;
    private String name;
    private String movie;
    private String music;
    private String book;
    private String cook;
    private String place;
    private Timestamp timestamp;
    public Widget() {}

    public Widget(String uid, String name, String movie, String music,
                  String book, String cook, String place) {
        this.uid = uid;
        this.name = name;
        this.movie = movie;
        this.music = music;
        this.book = book;
        this.cook = cook;
        this.place = place;
    }
    public String getUid(){ return uid; }
    public String getName(){ return name; }
    public String getMovie(){ return movie; }
    public String getMusic(){ return music; }
    public String getBook(){ return book; }
    public String getCook(){ return cook; }
    public String getPlace(){ return place; }
    public Timestamp getTimestamp(){ return timestamp; }
}
