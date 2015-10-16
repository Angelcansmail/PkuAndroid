package com.gzh.job.weather.com.gzh.job.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Angel on 2015/10/4.
 */
public class NetUtil {
 //   if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE)


    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            System.out.println("wifi");
            return NETWORN_WIFI;
        }
        //mobile
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == NetworkInfo.State.CONNECTING || state == NetworkInfo.State.CONNECTED)
            return NETWORN_MOBILE;
        //none
        else
            return  NETWORN_NONE;
    }
}
