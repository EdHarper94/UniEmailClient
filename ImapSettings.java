package egwh.uniemailclient;

/**
 * Created by eghar on 29/03/2017.
 */

public class ImapSettings {

    private static final String SERVER_ADDRESS = "mobile.swansea.ac.uk";
    private static final String INC_SETTINGS = "SSL/TLS";
    private static final int IN_PORT = 993;
    private static final String OUT_SETTINGS = "STARTTLS";
    private static final int OUT_PORT = 587;

    public String getServerAddress(){
        return SERVER_ADDRESS;
    }

    public String getIncSettings(){
        return INC_SETTINGS;
    }

    public int getInPort(){
        return IN_PORT;
    }

    public String getOutSettings(){
        return OUT_SETTINGS;
    }

    public int getOutPort(){
        return OUT_PORT;
    }
}
