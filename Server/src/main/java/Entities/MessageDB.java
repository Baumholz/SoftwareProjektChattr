package Entities;
import org.json.JSONObject;

public interface MessageDB
{

    void createMessageTable();

    void insert(Message message);

    JSONObject sendChactHistoryBetweenSenderAndReceiver(String senderNr, String recipientNr);


}





