package com.hb.rssai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hb.rssai.R;

import java.util.HashSet;
import java.util.Set;

public class UrlImageGetter implements Html.ImageGetter, Drawable.Callback {

    Context mContext;
    TextView mTextView;
    int mWidth;
    private final Set<GifViewTarget> mTargets;

    public static UrlImageGetter get(View view) {
        return (UrlImageGetter) view.getTag(R.id.drawable_callback_tag);
    }

    public UrlImageGetter(TextView t, Context c) {
        this.mContext = c;
        this.mTextView = t;
        mWidth = c.getResources().getDisplayMetrics().widthPixels;
        mTargets = new HashSet<>();
        mTextView.setTag(R.id.drawable_callback_tag, this);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

    }

    private class GifViewTarget extends SimpleTarget<GifDrawable> {

        private final UrlDrawable2 mDrawable;

        private GifViewTarget(UrlDrawable2 drawable) {
            mTargets.add(this);
            this.mDrawable = drawable;
        }

        private Request request;

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
            Rect rect;
            if (resource.getIntrinsicWidth() > 100) {
                float width;
                float height;
                Log.e("koen", "resource width is " + resource.getIntrinsicWidth());
                Log.e("koen", "mTextView width is " + mWidth);
                if (resource.getIntrinsicWidth() >= mWidth) {
                    float downScale = (float) resource.getIntrinsicWidth() / mWidth;
                    width = (float) resource.getIntrinsicWidth() / downScale;
                    height = (float) resource.getIntrinsicHeight() / downScale;
                } else {
                    float multiplier = (float) mWidth / resource.getIntrinsicWidth();
                    width = (float) resource.getIntrinsicWidth() * multiplier;
                    height = (float) resource.getIntrinsicHeight() * multiplier;
                }
                Log.e("koen", "Final view width is " + width);
                rect = new Rect(0, 0, Math.round(width), Math.round(height));
            } else {
                Log.e("koen", "else view width");
                rect = new Rect(0, 0, resource.getIntrinsicWidth() * 2, resource.getIntrinsicHeight() * 2);
            }
            resource.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);
            //if (resource.isRunning()) {
            mDrawable.setCallback(get(mTextView));
            resource.setLoopCount(GifDrawable.LOOP_FOREVER);
            resource.start();
            // }
            mTextView.setText(mTextView.getText());
            mTextView.invalidate();
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }

    private boolean isGif(String path) {
        int index = path.lastIndexOf('.');
        return index > 0 && "gif".toUpperCase().equals(path.substring(index + 1).toUpperCase());
    }

    @Override
    public Drawable getDrawable(String source) {

        if (isGif(source)) {
            final UrlDrawable2 urlDrawable2 = new UrlDrawable2();
            Glide.with(mContext).asGif().load(source).into(new GifViewTarget(urlDrawable2));
            return urlDrawable2;
        } else {
            final UrlDrawable urlDrawable = new UrlDrawable();
            Glide.with(mContext).asBitmap().load(source).into(new BitmapTartget(urlDrawable));
            return urlDrawable;
        }
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }

    private class BitmapTartget extends SimpleTarget<Bitmap> {

        private final UrlDrawable urlDrawable;

        public BitmapTartget(UrlDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            float scaleWidth = ((float) mWidth) / resource.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            resource.setConfig(Bitmap.Config.ARGB_4444);
            resource = Bitmap.createBitmap(resource, 0, 0,
                    resource.getWidth(), resource.getHeight(),
                    matrix, true);
            urlDrawable.bitmap = resource;
            urlDrawable.setBounds(0, 0, resource.getWidth(),
                    resource.getHeight());
            mTextView.invalidate();
            mTextView.setText(mTextView.getText());
        }
    }

}