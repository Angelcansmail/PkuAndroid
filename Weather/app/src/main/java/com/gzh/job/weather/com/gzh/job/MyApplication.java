package com.gzh.job.weather.com.gzh.job;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.gzh.job.weather.com.gzh.job.db.CityDB;
import com.gzh.job.weather.com.gzh.job.entity.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 2015/10/17.
 */
public class MyApplication extends Application {
    private String TAG = CityDB.class.getSimpleName();
    private static MyApplication mApplication;
    SQLiteDatabase db;
    Context context;
    List<City> mCityList;
    public CityDB mCityDB,mdb;
    public static final String CITY_DB_NAME = "city.db";

    //List<City> mCityList = new ArrayList<City>();
    List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "myapplication->onCreate");   //优先于acitvity调用，application的生命周期贯穿整个程序
        mApplication = this;
        //从数据库获取城市
        mCityDB = openCityDB();
        initCityList();
        setCityItemsList(listItems);
    }
    public CityDB openCityDB(){
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator
                + getPackageName()
                + File.separator
                + "databases"
                + File.separator
                + CITY_DB_NAME;
        File dbFile = new File(path);
        if(dbFile.exists()){
            Log.d("debug", "存在");
        }else{
            try{
                db = openOrCreateDatabase("city.db",Context.MODE_PRIVATE,null);
                db.close();
                InputStream is = getResources().getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(dbFile);
                //FileOutputStream fos = new FileOutputStream(path);
                int len = -1;
                byte[] buffer = new byte[1024];
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
       return new CityDB(this,path);
        //   db = context.openOrCreateDatabase("city.db",Context.MODE_PRIVATE,null);
    }
    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable(){
            public void run(){
                listItems = prepareCityList();
            }
        }).start();
    }
    private List<Map<String,String>> prepareCityList(){
        mCityList = mCityDB.getAllCity();
        listItems = CityDB.transtoCityItemsList(mCityList);
        return listItems;
    }

    public List<Map<String, String>> getCityItemsList() {
        Log.d("cityInfo: ",listItems.toString());
        return listItems;
    }

    public void setCityItemsList(List<Map<String, String>> listItems) {
        this.listItems = listItems;
    }
    public static MyApplication getInstance(){
        return mApplication;
    }
    public List<City> getCityList(){
        return mCityList;
    }

}
