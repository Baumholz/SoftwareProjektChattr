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
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db"); //im Root Ordner auf datenbank.db zugreifen
            stmt = c.createStatement(); //manipulation der DB
            stmt.execute("CREATE TABLE IF NOT EXISTS person (phoneNumber VARCHAR(50)PRIMARY KEY unique NOT NULL, status VARCHAR(55), firstName VARCHAR(55), name VARCHAR(55), profilePicture TEXT, coverImage TEXT)");
            System.out.println("Person Table created! in dir:\n" + System.getProperty("user.dir"));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void insert(Person person) {


        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            preStmt = c.prepareStatement("INSERT INTO person (phoneNumber,status , firstName , name, profilePicture, coverImage)" +
                    "VALUES (?,?,?,?,?,?)"); //in the brackets mqsl statement syntax protects from sql inject.
            preStmt.setString(1, person.getPhoneNumber());
            preStmt.setString(2, person.getStatus());
            preStmt.setString(3, person.getFirstName());
            preStmt.setString(4, person.getName());
            preStmt.setString(5, person.getProfilePicture());
            preStmt.setString(6, person.getCoverImage());
            preStmt.executeUpdate();
            System.out.println("INSERT INTO person (phoneNumber as PRIMARY KEY,status , firstName , name, profilePicture, coverImage)" +
                    "VALUES (?,?,?,?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preStmt != null) {
                    preStmt.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }


    }

    /**
     * Kreiert mit Hilfe der cellphoneNumber als unique identifier einer Person aus der Datenbank diese Person.
     *
     * @param phoneNumber
     * @return
     */
    public Person selectById(String phoneNumber) {
        Person person = new Person();
        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            preStmt = c.prepareStatement("SELECT * FROM person WHERE phoneNumber = ?");
            preStmt.setString(1, phoneNumber);
            resSet = preStmt.executeQuery();

            while (resSet.next()) {
                person.setStatus(resSet.getString("status"));
                person.setFirstName(resSet.getString("firstName"));
                person.setName(resSet.getString("name"));
                person.setProfilePicture(resSet.getString("profilePicture"));
                person.setCoverImage(resSet.getString("coverImage"));
                person.setPhoneNumber(resSet.getString("phoneNumber"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
        return person;
    }

    public List<Person> selectAll() {
        return null;
    }

    public void delete(String id) {

    }

    public void update(Person person, String id) {

    }
}
