package com.example.n50.s1212491_khosach.Common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Book implements Serializable {
    public static final int STYLE_ONLINE = 0;
    public static final int STYLE_OFFLINE = 1;

    public static final String KEY_BOOK_ID = "mBookId";
    public static final String KEY_BOOK_NAME = "mBookName";

    private int mReadingChapter;
    private int mReadingY;

    @SerializedName("mBookId")
    @Expose
    private int mBookId;
    @SerializedName("mBookName")
    @Expose
    private String mBookName;
    @SerializedName("mCategory")
    @Expose
    private int mCategory;
    @SerializedName("mAuthorId")
    @Expose
    private int mAuthorId;
    @SerializedName("mAuthorName")
    @Expose
    private String mAuthorName;
    @SerializedName("mPublicYear")
    @Expose
    private int mPublicYear;
    @SerializedName("mAppReleaseDate")
    @Expose
    private String mAppReleaseDate;
    @SerializedName("mTranslator")
    @Expose
    private Object mTranslator;
    @SerializedName("mIntroduction")
    @Expose
    private String mIntroduction;
    @SerializedName("mCoverUrl")
    @Expose
    private String mCoverUrl;


    @SerializedName("mViews")
    @Expose
    private int mViews;
    @SerializedName("mRatingPoints")
    @Expose
    private float mRatingPoints;
    @SerializedName("mRatingTimes")
    @Expose
    private int mRatingTimes;
    @SerializedName("mChapterNumber")
    @Expose
    private int mChapterNumber;


    public int getBookId() {
        return mBookId;
    }

    public void setBookId(int mBookId) {
        this.mBookId = mBookId;
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int mCategory) {
        this.mCategory = mCategory;
    }

    public int getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(int mAuthorId) {
        this.mAuthorId = mAuthorId;
    }

    public int getPublicYear() {
        return mPublicYear;
    }

    public void setPublicYear(int mPublicYear) {
        this.mPublicYear = mPublicYear;
    }

    public String getAppReleaseDate() {
        return mAppReleaseDate;
    }

    public void setAppReleaseDate(String mAppReleaseDate) {
        this.mAppReleaseDate = mAppReleaseDate;
    }

    public Object getTranslator() {
        return mTranslator;
    }

    public void setTranslator(Object mTranslator) {
        this.mTranslator = mTranslator;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(String mCoverUrl) {
        this.mCoverUrl = mCoverUrl;
    }


    public int getViews() {
        return mViews;
    }

    public void setViews(int mViews) {
        this.mViews = mViews;
    }


    public float getRatingPoints() {
        return mRatingPoints;
    }

    public void setRatingPoints(float mRatingPoints) {
        this.mRatingPoints = mRatingPoints;
    }


    public int getRatingTimes() {
        return mRatingTimes;
    }

    public void setRatingTimes(int mRatingTimes) {
        this.mRatingTimes = mRatingTimes;
    }

    public int getChapterNumber() {
        return mChapterNumber;
    }

    public void setChapterNumber(int mChapterNumber) {
        this.mChapterNumber = mChapterNumber;
    }

    public int getReadingChapter() {
        return mReadingChapter;
    }

    public void setReadingChapter(int mReadingChapter) {
        this.mReadingChapter = mReadingChapter;
    }

    public int getReadingY() {
        return mReadingY;
    }

    public void setReadingY(int mReadingY) {
        this.mReadingY = mReadingY;
    }

}