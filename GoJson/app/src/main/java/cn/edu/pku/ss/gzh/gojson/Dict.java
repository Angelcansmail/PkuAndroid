package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.pku.ss.gzh.gojson.entity.MyDatabaseHelper;

/**
 * Created by Angel on 2015/10/18.
 */
public class Dict extends Activity {
    MyDatabaseHelper dbHelper;
    Button insert = null;
    Button search = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_layout);

        dbHelper = new MyDatabaseHelper(this,"myDict.db3",1);
        insert = (Button) findViewById(R.id.insert);
        search = (Button) findViewById(R.id.search);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = ((EditText)findViewById(R.id.word)).getText().toString();
                String detail = ((EditText)findViewById(R.id.details)).getText().toString();

                insertData(dbHelper.getReadableDatabase(),word,detail);
                Toast.makeText(Dict.this,"添加成功",Toast.LENGTH_LONG).show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ((EditText)findViewById(R.id.key)).getText().toString();
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from dict where word like ? or detail like ?", new String[]{"%" + key + "%" + key + "%"});

                Bundle data = new Bundle();
                data.putSerializable("data",converCursorToList(cursor));

                Intent intent = new Intent(Dict.this,ResultActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    protected ArrayList<Map<String,String>> converCursorToList(Cursor cursor){
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String,String> map = new HashMap<String,String>();
            map.put("word",cursor.getString(1));
            map.put("detail",cursor.getString(2));
            result.add(map);
        }
        return result;
    }
    private void insertData(SQLiteDatabase db,String word,String details){
        db.execSQL("insert into dict values(null,?,?)",new String[] {word,details});
    }
    public void onDestory(){
        super.onDestroy();
        if (dbHelper != null)
            dbHelper.close();
    }
}
