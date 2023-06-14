package com.example.habittrack.main;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habittrack.MainActivity;
import com.example.habittrack.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class SetReminderToDo extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        NotificationChannel channel = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel=new NotificationChannel("1","Todo", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager=context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Intent in=new Intent(context, MainActivity.class);

            PendingIntent pendingIntent=PendingIntent.getActivity(context,intent.getExtras().getString("uid").hashCode(),in,PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder notification=new NotificationCompat.Builder(context,"1").
                    setSmallIcon(R.drawable.task)
                    .setContentTitle(intent.getExtras().getString("TaskName"))
                    .setContentText(intent.getExtras().getString("Description"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notifyAdmin =NotificationManagerCompat.from(context);
            notifyAdmin.notify(intent.getExtras().getString("uid").hashCode(),notification.build());

        }
    }
}
