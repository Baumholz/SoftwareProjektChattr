package Daba;

import Entities.Message;
import Entities.MessageDB;
import org.json.JSONObject;
import java.sql.*;

public class MessageDBImpl implements MessageDB {

    Connection c = null;
    Statement stmt = null;
    PreparedStatement preStmt;
    ResultSet resSet;

    public void createMessageTable() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db"); //im Root Ordner auf datenbank.db zugreifen
            stmt = c.createStatement(); //manipulation der DB
            stmt.execute("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY, recipientNr VARCHAR(50), senderNr VARCHAR(50), timeStampSender VARCHAR(255) ,content TEXT, topic VARCHAR(255), idM VARCHAR(255))");
            System.out.println("Message Table created! in dir:\n" + System.getProperty("user.dir"));


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

    public void insert(Message message) {

        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            preStmt = c.prepareStatement("INSERT INTO messages (recipientNr, senderNr, timeStampSender ,content, topic, idM)" +
                    "VALUES (?,?,?,?,?,?)"); //in the brackets mqsl statement syntax protects from sql inject.
            preStmt.setString(1, message.getRecipientNr());
            preStmt.setString(2, message.getSenderNr());
            preStmt.setString(3, message.getTimestampSender());
            preStmt.setString(4, message.getContent());
            preStmt.setString(5, message.getTopic());
            preStmt.setString(6, message.getId());
            preStmt.executeUpdate();
            System.out.println("INSERT INTO messages (recipientNr, senderNr, timeStampSender, content, topic, idM)" +
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
     * Liefert den chat Verlauf eines Chates zurueck (Mit Hilfe des Topic).
     * @param topic
     * @return
     */
    public JSONObject getAllFromTopic(String topic) {

JSONObject tmpJsonObj = new JSONObject();

        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            preStmt = c.prepareStatement("SELECT * FROM messages WHERE topic = ?");
            preStmt.setString(1, topic);
            resSet = preStmt.executeQuery();

            while (resSet.next()) {

                tmpJsonObj.accumulate("recipientNr",   resSet.getString("recipientNr"));
                tmpJsonObj.accumulate("senderNr", resSet.getString("senderNr"));
                tmpJsonObj.accumulate("timeStampSender",  resSet.getString("timeStampSender"));
                tmpJsonObj.accumulate("content", resSet.getString("content"));
                tmpJsonObj.accumulate("idM",  resSet.getString("idM"));
                tmpJsonObj.accumulate("topic", topic);

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
        return tmpJsonObj;
    }



}
