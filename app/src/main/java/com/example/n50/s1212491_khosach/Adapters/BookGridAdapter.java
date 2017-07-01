package com.example.n50.s1212491_khosach.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n50.s1212491_khosach.Common.Book;
import com.example.n50.s1212491_khosach.Common.Utils;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

/**
 * Created by 12124 on 6/24/2017.
 */

public class BookGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Book> mList;

    public BookGridAdapter(Context context, List<Book> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = inflater.inflate(R.layout.row_grid_book, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = mList.get(position);
        if (book != null) {
            try {
                viewHolder.coverIv.setImageBitmap(Utils.decodeStringToImage(book.getCoverUrl()));
            } catch (Exception ex) {
                // default image is "R.drawable.noimage"
                Log.e("<<ERROR>>", ex.toString());
                viewHolder.coverIv.setImageResource(R.drawable.noimage);
            }
            viewHolder.titleTv.setText(book.getBookName());
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView coverIv;
        private TextView titleTv;

        public ViewHolder(View view) {
            coverIv = (ImageView) view.findViewById(R.id.cover_iv);
            titleTv = (TextView) view.findViewById(R.id.title_tv);
        }
    }

}
