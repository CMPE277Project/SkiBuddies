package com.example.davchen.skibuddies;

import com.parse.ParseClassName;

/**
 * Created by emy on 12/4/15.
 */
@ParseClassName("User")
public class User {

    private String firstName;
    private String lastName;
    private String imageId;


    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
