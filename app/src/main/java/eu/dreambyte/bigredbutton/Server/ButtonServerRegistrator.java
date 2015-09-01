package eu.dreambyte.bigredbutton.Server;

import android.util.Log;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

public class ButtonServerRegistrator implements ServerRegistrator {
    // Private Member
    private String mServerAdress = null;

    // Constants
    private final String TAG = "ButtonServerRegistrator";

    public ButtonServerRegistrator()
    {

    }

    @Override
    public String getServerAdress()
    {
        return mServerAdress;
    }

    @Override
    public void setServerAdress(String value)
    {
        mServerAdress = value;
    }

    @Override
    public boolean registerAtServer(String GcmId)
    {
        if (GcmId == null)
        {
            throw new IllegalArgumentException("GcmID");
        }

        if (mServerAdress == null)
        {
            throw new IllegalArgumentException("ServerAdress");
        }

        try
        {
            BasicHttpClient httpClient = new BasicHttpClient("https://dreambyte.eu");
            ParameterMap params = httpClient.newParams()
                    .add("id", GcmId);
            httpClient.setConnectionTimeout(4000);
            HttpResponse httpResponse = httpClient.get("/bigredbutton/setGcmId", params);

            return httpResponse.getStatus() == 200;

        } catch (Exception e) {
            Log.e(TAG, "registerAtServer: " + e.getMessage());
            return false;
        }
    }
}
