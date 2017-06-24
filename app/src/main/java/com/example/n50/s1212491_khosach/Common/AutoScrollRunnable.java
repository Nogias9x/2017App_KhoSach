package com.example.n50.s1212491_khosach.Common;

import android.util.Log;
import android.widget.ScrollView;

public class AutoScrollRunnable implements Runnable {
    private ScrollView mScrollView;
    private int mScrollSpeed;
    private static boolean mStop;

    public AutoScrollRunnable(ScrollView mScrollView, int mScrollSpeed) {
        this.mScrollView = mScrollView;
        this.mScrollSpeed = mScrollSpeed;
    }

    @Override
    public void run() {
        Log.i("<<", "run, mStop= " + mStop);
        while (!mStop) {
            try {
                Log.i("<<", "run ưhile");
                Log.i("<<", "mScrollSpeed: " + mScrollSpeed);
                mScrollView.scrollTo(0, mScrollView.getScrollY() + mScrollSpeed);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("<<NOGIAS>>", e.toString());
            }
        }
    }//run

    public void setmStop(boolean mStop) {
        this.mStop = mStop;
    }

    public boolean isStop() {
        return mStop;
    }
}


//    ScrollView mScrollView;
//    int mScrollSpeed;
//    static boolean mIsRunning;
//
//    public AutoScrollRunnable(ScrollView mScrollView, int mScrollSpeed) {
//        this.mScrollView = mScrollView;
//        this.mScrollSpeed = mScrollSpeed;
//    }
//
//    public void setmScrollSpeed(int mScrollSpeed) {
//        this.mScrollSpeed = mScrollSpeed;
//    }
//
//    @Override
//    public void run() {
//        while(true){
//            Log.i("<<","run");
//            mIsRunning= true;
//            mScrollView.scrollTo(0, mScrollView.getScrollY()+ mScrollSpeed);
//        }
//    }
//
//    public static boolean isRunning(){
//        return mIsRunning;
//    }
//}
