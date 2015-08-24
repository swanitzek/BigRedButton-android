package eu.dreambyte.bigredbutton.PushMessage;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import eu.dreambyte.bigredbutton.Interfaces.GCM.DeviceIdProvider;

public class GcmDeviceIdProvider implements DeviceIdProvider {
    private String mDeviceId = null;
    private Context mContext = null;
    private String mProjectId = null;

    public GcmDeviceIdProvider(Context applicationContext, String projectId)
    {
        if (applicationContext == null)
        {
            throw new IllegalArgumentException("context");
        }

        if (projectId == null)
        {
            throw new IllegalArgumentException("projectId");
        }

        mContext = applicationContext;
        mProjectId = projectId;
    }

    public String getDeviceId()
    {
        return mDeviceId;
    }

    public boolean requestDeviceId()
    {
        String msg;
        GoogleCloudMessaging gcm;

        try {
            gcm = GoogleCloudMessaging.getInstance(mContext);

            mDeviceId = gcm.register(mProjectId);

            msg = "Device registered, registration ID=" + mDeviceId;
            Log.i("GCM", msg);

            return true;

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            Log.w("GCM", msg);

            return false;
        }
    }
}
