package com.gzh.job.weather.com.gzh.job;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.activity.MainActivity;
import com.gzh.job.weather.com.gzh.job.dao.WeatherDao;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;
import com.gzh.job.weather.com.gzh.job.util.NetUtil;

public class AutoUpdateService extends Service {

    /*public DownloadBinder mBinder = new DownloadBinder();

    public class DownloadBinder extends Binder {

        public void startDownload() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // start downloading
                }
            }).start();
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }

    }*/

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService", "onBind executed");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, MainActivity.class);//点击该通知后要跳转的Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("title")
                .setContentText("describe")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        notification = builder.getNotification();
        //设置下拉点击之后回到应用程序，可以有多个值选择
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.defaults = Notification.DEFAULT_VIBRATE;
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "loading now", Toast.LENGTH_LONG).show();
		SharedPreferences sharedPreferences = getSharedPreferences("cityCode",MODE_PRIVATE);
        if (NetUtil.getNetworkState(AutoUpdateService.this) != NetUtil.NETWORN_NONE) {
            Log.d("NETWORK", "网络通畅");
            String cityCode = sharedPreferences.getString("cityCode","");
			updateTodayWeather(cityCode);
        } else {
            Log.d("NETWORK", "网络无响应");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something here
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

    public void updateTodayWeather(final String cityCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent();
                    intent.setAction("com.gzh.job.weather.com.gzh.job.activity.MainActivity");
                    TodayWeather todayWeather = new WeatherDao().queryWeatherCode(cityCode);
                    Log.d("后台更新天气1：", todayWeather.toString());
                    intent.setAction("com.gzh.job.weather.com.gzh.job.activity.MainActivity");
                    intent.putExtra("todayweather", todayWeather);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("updateTodayWeather", "更新天气失败！");
                }
            }
        }).start();
    }
}
