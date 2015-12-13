package com.example.davchen.skibuddies.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by emy on 12/5/15.
 */
@ParseClassName("Invite")
public class Invitation extends ParseObject{

    public void setId(String id) {
        put("InvitationId", id);
    }

    public String getId() {
        return getString("InvitationId");
    }

    public void setEventId(String id) {
        put("EventId", id);
    }

    public String getEventId() {
        return getString("EventId");
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

    public String getFlag() {
        return getString("Status");
    }


}
