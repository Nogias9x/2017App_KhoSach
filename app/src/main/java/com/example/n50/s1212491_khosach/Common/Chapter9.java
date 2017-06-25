package com.example.n50.s1212491_khosach.Common;

import java.io.Serializable;

/**
 * Created by TuNTC2 on 5/8/2016.
 */
public class Chapter9 implements Serializable {
//    public static final String KEY = "chapter";
//    public static final String TABLE_NAME = "Chapter9";
//    public static final String KEY_STORYID = "StoryID";
//    public static final String KEY_CHAPTERID = "ChapterID";
//    public static final String KEY_TITLE = "ChapterTitle";
//    public static final String KEY_CONTENT = "ChapterContent";


    private int mStoryId;
    private int mChapterId;
    private String mTitle;
    private String mContent;

    public Chapter9() {

    }

    public Chapter9(int mStoryId, int mChapterId, String mTitle) {
        this.mStoryId = mStoryId;
        this.mChapterId = mChapterId;
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmStoryId(int mStoryId) {
        this.mStoryId = mStoryId;
    }

    public void setmChapterId(int mChapterId) {
        this.mChapterId = mChapterId;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmStoryId() {
        return mStoryId;
    }

    public int getmChapterId() {
        return mChapterId;
    }

    public String getmContent() {
        return mContent;
    }

    public String getHint() {
        if (mContent.length() < 300)
            return mContent + "...";
        return mContent.substring(0, 300) + "...";
    }
}

