package com.gzh.job.weather.com.gzh.job.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gzh.job.weather.com.gzh.job.entity.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 15/10/20.
 */
public class CityDB {
    private final String TAG = CityDB.class.getSimpleName();
    public static final String CITY_DB_NAME = "city.db";
    public static final String CITY_DB_TABLE = "city";
    private SQLiteDatabase db;
    private Context context;
    private static List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    //直接代开，没用application，如果用application时，注释掉
/*    public CityDB(Context context){
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator
                + context.getPackageName()
                + File.separator
                + "databases"
                + File.separator
                + CITY_DB_NAME;
        File dbFile = new File(path);
        Log.d("debug","1");
        if(dbFile.exists()){
            Log.d("debug","存在");
        }else{
            try{
                db = context.openOrCreateDatabase("city.db",Context.MODE_PRIVATE,null);
                db.close();
                InputStream is = context.getAssets().open("city.db");
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
        db = context.openOrCreateDatabase("city.db",Context.MODE_PRIVATE,null);
    }*/
    //application
    public CityDB(Context context,String path) {
        db = context.openOrCreateDatabase(CITY_DB_NAME, Context.MODE_PRIVATE, null);
    }

    public List<City>  getAllCity(){
        List<City> list = new ArrayList<City>();
        Cursor c = db.rawQuery("SELECT * FROM "+CITY_DB_TABLE,null);
        while(c.moveToNext()){
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allfirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allPY,firstPY);
            list.add(item);
        }
        return list;
    }
    public static List<Map<String,String>> transtoCityItemsList(List<City> mCityList){
        listItems = new ArrayList<Map<String,String>>();
        Iterator<City> iterator = mCityList.iterator();
        while(iterator.hasNext()){
            Map<String,String> listem = new HashMap<String,String>();
            City c = iterator.next();
            listem.put("number",c.getNumber());
            listem.put("province",c.getProvince());
            listem.put("city",c.getCity());
            listItems.add(listem);
        }
        return listItems;
    }
    public List<Map<String,String>> getCity(String cityEdittext){
        listItems = new ArrayList<Map<String,String>>();
        List<City> list = new ArrayList<City>();
        String strSql = "SELECT * FROM " + CITY_DB_TABLE + " WHERE number like '%" + cityEdittext + "%' or city like '%" + cityEdittext + "%' or allpy like '%" + cityEdittext + "%' or allfirstpy like '%" + cityEdittext + "%' or firstpy like '%" + cityEdittext + "%'";
        Log.d("sql",strSql);
        Cursor c = db.rawQuery(strSql,null);
        while(c.moveToNext()){
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allfirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allPY,firstPY);
            list.add(item);
        }
        listItems = transtoCityItemsList(list);
        Log.d("cityListSearch", "" + listItems);
        return listItems;
    }
    public void test () {
        Cursor c = db.rawQuery("SELECT * FROM city",null);
        c.moveToNext();
        Log.d("bug",c.getString(c.getColumnIndex("city")));
    }

}
