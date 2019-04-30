package com.spot.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Reserva {
    public String uid;
    public String parking;
    public String endHour;
    public String startHour;
    public String user;
    public long day;


    public Reserva() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Reserva(String uid, String parking, String user, long day, String endHour, String startHour) {
        this.uid = uid;
        this.parking = parking;
        this.user = user;
        this.day = day;
        this.endHour = endHour;
        this.startHour = startHour;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("parking", parking);
        result.put("user", user);
        result.put("day", day);
        result.put("endHour", endHour);
        result.put("startHour", startHour);

        return result;
    }
}
