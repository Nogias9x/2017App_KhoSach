package com.example.n50.s1212491_khosach.Adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Utils;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

public class BookListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Book> mListNEW;

    public BookListAdapter(Context context, List<Book> list) {
        mContext = context;
        mListNEW = list;
    }

    @Override
    public int getCount() {
        if (mListNEW == null) {
            return 0;
        }
        return mListNEW.size();
    }

    @Override
    public Object getItem(int position) {
        return mListNEW.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_list_book, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = mListNEW.get(position);
        if (book != null) {
            try {
                viewHolder.coverIv.setImageBitmap(Utils.decodeStringToImage(book.getCoverUrl()));
            } catch (Exception ex) {
                // default image is "R.drawable.noimage"
                Log.e("<<ERROR>>", ex.toString());
            }

            viewHolder.titleTv.setText(book.getBookName());
            viewHolder.authorTv.setText(book.getAuthorName());
            viewHolder.viewTv.setText(mContext.getString(R.string.book_view) + " " + book.getViews());
            viewHolder.chapterTv.setText(mContext.getString(R.string.book_chapter) + " " + book.getChapterNumber());
            viewHolder.ratingRb.setRating(book.getRatingPoints());
            viewHolder.ratingRb.setScaleX(0.5f);
            viewHolder.ratingRb.setScaleY(0.5f);
            viewHolder.ratingRb.setPivotX(0);
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView coverIv;
        private TextView titleTv;
        private TextView authorTv;
        private TextView viewTv;
        private TextView chapterTv;
        private RatingBar ratingRb;

        public ViewHolder(View view) {
            coverIv = (ImageView) view.findViewById(R.id.cover_iv);
            titleTv = (TextView) view.findViewById(R.id.title_tv);
            authorTv = (TextView) view.findViewById(R.id.author_tv);
            viewTv = (TextView) view.findViewById(R.id.view_tv);
            chapterTv = (TextView) view.findViewById(R.id.chapter_tv);
            ratingRb = (RatingBar) view.findViewById(R.id.rating_rb);
        }
    }
}
