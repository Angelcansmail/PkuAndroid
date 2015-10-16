package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Angel on 2015/10/14.
 */
public class OtherActivity extends Activity {
    //1 Json BOOK
    /*ImageView imgview;
    Bitmap bitmap;

    //1 Json BOOK
    public String url = "https://api.douban.com/v2/book/1220562";*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取上一个Activity的信息
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);

        //Toast.makeText(OtherActivity.this,name + "\t" + age,Toast.LENGTH_LONG).show();
        Log.d("name", name + "age: " + age);
        //getData();
    }
    /*private void getData() {
        Log.i("data", "getData");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String arg0) {
                Log.i("info", arg0);
                Book b = dealData(arg0);
                Log.i("书名：",b.getTitle());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        new Volley().newRequestQueue(getApplicationContext()).add(request);
    }

    private Book dealData(String result) {
        Gson gson = new Gson();
        Book book = gson.fromJson(result, Book.class);
        Log.d("info", book.getId() + ":" + book.getTitle() + ":" + book.getPublisher() + ":" + book.getSummary() + ":" + book.getTags().size());
        return book;
    }*/
}
