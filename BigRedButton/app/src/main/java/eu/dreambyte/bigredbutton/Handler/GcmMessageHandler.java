package eu.dreambyte.bigredbutton.Handler;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import eu.dreambyte.bigredbutton.Receiver.GcmBroadcastReceiver;

public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
    LocalBroadcastManager broadcaster;

    static final public String GCM_RESULT = "io.plauder.app.REQUEST_PROCESSED";

    static final public String GCM_MESSAGE = "io.plauder.app.GCM_MSG";

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();

        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        // showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        sendResult(mes);

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void sendResult(String message) {
        Intent intent = new Intent(GCM_MESSAGE);
        if(message != null)
            intent.putExtra(GCM_RESULT, message);
        broadcaster.sendBroadcast(intent);
    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {


                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
            }
        });

    }
}
