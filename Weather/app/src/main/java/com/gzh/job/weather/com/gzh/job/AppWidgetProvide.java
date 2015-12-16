package com.gzh.job.weather.com.gzh.job;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.dao.WeatherDao;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Angel on 2015/12/10.
 */
public class AppWidgetProvide extends AppWidgetProvider {
    Timer timer = new Timer();
    private AppWidgetManager appWidgetManager;
    private Context context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
        Timer wTimer = new Timer();
        wTimer.scheduleAtFixedRate(new MyWeather(context, appWidgetManager), 1, 36000);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
    public class MyTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;

        DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.CHINA);
        public MyTime(Context context,AppWidgetManager appWidgetManager){
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, AppWidgetProvide.class);
        }

        @Override
        public void run() {
            remoteViews.setTextViewText(R.id.time, format.format(new Date()));//显示时间
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }
    }
    //显示天气信息处理
    public class MyWeather extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        public MyWeather(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, AppWidgetProvide.class);
        } @Override
      public void run() {
       /* String temp = "";
        String city = "大兴";
        String image_url="";*/
        //String cityCode = "101011100";
        //获得appplication中的context，否则为空
        context = MyApplication.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cityCode", Context.MODE_PRIVATE);
        String cityCode = sharedPreferences.getString("cityCode","");
        TodayWeather todayWeather;
        WeatherDao weatherDao = new WeatherDao();
        todayWeather = weatherDao.queryWeatherCode(cityCode);
        String city = todayWeather.getCity();
        String wendu = todayWeather.getWendu() + "℃";
        String type = todayWeather.getType();
        if(type != null){
            remoteViews.setImageViewResource(R.id.widget_image, WeatherDao.parseIcon(todayWeather.getType()));
        }
        remoteViews.setTextViewText(R.id.temperature, city + "\t" + wendu);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        /*try {
            //此处笔者用的是“易源接口”提供的API， 下面的***分别代表你应用的appid和secretid
           String info = new ShowApiRequest("http://wthrcdn.etouch.cn/WeatherApi?citykey=","****","*********")
                    .addTextPara("areaid", "101050701")
                    .addTextPara("area", city)
                    .addTextPara("needMoreDay", "0")
                    .addTextPara("needIndex", "0")
                    .addTextPara("needHourData", "0")
                    .post();
            //获取接口返回的信息
            JSONObject wholeInfo = new JSONObject(info);
            JSONObject showapi_res_body = wholeInfo.getJSONObject("showapi_res_body");
            JSONObject now = showapi_res_body.getJSONObject("now");
            temp = now.getString("temperature")+"℃";
            image_url = now.getString("weather_pic");
            Bitmap weather_pic = getBitmap(image_url);
            System.out.println("temp:" + temp);
            remoteViews.setTextViewText(R.id.temperature, city +":"+ temp);
            if(weather_pic!=null){
                remoteViews.setImageViewBitmap(R.id.image,weather_pic);
            }
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        } catch (JSONException e) {
            e.printStackTrace();
            remoteViews.setTextViewText(R.id.temperature, city+"：-5℃");
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }catch (IOException e){
            e.printStackTrace();
        }*/
    }
        //根据天气图标url从网络获取图片
        public Bitmap getBitmap(String path) throws IOException {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        }
    }
}
