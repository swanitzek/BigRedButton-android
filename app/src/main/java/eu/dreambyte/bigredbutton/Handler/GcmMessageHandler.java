package eu.dreambyte.bigredbutton.Handler;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import eu.dreambyte.bigredbutton.AlarmActivity;
import eu.dreambyte.bigredbutton.Receiver.GcmBroadcastReceiver;

public class GcmMessageHandler extends IntentService {

    private String mMes;
    private Handler mHandler;
    private LocalBroadcastManager mBroadcaster;

    static final private String GCM_RESULT = "eu.dreambyte.bigredbutton.app.REQUEST_PROCESSED";
    static final public String GCM_MESSAGE = "eu.dreambyte.bigredbutton.GCM_MSG";

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mHandler = new Handler();

        mBroadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        mMes = "Alarm";
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        startAlarmActivity();
        sendResult(mMes);

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void startAlarmActivity() {
        Intent dialogIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("alarm", true);

        getApplicationContext().startActivity(dialogIntent);
    }

    private void sendResult(String message) {
        Intent intent = new Intent(GCM_MESSAGE);
        if(message != null)
            intent.putExtra(GCM_RESULT, message);
        mBroadcaster.sendBroadcast(intent);
    }
}
