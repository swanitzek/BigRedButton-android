package eu.dreambyte.bigredbutton.Handler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import eu.dreambyte.bigredbutton.R;
import eu.dreambyte.bigredbutton.Receiver.GcmBroadcastReceiver;

public class GcmMessageHandler extends IntentService {

    String mMes;
    private Handler mHandler;
    private LocalBroadcastManager mBroadcaster;

    static final public String GCM_RESULT = "eu.dreambyte.bigredbutton.app.REQUEST_PROCESSED";
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

        mMes = "Alarm"; // extras.getString("title");
        // showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        sendResult(mMes);
        showToast();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Alarm!")
                        .setContentText("Hier ist es!");

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void sendResult(String message) {
        Intent intent = new Intent(GCM_MESSAGE);
        if(message != null)
            intent.putExtra(GCM_RESULT, message);
        mBroadcaster.sendBroadcast(intent);
    }

    public void showToast(){
        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mMes, Toast.LENGTH_LONG).show();
            }
        });

    }
}
