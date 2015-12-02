package cn.edu.pku.ss.gzh.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Angel on 2015/11/30.
 */
public class LightActivity extends Activity {
    SensorManager sensorManager;
    StringBuffer stringBuffer;
    StringBuffer result = new StringBuffer();
    private EditText light_editText;
    StringBuffer json = new StringBuffer();
    /*SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    Date startTime = new Date(System.currentTimeMillis());//获取当前时间
    String str  = format.format(startTime);
    long startTime = System.currentTimeMillis();*/
    Mongo conn;
    float lus;//光线强度值
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);
        light_editText = (EditText)findViewById(R.id.lightTxt);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //为系统的加速传感器注册监听事件
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager = SensorManagerSimulator.getSystemService(this,SENSOR_SERVICE);
        //mSensorManager.connectSimulator();
    }
    //SensorEventListener接口必须实现的方法，传感器值发生改变时回调该方法
    private SensorEventListener listener = new SensorEventListener() {
        //{ "specs" :  [ {"id":"value1" , "name":"jack"},   {"id": "value2", "name":"jack2"},{"id": "value3", "name":"jack3"} ]}
        @Override
        public void onSensorChanged(SensorEvent event) {
            //获取精度
            float acc=event.accuracy;
            //获取光线强度
            lus = event.values[0];
//            light_textView.setText("目前光线强度为："+ lus +" 精度为：" + acc);
            //json.append("{\"light\":" );
            //json.append("{\"val\":" + lus + ",\"acc\":" + acc + "}\n");
/*            json.deleteCharAt(json.length() - 1);
            json.append("}");*/
            //在界面上显示
//            StringBuilder sb = new StringBuilder();
//            sb.append("当前光强度为： ");
            //sb.append(lus);
            light_editText.setText(String.valueOf(lus));
            //result.append(light_editText.getText().toString());
            //存入文件
            //SaveInText(json);
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
            //DBObject query = (BasicDBObject) JSON.parse(strJson);
        }
        //传感器精度改变时回调该方法
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void SaveInText(StringBuffer stringBuffer ) {
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LIGHT");
        Log.d("路径", Environment.getExternalStorageDirectory().getAbsolutePath() + "/LIGHT");
        if (!file.exists()){
            file.mkdirs();
            Log.d("file:", "not exit!");
        }
        else {
            Log.d("file","has exit!");
        }
        file = new File(file,"light.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            int fileLen = fis.available(); //这就是文件大小
            if(fileLen >= 524288000 ) {     //500MB
                //
                saveInMongoDB(file);
            }
            file.createNewFile();
            if(!file.exists()){
                Log.d("exit or not","不存在光线传感器文件");
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file,true);
            fileOutputStream.write(stringBuffer.toString().getBytes("utf-8"));

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveInMongoDB(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            Log.d("fileSize:","" + fis.available());
            conn = new MongoClient("10.0.2.2",27017);

        }catch (Exception e){
            throw new RuntimeException(e);
        };
        DB db = conn.getDB("test");
        DBCollection coll = db.getCollection("person");
        BasicDBObject doc = new BasicDBObject();
        doc.put("light",light_editText.getText().toString());
        coll.setWriteConcern(WriteConcern.SAFE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //取消注册
        sensorManager.unregisterListener(listener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManager != null)
            sensorManager.unregisterListener(listener);
    }
}
