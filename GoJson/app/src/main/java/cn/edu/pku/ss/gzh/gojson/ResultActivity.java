package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Angel on 2015/10/14.
 */
public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pupup);

        //获取上一个Activity的信息
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);
        Toast.makeText(ResultActivity.this, name + "\t" + age, Toast.LENGTH_LONG).show();

        //获取intent携带的信息
        /*Bundle data = intent.getExtras();
        Log.d("info", "" + data);*/

        //数据库查词
        //ListView listView = (ListView)findViewById(R.id.show);
        //查询单词
        /*List<Map<String,String>> list = (List<Map<String,String>>)data.getSerializable("data");
        //显示数据
        SimpleAdapter adapter = new SimpleAdapter(
                ResultActivity.this,list,R.layout.pupup,new String[]{"word","detail"},new int[]{R.id.word,R.id.details});
        //填充ListView
        listView.setAdapter(adapter);*/
    }

}
