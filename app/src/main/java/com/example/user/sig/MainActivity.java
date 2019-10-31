package com.example.user.sig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends AppCompatActivity {
    private String TAG=MainActivity.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;
    private TextView txtActivity, txtConfidence;
    private ImageView imgActivity;
    private Button btnStartTrcking, btnStopTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtActivity=findViewById(R.id.txt_activity);
        txtConfidence=findViewById(R.id.txt_confidence);
        imgActivity=findViewById(R.id.img_activity);
        btnStartTrcking=findViewById(R.id.btn_start_tracking);
        btnStopTracking=findViewById(R.id.btn_stop_tracking);

        btnStartTrcking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });
        btnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)){
                    int type=intent.getIntExtra("type",-1);
                    int confidence=intent.getIntExtra("confidence",0);
                    handleUserActivity(type, confidence);
                }
            }
        };
        startTracking();
    }

    private void stopTracking() {
        Intent intent2=new Intent(MainActivity.this,BackgroundDetectedActivitiesService.class);
        startService(intent2);
    }

    private void startTracking() {
        Intent intent1=new Intent(MainActivity.this,BackgroundDetectedActivitiesService.class);
        startService(intent1);
    }

    private void handleUserActivity(int type,int confidence){
        String label=getString(R.string.activity_unknown);
        int icon=R.drawable.ic_still;
        switch (type){
            case DetectedActivity.IN_VEHICLE:{
                label=getString(R.string.activity_in_vehicle);
                icon=R.drawable.ic_driving;
                break;
            }
            case DetectedActivity.ON_BICYCLE:{
                label=getString(R.string.activity_on_bicycle);
                icon=R.drawable.ic_on_bicycle;
                break;
            }
            case DetectedActivity.ON_FOOT:{
                label=getString(R.string.activity_on_foot);
                icon=R.drawable.ic_walking;
                break;
            }
            case DetectedActivity.RUNNING:{
                label=getString(R.string.activity_running);
                icon=R.drawable.ic_running;
                break;
            }
            case DetectedActivity.STILL: {
                label = getString(R.string.activity_still);
                break;
            }
            case DetectedActivity.TILTING:{
                label=getString(R.string.activity_tilting);
                icon=R.drawable.ic_tilting;
                break;
            }case DetectedActivity.UNKNOWN:{
                label=getString(R.string.activity_unknown);
                break;
            }
        }
        Log.e(TAG, "User activity: "+label + ",Confidence: "+confidence);
        if (confidence>Constants.CONFIDENCE){
            txtActivity.setText(label);
            txtConfidence.setText("Confidence: "+confidence);
            imgActivity.setImageResource(icon);
        }
    }
@Override
protected void onResume(){
        super.onResume();
    LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));
}
@Override
protected void onPause(){
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
}
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
