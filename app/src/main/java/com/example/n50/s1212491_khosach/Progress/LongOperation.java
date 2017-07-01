package com.example.n50.s1212491_khosach.Progress;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Activities.BaseActivity;
import com.example.n50.s1212491_khosach.Activities.SearchingActivity;
import com.example.n50.s1212491_khosach.Adapters.BookListAdapter;
import com.example.n50.s1212491_khosach.Common.ApiUtils;
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Common.MyWebService;
import com.example.n50.s1212491_khosach.Fragments.AllBookFragment;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LongOperation {
    private BaseActivity mContext;
    private ProgressDialog Dialog;

    public LongOperation(BaseActivity mContext) {
        this.mContext = mContext;
    }

    //tải tất cả các truyện từ server
    public void getAllBooksListNEW(final AllBookFragment fragment, final ListView listView) {
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.getAllBooksList().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                if (response.isSuccessful()) {
                    List<Book> list = response.body();
                    if (list != null) {
                        BookListAdapter adapter = new BookListAdapter(mContext, list);
                        listView.setAdapter(adapter);
                        fragment.setmBooksNEW(list);
                    }
                    Dialog.dismiss();

                } else {
                    int statusCode = response.code();
                    Log.d("<<ERROR>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("<<ERROR>>", "error loading from API");
//                showErrorMessage();
//                Log.d("MainActivity", "error loading from API");
            }
        });
    }


    //tải tất cả các chapter của một truyện từ server và lưu vào database
    public void getAllChapterOfBooksNEW(int bookId) {
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.getAllChaptersOfBook(bookId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {

                if (response.isSuccessful()) {
                    List<Chapter> list = response.body();

                    DBHelper db = ((MyApplication) mContext.getApplication()).getmLocalDatabase();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            Chapter c = list.get(i);
                            db.insertChapter(c.getBookId(), c.getChapterIndex(), c.getChapterName(), c.getContent());
                        }
                    }
                    ((MyApplication) mContext.getApplication()).setmLocalDatabase(db);
                    Dialog.dismiss();
                    Toast.makeText(mContext, "Truyện đã được thêm vào TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();

                } else {
                    int statusCode = response.code();
                    Log.d("<<ERROR>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Log.d("<<ERROR>>", "error loading from API");
            }
        });
    }


    //tìm truyện theo tên
    public void searchBookNEW(String bookName, final ListView resultList, final SearchingActivity act) {
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.searchBook(bookName).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> list = response.body();
                    if (list != null) {
                        act.setAdapterForList(list);
                        Dialog.dismiss();
                    }
                    Dialog.dismiss();

                } else {
                    int statusCode = response.code();
                    Log.d("<<ERROR>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("<<ERROR>>", "error loading from API");
            }
        });
    }


    //rating truyện
    public void rateBookNEW(int bookId, float point) {
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg_rating));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.rateBook(bookId, point).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean result = Boolean.parseBoolean(response.body().toString());
                    Dialog.dismiss();
                } else {
                    int statusCode = response.code();
                    Log.d("<<ERROR>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                    Handler handler = new Handler(mContext.getMainLooper());
                    Runnable showingToastTask = new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, R.string.msg_rate_success, Toast.LENGTH_SHORT).show();
                        }
                    };
                    handler.post(showingToastTask);
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("<<ERROR>>", "error loading from API");
                Dialog.dismiss();
            }
        });
    }

    //đếm view truyện
    public void sendViewNEW(int bookId) {
        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.sendView(bookId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("<<ERROR>>", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
