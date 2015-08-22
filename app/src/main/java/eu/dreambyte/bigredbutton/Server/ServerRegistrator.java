package eu.dreambyte.bigredbutton.Server;

/**
 * Created by dev on 22.08.2015.
 */
public interface ServerRegistrator {
    String getServerAdress();

    void setServerAdress(String value);

    boolean registerAtServer(String GcmId);
}
