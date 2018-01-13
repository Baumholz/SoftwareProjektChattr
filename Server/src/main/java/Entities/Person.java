package Entities;

public class Person {


    private String cellphoneNumber;
    private String status;
    private String sureName;
    private String lastName;
    private String pictureURL;
    private String coverImage;



    public Person(String cellphoneNumber, String status, String sureName, String lastName, String pictureURL) {
        this.cellphoneNumber = cellphoneNumber;
        this.status = status;
        this.sureName = sureName;
        this.lastName = lastName;
        this.pictureURL = pictureURL;
    }

    public Person(){};

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getPictureURL(){
        return pictureURL;
    }
    public void setPictureURL(String pictureURL){
        this.pictureURL = pictureURL;
    }
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cellphoneNumber='" + cellphoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", sureName='" + sureName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
