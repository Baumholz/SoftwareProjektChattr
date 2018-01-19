package com.example.david.chattr.entities.users;


import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends Profile {

    private String phoneNumber;
    private String status;
    private byte[] coverImage;
    private String writeable;
    private String topic;

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

    public String getTopic() {
        return topic;
    }

    public UserProfile(String phoneNumber, String status, String firstName, String name, byte[] profilePicture, byte[] coverImage, String writeable, String topic) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.firstName = firstName;
        this.name = name;
        this.profilePicture = profilePicture;
        this.coverImage = coverImage;
        this.writeable = writeable;
        this.topic = topic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public JSONObject toJson() {

        String profile = "";
        String cover = "";
        if (profilePicture != null) {
            profile = new String(profilePicture);
        }
        if (coverImage != null) {
            cover = new String(coverImage);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "10");
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("status", status);
            jsonObject.put("firstName", firstName);
            jsonObject.put("name", name);
            jsonObject.put("profilePicture", profile);
            jsonObject.put("coverImage", cover);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
