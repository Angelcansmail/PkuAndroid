package com.gzh.job.weather.com.gzh.job.dao;

import android.util.Log;

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
import java.util.zip.GZIPInputStream;

/**
 * Created by Angel on 2015/12/2.
 */
public class WeatherDao {
    public TodayWeather queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("WeatherDao：", address);
        TodayWeather todayWeather = null;
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
                        todayWeather = parseXML(responseStr);
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
                int typeCount = 0;   //��
                int dateCount = 0;
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
}
