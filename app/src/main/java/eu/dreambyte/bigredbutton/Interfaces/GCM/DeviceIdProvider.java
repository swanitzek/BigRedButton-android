package eu.dreambyte.bigredbutton.Interfaces;

/**
 * Created by dev on 22.08.2015.
 */
public interface DeviceIdProvider {
    String getDeviceId();

    boolean requestDeviceId();
}
