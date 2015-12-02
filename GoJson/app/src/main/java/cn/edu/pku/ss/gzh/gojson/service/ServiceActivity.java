package cn.edu.pku.ss.gzh.gojson.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.edu.pku.ss.gzh.gojson.R;

/**
 * Created by Angel on 2015/10/28.
 */
public class ServiceActivity extends Activity implements View.OnClickListener{
    Button btnStartService;
    Button btnStopService;
    Button btnBindService;
    Button btnUnbindService;
    MyService.MyBinder myBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("serviceConn","Service Connected");
            myBinder = (MyService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("serviceConn","Service DisConnected");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStopService = (Button)findViewById(R.id.btnStopService);
        btnBindService = (Button)findViewById(R.id.btnBindService);
        btnUnbindService = (Button)findViewById(R.id.btnUnbindService);
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnStartService:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.btnStopService:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.btnBindService:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                unbindService(connection);
                break;
        }
    }
}
