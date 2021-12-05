package com.example.notify;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class mailcall extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
}