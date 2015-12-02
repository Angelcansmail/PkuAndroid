package cn.edu.pku.ss.gzh.gojson.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.pku.ss.gzh.gojson.R;

/**
 * Created by Angel on 2015/11/8.
 */
public class SecondFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        //Log.d("SecondFragment","onCreateView");
        return inflater.inflate(R.layout.fragment2,container,false);
    }
    /*@Override
    public void onAttach(Activity activity) {
        Log.d("SecondFragment", "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("SecondFragment","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("SecondFragment","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("SecondFragment","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("SecondFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("SecondFragment","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("SecondFragment","onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("SecondFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("SecondFragment","onDetach");
        super.onDetach();
    }*/
}
