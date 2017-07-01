package com.example.n50.s1212491_khosach.Common;

import android.app.Application;

public class MyApplication extends Application {
    private DBHelper mLocalDatabase;

    private String[] mColors = {
            MyColor.BLACK,
            MyColor.WHITE,
            MyColor.L_GREY,
            MyColor.D_GREY,
            MyColor.L_RED,
            MyColor.D_RED,
            MyColor.L_ORANGE,
            MyColor.D_ORANGE,
            MyColor.L_YELLOW,
            MyColor.D_YELLOW,
            MyColor.L_GREEN,
            MyColor.D_GREEN,
            MyColor.L_CYAN,
            MyColor.D_CYAN,
            MyColor.L_BLUE,
            MyColor.D_BLUE,
            MyColor.L_VIOLET,
            MyColor.D_VIOLET,
            MyColor.L_BROWN,
            MyColor.D_BROWN,
            MyColor.L_IVORY,
            MyColor.D_IVORY,
            MyColor.L_PURPLE,
            MyColor.D_PURPLE
    };

    private String[] mReadMode = {
            "Điều khiển",
            "Tự động cuộn 1",
            "Tự động cuộn 2",
            "Tự động cuộn 3",
            "Tự động cuộn 4",
            "Tự động cuộn 5"
    };

    private int[] mLineSpace = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

    private int[] mTextSize = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};

    private int mCurrentTextColor = 0;
    private int mCurrentBackgroundColor = 1;
    private int mCurrentReadMode = 0;
    private int mCurrentLineSpace = 10;
    private int mCurrentTextSize = 11;

    public DBHelper getmLocalDatabase() {
        return mLocalDatabase;
    }

    public void setmLocalDatabase(DBHelper mLocalDatabase) {
        this.mLocalDatabase = mLocalDatabase;
    }

    public int getmCurrentTextColor() {
        return mCurrentTextColor;
    }

    public void setmCurrentTextColor(int mCurrentTextColor) {
        this.mCurrentTextColor = mCurrentTextColor;
    }

    public int getmCurrentBackgroundColor() {
        return mCurrentBackgroundColor;
    }

    public void setmCurrentBackgroundColor(int mCurrentBackgroundColor) {
        this.mCurrentBackgroundColor = mCurrentBackgroundColor;
    }

    public int getmCurrentReadMode() {
        return mCurrentReadMode;
    }

    public void setmCurrentReadMode(int mCurrentReadMode) {
        this.mCurrentReadMode = mCurrentReadMode;
    }

    public int getmCurrentLineSpace() {
        return mCurrentLineSpace;
    }

    public void setmCurrentLineSpace(int mCurrentLineSpace) {
        this.mCurrentLineSpace = mCurrentLineSpace;
    }

    public int getmCurrentTextSize() {
        return mCurrentTextSize;
    }

    public void setmCurrentTextSize(int mCurrentTextSize) {
        this.mCurrentTextSize = mCurrentTextSize;
    }

    public String getCurrentTextColor() {
        return this.mColors[mCurrentTextColor];
    }

    public String getCurrentBackgroundColor() {
        return this.mColors[mCurrentBackgroundColor];
    }

    public String getCurrentReadMode() {
        return this.mReadMode[mCurrentReadMode];
    }

    public int getCurrentLineSpace() {
        return this.mLineSpace[mCurrentLineSpace];
    }

    public int getCurrentTextSize() {
        return this.mTextSize[mCurrentTextSize];
    }

    public String[] getmReadMode() {
        return mReadMode;
    }

    public String goToNextTextColor() {
        if (mCurrentTextColor >= mColors.length - 1)
            mCurrentTextColor = 0;
        else mCurrentTextColor++;
        return this.mColors[mCurrentTextColor];
    }

    public String goToPrevTextColor() {
        if (mCurrentTextColor == 0)
            mCurrentTextColor = mColors.length - 1;
        else mCurrentTextColor--;
        return this.mColors[mCurrentTextColor];
    }

    public String goToNextBGColor() {
        if (mCurrentBackgroundColor >= mColors.length - 1)
            mCurrentBackgroundColor = 0;
        else mCurrentBackgroundColor++;
        return this.mColors[mCurrentBackgroundColor];
    }

    public String goToPrevBGColor() {
        if (mCurrentBackgroundColor == 0)
            mCurrentBackgroundColor = mColors.length - 1;
        else mCurrentBackgroundColor--;
        return this.mColors[mCurrentBackgroundColor];
    }

    public String goToNextReadMode() {
        if (mCurrentReadMode >= mReadMode.length - 1)
            mCurrentReadMode = 0;
        else mCurrentReadMode++;
        return this.mReadMode[mCurrentReadMode];
    }

    public String goToPrevReadMode() {
        if (mCurrentReadMode == 0)
            mCurrentReadMode = mReadMode.length - 1;
        else mCurrentReadMode--;
        return this.mReadMode[mCurrentReadMode];
    }

    public int goToNextLineSpace() {
        if (mCurrentLineSpace >= mLineSpace.length - 1)
            mCurrentLineSpace = 0;
        else mCurrentLineSpace++;
        return this.mLineSpace[mCurrentLineSpace];
    }

    public int goToPrevLineSpace() {
        if (mCurrentLineSpace == 0)
            mCurrentLineSpace = mLineSpace.length - 1;
        else mCurrentLineSpace--;
        return this.mLineSpace[mCurrentLineSpace];
    }
    //

    public int goToNextTextSize() {
        if (mCurrentTextSize >= mTextSize.length - 1)
            mCurrentTextSize = 0;
        else mCurrentTextSize++;
        return this.mTextSize[mCurrentTextSize];
    }

    public int goToPrevTextSize() {
        if (mCurrentTextSize == 0)
            mCurrentTextSize = mTextSize.length - 1;
        else mCurrentTextSize--;
        return this.mTextSize[mCurrentTextSize];
    }
}
