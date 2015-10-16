package cn.edu.pku.ss.gzh.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private  Button btn;
    private DisplayMetrics dm;
    private WindowManager wm;
    public String tag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("info", "landscape");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("info", "portrait");
        }
        Log.i(tag, "onCreat");
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                String strPM = "手机屏幕分辨率为：" + dm.widthPixels + "*" + dm.heightPixels;//像素个数
                wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                //strPM += wm.getDefaultDisplay().getWidth() + "\t" + wm.getDefaultDisplay().getHeight();
                //Density屏幕密度通常指dpi(每英寸点数120\160\240\320)
                strPM += "\nlogic density： " + dm.density + "\t X dimension: " + dm.xdpi + "pixels per inch" + "\t Y dimension: " + dm.ydpi + "pixels per inch";
                System.out.print(strPM);
                tv.setText(strPM);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        Log.i(tag,"onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(tag,"onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(tag,"onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(tag,"onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i(tag,"onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(tag,"onStop");
        super.onStop();
    }
}
