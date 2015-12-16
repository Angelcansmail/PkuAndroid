package com.gzh.job.weather.com.gzh.job.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Angel on 2015/11/4.
 */
public class ViewPagerAdapter2 extends PagerAdapter {
    private List<View> views;
    private Context context;

    public ViewPagerAdapter2(List<View> views, Context context){
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
        return views.get(position);
    }
    //判断instantiateItem返回的对象是否与当前View代表的是同一个对象
    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.d("isViewFromObject","instantiateItem返回对象与当前View一致？");
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("destoryItem","删除position位置的视图");
        //删除position位置指定的视图
        container.removeView(views.get(position));
        super.destroyItem(container, position, object);
    }
}
