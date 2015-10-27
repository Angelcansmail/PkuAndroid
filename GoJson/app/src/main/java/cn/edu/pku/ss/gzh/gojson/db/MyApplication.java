package cn.edu.pku.ss.gzh.gojson.db;

import android.app.Application;
import android.util.Log;

/**
 * Created by Angel on 2015/10/17.
 */
public class MyApplication extends Application {
    private static final String TAG = "myapp";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"myapplication->onCreate");   //优先于acitvity调用，application的生命周期贯穿整个程序
        
    }
}
