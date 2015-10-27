package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class seletcCityActivity extends Activity {
    private String[] data={"第1组","第2组","第3组","第4组","第5组","第6组",
            "第7组","第8组","第9组","第10组","第11组","第12组","第13组",
            "第14组","第15组","第16组","第17组","第18组","第19组","第20组",
            "第21组","第22组"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                seletcCityActivity.this,android.R.layout.simple_expandable_list_item_1,data);
        ListView mlistView = (ListView)findViewById(R.id.list_view);
        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(seletcCityActivity.this, "你单击了:" + i,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
