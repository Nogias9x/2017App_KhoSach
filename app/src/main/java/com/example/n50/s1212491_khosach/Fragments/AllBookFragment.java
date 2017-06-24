package com.example.n50.s1212491_khosach.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Activities.DetailActivity;
import com.example.n50.s1212491_khosach.Activities.MainActivity;
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Book9;
import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Progress.LongOperation;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

public class AllBookFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private List<Book9> mBook9s = null;
    private List<Book> mBooksNEW = null;

    private ListView mListView;
    private MainActivity mContext;
    private ProgressDialog Dialog;
    private List<Chapter> mChapters = null;
    LongOperation mLongOperation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_allbook, container, false);


        mLongOperation = new LongOperation(mContext);

        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setEmptyView(view.findViewById(R.id.empty));

        if (!mContext.isOnline()) {
            Toast.makeText(mContext, R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
            return view;
        }

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mLongOperation.getAllBooksListNEW(this, mListView);

        return view;
    }

    public void setmBook9s(List<Book9> mBook9s) {
        this.mBook9s = mBook9s;
    }
    public void setmBooksNEW(List<Book> mBooksNEW) {
        this.mBooksNEW = mBooksNEW;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent detailIntent = new Intent(mContext, DetailActivity.class);
        detailIntent.putExtra("selectedBook", mBooksNEW.get(position));
        mContext.startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DBHelper db = ((MyApplication) mContext.getApplication()).getmLocalDatabase();
                        if (db.checkIfExistDownloadedBook(mBook9s.get(position).getId()) == true) {
                            Toast.makeText(mContext, mBook9s.get(position).getTitle().toUpperCase() + " đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            db.insertDownloadedBook(mBook9s.get(position).getId(), mBook9s.get(position).getTitle(), mBook9s.get(position).getAuthor(), mBook9s.get(position).getCoverUrl());

                            //tải và thêm các chapter của book xuống local database
                            mLongOperation.getAllChaptersTask(mBook9s.get(position).getId());
                            ((MyApplication) mContext.getApplication()).setmLocalDatabase(db);
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        //////////
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn thêm truyện " + mBook9s.get(position).getTitle().toUpperCase() + " vào TRUYỆN CỦA TÔI không?")
                .setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
        return true;
    }

}

