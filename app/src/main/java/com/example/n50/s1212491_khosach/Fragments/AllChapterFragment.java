package com.example.n50.s1212491_khosach.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.n50.s1212491_khosach.Activities.MainActivity;
import com.example.n50.s1212491_khosach.Activities.ViewerActivity;
import com.example.n50.s1212491_khosach.Adapters.ChapterListAdapter;
import com.example.n50.s1212491_khosach.Common.Book9;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
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

public class AllChapterFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<Chapter> mChapters = null;
    private ListView mListView;
    private MainActivity mContext;
    private ProgressDialog Dialog;
    private int mStoryID;
    private String mStoryTitle;
    private DBHelper mLocalDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_allbook, container, false);
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setEmptyView(view.findViewById(R.id.empty));
        mListView.setOnItemClickListener(this);
        mListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mLocalDatabase = ((MyApplication) mContext.getApplication()).getmLocalDatabase();

        mContext.getActionBar().setDisplayShowTitleEnabled(true);
        mContext.getActionBar().setTitle(mStoryTitle);

        getAllChaptersTask();

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(mContext, ViewerActivity.class);
                        intent.putExtra("BookTitle", mStoryTitle);
                        intent.putExtra("BookID", mStoryID);
                        intent.putExtra("ChapterID", mLocalDatabase.getReadingChapter(mStoryID));
                        intent.putExtra("ReadingY", mLocalDatabase.getReadingY(mStoryID));
                        Log.i("<<NOGIAS>>", "ChapterID" + mLocalDatabase.getReadingChapter(mStoryID));
                        Log.i("<<NOGIAS>>", "ReadingY" + mLocalDatabase.getReadingY(mStoryID));
                        intent.putExtra("Style", Book9.STYLE_OFFLINE);

                        ArrayList<String> titleArray = new ArrayList<String>();
                        ArrayList<String> contentArray = new ArrayList<String>();

                        for (int i = 0; i < mChapters.size(); i++) {
                            titleArray.add(mChapters.get(i).getmTitle());
                            contentArray.add(mChapters.get(i).getmContent());
                        }

                        intent.putStringArrayListExtra("titleArray", titleArray);
                        intent.putStringArrayListExtra("contentArray", contentArray);

                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        //////////
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn đọc tiếp không?")
                .setPositiveButton("Có, đọc tiếp", dialogClickListener)
                .setNegativeButton("Không, chọn lại chương khác", dialogClickListener).show();


        return view;
    }


    private void getAllChaptersTask() {
        new AsyncTask<Void, Void, List<Chapter>>() {
            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Chapter> doInBackground(Void... voids) {
                return mLocalDatabase.getAllChapters(mStoryID);
            }

            @Override
            protected void onPostExecute(List<Chapter> list) {
                if (list != null) {
                    mChapters = list;
                    ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), mChapters);
                    mListView.setAdapter(adapter);
                }
                Dialog.dismiss();
            }
        }.execute();
    }

    private List<Chapter> getAllChapters(String path) {
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
        return mChapterList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(mContext, ViewerActivity.class);
        intent.putExtra("BookTitle", mStoryTitle);
        intent.putExtra("ChapterID", position);
        intent.putExtra("ReadingY", 0);
        intent.putExtra("Style", new Book9().STYLE_OFFLINE);

        ArrayList<String> titleArray = new ArrayList<String>();
        ArrayList<String> contentArray = new ArrayList<String>();

        for (int i = 0; i < mChapters.size(); i++) {
            titleArray.add(mChapters.get(i).getmTitle());
            contentArray.add(mChapters.get(i).getmContent());
        }

        intent.putStringArrayListExtra("titleArray", titleArray);
        intent.putStringArrayListExtra("contentArray", contentArray);

        startActivity(intent);
    }


    public void setStoryID(int storyID) {
        this.mStoryID = storyID;
    }

    public void setStoryTitle(String storyTitle) {
        this.mStoryTitle = storyTitle;
    }


}

