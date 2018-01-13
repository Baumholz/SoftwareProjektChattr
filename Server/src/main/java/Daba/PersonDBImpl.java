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
            stmt.execute("CREATE TABLE IF NOT EXISTS person (cellphoneNumber VARCHAR(50)PRIMARY KEY unique NOT NULL, status VARCHAR(55), sureName VARCHAR(55), lastName VARCHAR(55), pictureURL VARCHAR(255), coverImage VARCHAR(255))");
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
            preStmt = c.prepareStatement("INSERT INTO person (cellphoneNumber,status , sureName , lastName, pictureURL, coverImage)" +
                    "VALUES (?,?,?,?,?,?)"); //in the brackets mqsl statement syntax protects from sql inject.
            preStmt.setString(1, person.getCellphoneNumber());
            preStmt.setString(2, person.getStatus());
            preStmt.setString(3, person.getSureName());
            preStmt.setString(4, person.getLastName());
            preStmt.setString(5, person.getPictureURL());
            preStmt.setString(5, person.getCoverImage());
            preStmt.executeUpdate();
            System.out.println("INSERT INTO person (cellphoneNumber as PRIMARY KEY,status , sureName , lastName, pictureURL, coverImage)" +
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
     * @param cellphoneNumber
     * @return
     */
    public Person selectById(String cellphoneNumber) {
        Person person = new Person();
        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            preStmt = c.prepareStatement("SELECT * FROM person WHERE cellphoneNumber = ?");
            preStmt.setString(1, cellphoneNumber);
            resSet = preStmt.executeQuery();

            while (resSet.next()) {
                person.setStatus(resSet.getString("status"));
                person.setSureName(resSet.getString("sureName"));
                person.setLastName(resSet.getString("lastName"));
                person.setPictureURL(resSet.getString("pictureURL"));
                person.setCoverImage(resSet.getString("coverImage"));

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
