package cn.edu.pku.ss.gzh.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Angel on 2015/11/9.
 */
public class AccelerometerTest extends Activity {
    SensorManager sensorManager;

    //SensorSimulator
    //private SensorManagerSimulator mSensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //为系统的加速传感器注册监听事件
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager = SensorManagerSimulator.getSystemService(this,SENSOR_SERVICE);
        //mSensorManager.connectSimulator();
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

    //SensorEventListener接口必须实现的方法，传感器值发生改变时回调该方法
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            StringBuilder sb = new StringBuilder();
            float xVal = Math.abs(event.values[0]);
            float yVal = Math.abs(event.values[1]);
            float zVal = Math.abs(event.values[2]);
            if(xVal > 15 || yVal > 15 || zVal >15)//15m/s^2 // STOPSHIP: 2015/11/9
                Toast.makeText(AccelerometerTest.this,"摇一摇",Toast.LENGTH_SHORT).show();
        }
        //传感器精度改变时回调该方法
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
