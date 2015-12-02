package cn.edu.pku.ss.gzh.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by Angel on 2015/11/9.
 */
public class Compass extends Activity {
    private SensorManager sensorManger;
    private ImageView compassImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);
        sensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManger.registerListener(listener,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManger.registerListener(listener,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        compassImg = (ImageView)findViewById(R.id.compass_img);
    }
    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magnetciValue = new float[3];
        private float lastRotateDegree;

        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断是加速度传感器还是地磁感传感器
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                accelerometerValues = event.values.clone();
            }else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                magnetciValue = event.values.clone();
            }
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R,null,accelerometerValues,magnetciValue);
            SensorManager.getOrientation(R,values);
            //将计算出的 旋转家督取反，用于旋转指南针背景图
            float rotateDegree = -(float)Math.toDegrees(values[0]);
            if (Math.abs(rotateDegree - lastRotateDegree) > 1){
                //旋转的起始角度、终止角度、旋转中心点
                RotateAnimation animation = new RotateAnimation(lastRotateDegree,rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);
                compassImg.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManger != null)
            sensorManger.unregisterListener(listener);
    }
}
