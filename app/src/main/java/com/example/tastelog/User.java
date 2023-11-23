package com.example.tastelog;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class User {
    private String uid;
    private String email;
    private String name;

    @ServerTimestamp private Timestamp timestamp;

    public User() {}

    public User(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Timestamp getTimestamp() { return timestamp; }
}
