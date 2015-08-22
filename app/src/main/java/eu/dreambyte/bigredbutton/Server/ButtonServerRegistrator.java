package eu.dreambyte.bigredbutton.Server;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import eu.dreambyte.bigredbutton.Network.HttpRequest;

public class ButtonServerRegistrator implements ServerRegistrator {
    // Private Member
    private String mServerAdress = null;

    // Dependencies
    private HttpRequest mHttpRequest = null;

    public ButtonServerRegistrator()
    {
        mHttpRequest = new HttpRequest();
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

        BasicHttpClient httpClient = new BasicHttpClient("https://dreambyte.eu");
        ParameterMap params = httpClient.newParams()
                .add("id", GcmId);
        httpClient.addHeader("name", "value");
        httpClient.setConnectionTimeout(4000);
        HttpResponse httpResponse = httpClient.get("/bigredbutton/setGcmId", params);

        return true;
    }
}
