package com.example.user.sig;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by user on 12/20/2017.
 */

public class DetectedActivitiesIntentService extends IntentService {
    protected static final String TAG=DetectedActivitiesIntentService.class.getSimpleName();
    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate(){
        onCreate();
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result=ActivityRecognitionResult.extractResult(intent);
        ArrayList<DetectedActivity> detectedActivities=(ArrayList)result.getProbableActivities();
        for (DetectedActivity activity:detectedActivities){
            Log.e(TAG, "Detected activity: "+ activity.getType()+","+activity.getConfidence());
            broadcastActivity(activity);

        }

    }
    private void broadcastActivity(DetectedActivity activity){
        Intent intent=new Intent(Constants.BROADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type",activity.getType());
        intent.putExtra("confidence",activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
