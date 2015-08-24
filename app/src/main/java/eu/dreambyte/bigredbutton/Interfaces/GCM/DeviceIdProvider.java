package eu.dreambyte.bigredbutton.Interfaces.GCM;

public interface DeviceIdProvider {
    String getDeviceId();

    boolean requestDeviceId();
}
