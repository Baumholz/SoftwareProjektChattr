package Daba;

import Entities.Person;
import Entities.PersonDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PersonDBImpl implements PersonDB {

    Connection c = null;
    Statement stmt = null;


    public void createPersonTable() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test.db"); //im Root Ordner auf test.db zugreifen

            stmt = c.createStatement(); //manipulation der DB
            stmt.execute("CREATE TABLE IF NOT EXISTS person (ID INT PRIMARY KEY unique NOT NULL," +
                    "cellphoneNumber BIGINT, nickName VARCHAR(55), pictureURL VARCHAR(256))");
            System.out.println("Person Table created! in:\n"+System.getProperty("user.dir"));


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

    }

    public Person selectById(long id) {
        return null;
    }

    public List<Person> selectAll() {
        return null;
    }

    public void delete(long id) {

    }

    public void update(Person person, int id) {

    }
}
