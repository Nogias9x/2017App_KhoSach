package com.example.n50.s1212491_khosach.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

import com.example.n50.s1212491_khosach.R;
import com.squareup.picasso.Picasso;

public class Utils {
//    public static void downloadImage(ImageView imageView, String url) {
//        new DownloadImageSimpleTask(imageView, url).execute();
//    }

//    private static class DownloadImageSimpleTask extends AsyncTask<Void, Void, Bitmap> {
//        private ImageView mImageView;
//        private String mUrl;
//
//        public DownloadImageSimpleTask(ImageView imageView, String url) {
//            mImageView = imageView;
//            mUrl = url;
//            mImageView.setTag(url);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mImageView.setImageResource(R.drawable.loading);
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... voids) {
//            Bitmap bitmap = null;
//            try {
//                InputStream in = new URL(mUrl).openStream();
//                bitmap = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            if (!mImageView.getTag().toString().equals(mUrl)) {
//               /* The path is not same. This means that this
//                  image view is handled by some other async task.
//                  We don't do anything and return. */
//                return;
//            }
//            if (result != null && mImageView != null) {
//                mImageView.setImageBitmap(result);
//            } else {
//                //
//            }
//        }
//    }

//    public static void loadImageFromUrl(Context context, ImageView imageView, String url) {
//        if (url.isEmpty()) {
//            imageView.setImageResource(R.drawable.noimage);
//            return;
//        }
//        Picasso.with(context)
//                .load(url)
//                .placeholder(R.drawable.loading)
//                .error(R.drawable.noimage)
//                .into(imageView);
//    }
}
