package com.example.davchen.skibuddies.Model;

import com.parse.ParseClassName;

/**
 * Created by emy on 12/4/15.
 */
@ParseClassName("User")
public class User {

    private String name;
    private String imageId;


    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
