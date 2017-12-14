package Entities;

public class Person {

    private long id;
    private long cellphoneNumber;
    private String nickName;
    private String pictureURL;

    public Person(long id, long cellphoneNumber, String nickName) {
        this.id = id;
        this.cellphoneNumber = cellphoneNumber;
        this.nickName = nickName;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(long cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
