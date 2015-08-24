package eu.dreambyte.bigredbutton.Server;

public interface ServerRegistrator {
    String getServerAdress();

    void setServerAdress(String value);

    boolean registerAtServer(String GcmId);
}
