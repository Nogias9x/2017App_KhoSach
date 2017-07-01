package com.example.n50.s1212491_khosach.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Common.ApiUtils;
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Common.MyWebService;
import com.example.n50.s1212491_khosach.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewerActivity extends BaseActivity implements View.OnClickListener {

    Handler mHandler = new Handler();
    AutoScrollOperation mScrollOperation = new AutoScrollOperation();
    private ScrollView mScrollView;
    private RelativeLayout mLayout;
    private TextView mChapterTitle;
    private TextView mChapterContent;
    private Button mPrevChapter;
    private Button mNextChapter;
    private ArrayList<String> mTitleArray = new ArrayList<String>();
    private ArrayList<String> mContentArray = new ArrayList<String>();
    private int mReadStyle;
    private Book mBookNEW;
    private Chapter mChapterNEW;
    private ProgressDialog mDialog;
    private DBHelper mLocalDatabase;
    private ViewerActivity mContext = (ViewerActivity) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        mLocalDatabase = ((MyApplication) getApplication()).getmLocalDatabase();
        mBookNEW = new Book();

        getActionBar().setDisplayShowTitleEnabled(true);
        mScrollView = (ScrollView) findViewById(R.id.viewer_scrollView);
        mLayout = (RelativeLayout) findViewById(R.id.viewer_layout);
        mChapterTitle = (TextView) findViewById(R.id.viewer_chapterTitle_tv);
        mChapterContent = (TextView) findViewById(R.id.viewer_chapterContent_tv);
        mPrevChapter = (Button) findViewById(R.id.viewer_prev_btn);
        mNextChapter = (Button) findViewById(R.id.viewer_next_btn);

        mChapterTitle.setTextColor(getResources().getColor(R.color.viewer_chapter_title));
        mPrevChapter.setVisibility(View.INVISIBLE);
        mNextChapter.setVisibility(View.INVISIBLE);
        mPrevChapter.setOnClickListener(this);
        mNextChapter.setOnClickListener(this);
        mChapterContent.setOnClickListener(this);
        mScrollView.scrollTo(0, 0);

        loadPreferences();

        setContentStyle();

        mChapterNEW = new Chapter();
        Intent callerIntent = getIntent();
        mBookNEW.setBookName(callerIntent.getStringExtra(Book.KEY_BOOK_NAME));
        mReadStyle = callerIntent.getIntExtra("Style", mBookNEW.STYLE_ONLINE);

        if (mReadStyle == mBookNEW.STYLE_OFFLINE) {
            mTitleArray = callerIntent.getStringArrayListExtra("titleArray");
            mContentArray = callerIntent.getStringArrayListExtra("contentArray");
            mBookNEW.setBookId(callerIntent.getIntExtra("BookID", -1));//
            mBookNEW.setReadingChapter(callerIntent.getIntExtra("ChapterID", 1));//
            mBookNEW.setReadingY(callerIntent.getIntExtra("ReadingY", 0));//

            changeChapter(mBookNEW.getReadingChapter(), mBookNEW.getReadingY());

        } else if (mReadStyle == mBookNEW.STYLE_ONLINE) {
            Intent callerIntent1 = getIntent();
            mBookNEW.setBookId(callerIntent1.getIntExtra("BookID", -1));
            mBookNEW.setCoverUrl(callerIntent.getStringExtra("BookCover"));
            mBookNEW.setReadingChapter(callerIntent1.getIntExtra("ChapterID", 1));
            mBookNEW.setReadingY(callerIntent1.getIntExtra("ReadingY", 0));
            if (mBookNEW.getBookId() == -1 || mBookNEW.getReadingChapter() == -1) {
                Toast.makeText(this, "Nội dung này không thể đọc!", Toast.LENGTH_SHORT).show();
                return;
            }
            getChapterOfBooksNEW(mBookNEW.getBookId(), mBookNEW.getReadingChapter(), mBookNEW.getReadingY());
        }
    }

    public void changeChapter(int chapter, final int y) {
        if (chapter == 0) {
            Toast.makeText(this, "Chương được chọn không tồn tại!!!", Toast.LENGTH_LONG).show();
            return;
        }

        mPrevChapter.setVisibility(View.VISIBLE);
        mNextChapter.setVisibility(View.VISIBLE);
        if (chapter == 2) mPrevChapter.setVisibility(View.INVISIBLE);
        if (chapter > mTitleArray.size()) mNextChapter.setVisibility(View.INVISIBLE);

        getActionBar().setTitle(mTitleArray.get(chapter - 1));

        mChapterTitle.setText(mTitleArray.get(chapter - 1));
        mChapterContent.setText(mContentArray.get(chapter - 1));

        mBookNEW.setReadingChapter(chapter);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, y);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position = mBookNEW.getReadingChapter();
        switch (v.getId()) {
            case R.id.viewer_prev_btn:
                position--;
                break;
            case R.id.viewer_next_btn:
                position++;
                break;
            case R.id.viewer_chapterContent_tv:
                Log.i("<<", "viewer_chapterContent_tv > onclick");
                if (mScrollOperation.isScrolling()) {
                    Log.i("<<", "viewer_chapterContent_tv > onclick while scroll");
                    mScrollOperation.setmIsScrolling(false);
                } else {
                    Log.i("<<", "viewer_chapterContent_tv > onclick while stop");
                    mScrollOperation = new AutoScrollOperation();
                    mScrollOperation.setmIsScrolling(true);
                    mScrollOperation.execute(((MyApplication) getApplication()).getmCurrentReadMode());
                }
                return;
            default:
                super.onClick(v);
                setContentStyle();
                return;
        }

        if (mReadStyle == mBookNEW.STYLE_OFFLINE) {
            if (position < 0 || position >= mTitleArray.size()) {
                Toast.makeText(this, "Không thể chuyển chương", Toast.LENGTH_SHORT).show();
                return;
            }
            changeChapter(position, 1);
            return;
        } else if (mReadStyle == mBookNEW.STYLE_ONLINE) {
            if (position < 0) {
                Toast.makeText(this, "Không thể chuyển chương", Toast.LENGTH_SHORT).show();
                return;
            }
            mBookNEW.setReadingChapter(position);
            mChapterNEW = null;
            getChapterOfBooksNEW(mBookNEW.getBookId(), position, 0);
        }
    }

    //tải 1 chapter của một truyện từ server
    public void getChapterOfBooksNEW(int bookId, int chapterIndex, final int y) {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        mDialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.getChapterOfBook(bookId, chapterIndex).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {

                if (response.isSuccessful()) {
                    Chapter chapter = response.body();
                    if (chapter != null) {
                        mChapterNEW = chapter;
                        showOnlineChapter(mChapterNEW, y);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(ViewerActivity.this, getString(R.string.book_not_exist_chapter), Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    }
                } else {
                    int statusCode = response.code();
                    Log.d("<<ERROR>>", "response.code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                Log.d("<<ERROR>>", "error loading from API");
                Toast.makeText(ViewerActivity.this, getString(R.string.book_server_error), Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

    public void showOnlineChapter(Chapter chapter, final int y) {
        if (chapter == null) {
            Toast.makeText(this, "Chương được chọn không tồn tại!!!", Toast.LENGTH_LONG).show();
            return;
        }

        mPrevChapter.setVisibility(View.VISIBLE);
        mNextChapter.setVisibility(View.VISIBLE);
        if (chapter.getChapterIndex() == 1) mPrevChapter.setVisibility(View.INVISIBLE);

        getActionBar().setTitle(chapter.getChapterName());

        mChapterTitle.setText(chapter.getChapterName());
        mChapterContent.setText(chapter.getContent());

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, y);
            }
        });
    }

    public void setContentStyle() {
        mChapterContent.setTextColor(Color.parseColor(((MyApplication) getApplication()).getCurrentTextColor()));
        mLayout.setBackgroundColor(Color.parseColor(((MyApplication) getApplication()).getCurrentBackgroundColor()));
        mChapterContent.setLineSpacing(((MyApplication) getApplication()).getCurrentLineSpace(), 1);
        mChapterContent.setTextSize(((MyApplication) getApplication()).getCurrentTextSize());
        mChapterTitle.setTextSize(((MyApplication) getApplication()).getCurrentTextSize() + 8);
        if (mScrollOperation.isScrolling())
            mScrollOperation.setmScrollSpeed(((MyApplication) getApplication()).getmCurrentReadMode());
    }

    @Override
    protected void onPause() {
        mScrollOperation.setmIsScrolling(false);
        super.onPause();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        MyApplication mApplication = (MyApplication) getApplication();
        mApplication.setmCurrentTextColor(sharedPreferences.getInt("CurrentTextColor", 0));
        mApplication.setmCurrentBackgroundColor(sharedPreferences.getInt("CurrentBackgroundColor", 1));
        mApplication.setmCurrentTextSize(sharedPreferences.getInt("CurrentTextSize", 11));
        mApplication.setmCurrentReadMode(sharedPreferences.getInt("CurrentReadMode", 0));
        mApplication.setmCurrentLineSpace(sharedPreferences.getInt("CurrentLineSpace", 10));
    }

    @Override
    protected void onStop() {
        mLocalDatabase = ((MyApplication) getApplication()).getmLocalDatabase();

        if (mReadStyle == Book.STYLE_ONLINE)
            mLocalDatabase.insertNondownloadedBook(mBookNEW.getBookId(), mBookNEW.getBookName(), mBookNEW.getAuthorName(), mBookNEW.getCoverUrl());
        mLocalDatabase.setReadingPositon(mBookNEW.getBookId(), mBookNEW.getReadingChapter(), (int) mScrollView.getScrollY());
        ((MyApplication) getApplication()).setmLocalDatabase(mLocalDatabase);
        super.onStop();
    }

    private class AutoScrollOperation extends AsyncTask<Integer, Integer, Void> {
        private int mScrollSpeed;
        private boolean mIsScrolling;

        public void setmIsScrolling(boolean mIsScrolling) {
            this.mIsScrolling = mIsScrolling;
        }

        public boolean isScrolling() {
            return mIsScrolling;
        }

        public void setmScrollSpeed(int mScrollSpeed) {
            this.mScrollSpeed = mScrollSpeed;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mScrollSpeed = params[0];

            Runnable scrollTask = new Runnable() {
                @Override
                public void run() {
                    mScrollView.smoothScrollTo(0, mScrollView.getScrollY() + mScrollSpeed);
                }
            };

            while (mIsScrolling) {
                try {
                    mHandler.post(scrollTask);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mIsScrolling = false;
        }

        @Override
        protected void onPreExecute() {
            mIsScrolling = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
