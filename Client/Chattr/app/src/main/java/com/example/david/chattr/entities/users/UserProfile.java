package com.example.david.chattr.entities.users;


import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends  Profile{

    private String phoneNumber;
    private String status;
    private byte[] coverImage;
    private String writeable;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public void setWriteable(String writeable) {
        this.writeable = writeable;
    }

    public byte[] getCoverImage() {

        return coverImage;
    }

    public String getWriteable() {
        return writeable;
    }

    public UserProfile(String phoneNumber, String status, String firstName, String name, byte[] profilePicture, byte[] coverImage, String writeable) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.firstName = firstName;
        this.name = name;
        this.profilePicture = profilePicture;
        this.coverImage = coverImage;
        this.writeable = writeable;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public JSONObject toJson () {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("status", status);
            jsonObject.put("firstName", firstName);
            jsonObject.put("name", name);
            jsonObject.put("profilePicture", profilePicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
