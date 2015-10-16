package com.gzh.job.weather.com.gzh.job.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.entity.FutureWeather;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;
import com.gzh.job.weather.com.gzh.job.util.NetUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

/**
 * Created by Angel on 2015/9/26.
 */
public class MainActivity extends Activity implements OnClickListener{
    private final static String TAG="MainActivity";
    private ImageView mUpdateBtn;
    /*private int[] imgId = new int[]{
            R.drawable.biz_plugin_weather_0_50,R.drawable.biz_plugin_weather_51_100,R.drawable.biz_plugin_weather_101_150,R.drawable.biz_plugin_weather_151_200,R.drawable.biz_plugin_weather_201_300,
            R.drawable.biz_plugin_weather_greater_300,R.drawable.biz_plugin_weather_baoxue,R.drawable.biz_plugin_weather_baoyu,R.drawable.biz_plugin_weather_dabaoyu,R.drawable.biz_plugin_weather_daxue,
            R.drawable.biz_plugin_weather_dayu,R.drawable.biz_plugin_weather_duoyun,R.drawable.biz_plugin_weather_leizhenyu,R.drawable.biz_plugin_weather_leizhenyubingbao,R.drawable.biz_plugin_weather_shachenbao,
            R.drawable.biz_plugin_weather_tedabaoyu,R.drawable.biz_plugin_weather_wu,R.drawable.biz_plugin_weather_xiaoxue,R.drawable.biz_plugin_weather_xiaoyu,R.drawable.biz_plugin_weather_yin,
            R.drawable.biz_plugin_weather_yujiaxue,R.drawable.biz_plugin_weather_zhenxue,R.drawable.biz_plugin_weather_zhenyu,R.drawable.biz_plugin_weather_zhongyu,R.drawable.biz_plugin_weather_zhongxue,R.drawable.biz_plugin_weather_qing,

    };*/

    private static final int UPDATE_WEATHER = 1;
    private Thread mThread;
    SharedPreferences preferences;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){//主线程
            switch (msg.what){
                case UPDATE_WEATHER:
                    //updateFutureWeather((FutureWeather)msg.obj);
                    updateTodayWeather((TodayWeather)msg.obj);
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*///设置屏幕横向
        Log.i(TAG, "onCreate");
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_update_btn){
            //MODE_PRIVATE 只能被本应用程序访问，MODE_WORLD_READABLE可被其他应用程序读，但不可写，MODE_WORLD_WRITEABLE可被其他应用程度读写
            Log.i("dir: ","" + getFilesDir());
            preferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = preferences.getString("main_city_code","101010100");
            Log.d("Weather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("Weather","网络通畅");
                queryWeatherCode(cityCode);
            }else{
                Log.d("Weather","无网络");
                Toast.makeText(MainActivity.this, "无网络", Toast.LENGTH_LONG).show();
            }
        }
        /*if (mThread == null){
            mThread = new Thread(runnable);
            mThread.start();
        }else{
            updateTodayWeather((TodayWeather)msg.obj);
        }*/
    }
    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("Weather",address);
        //主线程调用子线程，从网络获取数据 主线程一般只是处理与UI相关的事件，用户按键、触屏、绘图，把相关的事件分发到对应的组件处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //1：创建HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //2：创建代表请求的对象，参数是访问服务器的地址
                    HttpGet httpGet = new HttpGet(address);
                    //3：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    //4：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200){
                        //5：从相应对象中取出数据，放到entity中
                        HttpEntity entity = httpResponse.getEntity();

                        InputStream responseStream = entity.getContent();
                        responseStream = new GZIPInputStream(responseStream);

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder response = new StringBuilder();
                        String str;
                        while((str = reader.readLine()) != null){
                            response.append(str);
                        }
                        String responseStr = response.toString();
                        Log.d("Get",responseStr);
                        TodayWeather todayWeather = parseXML(responseStr);
                        if (todayWeather != null) {
                            //新启动的线程无法访问该Activity里的组件，所以需要通过Handler发送信息
                            Message msg = new Message();
                            msg.what = UPDATE_WEATHER;
                            msg.obj = todayWeather;
                            //发送消息
                            mHandler.sendMessage(msg);
                            Log.d("weather ", todayWeather.toString());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //解析天气数据
    private TodayWeather parseXML(String xmlData){
        TodayWeather todayWeather = null;
        FutureWeather tomorrowWeather = null;
        FutureWeather afterdayWeater = null;
        FutureWeather theThirddayWeather = null;
        try{
            int shiduCount = 0;
            int fengliCount = 0;
            int fengxiangCount = 0;
            int pm25Count = 0;
            int highWenduCount = 0;
            int lowWenduCount = 0;
            int typeCount = 0;   //雾
            int dateCount = 0;

            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Log.d("myapp","parseXML");

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType) {
                    //文档开始事件0
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                            tomorrowWeather = new FutureWeather();
                        }
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("shidu") && shiduCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setShidu(xmlPullParser.getText());
                            shiduCount++;
                        }else if (xmlPullParser.getName().equals("wendu")){
                            eventType = xmlPullParser.next();
                            todayWeather.setWendu(xmlPullParser.getText());
                            tomorrowWeather.setWendu(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setFengli(xmlPullParser.getText());
                            tomorrowWeather.setFengli(xmlPullParser.getText());
                            fengliCount++;
                        }else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setFengxiang(xmlPullParser.getText());
                            tomorrowWeather.setFengxiang(xmlPullParser.getText());
                            fengxiangCount++;
                        }else if (xmlPullParser.getName().equals("pm25") && pm25Count == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                            todayWeather.setPm25Img(Integer.parseInt(xmlPullParser.getText()));
                            pm25Count++;
                        }else if (xmlPullParser.getName().equals("high")  && highWenduCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText().substring(2));
                            tomorrowWeather.setHigh(xmlPullParser.getText().substring(2));
                            highWenduCount++;
                        }else if (xmlPullParser.getName().equals("low") && lowWenduCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText().substring(2));
                            tomorrowWeather.setHigh(xmlPullParser.getText().substring(2));
                            lowWenduCount++;
                        }else if (xmlPullParser.getName().equals("type") && typeCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
                            todayWeather.setWeather_pic(xmlPullParser.getText());
                            tomorrowWeather.setType(xmlPullParser.getText());
                            tomorrowWeather.setWeather_pic(xmlPullParser.getText());
                            typeCount++;
                        }else if (xmlPullParser.getName().equals("date") && dateCount == 0){
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            tomorrowWeather.setDate(xmlPullParser.getText());
                            dateCount++;
                        }else if (xmlPullParser.getName().equals("suggest")){
                            eventType = xmlPullParser.next();
                            todayWeather.setSuggest(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("quality")){
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("MajorPollutants")){
                            eventType = xmlPullParser.next();
                            todayWeather.setMajorPollutants(xmlPullParser.getText());
                        }
                        break;
                    //结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return todayWeather;
    }

    public TextView cityTv,timeTv,humidyTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    public TextView weekTv1,temperatureTv1,climateTv1,windTv1;
    public ImageView weatherImg,pmImg,weatherImg1;
    //更新数据
    private void initView(){
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
        Log.i(TAG, "onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
