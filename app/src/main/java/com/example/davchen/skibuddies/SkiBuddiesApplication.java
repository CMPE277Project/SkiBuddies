package com.example.davchen.skibuddies;

import android.app.Application;

import com.example.davchen.skibuddies.Model.Event;
import com.example.davchen.skibuddies.Model.FriendShip;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by davchen on 12/4/15.
 */
public class SkiBuddiesApplication extends Application {

    private static final String appId= "PwkOGqFnv97ycE8GRkiv8dKwwDbVGhuoAmfp70N8";
    private static final String clientKey = "HKfztWDeYtByougLmaEN2rxLmkKxR2kTdQUn09zs";
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, appId, clientKey);

        ParseFacebookUtils.initialize(this);

        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(FriendShip.class);

//        ParseUser.enableAutomaticUser();
//        ParseACL defaultACL = new ParseACL();
//        // Optionally enable public read access.
//        // defaultACL.setPublicReadAccess(true);
//        ParseACL.setDefaultACL(defaultACL, true);
    }
}
