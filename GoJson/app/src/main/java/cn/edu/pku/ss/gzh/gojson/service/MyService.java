package cn.edu.pku.ss.gzh.gojson.service;

/**
 * Created by Angel on 2015/10/28.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyService extends Service {

    @Override
    public void onCreate() {
        Log.d("MyService", "onCreate executed");
        super.onCreate();
    }
    private MyBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
    public String getTime(){
         Time t = new Time();
         t.setToNow();
         return t.toString();
     }
    public IBinder onBind(Intent arg0){
        Log.d("MyService","onBind executed");
        new Thread(new Runnable() {
            @Override
            public void run() {
 //               while(true) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
                        sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");
                        String timeStr = sdf.format(new Date());
          //              Log.d("info", "" + timeStr);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        //    }
        }).start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService", "onUnBind executed");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//可以多次开始
        Log.d("MyService", "onStartCommand executed");

        Toast.makeText(MyService.this,"startCommand",Toast.LENGTH_LONG).show();
        return START_STICKY;    //1
    }

    //只创建一次，销毁一次
    @Override
    public void onDestroy() {   //The onDestroy() method is called when the service is stopped using the stopService() method. This is where you clean up the resources used by your service.
        super.onDestroy();
        Log.d("destory:","destoryService");
        Toast.makeText(this,"Service Destoryed",Toast.LENGTH_LONG).show();
    }
}
