package com.example.n50.s1212491_khosach.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.n50.s1212491_khosach.Activities.MainActivity;
import com.example.n50.s1212491_khosach.Adapters.BookGridAdapter;
import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Book9;
import com.example.n50.s1212491_khosach.Common.DBHelper;
import com.example.n50.s1212491_khosach.Common.MyApplication;
import com.example.n50.s1212491_khosach.Progress.LongOperation;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

public class MyBookShelfFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {//, Animation.AnimationListener {
//    private List<Book9> mBook9s;
    private List<Book> mBooksNEW;

    private DBHelper mLocalDatabase;
    private GridView mGridView;
    private int mPosition = 0;
    private MainActivity mContext;
    private ProgressDialog Dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("--", "onCreateView");
        mContext = (MainActivity) getActivity();
        mLocalDatabase = ((MyApplication) mContext.getApplication()).getmLocalDatabase();

        View view = inflater.inflate(R.layout.fragment_myshelf, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridView.setEmptyView(view.findViewById(R.id.empty));
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMyBooks();
    }

    private void displayMyBooks() {
        new AsyncTask<Void, Void, List<Book>>() {

            @Override
            protected void onPreExecute() {
                Dialog = new ProgressDialog(mContext);
                Dialog.setMessage(getString(R.string.progress_msg));
                Dialog.show();
            }

            @Override
            protected List<Book> doInBackground(Void... voids) {
                Log.i("<<NOGIAS>>", "doInBackground getAllBooks");
                return mLocalDatabase.getAllBooks();
            }

            @Override
            protected void onPostExecute(List<Book> list) {
                mBooksNEW = list;
                if (list != null) {
                    mGridView.setAdapter(new BookGridAdapter(getActivity(), list));
                }
                Dialog.dismiss();
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(mContext, "show chapters list", Toast.LENGTH_LONG).show();
        LongOperation longOperation = new LongOperation(mContext);
        longOperation.sendViewTask(mBooksNEW.get(position).getBookId());

        AllChapterFragment fragment = null;
        fragment = new AllChapterFragment();
        fragment.setStoryID(mBooksNEW.get(position).getBookId());
        fragment.setStoryTitle(mBooksNEW.get(position).getBookName());
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("--", "Ondestroy");
        ((MyApplication) getActivity().getApplication()).setmLocalDatabase(mLocalDatabase);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("--", "OndestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("--", "onDetach");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DBHelper db = ((MyApplication) mContext.getApplication()).getmLocalDatabase();
                        db.deleteBookAndItsChapter(mBooksNEW.get(position).getBookId());
                        ((MyApplication) mContext.getApplication()).setmLocalDatabase(db);
                        Toast.makeText(mContext, mBooksNEW.get(position).getBookName().toUpperCase() + " đã được xoá khỏi TRUYỆN CỦA TÔI...", Toast.LENGTH_SHORT).show();
                        refresh();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có muốn xoá truyện " + mBooksNEW.get(position).getBookName().toUpperCase() + " khỏi TRUYỆN CỦA TÔI không?")
                .setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
        return true;
    }

    public void refresh() {
        Fragment fragment = new MyBookShelfFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
