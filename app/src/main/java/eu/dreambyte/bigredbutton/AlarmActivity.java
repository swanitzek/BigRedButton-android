package eu.dreambyte.bigredbutton;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;
import eu.dreambyte.bigredbutton.AlarmExecuter.DefaultAlarmExecuter;
import eu.dreambyte.bigredbutton.Handler.GcmMessageHandler;
import eu.dreambyte.bigredbutton.Interfaces.AlarmExecuter;
import eu.dreambyte.bigredbutton.Interfaces.DeviceIdProvider;
import eu.dreambyte.bigredbutton.PushMessage.GcmDeviceIdProvider;
import eu.dreambyte.bigredbutton.Server.ButtonServerRegistrator;
import eu.dreambyte.bigredbutton.Server.ServerRegistrator;

public class AlarmActivity extends Activity implements View.OnClickListener {
    // Local members
    private BroadcastReceiver mReceiver;
    private int mCounter = 0;

    // References to views
    private TextView txtCounter;
    private CircleButton btnStop;

    // Static members
    private static String PROJECT_NUMBER = "632557272302";
    private static String SERVER_ADRESS = "http://www.dreambyte.eu/bigredbutton";
    private static String TAG = "eu.dreambyte.BigRedButton";

    // Dependencies
    private DeviceIdProvider mDeviceIdProvider = null;
    private ServerRegistrator mButtonServerRegistrator = null;
    private AlarmExecuter mAlarmExecuter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_alarm);

       getRegId();

       mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(GcmMessageHandler.GCM_RESULT);

                mCounter++;

                mAlarmExecuter.execute();
                btnStop.setVisibility(View.VISIBLE);
            }
        };

        btnStop = (CircleButton) findViewById(R.id.btnStop);

        btnStop.setOnClickListener(this);
        btnStop.setVisibility(View.INVISIBLE);

        mDeviceIdProvider = new GcmDeviceIdProvider(getApplicationContext(), PROJECT_NUMBER);
        mButtonServerRegistrator = new ButtonServerRegistrator();
        mAlarmExecuter = new DefaultAlarmExecuter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Window wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mReceiver),
                new IntentFilter(GcmMessageHandler.GCM_MESSAGE)
        );

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            if (intent.getExtras().getBoolean("alarm"))
            {
                mAlarmExecuter.execute();
                btnStop.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        mAlarmExecuter.cancel();

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

    @Override
    public void onClick(View v) {
        mAlarmExecuter.cancel();

        finish();
    }
}
