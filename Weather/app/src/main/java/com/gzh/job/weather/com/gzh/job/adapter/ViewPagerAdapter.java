package com.gzh.job.weather.com.gzh.job.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gzh.job.weather.R;
import com.gzh.job.weather.com.gzh.job.activity.MainActivity;

import java.util.List;

/**
 * Created by Angel on 2015/11/4.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;

    public ViewPagerAdapter(List<View> views,Context context){
        this.views = views;
        this.context = context;
    }
    @Override
    public int getCount() {
       return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("instantiateItem", "添加视图");
        //添加position位置的视图，并返回这个视图对象
        container.addView(views.get(position));
        if(position == views.size() - 1) {
            Button start = (Button) container.findViewById(R.id.start);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.start){
                        Algorithm();
                    }
                }
            });
        }
        return views.get(position);
    }
    private void Algorithm(){
        //设置标志位
        SharedPreferences sharedPreferences = (SharedPreferences)context.getSharedPreferences("firstIn",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstIn",false);//存入数据
        editor.commit();//提交修改
        //跳转到主界面
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    //判断instantiateItem返回的对象是否与当前View代表的是同一个对象
    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.d("isViewFromObject","instantiateItem返回对象与当前View一致？" + (view == object));
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("destoryItem","删除" + position + "位置的视图");
        //删除position位置指定的视图
        container.removeView(views.get(position));
    }
}
