package cn.edu.pku.ss.gzh.gojson.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import cn.edu.pku.ss.gzh.gojson.R;

/**
 * Created by Angel on 2015/10/17.
 */
public class DBTest extends Activity {
    SQLiteDatabase db;
    Button btn = null;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_layout);
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("city.db");
        // 对数据库进行操作
        Cursor cursor = db.rawQuery("select * from city", null);
        inflateList(cursor);

        /*String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator + getPackageName() + File.separator +  "database" + "news.db";
        Log.d("path:",path);
        File file = new File(path);
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
        listView = (ListView) findViewById(R.id.show);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.my_title)).getText().toString();
                String content = ((EditText) findViewById(R.id.my_content)).getText().toString();
                try {
                    insertData(db, title, content);
                    Cursor cursor = db.rawQuery("select * from news", null);
                    inflateList(cursor);
                } catch (SQLiteException e) {
                    //执行DDL创建数据表
                    db.execSQL("create table news(_id integer primary key autoincrement," + " news_title varchar(50)," + " news_content varchar(255))");
                    //执行insert插入数据
                    insertData(db, title, content);
                    // 执行查询
                    Cursor cursor = db.rawQuery("select * from news", null);
                    inflateList(cursor);
                }
            }
        });*/
    }
    private void inflateList(Cursor cursor){
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(DBTest.this,R.layout.db_layout,cursor,new String[]{"province"},new int[]{R.id.show});
        //显示数据
        listView.setAdapter(adapter);
    }

    private void insertData(SQLiteDatabase db,String title,String content) {
        db.execSQL("insert into news values(null,?,?)", new String[]{title, content});
    }
   /* private void inflateList(Cursor cursor){
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(DBTest.this,R.layout.db_layout,cursor,new String[]{"news_title","news_content"},new int[]{R.id.my_title,R.id.my_content});
        //显示数据
        listView.setAdapter(adapter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen())
            db.close();
    }
}
