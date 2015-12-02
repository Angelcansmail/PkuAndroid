package com.gzh.job.weather.com.gzh.job.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.dao.WeatherDao;
import com.gzh.job.weather.com.gzh.job.entity.City;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;
import com.gzh.job.weather.com.gzh.job.util.NetUtil;

/**
 * Created by Angel on 2015/9/26.
 */
public class MainActivity extends Activity implements OnClickListener{
    private String cityCode = "101010100";  //默认为北京
    private final static String TAG="MainActivity";
    private City city = new City(null,null,null,null,null,null);
    private TextView mCurrentCity;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    //更新
    private ProgressBar mProgress;
    private ProgressDialog progressDialog = null;
    WeatherDao weatherDao = new WeatherDao();
    DataReceiver dataReceiver = null;
    /*private int[] imgId = new int[]{
            R.drawable.biz_plugin_weather_0_50,R.drawable.biz_plugin_weather_51_100,R.drawable.biz_plugin_weather_101_150,R.drawable.biz_plugin_weather_151_200,R.drawable.biz_plugin_weather_201_300,
            R.drawable.biz_plugin_weather_greater_300,R.drawable.biz_plugin_weather_baoxue,R.drawable.biz_plugin_weather_baoyu,R.drawable.biz_plugin_weather_dabaoyu,R.drawable.biz_plugin_weather_daxue,
            R.drawable.biz_plugin_weather_dayu,R.drawable.biz_plugin_weather_duoyun,R.drawable.biz_plugin_weather_leizhenyu,R.drawable.biz_plugin_weather_leizhenyubingbao,R.drawable.biz_plugin_weather_shachenbao,
            R.drawable.biz_plugin_weather_tedabaoyu,R.drawable.biz_plugin_weather_wu,R.drawable.biz_plugin_weather_xiaoxue,R.drawable.biz_plugin_weather_xiaoyu,R.drawable.biz_plugin_weather_yin,
            R.drawable.biz_plugin_weather_yujiaxue,R.drawable.biz_plugin_weather_zhenxue,R.drawable.biz_plugin_weather_zhenyu,R.drawable.biz_plugin_weather_zhongyu,R.drawable.biz_plugin_weather_zhongxue,R.drawable.biz_plugin_weather_qing,

    };*/

    private static final int UPDATE_WEATHER = 1;
    private static final int UPDATE_DELY = 0;
    private Thread mThread;
    SharedPreferences preferences;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){//主线程
            switch (msg.what){
                case UPDATE_DELY:
                        Toast.makeText(MainActivity.this, "加载延迟,请检查网络", Toast.LENGTH_LONG).show();
                        mProgress.setVisibility(View.INVISIBLE);
                        mUpdateBtn.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_WEATHER:
                    //updateFutureWeather((FutureWeather)msg.obj);
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:break;
            }
        }
    };
//    public AutoUpdateService.DownloadBinder downloadBinder;
    /*public ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            *//*downloadBinder = (AutoUpdateService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();*//*
        }
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*///设置屏幕横向
        Log.i(TAG, "mainActivity->OnCreate");

        //更新天气信息
        mCurrentCity = (TextView)findViewById(R.id.current_city);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mProgress = (ProgressBar)findViewById(R.id.title_update_progress);
        mProgress.setVisibility(View.INVISIBLE);
        mUpdateBtn.setOnClickListener(this);
        initView();

        //选择城市列表
        mCitySelect = (ImageView) findViewById(R.id.title_cicy_page);
        mCitySelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.title_update_btn:
                //后台进行实时监控
                /*Intent startIntent = new Intent(this,AutoUpdateService.class);
                startService(startIntent);*/

                mUpdateBtn.setVisibility(View.INVISIBLE);
                mProgress.setVisibility(View.VISIBLE);
                //MODE_PRIVATE 只能被本应用程序访问，MODE_WORLD_READABLE可被其他应用程序读，但不可写，MODE_WORLD_WRITEABLE可被其他应用程度读写
                preferences = getSharedPreferences("config", MODE_PRIVATE);
                //String cityCode = preferences.getString("main_city_code","101010100");

                if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORN_NONE){
                    Log.d("Weather", "网络通畅");
                    new Thread() {
                        public void run(){
                            Log.d("Weather", cityCode);
                            TodayWeather todayWeather = weatherDao.queryWeatherCode(cityCode);
                            if(todayWeather != null) {
                                Message msg = new Message();
                                msg.what = UPDATE_WEATHER;
                                msg.obj=todayWeather;
                                //发送消息
                                mHandler.sendMessage(msg);
                            }
                        }
                    }.start();
                }else {
                    //线程里不能更新UI
                    Log.d("Weather", "无网络");
                    new Thread() {
                        public void run(){
                            try{
                                Thread.sleep(10000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = UPDATE_DELY;
                            //发送消息
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                }
                break;
           case R.id.title_cicy_page:
                //Intent i = new Intent(this, SelectCity.class);
                //startActivity(i);
                //得到新打开Activity关闭后返回的数据
                //第二个参数为请求码，可以根据业务需求自己编号
                Intent i = new Intent(MainActivity.this, SelectCity.class);
                startActivityForResult(i, 1);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cityCode= data.getStringExtra("cityCode");
            String cityString = data.getStringExtra("city");
            mCurrentCity.setText(cityString + "天气预报");
            SharedPreferences sharedPreferences = getSharedPreferences("cityCode",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("cityCode", cityCode);
            editor.putString("cityName", cityString);//存入数据
            editor.commit();//提交修改
            if (cityString.equals("")){
                Intent intent = new Intent(MainActivity.this, SelectCity.class);
                startActivity(intent);
                finish();
            }
            new Thread() {
                public void run(){
                    TodayWeather todayWeather = weatherDao.queryWeatherCode(cityCode);
                    if(todayWeather != null) {
                        Message msg = new Message();
                        msg.what = UPDATE_WEATHER;
                        msg.obj=todayWeather;
                        //发送消息
                        mHandler.sendMessage(msg);
                    }
                }
            }.start();
        }//得到新Activity 关闭后返回的数据
    }
    
    public TextView cityTv,timeTv,humidyTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    public TextView weekTv1,temperatureTv1,climateTv1,windTv1;
    public ImageView weatherImg,pmImg,weatherImg1;
    //更新数据
    private void initView(){
        mCurrentCity = (TextView)findViewById(R.id.current_city);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.publish_time);
        humidyTv = (TextView) findViewById(R.id.weather_humid);
        pmDataTv = (TextView) findViewById(R.id.pm_value);
        pmQualityTv = (TextView) findViewById(R.id.pollute_type);
        pmImg = (ImageView) findViewById(R.id.pm_img);
        weekTv = (TextView) findViewById(R.id.today_date);
        temperatureTv = (TextView) findViewById(R.id.today_temp);
        climateTv = (TextView) findViewById(R.id.today_type);
        windTv = (TextView) findViewById(R.id.today_wind);
        weatherImg = (ImageView) findViewById(R.id.weather_pic);

        //未来天气数据
        weekTv1 = (TextView) findViewById(R.id.weather1_date);
        temperatureTv1 = (TextView) findViewById(R.id.weather1_temp);
        climateTv1 = (TextView) findViewById(R.id.weather1_type);
        windTv1 = (TextView) findViewById(R.id.weather1_wind);
        weatherImg1 = (ImageView) findViewById(R.id.weather1_pic);

        //未加载初始化
        mCurrentCity.setText("北京天气");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidyTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        pmImg.setImageResource(0);
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        weatherImg.setImageResource(0);

        //未来
        weekTv1.setText("N/A");
        temperatureTv1.setText("N/A");
        climateTv1.setText("N/A");
        windTv1.setText("N/A");
        weatherImg1.setImageResource(0);
    }
    private void updateTodayWeather(TodayWeather todayWeather){
        Log.d("myapp3", todayWeather.toString());
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidyTv.setText("湿度" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        int icon = parseIcon(todayWeather.getPm25Img() + ".png");
        pmImg.setImageResource(icon);
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText("今天 " + todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        windTv.setText(todayWeather.getFengxiang() + " " + todayWeather.getFengli());
        int icon2 = parseIcon(todayWeather.getType());
        weatherImg.setImageResource(icon2);

        //未来--明天
        weekTv.setText(todayWeather.getDate());
        temperatureTv1.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv1.setText(todayWeather.getType());
        windTv1.setText(todayWeather.getFengxiang() + " " + todayWeather.getFengli());
        icon2 = parseIcon(todayWeather.getType());
        weatherImg1.setImageResource(icon2);

        mUpdateBtn.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
    }
    private int parseIcon(String strIcon){
        if (strIcon == null)
            return -1;

        if (("0.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_0_50;
        else  if (("1.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_51_100;
        else  if (("2.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_101_150;
        else  if (("3.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_151_200;
        else  if (("4.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_201_300;
        else  if (("5.png").equals(strIcon))
            return R.drawable.biz_plugin_weather_greater_300;
        else if(("晴").equals(strIcon))
            return R.drawable.biz_plugin_weather_qing;
        else if (("多云").equals(strIcon))
            return R.drawable.biz_plugin_weather_duoyun;
        else if(("雾").equals(strIcon))
            return R.drawable.biz_plugin_weather_wu;
        else if(("小雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_xiaoyu;
        else if (("阴").equals(strIcon))
            return R.drawable.biz_plugin_weather_yin;
        else if (("暴雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_baoxue;
        else if (("暴雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_baoyu;
        else if (("大暴雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_dabaoyu;
        else if (("大雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_daxue;
        else if (("大雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_dayu;
        else if (("雷阵雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_leizhenyu;
        else if (("雷阵雨冰雹").equals(strIcon))
            return R.drawable.biz_plugin_weather_leizhenyubingbao;
        else if (("沙尘暴").equals(strIcon))
            return R.drawable.biz_plugin_weather_shachenbao;
        else if (("特大暴雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_tedabaoyu;
        else if (("小雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_xiaoxue;
        else if (("雨加雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_yujiaxue;
        else if (("阵雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_zhenxue;
        else if (("阵雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_zhenyu;
        else if (("中雪").equals(strIcon))
            return R.drawable.biz_plugin_weather_zhongxue;
        else if (("中雨").equals(strIcon))
            return R.drawable.biz_plugin_weather_zhongyu;
        else
            return -1;
    }
    @Override
    protected void onStart() {
        dataReceiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.gzh.job.weather.com.gzh.job.activity.MainActivity");
        registerReceiver(dataReceiver,filter);
        Log.i(TAG, "onStart");
        super.onStart();
    }
    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart");
        super.onRestart();
    }
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(dataReceiver);
        Log.i(TAG, "onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
    public class DataReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            TodayWeather todayWeather = (TodayWeather)intent.getSerializableExtra("todayweather");
            updateTodayWeather(todayWeather);
        }
    }
}
