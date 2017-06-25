package com.example.n50.s1212491_khosach.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Common.Book;
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

    private Book mBookNEW;
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
        mBookNEW = (Book) callerIntent.getSerializableExtra("selectedBook");

        if (mBookNEW != null) {
            try{
                coverIv.setImageBitmap(Utils.decodeStringToImage(mBookNEW.getCoverUrl()));
            } catch(Exception ex){
                Log.e("QWERTY", ex.toString());
                coverIv.setImageResource(R.drawable.noimage);
            }
            coverIv.setTag(mBookNEW.getCoverUrl());
            titleTv.setText(mBookNEW.getBookName());
            authorTv.setText(mBookNEW.getAuthorName());
            if(mBookNEW.getIntroduction()==null){
                introTv.setText(getString(R.string.book_no_description));

            } else {
                introTv.setText(mBookNEW.getIntroduction());
            }
            viewTv.setText(getString(R.string.book_view) + " " + mBookNEW.getViews());
            chapterTv.setText(getString(R.string.book_chapter) + " " + mBookNEW.getChapterNumber());
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
                                    longOperation.sendRatingRequestTask(mBookNEW.getBookId(), mCurrentRating);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    ratingRb.setRating(mCurrentRating);
                                    break;
                            }
                        }
                    };

                    //////////
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setMessage("Bạn có muốn đánh giá truyện " + mBookNEW.getBookName() + " với " + ratingRb.getRating() + " sao không?")
                            .setPositiveButton("Có", dialogClickListener)
                            .setNegativeButton("Không", dialogClickListener).show();
                }
            });
        }
////

        mIsMine = ((MyApplication) getApplication()).getmLocalDatabase().checkIfExistDownloadedBook(mBookNEW.getBookId());

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
                longOperation.sendViewTask(mBookNEW.getBookId());

                mLocalDatabase = ((MyApplication) getApplication()).getmLocalDatabase();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // đọc tiếp vị trí dở dang
                                Toast.makeText(DetailActivity.this, "1111", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(DetailActivity.this, ViewerActivity.class);
                                intent1.putExtra(Book.KEY_BOOK_ID, mBookNEW.getBookName());
                                intent1.putExtra("BookAuthor", mBookNEW.getAuthorName());
                                intent1.putExtra("BookCover", mBookNEW.getCoverUrl());
                                intent1.putExtra("BookID", mBookNEW.getBookId());
                                intent1.putExtra("ChapterID", mLocalDatabase.getReadingChapter(mBookNEW.getBookId()));
                                intent1.putExtra("ReadingY", mLocalDatabase.getReadingY(mBookNEW.getBookId()));
                                intent1.putExtra("Style", mBookNEW.STYLE_ONLINE);
                                startActivity(intent1);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // xem danh sách chương
                                Toast.makeText(DetailActivity.this, "2222", Toast.LENGTH_LONG).show();

                                Intent intent2 = new Intent(DetailActivity.this, ViewerActivity.class);
                                intent2.putExtra(Book.KEY_BOOK_ID, mBookNEW.getBookName());
                                intent2.putExtra("BookID", mBookNEW.getBookId());
                                intent2.putExtra("ChapterID", 0);
                                intent2.putExtra("Style", mBookNEW.STYLE_ONLINE);
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
        intent.putExtra(Intent.EXTRA_TEXT, mBookNEW.getBookName() + getString(R.string.share_content));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }


    private void addToBookshelf() {
        DBHelper db = ((MyApplication) this.getApplication()).getmLocalDatabase();
        if (db.checkIfExistDownloadedBook(mBookNEW.getBookId()) == true) {
            Toast.makeText(this, mBookNEW.getBookName().toUpperCase() + " đã tồn tại", Toast.LENGTH_SHORT).show();
        } else {
            db.insertDownloadedBook(mBookNEW.getBookId(), mBookNEW.getBookName(), mBookNEW.getAuthorName(), mBookNEW.getCoverUrl());

            //tải và thêm các chapter của book xuống local database
            mLongOperation.getAllChapterOfBooksNEW(mBookNEW.getBookId());
//            mLongOperation.getAllChaptersTask(mBookNEW.getBookId());

            ((MyApplication) this.getApplication()).setmLocalDatabase(db);
            mIsMine= true;
            changeBookStatus();
        }
    }

    private void removeBookFromBookshelf() {
        DBHelper db = ((MyApplication) getApplication()).getmLocalDatabase();
        db.deleteBookAndItsChapter(mBookNEW.getBookId());
        ((MyApplication) getApplication()).setmLocalDatabase(db);
        mIsMine = false;
        changeBookStatus();
        Toast.makeText(this, mBookNEW.getBookName().toUpperCase() + " đã được xoá khỏi TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();
    }

    public void changeBookStatus() {
        if (mIsMine) {
            bookshelfIv.setBackgroundResource(R.drawable.add_book_icon);
        } else {
            bookshelfIv.setBackgroundResource(R.drawable.remove_book_icon);
        }
    }
}
