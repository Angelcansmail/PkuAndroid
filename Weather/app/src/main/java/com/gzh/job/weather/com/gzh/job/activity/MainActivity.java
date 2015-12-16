package com.gzh.job.weather.com.gzh.job.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.AutoUpdateService;
import com.gzh.job.weather.com.gzh.job.MyApplication;
import com.gzh.job.weather.com.gzh.job.adapter.ViewPagerAdapter2;
import com.gzh.job.weather.com.gzh.job.dao.WeatherDao;
import com.gzh.job.weather.com.gzh.job.entity.City;
import com.gzh.job.weather.com.gzh.job.entity.FutureWeather;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;
import com.gzh.job.weather.com.gzh.job.util.NetUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2015/9/26.
 */
public class MainActivity extends Activity implements OnClickListener, ViewPager.OnPageChangeListener{
    private String cityCode = "";
    private final static String TAG="MainActivity";
    private City city = new City(null,null,null,null,null,null);
    private TextView mCurrentCity;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    private ImageView mLoctionBtn;
    private ImageView mShareBtn;

    private ViewPagerAdapter2 vpAdapter;
    private ViewPager vp;
    private List<View> views;   //用于存储页面数量和内容的集合
    private final static int SIZE = 3; //可以添加的城市数量
    //页面监听事件，使得小圆点随着页面的变化而变化
    private ImageView[] dots;
    private int[] ids = {R.id.future_weather_page1,R.id.future_weather_page2};
    //用于区分更新的是那个页面的天气
    private static final int UPDATE_TODAY_WEATHER = 0;
    private static final int UPDATE_self1_WEATHER = 1;
    private static final int UPDATE_self2_WEATHER = 2;
    private static final int UPDATE_self3_WEATHER = 3;

    //定位服务
    //此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
    private LocationClient mLocationClient;// LocationClient
    private MyLocationListener mMyLocationListener;// BDLocationListener
    //微信
    private IWXAPI wxApi;
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
                    TodayWeather todayWeather = (TodayWeather)msg.getData().get("todayWeather");
                    //Toast.makeText(MainActivity.this,"" + msg.obj,Toast.LENGTH_LONG).show();
                    List<FutureWeather> futureWeatherList = (ArrayList<FutureWeather>)msg.obj;
                    futureWeatherList.remove(0);
                    updateTodayWeather(todayWeather, futureWeatherList);
                    //updateTodayWeather((TodayWeather) msg.obj);
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
    void initDots(){
        dots = new ImageView[views.size()];
        for (int i = 0;i < views.size();i++){
            dots[i] =(ImageView) findViewById(ids[i]);
        }
    }
    public void onPageScrolled(int i,float v,int i2){

    }
    public void onPageSelected(int i){
        for (int a = 0; a < ids.length; a++){
            if(a == i){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
    public void onPageScrollStateChanged(int i){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*///设置屏幕横向——有待实现屏幕切换状态的保存
        Log.i(TAG, "mainActivity->OnCreate");
        SharedPreferences sharedPreferences = getSharedPreferences("cityCode", MODE_PRIVATE);
        cityCode = sharedPreferences.getString("cityCode","101010100");  //默认为北京
        //更新天气信息
        mCurrentCity = (TextView)findViewById(R.id.current_city);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mProgress = (ProgressBar)findViewById(R.id.title_update_progress);
        mProgress.setVisibility(View.INVISIBLE);
        mUpdateBtn.setOnClickListener(this);
        initView();
        initDots();
        //选择城市列表
        mCitySelect = (ImageView) findViewById(R.id.title_cicy_page);
        mCitySelect.setOnClickListener(this);
        //定位服务
        mLoctionBtn = (ImageView) findViewById(R.id.title_locate_btn);
        mLoctionBtn.setOnClickListener(this);
        //分享
        mShareBtn = (ImageView)findViewById(R.id.title_share_btn);
        mShareBtn.setOnClickListener(this);
        //后台进行实时监控
        Intent startIntent = new Intent(this,AutoUpdateService.class);
        startService(startIntent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.title_update_btn:
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
                            List<FutureWeather> futureWeather = weatherDao.queryFutureWeatherCode(cityCode);
                            if(todayWeather != null && futureWeather != null) {
                                Message msg = new Message();
                                msg.what = UPDATE_WEATHER;
                                msg.obj = futureWeather;
                                Bundle data = new Bundle();
                                data.putSerializable("todayWeather",todayWeather);
                                msg.setData(data);
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
                                Thread.sleep(5000);
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
            case R.id.title_locate_btn:
                if(mLocationClient == null)
                    return;
                //初始化LBS
                InitLocation();
                if (!mLocationClient.isStarted()) {
                    //启动定位SDK
                    mLocationClient.start();
                }
                // 发起定位
                mLocationClient.requestLocation();
                break;
            case R.id.title_share_btn:
                /*LayoutInflater flater = getLayoutInflater();
                View share_dialog =  flater.inflate(R.layout.share_dialog, null);
                ImageView share_friend = (ImageView) share_dialog.findViewById(R.id.share_friend);
                ImageView share_friends = (ImageView) share_dialog.findViewById(R.id.share_friends);
                share_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wechatShare(0);
                    }
                });
                share_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wechatShare(1);
                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setIcon(R.drawable.logo);//窗口头图标
                dialog.setTitle(getString(R.string.share));//窗口名
                dialog.setView(share_dialog);
                dialog.setNegativeButton(getString(R.string.cancel), null);
                dialog.show();*/
                shareInfo();
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
            //mCurrentCity.setText(cityString + "天气预报");
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
                    List<FutureWeather> futureWeather = weatherDao.queryFutureWeatherCode(cityCode);
                    if(todayWeather != null && futureWeather != null) {
                        Message msg = new Message();
                        msg.what = UPDATE_WEATHER;
                        msg.obj = futureWeather;
                        Bundle data = new Bundle();
                        data.putSerializable("todayWeather",todayWeather);
                        msg.setData(data);
                        //发送消息
                        mHandler.sendMessage(msg);
                    }
                }
            }.start();
        }//得到新Activity 关闭后返回的数据
    }

    public TextView cityTv,timeTv,humidyTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,currentTemTv,climateTv,windTv;
    public ImageView weatherImg,pmImg;
    //public TextView weekTv1,temperatureTv1,climateTv1,windTv1;
    //public ImageView weatherImg,pmImg,weatherImg1;
    TextView[] FurWeeks = new TextView[6];
    TextView[] FurTemps = new TextView[6];
    TextView[] FurTypes = new TextView[6];
    TextView[] FurWinds = new TextView[6];
    ImageView[] FurImages = new ImageView[6];

    //更新数据
    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View one_page = inflater.inflate(R.layout.future_weather1, null);
        View two_page = inflater.inflate(R.layout.welcome, null);

        views = new ArrayList<View>();
        views.add(one_page);
        views.add(two_page);
        vpAdapter = new ViewPagerAdapter2(views, this);
        vp = (ViewPager)findViewById(R.id.viewpager2);
        vp.setAdapter(vpAdapter);

        //页面监听事件，使得小圆点随着页面的变化而变化
        vp.setOnPageChangeListener(this);

        mCurrentCity = (TextView)findViewById(R.id.current_city);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.publish_time);
        currentTemTv = (TextView)findViewById(R.id.weather_temp);
        humidyTv = (TextView) findViewById(R.id.weather_humid);
        pmDataTv = (TextView) findViewById(R.id.pm_value);
        pmQualityTv = (TextView) findViewById(R.id.pollute_type);
        pmImg = (ImageView) findViewById(R.id.pm_img);
        weekTv = (TextView) findViewById(R.id.today_date);
        temperatureTv = (TextView) findViewById(R.id.today_temp);
        climateTv = (TextView) findViewById(R.id.today_type);
        windTv = (TextView) findViewById(R.id.today_wind);
        weatherImg = (ImageView) findViewById(R.id.weather_pic);

        //未来四天天气
        FurWeeks[0] = (TextView)one_page.findViewById(R.id.weather1_date);
        FurImages[0] = (ImageView)one_page.findViewById(R.id.weather1_pic);
        FurTemps[0] = (TextView)one_page.findViewById(R.id.weather1_temp);
        FurTypes[0] = (TextView)one_page.findViewById(R.id.weather1_type);
        FurWinds[0] = (TextView)one_page.findViewById(R.id.weather1_wind);

        FurWeeks[1] = (TextView)one_page.findViewById(R.id.weather2_date);
        FurImages[1] = (ImageView)one_page.findViewById(R.id.weather2_pic);
        FurTemps[1] = (TextView)one_page.findViewById(R.id.weather2_temp);
        FurTypes[1] = (TextView)one_page.findViewById(R.id.weather2_type);
        FurWinds[1] = (TextView)one_page.findViewById(R.id.weather2_wind);

        FurWeeks[2] = (TextView)one_page.findViewById(R.id.weather3_date);
        FurImages[2] = (ImageView)one_page.findViewById(R.id.weather3_pic);
        FurTemps[2] = (TextView)one_page.findViewById(R.id.weather3_temp);
        FurTypes[2] = (TextView)one_page.findViewById(R.id.weather3_type);
        FurWinds[2] = (TextView)one_page.findViewById(R.id.weather3_wind);

        FurWeeks[3] = (TextView)one_page.findViewById(R.id.weather4_date);
        FurImages[3] = (ImageView)one_page.findViewById(R.id.weather4_pic);
        FurTemps[3] = (TextView)one_page.findViewById(R.id.weather4_temp);
        FurTypes[3] = (TextView)one_page.findViewById(R.id.weather4_type);
        FurWinds[3] = (TextView)one_page.findViewById(R.id.weather4_wind);

        /*FurWeeks[4] = (TextView)two_page.findViewById(R.id.weather5_date);
        FurImages[4] = (ImageView)two_page.findViewById(R.id.weather5_pic);
        FurTemps[4] = (TextView)two_page.findViewById(R.id.weather5_temp);
        FurTypes[4] = (TextView)two_page.findViewById(R.id.weather5_type);
        FurWinds[4] = (TextView)two_page.findViewById(R.id.weather5_wind);

        FurWeeks[5] = (TextView)two_page.findViewById(R.id.weather6_date);
        FurImages[5] = (ImageView)two_page.findViewById(R.id.weather6_pic);
        FurTemps[5] = (TextView)two_page.findViewById(R.id.weather6_temp);
        FurTypes[5] = (TextView)two_page.findViewById(R.id.weather6_type);
        FurWinds[5] = (TextView)two_page.findViewById(R.id.weather6_wind);*/

        //未加载初始化
        //mCurrentCity.setText(mCurrentCity.getText() + "天气");
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
    }
    private void updateTodayWeather(TodayWeather todayWeather, List<FutureWeather> futureWeatherList){
        mCurrentCity.setText(todayWeather.getCity() + " " + getString(R.string.weather));
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(getString(R.string.publish) + " " + todayWeather.getUpdatetime());
        currentTemTv.setText(getString(R.string.wendu) + " " + todayWeather.getWendu() + "℃");
        humidyTv.setText(getString(R.string.humidy) + " " + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        int icon = WeatherDao.parseIcon(todayWeather.getPm25Img() + ".png");
        pmImg.setImageResource(icon);
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        windTv.setText(todayWeather.getFengxiang() + " " + todayWeather.getFengli());
        if (todayWeather.getType()!=null) {
            weatherImg.setImageResource(WeatherDao.parseIcon(todayWeather.getType()));
        }

        //昨天天气
        /*FurWeeks[0].setText(todayWeather.getFdate0());
        FurTemps[0].setText(todayWeather.getFhigh0() + "~" + todayWeather.getFlow0());
        FurTypes[0].setText(todayWeather.getFtype0());
        FurWinds[0].setText(todayWeather.getFfengxiang0());
        if (todayWeather.getFtype0()!=null) {
            FurImages[0].setImageResource(WeatherDao.parseIcon(todayWeather.getFtype0()));
        }*/
        //未来4天
        for(int i = 0; i < futureWeatherList.size(); i++){
            FutureWeather futureWeather = futureWeatherList.get(i);
            Log.d("future:" + i, futureWeather.toString());
            FurWeeks[i].setText(futureWeather.getDate());
            FurTemps[i].setText(futureWeather.getLow() + "~" + futureWeather.getHigh());
            FurTypes[i].setText(futureWeather.getType());
            FurWinds[i].setText(futureWeather.getFengxiang());
            if(futureWeather.getType() != null)
                FurImages[i].setImageResource(WeatherDao.parseIcon(futureWeather.getType()));
        }

        mUpdateBtn.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
    }
    /**
     * 初始化LBS服务
     */
    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式:精确定位
        //option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02

        //当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
        // 每调用一次requestLocation( )，定位SDK会发起一次定位。请求定位与监听结果一一对应
        option.setScanSpan(0);//设置发起定位请求的间隔时间为0ms
        option.setIsNeedAddress(true);//获取位置信息
        mLocationClient.setLocOption(option);
    }

    /**
     * BDLocationListener监听对象
     */
    private class MyLocationListener implements BDLocationListener {

        /**
         * 1.接收异步返回的定位结果，参数是BDLocation类型参数。
         *
         * @param location 2. BDLocation类，封装了定位SDK的定位结果，
         *                 在BDLocationListener的onReceive方法中获取。
         *                 通过该类用户可以获取error code，位置的坐标，精度半径等信息。
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d("onReceiveLocation: ","onReceiveLocation");
            // Receive Location
            StringBuffer sb = new StringBuffer();
            sb.append("\ncityName : ");
            sb.append(location.getCity());
            sb.append("\nProvince : ");
            sb.append(location.getProvince());
            sb.append("\nDistrict : ");
            sb.append(location.getDistrict());
            Log.i("LocationApiDem", sb.toString());
            //通过区/县信息 信息更行天气
            String district = location.getDistrict();
            String myDistrict = district.substring(0, district.length() - 1);
            Log.d("onReceiveLocation::", myDistrict);
            //执行更新LBS
            executeUpdateByLBS(myDistrict.trim());
        }
    }
    /**
     * * 通过LBS查询天气
     *
     * @param myDistrict 城市名称（区县市）
     */
    protected void executeUpdateByLBS(String myDistrict) {
        //获取城市列表
        MyApplication myApplication = (MyApplication) MyApplication.getInstance();
        List<City> mCityList = myApplication.getCityList();
        //获取定位到的城市
        Log.d("executeUpdateByLBS", myDistrict);
        for (City city : mCityList) {
            //查询到定位的城市的ID
            if ((city.getCity()).equals(myDistrict)) {
                cityCode = city.getNumber();
                Log.d("executeUpdateByLBS::", cityCode);
                //查询并更新UI
                new Thread() {
                    public void run(){
                        TodayWeather todayWeather = weatherDao.queryWeatherCode(cityCode);
                        List<FutureWeather> futureWeather = weatherDao.queryFutureWeatherCode(cityCode);
                        if(todayWeather != null && futureWeather != null) {
                            Message msg = new Message();
                            msg.what = UPDATE_WEATHER;
                            msg.obj = futureWeather;
                            Bundle data = new Bundle();
                            data.putSerializable("todayWeather",todayWeather);
                            msg.setData(data);
                            //发送消息
                            mHandler.sendMessage(msg);
                        }
                    }
                }.start();
                //executeUpdate(0, cityCode);
                //跳转到第一页
                //viewPager.setCurrentItem(0);
            }
        }
    }
    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://mobile100.zhangqx.com/studentworks.html";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "#小天气#";
        //提取天气信息
        StringBuilder weatherInfo = new StringBuilder();

        /*switch(vp.getCurrentItem()){
            case 0:*/
                //View view = views.get(vp.getCurrentItem());
                weatherInfo.append("今天"+((TextView)findViewById(R.id.city)).getText()+"天气");
                weatherInfo.append(((TextView)findViewById(R.id.today_type)).getText()+",");
                weatherInfo.append(((TextView)findViewById(R.id.today_temp)).getText()+",");
                weatherInfo.append(((TextView)findViewById(R.id.today_wind)).getText()+",");
                weatherInfo.append(((TextView)findViewById(R.id.publish_time)).getText()+"。");
            /*    break;
            case 1:
            case 2:
            case 3:
                View viewSelf = views.get(vp.getCurrentItem());
                weatherInfo.append("今天"+((TextView)viewSelf.findViewById(R.id.page2_city)).getText()+"天气");
                weatherInfo.append(((TextView)viewSelf.findViewById(R.id.page2_climate)).getText()+",");
                weatherInfo.append(((TextView)viewSelf.findViewById(R.id.page2_temperature)).getText()+",");
                weatherInfo.append(((TextView)viewSelf.findViewById(R.id.page2_wind)).getText()+",");
                weatherInfo.append(((TextView)viewSelf.findViewById(R.id.page2_time)).getText()+"。");
                break;
            default:
                break;
        }*/

        msg.description = weatherInfo.toString();
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        msg.thumbData = Util.bmpToByteArray(thumb, true);
        //msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());// transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = (flag==0)?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        wxApi.sendReq(req);
        finish();
    }
    //存储和分享方法
    private void shareInfo() {
        File file = null;
        Bitmap b = takeScreenShot(MainActivity.this);
        String SavePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
        try {
            File path = new File(SavePath);
            String filepath = SavePath + "/Screen_1.png";
            file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.setType("image/*");
        Uri u = Uri.fromFile(file);
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.putExtra(Intent.EXTRA_TEXT, "嘻哈天气");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }
    //截屏方法
    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }
    @Override
    protected void onStart() {
        dataReceiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.gzh.job.weather.com.gzh.job.activity.MainActivity");
        registerReceiver(dataReceiver,filter);
        Log.i(TAG, "onStart");
        //LBS服务
        // 此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
        //Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
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
            TodayWeather todayWeather = (TodayWeather)intent.getSerializableExtra("todayWeather");
            List<FutureWeather> futureWeatherList = (List<FutureWeather>) intent.getSerializableExtra("futureWeather");
            Log.d("broad: ",todayWeather.toString() + "\t futureWeather: " + futureWeatherList.toString());
            updateTodayWeather(todayWeather, futureWeatherList);
        }
    }
}
