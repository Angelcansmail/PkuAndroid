package com.gzh.job.weather.com.gzh.job.dao;

import android.util.Log;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.entity.FutureWeather;
import com.gzh.job.weather.com.gzh.job.entity.TodayWeather;

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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by Angel on 2015/12/2.
 */
public class WeatherDao {
    public TodayWeather queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("WeatherDao：", address);
        TodayWeather todayWeather = null;
        List<FutureWeather> futureWeathers = null;
        //主线程调用子线程，从网络获取数据 主线程一般只是处理与UI相关的事件，用户按键、触屏、绘图，把相关的事件分发到对应的组件处理
       /* new Thread(new Runnable() {
            @Override
            public void run() {*/
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
                todayWeather = parseXML(responseStr);   //今天+未来四天的天气
                //List<FutureWeather> lists = ParseXMLFuture(responseStr);//12-14更新未来天气信息
                if (todayWeather != null) {
                    Log.d("weather ", todayWeather.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
           /* }
        }).start();*/
        return todayWeather;
    }
    //�����������
    public TodayWeather parseXML(String xmlData){
        //Log.d("aaaaaaaaaaaaaaa",xmlData);
        TodayWeather todayWeather = null;
        try{
            int shiduCount = 0;
            int fengliCount = 0;
            int fengxiangCount = 0;
            int pm25Count = 0;
            int highWenduCount = 0;
            int lowWenduCount = 0;
            int typeCount = 0;   //��
            int dateCount = 0;
            //昨天天气信息
            int type_1Count = 0;
            int fx_1Count = 0;
            //��������������
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            //���н���
            xmlPullParser.setInput(new StringReader(xmlData));
            //�����һ�������¼�
            int eventType = xmlPullParser.getEventType();
            //ʹ��ѭ���ж��Ƿ�������
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType) {
                    //�ĵ���ʼ�¼�0
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //�жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�صĿ�ʼ�¼�
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu") && shiduCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                                shiduCount++;
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25") && pm25Count == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                                todayWeather.setPm25Img(Integer.parseInt(xmlPullParser.getText()));
                                pm25Count++;
                            } else if (xmlPullParser.getName().equals("high") && highWenduCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2));
                                highWenduCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowWenduCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2));
                                lowWenduCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                todayWeather.setWeather_pic(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("suggest")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setSuggest(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("MajorPollutants")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setMajorPollutants(xmlPullParser.getText());
                            }
                            //昨天的天气信息
                            else if (xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFdate0(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFhigh0(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFlow0(xmlPullParser.getText().substring(2).trim());
                            }else if (xmlPullParser.getName().equals("type_1") && type_1Count==0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFtype0(xmlPullParser.getText());
                                type_1Count++;
                            } else if (xmlPullParser.getName().equals("fx_1") && fx_1Count==0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFfengxiang0(xmlPullParser.getText());
                                fx_1Count++;
                            }
                        }
                        break;
                    //�����¼�
                    case XmlPullParser.END_TAG:
                        break;
                }
                //������һ��Ԫ�ز�������Ӧ�¼�
                eventType = xmlPullParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return todayWeather;
    }
    public List<FutureWeather> queryFutureWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("WeatherDao：", address);
        List<FutureWeather> futureWeathers = null;
        //主线程调用子线程，从网络获取数据 主线程一般只是处理与UI相关的事件，用户按键、触屏、绘图，把相关的事件分发到对应的组件处理
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
                futureWeathers = ParseXMLFuture(responseStr);
                if (futureWeathers != null) {
                    Log.d("weather ", futureWeathers.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return futureWeathers;
    }
    private List<FutureWeather> ParseXMLFuture(String xmldata){
        List<FutureWeather> lists = null;
        FutureWeather futureWeather = null;
        try{
            int typeCou = 0;
            int fengxiangCou = 0;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(new StringReader(xmldata));
            int eventType = pullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        lists = new ArrayList<FutureWeather>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(pullParser.getName().equals("weather")) {
                            futureWeather = new FutureWeather();
                        }
                        if (futureWeather != null) {
                            if (pullParser.getName().equals("date")) {
                                eventType = pullParser.next();
                                futureWeather.setDate(pullParser.getText());
                            } else if (pullParser.getName().equals("high")) {
                                eventType = pullParser.next();
                                futureWeather.setHigh(pullParser.getText().substring(2).trim());
                            } else if (pullParser.getName().equals("low")) {
                                eventType = pullParser.next();
                                futureWeather.setLow(pullParser.getText().substring(2).trim());
                            } else if (pullParser.getName().equals("type") && typeCou==0) {
                                eventType = pullParser.next();
                                futureWeather.setType(pullParser.getText());
                                typeCou++;
                            } else if (pullParser.getName().equals("fengxiang") && fengxiangCou==0) {
                                eventType = pullParser.next();
                                futureWeather.setFengxiang(pullParser.getText());
                                fengxiangCou++;
                            }
                        } break;
                    case XmlPullParser.END_TAG:
                        if (pullParser.getName().equals("weather")) {
                            lists.add(futureWeather);
                            futureWeather = null;
                            typeCou = 0;
                            fengxiangCou = 0;
                        } break;
                    default:
                        break;
                }
                eventType = pullParser.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }
    public static int parseIcon(String strIcon){
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
}
