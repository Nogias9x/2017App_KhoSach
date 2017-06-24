package com.example.n50.s1212491_khosach.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Fragments.AllBookFragment;
import com.example.n50.s1212491_khosach.Fragments.MyBookShelfFragment;
import com.example.n50.s1212491_khosach.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MyApplication mApplication;
    private ActionBar mActionBar;
    private ActionBar.Tab mTab1;
    private ActionBar.Tab mTab2;
    private List<Fragment> mFragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApplication = super.getmApplication();
        mFragmentList = new ArrayList<Fragment>();

        mActionBar = getActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Fragment fragment = null;
                if (tab.getPosition() < mFragmentList.size()) {
                    fragment = mFragmentList.get(tab.getPosition());
                }
                if (fragment == null) {
                    if (tab.getPosition() == 0) {
                        fragment = new AllBookFragment();
                        mFragmentList.add(0, fragment);
                    } else if (tab.getPosition() == 1) {
                        fragment = new MyBookShelfFragment();
                        mFragmentList.add(1, fragment);
                    }
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        mTab1 = mActionBar.newTab().setText(getString(R.string.tab_1)).setTabListener(tabListener);
        mTab2 = mActionBar.newTab().setText(getString(R.string.tab_2)).setTabListener(tabListener);
        mActionBar.addTab(mTab1);
        mActionBar.addTab(mTab2);

        initLocalDatabase();
    }

    public void initLocalDatabase() {
        Log.i("<<NOGIAS>>", "initLocalDatabase");
        DBHelper localDatabase = mApplication.getmLocalDatabase();
        if (localDatabase == null) localDatabase = new DBHelper(this);
        mApplication.setmLocalDatabase(localDatabase);
    }
}