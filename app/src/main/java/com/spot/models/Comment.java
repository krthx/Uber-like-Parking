package com.spot.models;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String uid;
    public String parking;
    public String user;
    public String comment;
    public long date;


    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Comment(String uid, String parking, String user, String comment, long date) {
        this.uid = uid;
        this.parking = parking;
        this.user = user;
        this.comment = comment;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("parking", parking);
        result.put("user", user);
        result.put("comment", comment);
        result.put("date", date);

        return result;
    }
}
