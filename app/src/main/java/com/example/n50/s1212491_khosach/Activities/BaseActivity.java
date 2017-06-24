package com.example.n50.s1212491_khosach.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Progress.NetworkReceiver;
import com.example.n50.s1212491_khosach.R;

/**
 * Created by 12124 on 6/24/2017.
 */

public class BaseActivity extends Activity implements View.OnClickListener {
    private MyApplication mApplication;
    private static BroadcastReceiver mNetworkBroadcastReceiver;

    //    setting mSettingDialog
    private Dialog mSettingDialog;
    private ImageButton ib_nextText;
    private ImageButton ib_prevText;
    private ImageButton ib_nextBackground;
    private ImageButton ib_prevBackground;
    private ImageButton ib_nextSize;
    private ImageButton ib_prevSize;
    private ImageButton ib_nextMode;
    private ImageButton ib_prevMode;
    private ImageButton ib_nextSpace;
    private ImageButton ib_prevSpace;
    private ImageView iv_textColor;
    private ImageView iv_backgroundColor;
    private TextView tv_textSize;
    private TextView tv_readMode;
    private TextView tv_lineSpace;
    private TextView tv_preview;
    private Button btn_save;
    private Button btn_cancel;
    private int currentTextColor;
    private int currentBackgroundColor;
    private int currentReadMode;
    private int currentLineSpace;
    private int currentTextSize;

    //    search mSearchingDialog
    private Dialog mSearchingDialog;
    private TextView tv_title;
    private Button btn_search;
    private Button btn_cancel2;


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (MyApplication) getApplication();

        mNetworkBroadcastReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");
        registerReceiver(mNetworkBroadcastReceiver, filter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, SearchingActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_setting:
                currentTextColor = mApplication.getmCurrentTextColor();
                currentBackgroundColor = mApplication.getmCurrentBackgroundColor();
                currentReadMode = mApplication.getmCurrentReadMode();
                currentLineSpace = mApplication.getmCurrentLineSpace();
                currentTextSize = mApplication.getmCurrentTextSize();

                mSettingDialog = new Dialog(this);
                mSettingDialog.setContentView(R.layout.dialog_setting);
                mSettingDialog.setTitle("Cài đặt");
                mSettingDialog.show();
                mSettingDialog.setCanceledOnTouchOutside(false);
//
                ib_nextText = (ImageButton) mSettingDialog.findViewById(R.id.dialog_nextTextColor_btn);
                ib_prevText = (ImageButton) mSettingDialog.findViewById(R.id.dialog_prevTextColor_btn);
                ib_nextBackground = (ImageButton) mSettingDialog.findViewById(R.id.dialog_nextBackColor_btn);
                ib_prevBackground = (ImageButton) mSettingDialog.findViewById(R.id.dialog_prevBackColor_btn);
                ib_nextMode = (ImageButton) mSettingDialog.findViewById(R.id.dialog_nextMode_btn);
                ib_prevMode = (ImageButton) mSettingDialog.findViewById(R.id.dialog_prevMode_btn);
                iv_textColor = (ImageView) mSettingDialog.findViewById(R.id.dialog_textColor_iv);
                iv_backgroundColor = (ImageView) mSettingDialog.findViewById(R.id.dialog_backColor_iv);
                tv_readMode = (TextView) mSettingDialog.findViewById(R.id.dialog_readMode_tv);
                tv_preview = (TextView) mSettingDialog.findViewById(R.id.dialog_preview_tv);
                ib_nextSpace = (ImageButton) mSettingDialog.findViewById(R.id.dialog_nextSpace_btn);
                ib_prevSpace = (ImageButton) mSettingDialog.findViewById(R.id.dialog_prevSpace_btn);
                tv_lineSpace = (TextView) mSettingDialog.findViewById(R.id.dialog_lineSpace_tv);

                ib_nextSize = (ImageButton) mSettingDialog.findViewById(R.id.dialog_nextSize_btn);
                ib_prevSize = (ImageButton) mSettingDialog.findViewById(R.id.dialog_prevSize_btn);
                tv_textSize = (TextView) mSettingDialog.findViewById(R.id.dialog_textSize_tv);

                btn_save = (Button) mSettingDialog.findViewById(R.id.dialog_save_btn);
                btn_cancel = (Button) mSettingDialog.findViewById(R.id.dialog_cancel_btn);

                setSettingDialog();

                ib_nextText.setOnClickListener(this);
                ib_prevText.setOnClickListener(this);
                ib_nextBackground.setOnClickListener(this);
                ib_prevBackground.setOnClickListener(this);
                ib_nextMode.setOnClickListener(this);
                ib_prevMode.setOnClickListener(this);
                ib_nextSpace.setOnClickListener(this);
                ib_prevSpace.setOnClickListener(this);
                ib_nextSize.setOnClickListener(this);
                ib_prevSize.setOnClickListener(this);
                btn_save.setOnClickListener(this);
                btn_cancel.setOnClickListener(this);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSettingDialog() {
        int width = (int) getResources().getDimension(R.dimen.color_button_width);
        int height = (int) getResources().getDimension(R.dimen.color_button_width);

        Bitmap textImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        textImage.eraseColor(Color.parseColor(mApplication.getCurrentTextColor()));
        Bitmap bgImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bgImage.eraseColor(Color.parseColor(mApplication.getCurrentBackgroundColor()));

        iv_textColor.setImageBitmap(textImage);
        iv_backgroundColor.setImageBitmap(bgImage);
        tv_readMode.setText(mApplication.getCurrentReadMode());
        tv_lineSpace.setText(String.valueOf(mApplication.getCurrentLineSpace()));
        tv_textSize.setText(String.valueOf(mApplication.getCurrentTextSize()));


        tv_preview.setTextColor(Color.parseColor(mApplication.getCurrentTextColor()));
        tv_preview.setBackgroundColor(Color.parseColor(mApplication.getCurrentBackgroundColor()));
        tv_preview.setLineSpacing(mApplication.getCurrentLineSpace(), 1);
        tv_preview.setTextSize(mApplication.getCurrentTextSize());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_nextTextColor_btn:
                mApplication.goToNextTextColor();
                setSettingDialog();
                break;
            case R.id.dialog_prevTextColor_btn:
                mApplication.goToPrevTextColor();
                setSettingDialog();
                break;
            case R.id.dialog_nextBackColor_btn:
                mApplication.goToNextBGColor();
                setSettingDialog();
                break;
            case R.id.dialog_prevBackColor_btn:
                mApplication.goToPrevBGColor();
                setSettingDialog();
                break;
            case R.id.dialog_nextMode_btn:
                mApplication.goToNextReadMode();
                setSettingDialog();
                break;
            case R.id.dialog_prevMode_btn:
                mApplication.goToPrevReadMode();
                setSettingDialog();
                break;
            case R.id.dialog_nextSpace_btn:
                mApplication.goToNextLineSpace();
                setSettingDialog();
                break;
            case R.id.dialog_prevSpace_btn:
                mApplication.goToPrevLineSpace();
                setSettingDialog();
                break;
            case R.id.dialog_nextSize_btn:
                mApplication.goToNextTextSize();
                setSettingDialog();
                break;
            case R.id.dialog_prevSize_btn:
                mApplication.goToPrevTextSize();
                setSettingDialog();
                break;
            case R.id.dialog_save_btn:
                //save value when OK
                MyApplication myApplication = (MyApplication) getApplication();
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("CurrentTextColor", myApplication.getmCurrentTextColor());
                editor.putInt("CurrentBackgroundColor", myApplication.getmCurrentBackgroundColor());
                editor.putInt("CurrentReadMode", myApplication.getmCurrentReadMode());
                editor.putInt("CurrentTextSize", myApplication.getmCurrentTextSize());
                editor.putInt("CurrentLineSpace", myApplication.getmCurrentLineSpace());
                editor.commit();

                mSettingDialog.dismiss();
                break;
            case R.id.dialog_cancel_btn:
                mApplication.setmCurrentTextColor(currentTextColor);
                mApplication.setmCurrentBackgroundColor(currentBackgroundColor);
                mApplication.setmCurrentReadMode(currentReadMode);
                mApplication.setmCurrentLineSpace(currentLineSpace);
                mApplication.setmCurrentTextSize(currentTextSize);
                mSettingDialog.dismiss();
                break;
        }
    }

    public MyApplication getmApplication() {
        return mApplication;
    }

    @Override
    protected void onDestroy() {
        if (mNetworkBroadcastReceiver != null) {
            Log.i("<<NOGIAS>>", "unregisterReceiver");
            unregisterReceiver(mNetworkBroadcastReceiver);
            mNetworkBroadcastReceiver = null;
        }
        super.onDestroy();
    }

    public boolean isOnline() {
        NetworkInfo ni = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (ni == null) {
            Log.i("<<NOGIAS>>", "isOnline offline");
            return false;
        }
        Log.i("<<NOGIAS>>", "isOnline online");
        return true;
    }
}