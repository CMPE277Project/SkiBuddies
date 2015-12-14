package com.example.davchen.skibuddies.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by emy on 12/5/15.
 */
@ParseClassName("Invitation")
public class Invitation extends ParseObject{

    public void setId(String id) {
        put("InvitationId", id);
    }

    public String getId() {
        return getString("InvitationId");
    }

    public void setEventId(ParseObject event) {
        put("EventId", event);
    }

    public ParseObject getEventId() {
        return getParseObject("EventId");
    }

    public void setUserId(ParseUser user) {
        put("Participants", user);
    }
    public ParseUser getUserId() {
        return getParseUser("Participants");
    }

    public void setFlag(String  status) {
        put("Status", status);
    }

    public void setOwner(ParseUser parseUser) {
        put("author", parseUser);
    }

    public ParseUser getOwner() {
        return getParseUser("author");
    }

    public String getFlag() {
        return getString("Status");
    }


}
