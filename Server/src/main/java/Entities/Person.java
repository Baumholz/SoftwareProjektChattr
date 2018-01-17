package Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Person {

    private String phoneNumber;
    private String status;
    private String firstName;
    private String name;
    private String profilePicture;
    private String coverImage;
    private String id;

    public Person(String phoneNumber, String status, String firstName, String name, String profilePicture, String coverImage) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.firstName = firstName;
        this.name = name;
        this.profilePicture = profilePicture;
        this.coverImage = coverImage;
    }

    public Person(){};

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", firstName='" + firstName + '\'' +
                ", name='" + name + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public JSONObject personToJSON (){

     JSONObject message = new JSONObject();
        try {
            message.put("id" , id);
            message.put("phoneNumber" , phoneNumber);
            message.put("status" , status);
            message.put("firstName" , firstName);
            message.put("name" , name);
            message.put("profilePicture", profilePicture);
            message.put("coverImage", coverImage);
            System.out.println("Created a Profil JSON:"+message.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }




        return message;

    }
}
