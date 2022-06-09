package com.example.esp32_android;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String[] topics = {"/topics/notify0","/topics/notify1", "/topics/notify2","/topics/notify3","/topics/notify4"};
    private TextView mTextViewData;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent send_intent = new Intent(this, RealTimeActivity.class);
        super.onMessageReceived(remoteMessage);
        makeNotification(remoteMessage);
    }


    @Override
    public void onNewToken(String s) {
        Log.e("token?", s);
        super.onNewToken(s);
    }


    private void makeNotification(RemoteMessage remoteMessage) {
        try {
            int notificationId = -1;
            Context mContext = getApplicationContext();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);



            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String topic = remoteMessage.getFrom();
            Log.d("_test",topic);
            if(topic.equals("/topics/realTime")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                }, 0);

            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setVibrate(new long[] {200, 100, 200});
            }
            builder.setSmallIcon(R.drawable.smile)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setContentText(message);
            if (topic.equals(topics[0])) {
                notificationId = 0;
            } else if (topic.equals(topics[1])) {
                notificationId = 1;
            } else if (topic.equals(topics[2])) {
                notificationId = 2;
            } else if (topic.equals(topics[3])) {
                notificationId = 3;
            } else if (topic.equals(topics[4])) {
                notificationId = 4;
            }

            if (notificationId >= 0) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(notificationId, builder.build());
            }

        } catch (NullPointerException nullException) {
            Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("error Notify", nullException.toString());
        }
    }}