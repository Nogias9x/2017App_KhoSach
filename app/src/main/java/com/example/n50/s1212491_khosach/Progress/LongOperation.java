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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

    //tải tất cả các chương của 1 truyện từ server
    public void getAllChaptersTask(final int storyID) {
        new AsyncTask<Void, Void, List<Chapter9>>() {
            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Chapter9> doInBackground(Void... voids) {
                return getAllChapters("http://wsthichtruyen-1212491.rhcloud.com/?function=1&StoryID=" + storyID);
            }

            @Override
            protected void onPostExecute(List<Chapter9> list) {
                DBHelper db = ((MyApplication) mContext.getApplication()).getmLocalDatabase();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Chapter9 c = list.get(i);
                        db.insertChapter(c.getmStoryId(), c.getmChapterId(), c.getmTitle(), c.getmContent());
                    }
                }
                ((MyApplication) mContext.getApplication()).setmLocalDatabase(db);
                Dialog.dismiss();
                Toast.makeText(mContext, "Truyện đã được thêm vào TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private List<Chapter9> getAllChapters(String path) {
        Log.i("<<NOGIAS>>", "getAllChapters S");
        Context mContext;
        String Content = null;
        String Error = null;
        ProgressDialog Dialog;
        String data = "";
        List<Chapter9> mChapter9List = null;

        BufferedReader reader = null;
        try {
            URL url = new URL(path);//"http://wsthichtruyen-1212491.rhcloud.com/?function=0");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + " ");
            }

            // Append Server Response To Content String
            Content = sb.toString();
        } catch (Exception ex) {
            Error = ex.getMessage();
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                Log.i("<<NOGIAS>>", ex.toString());
            }
        }

        if (Error != null) {
            Log.i("<<NOGIAS>>", Error);
        } else {
            /****************** Start Parse Response JSON Data *************/
            JSONArray jsonArray;
            try {
                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                Content = Html.fromHtml(Content).toString();
                if (Content.equals("")) {
                    Log.i("<<NOGIAS>>", "Content: NULL ");
                    return null;
                }
                jsonArray = new JSONArray(Content);//
//                /*********** Process each JSON Node ************///
                int lengthJsonArr = jsonArray.length();
                mChapter9List = new ArrayList<Chapter9>(lengthJsonArr);
                Log.i("<<NOGIAS>>", "lengthJsonArr: " + lengthJsonArr);//
                for (int i = 0; i < lengthJsonArr; i++) {
//                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
//
                    Chapter9 chapter9 = new Chapter9();
                    chapter9.setmStoryId(jsonChildNode.optInt(Chapter.KEY_STORYID));
                    chapter9.setmChapterId(jsonChildNode.optInt(Chapter.KEY_CHAPTERID));
                    chapter9.setmTitle(jsonChildNode.optString(Chapter.KEY_TITLE).toString());
                    chapter9.setmContent(jsonChildNode.optString(Chapter.KEY_CONTENT).toString());

                    mChapter9List.add(chapter9);
                }
            } catch (JSONException e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }
        Log.i("<<NOGIAS>>", "getAllChapters E");
        if (mChapter9List == null) Log.i("<<NOGIAS>>", "mChapter9List null");
        if (mChapter9List != null) Log.i("<<NOGIAS>>", "mChapter9List NOT null");
        return mChapter9List;
    }

//    //tải tất cả các truyện từ server
//    public void getAllBooksTask(final AllBookFragment fragment, final ListView listView) {
//        new AsyncTask<Void, Void, List<Book9>>() {
//            @Override
//            protected void onPreExecute() {
//                Dialog = new ProgressDialog(mContext);
//                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
//                Dialog.show();
//            }
//
//            @Override
//            protected List<Book9> doInBackground(Void... voids) {
//                return getAllBooks("http://wsthichtruyen-1212491.rhcloud.com/?function=0");
//            }
//
//            @Override
//            protected void onPostExecute(List<Book9> list) {
//                if (list != null) {
//                    BookListAdapter adapter = new BookListAdapter(mContext, list);
//                    listView.setAdapter(adapter);
//                    fragment.setmBook9s(list);
//                }
//                Dialog.dismiss();
//            }
//        }.execute();
//    }
//
//    private List<Book9> getAllBooks(String path) {
//        Context mContext;
//        String Content = null;
//        String Error = null;
//        ProgressDialog Dialog;
//        String data = "";
//        List<Book9> mBook9List = null;
//
//        BufferedReader reader = null;
//        try {
//            URL url = new URL(path);//"http://wsthichtruyen-1212491.rhcloud.com/?function=0");
//
//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            wr.write(data);
//            wr.flush();
//
//            // Get the server response
//            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//
//            // Read Server Response
//            while ((line = reader.readLine()) != null) {
//                // Append server response in string
//                sb.append(line + " ");
//            }
//
//            // Append Server Response To Content String
//            Content = sb.toString();
//        } catch (Exception ex) {
//            Error = ex.getMessage();
//        } finally {
//            try {
//                reader.close();
//            } catch (Exception ex) {
//                Log.i("<<NOGIAS>>", ex.toString());
//            }
//        }
//
//        if (Error != null) {
//            Log.i("<<NOGIAS>>", Error);
//        } else {
//            /****************** Start Parse Response JSON Data *************/
//            JSONArray jsonArray;
//            try {
//                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
//                Content = Html.fromHtml(Content).toString();
//                if (Content.equals("")) {
//                    Log.i("<<NOGIAS>>", "Content: NULL ");
//                    return null;
//                }
//                jsonArray = new JSONArray(Content);//
////                /*********** Process each JSON Node ************///
//                int lengthJsonArr = jsonArray.length();
//                mBook9List = new ArrayList<Book9>(lengthJsonArr);
//                for (int i = 0; i < lengthJsonArr; i++) {
////                    /****** Get Object for each JSON node.***********/
//                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
////
//                    Book9 book9 = new Book9();
//                    book9.setId(jsonChildNode.optInt(Book9.KEY_ID));
//                    book9.setAuthor(jsonChildNode.optString(Book9.KEY_AUTHOR).toString());
//                    book9.setCoverUrl(jsonChildNode.optString(Book9.KEY_COVER).toString());
//                    book9.setChapter(jsonChildNode.optString(Book9.KEY_CHAPTER).toString());
//                    book9.setDescription(jsonChildNode.optString(Book9.KEY_DESCRIPTION).toString());
//                    book9.setTitle(jsonChildNode.optString(Book9.KEY_TITLE).toString());
//                    book9.setView(jsonChildNode.optInt(Book9.KEY_VIEW));
//                    book9.setGoodPoint(jsonChildNode.optInt(Book9.KEY_GOODPOINT));
//                    book9.setTotalPoint(jsonChildNode.optInt(Book9.KEY_TOTALPOINT));
//                    mBook9List.add(book9);
//                }
//            } catch (JSONException e) {
//                Log.i("<<NOGIAS>>", e.toString());
//            }
//        }
//        return mBook9List;
//    }

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

    ///////////////////////
    //tìm các truyện từ server chứa từ khoá
//    public void searchBookTask(final String searchKey, final ListView resultList, final SearchingActivity act) {
//        new AsyncTask<Void, Void, List<Book9>>() {
//            @Override
//            protected void onPreExecute() {
//                Dialog = new ProgressDialog(mContext);
//                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
//                Dialog.show();
//            }
//
//            @Override
//            protected List<Book9> doInBackground(Void... voids) {
//                String str = searchKey.replace(" ", "%20");
//                return searchBook("http://wsthichtruyen-1212491.rhcloud.com/?function=5&title=" + str);
//            }
//
//            @Override
//            protected void onPostExecute(List<Book9> list) {
//                act.setAdapterForList(list);
//                Dialog.dismiss();
//            }
//        }.execute();
//    }

    private List<Book9> searchBook(String path) {
        Context mContext;
        String Content = null;
        String Error = null;
        ProgressDialog Dialog;
        String data = "";
        List<Book9> mBook9List = null;

        BufferedReader reader = null;
        try {
            Log.i("<<NOGIAS>>", path);
            URL url = new URL(path);//http://wsthichtruyen-1212491.rhcloud.com/?function=5&title=ACBDE

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + " ");
            }

            // Append Server Response To Content String
            Content = sb.toString();
        } catch (Exception ex) {
            Error = ex.getMessage();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }

        if (Error != null) {
            Log.i("<<NOGIAS>>", Error);
        } else {
            /****************** Start Parse Response JSON Data *************/
            JSONArray jsonArray;
            try {
                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                Content = Html.fromHtml(Content).toString();
                if (Content.equals("")) {
                    Log.i("<<NOGIAS>>", "Content: NULL ");
                    return null;
                }
                jsonArray = new JSONArray(Content);//
//                /*********** Process each JSON Node ************///
                int lengthJsonArr = jsonArray.length();
                Log.i("<<NOGIAS>>", "lengthJsonArr: " + lengthJsonArr);
                mBook9List = new ArrayList<Book9>(lengthJsonArr);
                for (int i = 0; i < lengthJsonArr; i++) {
//                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
//
                    Book9 book9 = new Book9();
                    book9.setId(jsonChildNode.optInt(Book9.KEY_ID));
                    book9.setAuthor(jsonChildNode.optString(Book9.KEY_AUTHOR).toString());
                    book9.setCoverUrl(jsonChildNode.optString(Book9.KEY_COVER).toString());
                    book9.setChapter(jsonChildNode.optString(Book9.KEY_CHAPTER).toString());
                    book9.setDescription(jsonChildNode.optString(Book9.KEY_DESCRIPTION).toString());
                    book9.setTitle(jsonChildNode.optString(Book9.KEY_TITLE).toString());
                    book9.setView(jsonChildNode.optInt(Book9.KEY_VIEW));
                    book9.setGoodPoint(jsonChildNode.optInt(Book9.KEY_GOODPOINT));
                    book9.setTotalPoint(jsonChildNode.optInt(Book9.KEY_TOTALPOINT));
                    mBook9List.add(book9);
                }
            } catch (JSONException e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }

        return mBook9List;
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
//                        fragment.setmBook9s(list);
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
