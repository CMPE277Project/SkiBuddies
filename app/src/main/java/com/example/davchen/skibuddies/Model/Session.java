package com.example.davchen.skibuddies.Model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by emy on 12/6/15.
 */
@ParseClassName("Session")
public class Session extends ParseObject
{
    public Session() {}

    public void setSessionId(String id) {
        put("SessionId", id);
    }

    public String getSessionId() {
        return getString("SessionId");
    }

    public void setEventId(String id) {
        put("EventId", id);
    }

    public String getEventId() {
        return getString("EventId");
    }

    public void setUserId(ParseUser parseUser) {
        put("UserId", parseUser);
    }

    public ParseUser getUserId() {
        return getParseUser("UserId");
    }

    public void setDistance(String distance) {
        put("Distance", distance);
    }
    public String getDistance() {
        return getString("Distance");
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        put("Location", parseGeoPoint);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("Location");
    }

}
