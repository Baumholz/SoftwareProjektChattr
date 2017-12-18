package Daba;

import Entities.Person;
import Entities.PersonDB;

import java.sql.*;
import java.util.List;

public class PersonDBImpl implements PersonDB {

    Connection c = null;
    Statement stmt = null;
    PreparedStatement preStmt;
    ResultSet resSet;

    public void createPersonTable() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test.db"); //im Root Ordner auf test.db zugreifen

            stmt = c.createStatement(); //manipulation der DB
            stmt.execute("CREATE TABLE IF NOT EXISTS person (ID VARCHAR(255) PRIMARY KEY unique NOT NULL," +
                    "cellphoneNumber VARCHAR(50) unique NOT NULL, nickName VARCHAR(55), pictureURL VARCHAR(255))");
            System.out.println("Person Table created! in dir:\n"+System.getProperty("user.dir"));


        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
            if (stmt != null){
                stmt.close();
            }
            if(c !=null){
                    c.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void insert(Person person) {


        try{
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            preStmt = c.prepareStatement("INSERT INTO person (ID, cellphoneNumber, nickName, pictureURL)" +
                    "VALUES (?,?,?,?)"); //in the brackets mqsl statement syntax protects from sql inject.
            preStmt.setString(1, person.getId());
            preStmt.setString(2, person.getCellphoneNumber());
            preStmt.setString(3, person.getNickName());
            preStmt.setString(4, person.getPictureURL());
            preStmt.executeUpdate();
            System.out.println("INSERT INTO person (ID, cellphoneNumber, nickName, pictureURL)" +
                    "VALUES (?,?,?,?)");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (preStmt != null) {
                    preStmt.close();
                }
                if (c != null) {
                    c.close();
                }
            }
             catch (SQLException e) {
                    e.printStackTrace();

                }

        }


    }

    public Person selectById(String id) {
        Person person = new Person();
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            preStmt = c.prepareStatement("SELECT * FROM person WHERE ID = ?");
            preStmt.setString(1, id);
            resSet = preStmt.executeQuery();

            while(resSet.next()){
                person.setId(resSet.getString("ID"));
                person.setCellphoneNumber(resSet.getString("cellphoneNumber"));
                person.setNickName(resSet.getString("nickName"));
                person.setPictureURL(resSet.getString("pictureURL"));
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (preStmt != null) {
                    preStmt.close();
                }
                if (c != null) {
                    c.close();
                }
                if (resSet != null) {
                    resSet.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();

            }

        }
        return null;
    }

    public List<Person> selectAll() {
        return null;
    }

    public void delete(String id) {

    }

    public void update(Person person, String id) {

    }
}
