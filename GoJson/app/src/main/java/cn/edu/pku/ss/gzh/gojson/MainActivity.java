package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    int request_Code = 1;
    //2 webview
    /*EditText url;
    WebView showURL;*/

    //3、网络获取图片
   /* ImageView imgview;
    Bitmap bitmap;*/

    //4、SAX解析XML
    //Button button;

    //2 webview
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH){
            String urlStr = url.getText().toString();
            //加载并显示urlStr对应的网页
            showURL.loadUrl(urlStr);
            return true;
        }
        return false;
    }*/

/*    int[] imagesId = new int[]{
                R.drawable.biz_plugin_weather_0_50, R.drawable.biz_plugin_weather_101_150, R.drawable.biz_plugin_weather_151_200,
                R.drawable.biz_plugin_weather_201_300, R.drawable.biz_plugin_weather_51_100, R.drawable.biz_plugin_weather_baoxue
        };*/
       /* int currentImgId = 0;

        Handler handler = new Handler(){
            public void handleMessage(Message msg){
                if (msg.what == 0x123)
                    //显示从网上下载的图片
                    imgview.setImageBitmap(bitmap);
            }
        };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        Button webBrowserbtn = (Button) findViewById(R.id.btn_webbrowser);
        Button makecallbtn = (Button) findViewById(R.id.btn_makecalls);
        Button showmapbtn = (Button) findViewById(R.id.btn_showMap);
        Button launchMybrowserrbtn = (Button) findViewById(R.id.btn_launchMyBrowser);
        //setContentView(R.layout.fragment);
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.xml_json_layout);
        //Intent实例
        /*Button btn = (Button)findViewById(R.id.call);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
               *//*Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                startActivity(i);
                //访问通讯录
                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/1"));
                startActivity(i2);
                //访问网站
                Intent i3=new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.ss.pku.edu.cn"));
                startActivity(i3);
                //调用新Activity
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);*//*
                //Intent传递用户信息
                Intent intent1 = new Intent(MainActivity.this,ResultActivity.class);
                intent1.putExtra("name","张");
                intent1.putExtra("age",22);
                //startActivityForResult(intent1,1);

                startActivity(intent1);
            }
        });*/
       /* protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                String str= intent.getStringExtra("name");
            }
        }*/
        //Goson获取图书json数据
        /*Intent intent = new Intent(MainActivity.this,JsonActivity.class);
        startActivity(intent);*/

        //4 SAX解析xml的控制事件
        /*button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = "http://wthrcdn.etouch.cn/WeatherApi?citykey=101010100";
                        InputStream inputStream = HttpUtils.getXML(path);
                        try{
                            List<HashMap<String,String>> list = SaxService.readXML(inputStream,"weather");
                            for (HashMap<String,String> map:list){
                                Log.d("weather:",map.toString());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });*/
        //2 webview
       /* url = (EditText) findViewById(R.id.url);
        showURL = (WebView) findViewById(R.id.showURL);*/

        //老师
        /*showURL.getSettings().setJavaScriptEnabled(true);//支持javascript对象
        //页面直接加载url
        showURL.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view) {
                view.loadUrl(url);  //目标仍在本页面显示
                return true;
            }
        });*/
        //老师
        //String url = "http://mobile100.sinaapp.com";
        //showURL.loadUrl(url);

        //3、网络获取图片
        //imgview = (ImageView) findViewById(R.id.imgview);

        //URL、URLCONNECTION获取网络数据测试
        /*new Thread(){
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
        }.start();*/

        //show = (ImageView) findViewById(R.id.show);
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
        //fragment
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //---get the current display info---
        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay();
        if (d.getWidth() > d.getHeight()){
            //---landscape mode--- 竖屏显示第一个
            FirstFragment fragment1 = new FirstFragment();
            // android.R.id.content refers to the content
            // view of the activity
            fragmentTransaction.replace(
                    android.R.id.content, fragment1);
        }
        else
        {
            //---portrait mode---竖屏显示第二个
            SecondFragment fragment2 = new SecondFragment();
            fragmentTransaction.replace(
                    android.R.id.content, fragment2);
        }
        fragmentTransaction.commit();*/
    }

        //InternFilter
        public void onClickWebBrowser(View view){

        }
        public void onClickMakeCalls(View view){

        }
        public void onClickShowMap(View view){

        }
        public void onClickLaunchMyBrowser(View view){
            Intent i = new Intent("cn.edu.pku.ss.gzh.gojson.MyBrowserActivity");
            i.setData(Uri.parse("http://www.amazon.com"));
            startActivity(i);
        }
}
