package com.example.n50.s1212491_khosach.Progress;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.R;

public class NetworkReceiver extends BroadcastReceiver {
    public ConnectivityManager connMgr;// = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    public android.net.NetworkInfo wifi;// = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    public android.net.NetworkInfo mobile;// = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


    public NetworkReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        this.mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            return;
        }
        Handler handler = new Handler(context.getMainLooper());
        Runnable showingToastTask = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
            }
        };
        handler.post(showingToastTask);
    }
}
