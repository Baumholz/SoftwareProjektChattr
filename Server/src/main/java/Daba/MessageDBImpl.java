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
            stmt.execute("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY, recipientNr VARCHAR(50), senderNr VARCHAR(50), timestamp INTEGER ,content TEXT, topic VARCHAR(255), idM VARCHAR(255))");
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
            preStmt = c.prepareStatement("INSERT INTO messages (recipientNr, senderNr, timestamp ,content, topic, idM)" +
                    "VALUES (?,?,?,?,?,?)"); //in the brackets mqsl statement syntax protects from sql inject.
            preStmt.setString(1, message.getRecipientNr());
            preStmt.setString(2, message.getSenderNr());
            preStmt.setInt(3, message.gettimestamp());
            preStmt.setString(4, message.getContent());
            preStmt.setString(5, message.getTopic());
            preStmt.setString(6, message.getId());
            preStmt.executeUpdate();
            System.out.println("INSERT INTO messages (recipientNr, senderNr, timestamp, content, topic, idM)" +
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
     * Liefert den chat Verlauf eines Chates zurueck (Mit Hilfe des Topic, bzw der beteiligten Nummern).
     *
     * @param senderNr
     * @param recipientNr
     * @return
     */
    public JSONObject sendChactHistoryBetweenSenderAndReceiver(String senderNr, String recipientNr) {

        JSONObject tmpJsonObj = new JSONObject();
        String topic ="";

        try {
            c = DriverManager.getConnection("jdbc:sqlite:datenbank.db");

            /**
             * Get topic connected to senderNr and recipientNr
             */
            preStmt = c.prepareStatement("SELECT topic FROM messages WHERE (senderNr = ? AND recipientNr = ? OR senderNR = ? and recipientNR = ? )");
            preStmt.setString(1, senderNr);
            preStmt.setString(2, recipientNr);
            preStmt.setString(3, recipientNr);
            preStmt.setString(4, senderNr);

            resSet = preStmt.executeQuery();

            while (resSet.next()) {
                topic = resSet.getString("topic");
            }

            /**
             * now fetch Chat
             */
            preStmt = c.prepareStatement("SELECT * FROM messages WHERE topic = ? AND idM = \"1\" ORDER BY timestamp ");
            preStmt.setString(1, topic);
            resSet = preStmt.executeQuery();

            while (resSet.next()) {
                Message msg = new Message("11", resSet.getInt("timestamp"), resSet.getString("senderNr"), resSet.getString("recipientNr"), resSet.getString("content"));
                msg.setTopic(topic); //schickt alle Nachrichten aus der DB einzelnd
                msg.sendMessage("all/"+senderNr);
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
