package com.example.n50.s1212491_khosach.Progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Activities.BaseActivity;
import com.example.n50.s1212491_khosach.Activities.SearchingActivity;
import com.example.n50.s1212491_khosach.Adapters.BookListAdapter;
import com.example.n50.s1212491_khosach.Common.ApiUtils;
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Book9;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.Chapter9;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Common.MyWebService;
import com.example.n50.s1212491_khosach.Fragments.AllBookFragment;
import com.example.n50.s1212491_khosach.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
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

    //gửi request rating 1 truyện về server
    public void sendRatingRequestTask(final int storyID, final float ratingPoint) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... voids) {
                sendRatingRequest(storyID, ratingPoint);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Handler handler = new Handler(mContext.getMainLooper());
                Runnable showingToastTask = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, R.string.msg_rate_success, Toast.LENGTH_SHORT).show();
                    }
                };
                handler.post(showingToastTask);
            }
        }.execute();
    }

    private void sendRatingRequest(int storyID, float ratingPoint) {
        String data = "";
        BufferedReader reader = null;
        String path = "http://wsthichtruyen-1212491.rhcloud.com/?function=3&storyID=" + storyID + "&ratingPoint=" + ratingPoint;
        Log.i("<<NOGIAS>>", "sendRatingRequest: " + path);
        try {
            URL url = new URL(path);

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            Log.e("<<ERROR>>", e.toString());
        }
    }


    ////gửi request rating 1 truyện về server
    public void sendViewTask(final int storyID) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... voids) {
                sendView(storyID);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }.execute();
    }

    private void sendView(int storyID) {
        String data = "";
        BufferedReader reader = null;
        String path = "http://wsthichtruyen-1212491.rhcloud.com/?function=4&storyID=" + storyID;
        Log.i("<<NOGIAS>>", "sendView: " + path);
        try {
            URL url = new URL(path);

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            Log.e("<<ERROR>>", e.toString());
        }
    }

    // MY DOING /////////////////////////////////////////////////////////>>>>>>>
    //tải tất cả các truyện từ server
    public void getAllBooksListNEW(final AllBookFragment fragment, final ListView listView){
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
//        Dialog.setCancelable(false);
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.getAllBooksList().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                if(response.isSuccessful()) {
                    Log.d("<<QWERTY>>", "posts loaded from API");
                    Log.d("<<QWERTY>>", response.body().toString());
                    List<Book> list = response.body();
                    if (list != null) {
                        BookListAdapter adapter = new BookListAdapter(mContext, list);
                        listView.setAdapter(adapter);
                        fragment.setmBooksNEW(list);
                    }
                    Dialog.dismiss();

                }else {
                    int statusCode  = response.code();
                    Log.d("<<QWERTY>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("<<QWERTY>>", "error loading from API");
                Log.d("<<QWERTY>>", call.toString());
                Log.d("<<QWERTY>>", t.toString());
//                showErrorMessage();
//                Log.d("MainActivity", "error loading from API");
            }
        });
    }



    //tải tất cả các chapter của một truyện từ server và lưu vào database
    public void getAllChapterOfBooksNEW(int bookId){
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.getAllChaptersOfBook(bookId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {

                if(response.isSuccessful()) {
                    Log.d("<<QWERTY>>", "posts loaded from API");
                    Log.d("<<QWERTY>>", response.body().toString());
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

                }else {
                    int statusCode  = response.code();
                    Log.d("<<QWERTY>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Log.d("<<QWERTY>>", "error loading from API");
            }
        });
    }


    //tìm truyện theo tên
    public void searchBookNEW(String bookName, final ListView resultList, final SearchingActivity act){
        Dialog = new ProgressDialog(mContext);
        Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
        Dialog.show();

        MyWebService mMyWebService;
        mMyWebService = ApiUtils.getMyWebService();
        mMyWebService.searchBook(bookName).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()) {
                    Log.d("<<QWERTY>>", "posts loaded from API");
                    Log.d("<<QWERTY>>", response.body().toString());
                    List<Book> list = response.body();
                    if (list != null) {
                        act.setAdapterForList(list);
                        Dialog.dismiss();
                    }
                    Dialog.dismiss();

                }else {
                    int statusCode  = response.code();
                    Log.d("<<QWERTY>>", "response.code: " + response.code());
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("<<QWERTY>>", "error loading from API");
            }
        });
    }

    // MY DOING /////////////////////////////////////////////////////////<<<<<<

}
