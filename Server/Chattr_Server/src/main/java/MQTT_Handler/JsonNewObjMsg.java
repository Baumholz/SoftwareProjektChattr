package MQTT_Handler;

/**
 * @author mkurras
 *
 */

public class JsonNewObjMsg {
    @Override
    public String toString() {
        return "new Message: [id=" + id + ", icon=" + icon + ", lat=" + lat + ", lng=" + lng + ", img=" + img
                + ", realimg=" + realimg + ", type=" + type + ", descr=" + descr + ", sigstrNr=" + sigstrNr + ", date="
                + date + ", time=" + time + "Hinweis "+ hinweis+ "]";
    }

    private String id;
    private int icon;
    private double lat;
    private double lng;
    private String img;
    private String realimg;
    private String type;
    private String descr;
    private String sigstrNr;
    private String date;
    private String time;
    private String hinweis;

    public String getHinweis(){
        return hinweis;
    }
    public  void setHinweis(String hinweis){
        this.hinweis = hinweis;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRealimg() {
        return realimg;
    }

    public void setRealimg(String realimg) {
        this.realimg = realimg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getSigstrNr() {
        return sigstrNr;
    }

    public void setSigstrNr(String sigstrNr) {
        this.sigstrNr = sigstrNr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JsonNewObjMsg(){

    }

    public JsonNewObjMsg(String id, int icon, double lat, double lng, String img, String realimg, String type, String descr,
                         String sigstrNr, String date, String time, String hinweis) {
        super();
        this.id = id;
        this.icon = icon;
        this.lat = lat;
        this.lng = lng;
        this.img = img;
        this.realimg = realimg;
        this.type = type;
        this.descr = descr;
        this.sigstrNr = sigstrNr;
        this.date = date;
        this.time = time;
        this.hinweis = hinweis;
    }

}
