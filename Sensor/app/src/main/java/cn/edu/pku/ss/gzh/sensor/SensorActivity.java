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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Angel on 2015/11/9.
 */
public class SensorActivity extends Activity implements SensorEventListener {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        String string = "";
        StringBuffer result = new StringBuffer(string);
        int sensorType;
        File file;
        FileOutputStream fileOutputStream;
        //真机
        SensorManager mSensorManager;

        //SensorSimulator
        //private SensorManagerSimulator mSensorManager;
        EditText etAcculerate;
        EditText etOrientatiion;
        EditText etMagnetic;
        EditText etTemerature;
        EditText etPressure;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etAcculerate = (EditText)findViewById(R.id.acculerateTxt);
        etOrientatiion = (EditText)findViewById(R.id.orienationTxt);
        etMagnetic = (EditText)findViewById(R.id.magneticTxt);
        etTemerature = (EditText)findViewById(R.id.temperatorTxt);
        etPressure = (EditText)findViewById(R.id.pressureTxt);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("count", "" + list.size());
        //mSensorManager = SensorManagerSimulator.getSystemService(this,SENSOR_SERVICE);
        //mSensorManager.connectSimulator();
        //android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sensor");
        if (!file.exists()){
            file.mkdirs();
        }
    }
    @Override
        protected void onResume() {
        super.onResume();
        //为系统的加速传感器注册监听事件
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        //为系统的方向传感器注册监听事件
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        //为系统的磁场传感器注册监听事件
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        //为系统的温度传感器注册监听事件
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE), SensorManager.SENSOR_DELAY_GAME);
        //为系统的光传感器注册监听事件
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_GAME);
        //为系统的压力传感器注册监听事件
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),SensorManager.SENSOR_DELAY_GAME);
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
        mSensorManager.unregisterListener(this);
    }

        //SensorEventListener接口必须实现的方法，传感器值发生改变时回调该方法
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            sensorType = event.sensor.getType();
            //int sensorType = event.type;
            StringBuilder sb = new StringBuilder();

            switch(sensorType){
                case Sensor.TYPE_ACCELEROMETER:
                    sb = new StringBuilder();
                    sb.append("X方向上加速度：");
                    sb.append(values[0] + "\n");
                    sb.append("Y方向上加速度：");
                    sb.append(values[1] + "\n");
                    sb.append("Z方向上加速度：");
                    sb.append(values[2]);
                    etAcculerate.setText(sb.toString());
                    try{
                        fileOutputStream = new FileOutputStream(file,true);
                        fileOutputStream.write(etAcculerate.getText().toString().getBytes("utf-8"));
                        /*out = openFileOutput("ACCELEROMETER", Context.MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                        writer.write(etAcculerate.getText().toString());*/
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb = new StringBuilder();
                    sb.append("绕Z轴转过的角度： ");
                    sb.append(values[0] + "\n");
                    sb.append("绕X轴转过的角度： ");
                    sb.append(values[1] + "\n");
                    sb.append("绕Y轴转过的角度： ");
                    sb.append(values[2]);
                    etOrientatiion.setText(sb.toString());
                    try{
                        out = openFileOutput("ORIENTATION",Context.MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                        writer.write(etOrientatiion.getText().toString());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb = new StringBuilder();
                    sb.append("X轴方向角度： ");
                    sb.append(values[0] + "\n");
                    sb.append("Y轴方向角度： ");
                    sb.append(values[1] + "\n");
                    sb.append("Z轴方向角度： ");
                    sb.append(values[2]);
                    etMagnetic.setText(sb.toString());
                    result.append(etMagnetic.getText().toString());
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sb = new StringBuilder();
                    sb.append("当前温度为： ");
                    sb.append(values[0]);
                    etTemerature.setText(sb.toString());
                    result.append(etTemerature.getText().toString());
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb = new StringBuilder();
                    sb.append("当前压力为： ");
                    sb.append(values[0]);
                    etPressure.setText(sb.toString());
                    result.append(etPressure.getText().toString());
                    break;
            }
    }
        private void save(StringBuffer result){
            FileOutputStream out = null;
            BufferedWriter writer = null;
            try{
                out = openFileOutput("data",Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(result.toString());
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                try{
                    if(writer != null)
                        writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        //传感器精度改变时回调该方法
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(writer != null) {
                writer.close();
                fileOutputStream.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //save(result);
    }
}
