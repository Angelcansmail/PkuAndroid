package com.gzh.job.weather.com.gzh.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.MyApplication;
import com.gzh.job.weather.com.gzh.job.db.CityDB;
import com.gzh.job.weather.com.gzh.job.entity.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 2015/10/17.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    SQLiteDatabase db;
    private ImageView mBackBtn;
    private TextView mTitle_city;
    List<City> list = new ArrayList<City>();
    List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    MyApplication application;
    CityDB cityDB;
    City city;
    public void onCreate(Bundle savedInstanceStatus){
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.city_layout);
        application = (MyApplication)this.getApplication();

        //在activity中调用
//        CityDB cityDB = new CityDB(this);
//        list = cityDB.getAllCity();

       /* Iterator<City> iterator = list.iterator();
        while(iterator.hasNext()){
            Map<String,String> listem = new HashMap<String,String>();
            City c = iterator.next();
            listem.put("number",c.getNumber());
            listem.put("province",c.getProvince());
            listem.put("city",c.getCity());
            listItems.add(listem);
        }*/
        listItems = application.getCityItemsList();
        //SimpleAdapter adapter = new SimpleAdapter(this,listItems,R.layout.city_items,new String[] {"number","province","city"},new int[]{R.id.list_number,R.id.list_province,R.id.list_city});
        SimpleAdapter adapter = new SimpleAdapter(this,listItems,R.layout.city_items,new String[] {"province","city"},new int[]{R.id.list_province,R.id.list_city});
        ListView mlistView = (ListView)findViewById(R.id.city_list);
        mlistView.setAdapter(adapter);

        Intent intent = SelectCity.this.getIntent();
        mTitle_city = (TextView)findViewById(R.id.title_current_city);

        SharedPreferences sharedPreferences = getSharedPreferences("cityCode", MODE_PRIVATE);
        mTitle_city.setText("当前城市：" + sharedPreferences.getString("cityName", "北京"));
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(SelectCity.this, "你单击了:" + listItems.get(i).get("city"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent();
                SharedPreferences sharedPreferences = getSharedPreferences("cityCode", MODE_PRIVATE);
                Log.d("cityName:", sharedPreferences.getString("cityName", "北京"));
                if(!listItems.get(i).get("city").equals(""))
                    mTitle_city.setText("当前城市：" + listItems.get(i).get("city"));
                intent.putExtra("cityCode", listItems.get(i).get("number"));
                intent.putExtra("city", listItems.get(i).get("city"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //选择城市
        Button btn = (Button)findViewById(R.id.search_city);
        btn.setOnClickListener(this);
        //返回主界面
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
           case R.id.title_back:
                //关闭Activity
                SelectCity.this.finish();
                break;
           case R.id.search_city:
               cityDB = application.openCityDB();
               listItems = new ArrayList<Map<String,String>>();
               EditText citynameEditText = (EditText) findViewById(R.id.myedit);
               application.getCityItemsList();
               listItems = cityDB.getCity(citynameEditText.getText().toString());
               SimpleAdapter adapter = new SimpleAdapter(this,listItems,R.layout.city_items,new String[] {"province","city"},new int[]{R.id.list_province,R.id.list_city});
               ListView mlistView = (ListView)findViewById(R.id.city_list);
               mlistView.setAdapter(adapter);
               break;
           default:
               break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null && db.isOpen())
            db.close();
    }
    /*    protected ArrayList<Map<String,String>> converCursorToList(Cursor cursor){
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> map = new HashMap<String,String>();
            map.put("province",cursor.getString(1));
            map.put("city",cursor.getString(2));
            result.add(map);
        }
        Log.d("list: ",result.toString());
        return result;
    }*/
}
