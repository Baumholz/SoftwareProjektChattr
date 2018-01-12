package com.example.david.chattr.entities.users;


public abstract class Profile {

    protected String id;
    protected String firstName;
    protected String name;
    protected byte[] profilePicture;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getName() {
        return name;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }
}
