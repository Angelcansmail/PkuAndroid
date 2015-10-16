package cn.edu.pku.ss.gzh.gojson;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Activity extends android.app.Activity {
    private String[] data={"第1组","第2组","第3组","第4组","第5组","第6组",
            "第7组","第8组","第9组","第10组","第11组","第12组","第13组",
            "第14组","第15组","第16组","第17组","第18组","第19组","第20组",
            "第21组","第22组"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mlistView = (ListView)findViewById(R.id.list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                Activity.this,android.R.layout.simple_list_item_1,data);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Activity.this, "你单击了:" + i,
                        Toast.LENGTH_SHORT).show();
            }
        });
        /*Button btn = (Button)findViewById(R.id.call);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
               *//* Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                startActivity(i);*//*
                //访问通讯录
                *//*Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/1"));
                startActivity(i2);
                //访问网站
                Intent i3=new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.ss.pku.edu.cn"));
                startActivity(i3);
                //调用新Activity*//*
                *//*Intent intent = new Intent(Activity.this, OtherActivity.class);
                startActivity(intent);*//*
                //Intent传递数据
                Intent intent1 = new Intent(Activity.this,OtherActivity.class);
                intent1.putExtra("name","zhang");
                intent1.putExtra("age",22);
                startActivity(intent1);
            }
        });*/
    }
}
