package com.example.habittrack.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habittrack.MainActivity;
import com.example.habittrack.R;

public class SetReminderHabit extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        NotificationChannel channel = null;
        Log.d("Habit","Notification");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel=new NotificationChannel("2","Habits", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Intent in=new Intent(context, MainActivity.class);

            PendingIntent pendingIntent=PendingIntent.getActivity(context,intent.getExtras().getString("uid").hashCode(),in,PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder notification=new NotificationCompat.Builder(context,"2").
                    setSmallIcon(R.drawable.task)
                    .setContentTitle(intent.getExtras().getString("habitName"))
                    .setContentText(intent.getExtras().getString("Question"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notifyAdmin =NotificationManagerCompat.from(context);
            notifyAdmin.notify(intent.getExtras().getString("uid").hashCode(),notification.build());

        }
    }
}
