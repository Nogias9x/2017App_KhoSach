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
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
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

public class LongOperation {
    private BaseActivity mContext;
    private ProgressDialog Dialog;

    public LongOperation(BaseActivity mContext) {
        this.mContext = mContext;
    }

    //tải tất cả các chương của 1 truyện từ server
    public void getAllChaptersTask(final int storyID) {
        new AsyncTask<Void, Void, List<Chapter>>() {
            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Chapter> doInBackground(Void... voids) {
                return getAllChapters("http://wsthichtruyen-1212491.rhcloud.com/?function=1&StoryID=" + storyID);
            }

            @Override
            protected void onPostExecute(List<Chapter> list) {
                DBHelper db = ((MyApplication) mContext.getApplication()).getmLocalDatabase();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Chapter c = list.get(i);
                        db.insertChapter(c.getmStoryId(), c.getmChapterId(), c.getmTitle(), c.getmContent());
                    }
                }
                ((MyApplication) mContext.getApplication()).setmLocalDatabase(db);
                Dialog.dismiss();
                Toast.makeText(mContext, "Truyện đã được thêm vào TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private List<Chapter> getAllChapters(String path) {
        Log.i("<<NOGIAS>>", "getAllChapters S");
        Context mContext;
        String Content = null;
        String Error = null;
        ProgressDialog Dialog;
        String data = "";
        List<Chapter> mChapterList = null;

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
                mChapterList = new ArrayList<Chapter>(lengthJsonArr);
                Log.i("<<NOGIAS>>", "lengthJsonArr: " + lengthJsonArr);//
                for (int i = 0; i < lengthJsonArr; i++) {
//                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
//
                    Chapter chapter = new Chapter();
                    chapter.setmStoryId(jsonChildNode.optInt(Chapter.KEY_STORYID));
                    chapter.setmChapterId(jsonChildNode.optInt(Chapter.KEY_CHAPTERID));
                    chapter.setmTitle(jsonChildNode.optString(Chapter.KEY_TITLE).toString());
                    chapter.setmContent(jsonChildNode.optString(Chapter.KEY_CONTENT).toString());

                    mChapterList.add(chapter);
                }
            } catch (JSONException e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }
        Log.i("<<NOGIAS>>", "getAllChapters E");
        if (mChapterList == null) Log.i("<<NOGIAS>>", "mChapterList null");
        if (mChapterList != null) Log.i("<<NOGIAS>>", "mChapterList NOT null");
        return mChapterList;
    }

    //tải tất cả các truyện từ server
    public void getAllBooksTask(final AllBookFragment fragment, final ListView listView) {
        new AsyncTask<Void, Void, List<Book>>() {
            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Book> doInBackground(Void... voids) {
                return getAllBooks("http://wsthichtruyen-1212491.rhcloud.com/?function=0");
            }

            @Override
            protected void onPostExecute(List<Book> list) {
                if (list != null) {
                    BookListAdapter adapter = new BookListAdapter(mContext, list);
                    listView.setAdapter(adapter);
                    fragment.setmBooks(list);
                }
                Dialog.dismiss();
            }
        }.execute();
    }

    private List<Book> getAllBooks(String path) {
        Context mContext;
        String Content = null;
        String Error = null;
        ProgressDialog Dialog;
        String data = "";
        List<Book> mBookList = null;

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
                mBookList = new ArrayList<Book>(lengthJsonArr);
                for (int i = 0; i < lengthJsonArr; i++) {
//                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
//
                    Book book = new Book();
                    book.setId(jsonChildNode.optInt(Book.KEY_ID));
                    book.setAuthor(jsonChildNode.optString(Book.KEY_AUTHOR).toString());
                    book.setCoverUrl(jsonChildNode.optString(Book.KEY_COVER).toString());
                    book.setChapter(jsonChildNode.optString(Book.KEY_CHAPTER).toString());
                    book.setDescription(jsonChildNode.optString(Book.KEY_DESCRIPTION).toString());
                    book.setTitle(jsonChildNode.optString(Book.KEY_TITLE).toString());
                    book.setView(jsonChildNode.optInt(Book.KEY_VIEW));
                    book.setGoodPoint(jsonChildNode.optInt(Book.KEY_GOODPOINT));
                    book.setTotalPoint(jsonChildNode.optInt(Book.KEY_TOTALPOINT));
                    mBookList.add(book);
                }
            } catch (JSONException e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }
        return mBookList;
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

    ///////////////////////
    //tìm các truyện từ server chứa từ khoá
    public void searchBookTask(final String searchKey, final ListView resultList, final SearchingActivity act) {
        new AsyncTask<Void, Void, List<Book>>() {
            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(mContext.getResources().getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Book> doInBackground(Void... voids) {
                String str = searchKey.replace(" ", "%20");
                return searchBook("http://wsthichtruyen-1212491.rhcloud.com/?function=5&title=" + str);
            }

            @Override
            protected void onPostExecute(List<Book> list) {
                act.setAdapterForList(list);
                Dialog.dismiss();
            }
        }.execute();
    }

    private List<Book> searchBook(String path) {
        Context mContext;
        String Content = null;
        String Error = null;
        ProgressDialog Dialog;
        String data = "";
        List<Book> mBookList = null;

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
                mBookList = new ArrayList<Book>(lengthJsonArr);
                for (int i = 0; i < lengthJsonArr; i++) {
//                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
//
                    Book book = new Book();
                    book.setId(jsonChildNode.optInt(Book.KEY_ID));
                    book.setAuthor(jsonChildNode.optString(Book.KEY_AUTHOR).toString());
                    book.setCoverUrl(jsonChildNode.optString(Book.KEY_COVER).toString());
                    book.setChapter(jsonChildNode.optString(Book.KEY_CHAPTER).toString());
                    book.setDescription(jsonChildNode.optString(Book.KEY_DESCRIPTION).toString());
                    book.setTitle(jsonChildNode.optString(Book.KEY_TITLE).toString());
                    book.setView(jsonChildNode.optInt(Book.KEY_VIEW));
                    book.setGoodPoint(jsonChildNode.optInt(Book.KEY_GOODPOINT));
                    book.setTotalPoint(jsonChildNode.optInt(Book.KEY_TOTALPOINT));
                    mBookList.add(book);
                }
            } catch (JSONException e) {
                Log.i("<<NOGIAS>>", e.toString());
            }
        }

        return mBookList;
    }
}
