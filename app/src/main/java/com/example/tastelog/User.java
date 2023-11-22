package com.example.tastelog;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class User {
    private String uid;
    private String email;

    @ServerTimestamp private Timestamp timestamp;

    public User() {}

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public Timestamp getTimestamp() { return timestamp; }
}
