package cn.edu.pku.ss.gzh.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class getlight_data extends ActionBarActivity {
    private Sensor light_sensor;
    private SensorManager sensorManager;
    private TextView light_textView;
    private StringBuffer stringBuffer;
    private Button button;
    float lus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);
        light_textView= (TextView) findViewById(R.id.lightTxt);
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LIGHT");
        if (!file.exists()){
            file.mkdirs();
            Log.d("HHHHHH", "XXXXXXXXXX");
        }
        else {
            Log.d("nn","ll");
        }
        file=new File(file,"light.txt");
        stringBuffer=new StringBuffer();
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream=new FileOutputStream(file,true);
            //stringBuffer=new StringBuffer();
            stringBuffer.append("本文件是记录手机中光传感器的文本文档\n");
            fileOutputStream.write(stringBuffer.toString().getBytes("utf-8"));
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        light_sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(new My_SensorListener(),light_sensor,SensorManager.SENSOR_DELAY_NORMAL);
        /*button= (Button) findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.list){
                    Intent intent=new Intent(getlight_data.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveInText(stringBuffer);
    }

    public class My_SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //获取精度
            float acc=event.accuracy;
            //获取光线强度
            lus=event.values[0];
            light_textView.setText("目前光线强度为："+lus+" 精度为："+acc);
            stringBuffer.append("目前光线强度为：" + lus + " 精度为：" + acc + "\n");
            //SaveInText(stringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Mongo conn;
                    try{
                        //conn = new MongoClient("localhost");
                        conn=new MongoClient("192.168.1.105",27017);
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    };
                    DB db = conn.getDB("test");
                    DBCollection light = db.getCollection("light");
                    BasicDBObject light_val = new BasicDBObject();
                    light_val.put("val", lus);
                    light.setWriteConcern(WriteConcern.SAFE);
                    light.insert(light_val);
                }
            }).start();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private void SaveInText(StringBuffer stringBuffer ) {
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LIGHT");
        Log.d("路径",Environment.getExternalStorageDirectory().getAbsolutePath()+"/LIGHT");
        if (!file.exists()){
            file.mkdirs();
            Log.d("HHHHHH", "XXXXXXXXXX");
        }
        else {
            Log.d("nn","ll");
        }
        if (!file.exists()){
            Log.d("HHHHHH", "XXXXXXXXXX");
        }
        file=new File(file,"light.txt");
        try {
            file.createNewFile();
            if(!file.exists()){
                Log.d("bucunzai","不存在光线传感器文件");
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file,true);
            fileOutputStream.write(stringBuffer.toString().getBytes("utf-8"));
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*String data="data_save";
        FileOutputStream fileOutputStream=null;
        BufferedWriter bufferedWriter=null;
        try {
            fileOutputStream=openFileOutput("data", Context.MODE_PRIVATE);
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
