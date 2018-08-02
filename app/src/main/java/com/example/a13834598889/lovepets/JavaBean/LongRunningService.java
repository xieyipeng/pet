package com.example.a13834598889.lovepets.JavaBean;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.example.a13834598889.lovepets.MainActivity;
import com.example.a13834598889.lovepets.R;

/**
 * Created by 13834598889 on 2018/5/15.
 */

public class LongRunningService extends Service {
    private String time;
    private String text;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        time = intent.getStringExtra("time");
        text = intent.getStringExtra("text");


        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap btm = BitmapFactory.decodeResource(getResources(),
                        R.drawable.tubiao_alarm);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.tubiao_alarm)
                        .setContentTitle("LovePets")
                        .setContentText(text);
                mBuilder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
                mBuilder.setNumber(12);
                mBuilder.setLargeIcon(btm);
                mBuilder.setAutoCancel(true);//自己维护通知的消失

                //构建一个Intent
                Intent resultIntent = new Intent(getApplicationContext(),
                        MainActivity.class);
                //封装一个Intent
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        getApplicationContext(), 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // 设置通知主题的意图
                mBuilder.setContentIntent(resultPendingIntent);
                //获取通知管理器对象
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());



            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long anHour = Integer.parseInt(time) * 60 * 1000;
        long triggerAtTime = System.currentTimeMillis() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.RTC, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}