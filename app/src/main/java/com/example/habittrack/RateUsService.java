package com.example.habittrack;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.ui.NavigationUI;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class RateUsService extends Service {
    private int count=0;
    private Timer timer=new Timer();
    MainActivity mainAct=new MainActivity();
    public RateUsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent in,int flag,int startId){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count++;
                if(count==15){
                    mainAct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NotificationChannel channel = null;
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                                channel=new NotificationChannel("3","Rate Us", NotificationManager.IMPORTANCE_HIGH);
                                NotificationManager manager=getApplicationContext().getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                                Intent in=new Intent(getApplicationContext(), RateUs.class);

                                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,in,PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder notification=new NotificationCompat.Builder(getApplicationContext(),"3").
                                        setSmallIcon(R.drawable.task)
                                        .setContentTitle("Enjoying the app?")
                                        .setContentText("If you are, consider rating us 5/5!!")
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true);

                                NotificationManagerCompat notifyAdmin =NotificationManagerCompat.from(getApplicationContext());
                                notifyAdmin.notify(0,notification.build());

                            }
                        }
                    });
                }


            }
        },0,1000);


        return super.onStartCommand(in,flag,startId);
    }

    public void onDestroy() {

        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}