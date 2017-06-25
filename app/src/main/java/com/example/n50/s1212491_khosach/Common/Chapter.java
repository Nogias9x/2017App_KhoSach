package com.example.n50.s1212491_khosach.Common;

/**
 * Created by 12124 on 6/25/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chapter {
    public static final String KEY = "chapter";
    public static final String TABLE_NAME = "Chapter9";
    public static final String KEY_STORYID = "StoryID";
    public static final String KEY_CHAPTERID = "ChapterID";
    public static final String KEY_TITLE = "ChapterTitle";
    public static final String KEY_CONTENT = "ChapterContent";

    @SerializedName("mChapterId")
    @Expose
    public int mChapterId;
    @SerializedName("mBookId")
    @Expose
    public int mBookId;
    @SerializedName("mChapterIndex")
    @Expose
    public int mChapterIndex;
    @SerializedName("mChapterName")
    @Expose
    public String mChapterName;
    @SerializedName("mContent")
    @Expose
    public String mContent;

    public String getShortDescription() {
        if (mContent.length() < 300)
            return mContent + "...";
        return mContent.substring(0, 300) + "...";
    }

    public int getChapterId() { return mChapterId; }
    public void setChapterId(int mChapterId) { this.mChapterId = mChapterId; }
    public int getBookId() { return mBookId; }
    public void setBookId(int mBookId) { this.mBookId = mBookId; }
    public int getChapterIndex() { return mChapterIndex; }
    public void setChapterIndex(int mChapterIndex) { this.mChapterIndex = mChapterIndex; }
    public String getChapterName() { return mChapterName; }
    public void setChapterName(String mChapterName) { this.mChapterName = mChapterName; }
    public String getContent() { return mContent; }
    public void setContent(String mContent) { this.mContent = mContent; }
}
