package com.example.n50.s1212491_khosach.Common;

import java.io.Serializable;

public class Book implements Serializable {
    public static final String KEY = "story";
    public static final String TABLE_NAME = "story";
    public static final String KEY_ID = "StoryID";
    public static final String KEY_COVER = "Cover";
    public static final String KEY_AUTHOR = "Author";
    public static final String KEY_VIEW = "View";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_CHAPTER = "Status";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_GOODPOINT = "GoodPoint";
    public static final String KEY_TOTALPOINT = "TotalPoint";


    public static final int STYLE_ONLINE = 0;
    public static final int STYLE_OFFLINE = 1;

    private int mId;
    private String mCoverUrl;
    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private long mView;
    private String mChapter;
    private long mLastVisit;
    private int mGoodPoint;
    private int mTotalPoint;

    private int mReadingChapter;
    private int mReadingY;

    public long getLastVisit() {
        return mLastVisit;
    }

    public void setLastVisit(long visit) {
        mLastVisit = visit;
    }

    public Book() {

    }

    public Book(String title, String coverUrl, String author, String description, long view, String chapter) {
        mTitle = title;
        mAuthor = author;
        mCoverUrl = coverUrl;
        mDescription = description;
        mView = view;
        mChapter = chapter;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        mCoverUrl = coverUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getView() {
        return mView;
    }

    public void setView(long view) {
        mView = view;
    }

    public String getChapter() {
        return mChapter;
    }

    public void setChapter(String chapter) {
        mChapter = chapter;
    }

    public void setTotalPoint(int mTotalPoint) {
        this.mTotalPoint = mTotalPoint;
    }

    public void setGoodPoint(int mGoodPoint) {
        this.mGoodPoint = mGoodPoint;
    }

    public float getRating() {
        if (mTotalPoint == 0) return 0;
        return (float) mGoodPoint * 5 / mTotalPoint;
    }

    public int getmReadingChapter() {
        return mReadingChapter;
    }

    public int getmReadingY() {
        return mReadingY;
    }

    public void setmReadingChapter(int mReadingChapter) {
        this.mReadingChapter = mReadingChapter;
    }

    public void setmReadingY(int mReadingY) {
        this.mReadingY = mReadingY;
    }
}

