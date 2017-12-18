package MQTT_Handler;

import java.io.*;

/**
 * Diese Klasse ordnet die kryptischen Error reason Codes bei einer MQTT Exception zu einem Sinnvollen Text zu
 */
public class MQTTErrorFileHandler {

    private String errorCodeFilePath = "./files/mqtt/errorMSGs.txt"; //hier liegt meine Bib der errors
    private File errorCodeFile;


    public String getErrorCode(int reason){

        try {
            return parseFile(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error during \"Error file parsing\" occured";
    }

    /**
     * Liest den Error File zeile für Zeile, bis er den reason code zuordnen kann.
     * @param reason
     * @return
     * @throws IOException
     */
    private String parseFile(int reason) throws IOException {
        String ErrorString = null;

        errorCodeFile = new File(errorCodeFilePath);

            BufferedReader br = new BufferedReader(new FileReader(errorCodeFile));
            String line;

            while ((line = br.readLine()) != null && ErrorString == null) {
                if (line.startsWith(Integer.toString(reason))) {
                    ErrorString = line.toString();
                }
            }

            if (ErrorString == null){
                ErrorString = "No Specific Error found";
            }

        return ErrorString;
    }
}
