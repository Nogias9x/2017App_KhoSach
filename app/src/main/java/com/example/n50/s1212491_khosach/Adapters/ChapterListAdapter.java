package com.example.n50.s1212491_khosach.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.n50.s1212491_khosach.Common.Chapter;
import com.example.n50.s1212491_khosach.R;

import java.util.List;

public class ChapterListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Chapter> mList;

    public ChapterListAdapter(Context context, List<Chapter> list) {
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
            convertView = inflater.inflate(R.layout.row_list_chapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Chapter chapter = mList.get(position);
        if (chapter != null) {
            viewHolder.titleTv.setText(chapter.getChapterName());
            viewHolder.hintTv.setText(chapter.getShortDescription());
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    static class ViewHolder {
        private TextView titleTv;
        private TextView hintTv;

        public ViewHolder(View view) {

            titleTv = (TextView) view.findViewById(R.id.chapterTitle_tv);
            hintTv = (TextView) view.findViewById(R.id.chapterHint_tv);
        }
    }
}
