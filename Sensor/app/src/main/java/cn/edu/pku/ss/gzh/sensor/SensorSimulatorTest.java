package cn.edu.pku.ss.gzh.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Angel on 2015/11/9.
 */
public class SensorSimulatorTest extends Activity implements View.OnClickListener{
        //真机
        SensorManager mSensorManager;

        //SensorSimulator
        //private SensorManagerSimulator mSensorManager;
        EditText etAcculerate;
        EditText etOrientatiion;
        EditText etMagnetic;
        EditText etTemerature;
        EditText etLight;
        EditText etPressure;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button orienationBtn = (Button)findViewById(R.id.orienationBtn);
        Button acculerateBtn = (Button)findViewById(R.id.acculerateBtn);
        Button voiceBtn = (Button)findViewById(R.id.voiceBtn);
        Button magneticBtn = (Button)findViewById(R.id.magneticBtn);
        Button temperatorBtn = (Button)findViewById(R.id.temperatorBtn);
        Button lightBtn = (Button)findViewById(R.id.lightBtn);
        Button pressureBtn = (Button)findViewById(R.id.pressureBtn);

        orienationBtn.setOnClickListener(this);
        acculerateBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
        magneticBtn.setOnClickListener(this);
        temperatorBtn.setOnClickListener(this);
        lightBtn.setOnClickListener(this);
        pressureBtn.setOnClickListener(this);

        /*List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("count","" + list.size());*/
        //mSensorManager = SensorManagerSimulator.getSystemService(this,SENSOR_SERVICE);
        //mSensorManager.connectSimulator();

        final TextView tx1 = (TextView) findViewById(R.id.sensorType);
        //从系统服务中获得传感器管理器
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        //显示有多少个传感器
        tx1.setText( "经检测该手机有" + allSensors.size() + "个传感器，他们分别是：/n" );

        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.acculerateBtn:
                Intent accularate_intenti = new Intent(SensorSimulatorTest.this,AccelerometerTest.class);
                startActivity(accularate_intenti);
                break;
            case R.id.orienationBtn:
                Intent orienation_intent = new Intent(SensorSimulatorTest.this,Compass.class);
                startActivity(orienation_intent);
                break;
            case R.id.lightBtn:
                Intent light_intent = new Intent(SensorSimulatorTest.this,LightActivity.class);
                startActivity(light_intent);
                break;
            default:
                Intent other_intent = new Intent(SensorSimulatorTest.this,SensorActivity.class);
                startActivity(other_intent);
                break;
        }
    }
}
