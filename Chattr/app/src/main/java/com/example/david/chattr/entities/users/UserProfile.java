package com.example.david.chattr.entities.users;


public class UserProfile extends  Profile{

    private String phoneNumber;
    private String status;

    public UserProfile(String phoneNumber,String status,String id,String name,int profilePicture) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }


}
