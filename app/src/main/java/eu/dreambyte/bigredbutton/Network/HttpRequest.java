package eu.dreambyte.bigredbutton.Network;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.ParameterMap;

import org.apache.http.HttpResponse;

public class HttpRequest {
    private String mErrorText = "";
    private String mResponse = "";

    public String getErrorText()
    {
        return mErrorText;
    }

    private void setErrorText(String value)
    {
        mErrorText = value;
    }

    public String getResponse() {
        return mResponse;
    }

    private void setResponse(String mResponse) {
        this.mResponse = mResponse;
    }

    public boolean Get(String url) {
        throw new UnsupportedOperationException();
    }
}
