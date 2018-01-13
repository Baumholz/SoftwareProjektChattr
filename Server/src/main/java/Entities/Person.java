package Entities;

public class Person {


    private String cellphoneNumber;
    private String status;
    private String sureName;
    private String lastName;
    private String pictureURL;



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
    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }
    public String getsureName() {
        return sureName;
    }

    public void setsureName(String sureName) {
        this.sureName = sureName;
    }
    public String getPictureURL(){
        return pictureURL;
    }
    public void setPictureURL(String pictureURL){
        this.pictureURL = pictureURL;
    }

    @Override
    public String toString() {
        return "Person{" +
                "cellphoneNumber='" + cellphoneNumber + '\'' +
                ", status='" + status + '\'' +
                ", sureName='" + sureName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                '}';
    }
}
