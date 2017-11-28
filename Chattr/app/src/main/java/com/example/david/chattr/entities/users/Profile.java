package com.example.david.chattr.entities.users;


public abstract class Profile {

    protected String id;
    protected String name;
    protected int profilePicture;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProfilePicture() {
        return profilePicture;
    }
}
