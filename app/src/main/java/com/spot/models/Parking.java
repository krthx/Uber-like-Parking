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
    public double longitude;
    public double latitude;
    public String costPerHour;
    public String availability;

    public Parking() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Parking(String uid, String title, String owner, String phone, String url, double longitude, double latitude, String costPerHour, String availability) {
        this.uid = uid;
        this.title = title;
        this.owner = owner;
        this.phone = phone;
        this.url = url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.costPerHour = costPerHour;
        this.availability = availability;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("owner", owner);
        result.put("phone", phone);
        result.put("url", url);
        result.put("longitude", longitude);
        result.put("latitude", latitude);
        result.put("costPerHour", costPerHour);
        result.put("availability", availability);

        return result;
    }
}
