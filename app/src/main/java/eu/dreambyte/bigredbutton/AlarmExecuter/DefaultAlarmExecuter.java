package eu.dreambyte.bigredbutton.AlarmExecuter;

import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;
import eu.dreambyte.bigredbutton.Interfaces.AlarmExecuter;

public class DefaultAlarmExecuter implements AlarmExecuter {
    private Context mContext = null;
    private PowerManager.WakeLock mWakeLock = null;
    private Vibrator mVibrator = null;

    private static final String TAG = "eu.dreambyte.BigRedButton";

    public DefaultAlarmExecuter(Context context)
    {
        mContext = context;
    }

    /*
        Shows an notification in the notification area.
     */
   // private void showNotification()
  //  {
        // NotificationCompat.Builder mBuilder =
       //        new NotificationCompat.Builder(mContext)
       //                 .setSmallIcon(R.mipmap.ic_launcher)
       //                 .setContentTitle("Alarm!")
      //                  .setContentText("Hier ist es!!!");

        // Sets an ID for the notification
       // int mNotificationId = 1;

        // Gets an instance of the NotificationManager service
        //NotificationManager mNotifyMgr =
        //        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        // mNotifyMgr.notify(mNotificationId, mBuilder.build());
   // }

    @Override
    public void execute()
    {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        mWakeLock.acquire();

        // Get instance of Vibrator from current Context
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 100, 1000, 1500, 1000, 100, 1000, 1500, 1000};

        mVibrator.vibrate(pattern, 0);
    }

    @Override
    public void cancel() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }

        if (mWakeLock != null)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
