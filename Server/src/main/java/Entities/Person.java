package Entities;

public class Person {

    private String id;
    private String cellphoneNumber;
    private String nickName;
    private String pictureURL;

    public Person(String id, String cellphoneNumber, String nickName, String pictureURL) {
        this.id = id;
        this.cellphoneNumber = cellphoneNumber;
        this.nickName = nickName;
        this.pictureURL = pictureURL;
    }

    public Person(){};
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPictureURL(){
        return pictureURL;
    }
    public void setPictureURL(String pictureURL){
        this.pictureURL = pictureURL;
    }
}
