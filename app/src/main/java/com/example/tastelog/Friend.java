package com.example.tastelog;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Friend {
    private String uid1;
    private String uid2;
    private String name1;
    private String name2;
    @ServerTimestamp
    private Timestamp timestamp;

    public Friend() {}

    public Friend(String uid1, String uid2, String name1, String name2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.name1 = name1;
        this.name2 = name2;
    }

    public String getUid1(){ return uid1; }
    public String getUid2(){ return uid2; }
    public String getName1(){ return name1; }
    public String getName2(){ return name2; }
    public Timestamp getTimestamp(){ return timestamp; }
}
