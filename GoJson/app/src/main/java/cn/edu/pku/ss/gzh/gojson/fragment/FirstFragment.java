package cn.edu.pku.ss.gzh.gojson.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.pku.ss.gzh.gojson.R;

/**
 * Created by Angel on 2015/11/8.
 */
public class FirstFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        Log.d("firstFragment","onCreateView");
        return inflater.inflate(R.layout.fragment1,container,false);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("firstFragment","onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("firstFragment","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("firstFragment","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("firstFragment","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("firstFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("firstFragment","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("firstFragment","onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("firstFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("firstFragment","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d("firstFragment","onDetach");
        super.onDetach();
    }
}
