package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    //2 webview
    EditText url;
    WebView showURL;
    ImageView imgview;
    Bitmap bitmap;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH){
            String urlStr = url.getText().toString();
            //加载并显示urlStr对应的网页
            showURL.loadUrl(urlStr);
            return true;
        }
        return false;
    }

    int[] imagesId = new int[]{
                R.drawable.biz_plugin_weather_0_50, R.drawable.biz_plugin_weather_101_150, R.drawable.biz_plugin_weather_151_200,
                R.drawable.biz_plugin_weather_201_300, R.drawable.biz_plugin_weather_51_100, R.drawable.biz_plugin_weather_baoxue
        };
        int currentImgId = 0;

        Handler handler = new Handler(){
            public void handleMessage(Message msg){
                if (msg.what == 0x123)
                    //显示从网上下载的图片
                    imgview.setImageBitmap(bitmap);
            }
        };
    //String url = "http://mobile100.sinaapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("get", "getData()");
        //getData();

        imgview = (ImageView) findViewById(R.id.imgview);
        url = (EditText) findViewById(R.id.url);
        showURL = (WebView) findViewById(R.id.showURL);
        showURL.getSettings().setJavaScriptEnabled(true);//支持javascript对象
        //页面直接加载url
        /*showURL.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view) {
                view.loadUrl(url);  //目标仍在本页面显示
                return true;
            }
        });
        showURL.loadUrl(url);*/

        //URL、URLCONNECTION获取网络数据测试
        new Thread(){
            public void run(){
                try{
                    URL url = new URL("http://www.baidu.com/img/bdlogo.png");
                    //URLConnection
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);
                    //打开URL对应的资源输入流
                    InputStream is = connection.getInputStream();
                    //URL访问网络资源
                    //InputStream is = url.openStream();
                    //从InputStream中解析图片
                    bitmap = BitmapFactory.decodeStream(is);
                    handler.sendEmptyMessage(0x123);
                    is.close();

                    //下载网络图片
                    //再次打开URL对应资源的输入流
                    is = url.openStream();
                    //打开手机对应的输出流
                    //存放在手机中，并命名为baidulogo.png
                    OutputStream os = openFileOutput("baidulogo.png",MODE_WORLD_READABLE);
                    byte[] buff = new byte[1024];
                    int hasRead = 0;
                    //因为网络下载一般不可能一次下载完毕，我们将每次下载好的有效数据写入
                    while((hasRead = is.read(buff)) > 0){
                        os.write(buff,0,hasRead);
                    }
                    is.close();
                    os.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        //final ImageView show = (ImageView) findViewById(R.id.show);
        /*final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    show.setImageResource(imagesId[currentImgId++]);
                    if (currentImgId >= 6)
                        currentImgId = 0;
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }, 0, 800);*/
    }
}
