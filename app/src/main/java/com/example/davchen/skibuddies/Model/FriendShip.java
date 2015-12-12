package com.example.davchen.skibuddies.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by emy on 12/6/15.
 */
@ParseClassName("Friendship")
public class FriendShip extends ParseObject {

    public FriendShip() {}

    public String getFriendShipId() {
        return getString("friendShipID");
    }

    public void setFriendShipId(String id) {
        put("friendShipID", id);
    }

    public void setFrom(ParseUser parseUser)
    {
        put("requesterId", parseUser);
    }

    public ParseUser getFrom() {
        return getParseUser("requesterId");
    }

    public void setAccepterId(ParseUser  parseUser) {
        put("accepterId", parseUser);
    }

    public ParseUser getAccepterId() {
        return getParseUser("accepterId");
    }

    public void setDate(Date date) {
        put("Date", date);
    }

    public Date getDate() {
        return getDate("Date");
    }

    public void setStatus(String status)  {
        put("Status", status);
    }

    public String getStatus() {
        return getString("Status");
    }

}
