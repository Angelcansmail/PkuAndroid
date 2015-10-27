package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import cn.edu.pku.ss.gzh.gojson.adapter.BookListAdapter;
import cn.edu.pku.ss.gzh.gojson.entity.Book;

/**
 * Created by Angel on 2015/10/19.
 */
public class JsonActivity extends Activity {
    //1 Json BOOK
    public String url = "https://api.douban.com/v2/book/1220562";
    //public String url = "http://apistore.baidu.com/microservice/weather?citypinyin=beijing";
    private ListView lv;
    private BookListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_json_layout);

        //1 使用Gson、Fastjson、Jackenson、ObjectJson获取图书信息
        getData();
    }
    //Goson 获取json数据
    private void getData() {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String arg0) {
                try{
                    dealData(arg0);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //解析多个信息
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        new Volley().newRequestQueue(getApplicationContext()).add(request);
    }

    private void dealData(String result) throws JSONException {
        Log.d("result",result);
        Gson gson = new Gson();
        Book book = gson.fromJson(result, Book.class);
        Log.d("info", book.getTitle() + ":" + book.getPublisher() + ":" + book.getSummary() + ":" + book.getTags().size());

        /*// Gson gson = new Gson();
        // Type listType = new TypeToken<ArrayList<Book>>() {
        // }.getType();
        JSONObject object = new JSONObject(result);
        // ArrayList<Book> books = gson.fromJson(object.getString("result"),
        // listType);
        ArrayList<Book> books = (ArrayList<Book>) JSON.parseArray(
                object.getString("result"), Book.class);
        adapter = new BookListAdapter(this, books);
        lv.setAdapter(adapter);*/
    }
}
