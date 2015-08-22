package eu.dreambyte.bigredbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.logging.Handler;

import eu.dreambyte.bigredbutton.Handler.GcmMessageHandler;
import eu.dreambyte.bigredbutton.Interfaces.DeviceIdProvider;
import eu.dreambyte.bigredbutton.PushMessage.GcmDeviceIdProvider;
import eu.dreambyte.bigredbutton.Server.ButtonServerRegistrator;
import eu.dreambyte.bigredbutton.Server.ServerRegistrator;

public class AlarmActivity extends ActionBarActivity {
    // Local members
    private BroadcastReceiver mReceiver;
    private int mCounter = 0;

    // References to views
    private TextView txtCounter;

    // Static members
    private static String PROJECT_NUMBER = "632557272302";
    private static String SERVER_ADRESS = "http://www.dreambyte.eu/bigredbutton";

    // Dependencies
    private DeviceIdProvider mDeviceIdProvider = null;
    private ServerRegistrator mButtonServerRegistrator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        txtCounter = (TextView)findViewById(R.id.txtCount);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(GcmMessageHandler.GCM_RESULT);

                mCounter++;

                txtCounter.setText(Integer.toString(mCounter));
            }
        };

        mDeviceIdProvider = new GcmDeviceIdProvider(getApplicationContext(), PROJECT_NUMBER);
        mButtonServerRegistrator = new ButtonServerRegistrator();

        getRegId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mReceiver),
                new IntentFilter(GcmMessageHandler.GCM_MESSAGE)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onStop();
    }

    public void getRegId(){
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (mDeviceIdProvider.requestDeviceId())
                {
                    String deviceId = mDeviceIdProvider.getDeviceId();

                    mButtonServerRegistrator.setServerAdress(SERVER_ADRESS);
                    return mButtonServerRegistrator.registerAtServer(deviceId);
                } else {
                    // Show error
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean status) {
                if (!status)
                {
                    Toast.makeText(getApplicationContext(), "Server-Registration error.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
