package com.example.n50.s1212491_khosach.Common;

/**
 * Created by 12124 on 6/25/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chapter {
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
