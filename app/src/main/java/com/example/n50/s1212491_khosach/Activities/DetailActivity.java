package com.example.n50.s1212491_khosach.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Common.Book9;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Common.Utils;
import com.example.n50.s1212491_khosach.Progress.LongOperation;
import com.example.n50.s1212491_khosach.R;


public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView coverIv;
    private TextView titleTv;
    private TextView authorTv;
    private TextView viewTv;
    private TextView chapterTv;
    private TextView introTv;

    private ImageView bookshelfIv;
    private ImageView readIv;
    private ImageView shareIv;
    private RatingBar ratingRb;

    private Book9 mBook9;
    private DBHelper mLocalDatabase;
    private boolean mIsMine = false;
    private LongOperation mLongOperation;

    private float mCurrentRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocalDatabase = new DBHelper(this);
        mLongOperation = new LongOperation(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_detail);

        coverIv = (ImageView) findViewById(R.id.cover_iv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        authorTv = (TextView) findViewById(R.id.author_tv);
        viewTv = (TextView) findViewById(R.id.view_tv);
        chapterTv = (TextView) findViewById(R.id.chapter_tv);
        bookshelfIv = (ImageView) findViewById(R.id.add_remove_my_bookshelf_iv);
        readIv = (ImageView) findViewById(R.id.read_iv);
        shareIv = (ImageView) findViewById(R.id.share_iv);
        ratingRb = (RatingBar) findViewById(R.id.rating_rb);
        introTv = (TextView) findViewById(R.id.intro_tv);

        ratingRb.setVisibility(View.VISIBLE);
        bookshelfIv.setEnabled(true);

        bookshelfIv.setOnClickListener(this);
        readIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);


        Intent callerIntent = getIntent();
        mBook9 = (Book9) callerIntent.getSerializableExtra("selectedBook");

        if (mBook9 != null) {
            coverIv.setImageResource(R.drawable.loading);
            coverIv.setTag(mBook9.getCoverUrl());
//            Utils.loadImageFromUrl(this, coverIv, mBook9.getCoverUrl());
            titleTv.setText(mBook9.getTitle());
            authorTv.setText(mBook9.getAuthor());
            introTv.setText(mBook9.getDescription());
            viewTv.setText(getString(R.string.book_view) + mBook9.getView());
            chapterTv.setText(mBook9.getChapter());
            ratingRb.setStepSize((float) 0.5);
            ratingRb.setIsIndicator(false);

            mCurrentRating = 0;
            ratingRb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Log.i("<<NOGIAS>>", "onRatingChanged");
                    if (ratingRb.getRating() == mCurrentRating) return;
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    mCurrentRating = ratingRb.getRating();
                                    LongOperation longOperation = new LongOperation(DetailActivity.this);
                                    longOperation.sendRatingRequestTask(mBook9.getId(), mCurrentRating);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    ratingRb.setRating(mCurrentRating);
                                    break;
                            }
                        }
                    };

                    //////////
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setMessage("Bạn có muốn đánh giá truyện " + mBook9.getTitle() + " với " + ratingRb.getRating() + " sao không?")
                            .setPositiveButton("Có", dialogClickListener)
                            .setNegativeButton("Không", dialogClickListener).show();
                }
            });
        }
////

        mIsMine = ((MyApplication) getApplication()).getmLocalDatabase().checkIfExistDownloadedBook(mBook9.getId());

        changeBookStatus();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.share_iv || id == R.id.read_iv || id == R.id.add_remove_my_bookshelf_iv) {
            if (!this.isOnline()) {
                Toast.makeText(this, R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        switch (id) {
            case R.id.share_iv:
                startShareActivity();
                break;
            case R.id.read_iv:
                LongOperation longOperation = new LongOperation(DetailActivity.this);
                longOperation.sendViewTask(mBook9.getId());

                mLocalDatabase = ((MyApplication) getApplication()).getmLocalDatabase();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent1 = new Intent(DetailActivity.this, ViewerActivity.class);
                                intent1.putExtra("BookTitle", mBook9.getTitle());
                                intent1.putExtra("BookAuthor", mBook9.getAuthor());
                                intent1.putExtra("BookCover", mBook9.getCoverUrl());
                                intent1.putExtra("BookID", mBook9.getId());
                                intent1.putExtra("ChapterID", mLocalDatabase.getReadingChapter(mBook9.getId()));
                                intent1.putExtra("ReadingY", mLocalDatabase.getReadingY(mBook9.getId()));
                                intent1.putExtra("Style", mBook9.STYLE_ONLINE);
                                startActivity(intent1);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent intent2 = new Intent(DetailActivity.this, ViewerActivity.class);
                                intent2.putExtra("BookTitle", mBook9.getTitle());
                                intent2.putExtra("BookID", mBook9.getId());
                                intent2.putExtra("ChapterID", 0);
                                intent2.putExtra("Style", mBook9.STYLE_ONLINE);
                                startActivity(intent2);
                                break;
                        }
                    }
                };

                //////////
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setMessage("Bạn có muốn đọc tiếp tại vị trí dở dang không?")
                        .setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();

                break;
            case R.id.add_remove_my_bookshelf_iv:
                if (mIsMine) {
                    removeBookFromBookshelf();
                } else {
                    addToBookshelf();
                }
                break;
            default:
                super.onClick(view);
                return;
        }
    }

    private void startShareActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mBook9.getTitle() + getString(R.string.share_content));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }


    private void addToBookshelf() {
        DBHelper db = ((MyApplication) this.getApplication()).getmLocalDatabase();
        if (db.checkIfExistDownloadedBook(mBook9.getId()) == true) {
            Toast.makeText(this, mBook9.getTitle().toUpperCase() + " đã tồn tại", Toast.LENGTH_SHORT).show();
        } else {
            db.insertDownloadedBook(mBook9.getId(), mBook9.getTitle(), mBook9.getAuthor(), mBook9.getCoverUrl());

            //tải và thêm các chapter của book xuống local database
            mLongOperation.getAllChaptersTask(mBook9.getId());
            ((MyApplication) this.getApplication()).setmLocalDatabase(db);
            mIsMine= true;
            changeBookStatus();
        }
    }

    private void removeBookFromBookshelf() {
        DBHelper db = ((MyApplication) getApplication()).getmLocalDatabase();
        db.deleteBookAndItsChapter(mBook9.getId());
        ((MyApplication) getApplication()).setmLocalDatabase(db);
        mIsMine = false;
        changeBookStatus();
        Toast.makeText(this, mBook9.getTitle().toUpperCase() + " đã được xoá khỏi TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();
    }

    public void changeBookStatus() {
        if (mIsMine) {
            bookshelfIv.setBackgroundResource(R.drawable.add_book_icon);
        } else {
            bookshelfIv.setBackgroundResource(R.drawable.remove_book_icon);
        }
    }
}
