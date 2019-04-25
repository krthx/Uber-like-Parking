package com.spot.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties

public class Parking {

    public String uid;
    public String title;
    public String owner;
    public String phone;
    public String url;
    public String longitude;
    public String latitude;
    public String costPerHour;

    public Parking() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Parking(String uid, String owner, String phone, String url, String longitude, String latitude, String costPerHour) {
        this.uid = uid;
        this.owner = owner;
        this.phone = phone;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.costPerHour = costPerHour;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("owner", owner);
        result.put("phone", phone);
        result.put("url", url);
        result.put("longitude", longitude);
        result.put("latitude", latitude);
        result.put("costPerHour", costPerHour);

        return result;
    }
}
