package com.gzh.job.weather.com.gzh.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2015/11/4.
 */
public class Guide extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private List<View> views;
    //页面监听事件，使得小圆点随着页面的变化而变化
    private ImageView[] dots;
    private int[] ids = {R.id.iv1,R.id.iv2,R.id.iv3};
    private Button btn;

    private int time = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        init();
        initViews();
        initDots();
        /*if(time == 0) {
            time = 1;
            initViews();
            initDots();
            btn = (Button) views.get(1).findViewById(R.id.start);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Guide.this, MainActivity.class);
                    intent.putExtra("time",String.valueOf(time));
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            Intent intent = new Intent(Guide.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences("firstIn",MODE_PRIVATE);
        Boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);
        if (!isFirstIn){
            Intent intent = new Intent(Guide.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{

        }
    }
    void initDots(){
        dots = new ImageView[views.size()];
        for (int i = 0;i < views.size();i++){
            dots[i] =(ImageView) findViewById(ids[i]);
        }
    }
    //初始化视图
    private void initViews(){
        //通过LayoutInflater加载视图，获取视图对象
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.intro, null));
        views.add(inflater.inflate(R.layout.intro2, null));
        views.add(inflater.inflate(R.layout.welcome, null));
        viewPagerAdapter = new ViewPagerAdapter(views,this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);

        //页面监听事件，使得小圆点随着页面的变化而变化
        viewPager.setOnPageChangeListener(this);
    }
    public void onPageScrolled(int i,float v,int i2){

    }
    public void onPageSelected(int i){
        for (int a = 0; a < ids.length; a++){
            if(a == i){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
    public void onPageScrollStateChanged(int i){

    }
}
