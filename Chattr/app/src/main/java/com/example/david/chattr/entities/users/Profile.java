package com.example.david.chattr.entities.users;


public abstract class Profile {

    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    protected int profilePicture;
}
