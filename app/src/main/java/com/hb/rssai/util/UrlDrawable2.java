package com.hb.rssai.util;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.resource.gif.GifDrawable;

/**
 * Created by Jesson_Yi on 2016/6/23.
 */
public class UrlDrawable2 extends Drawable implements Drawable.Callback {
    private GifDrawable mDrawable;

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(cf);
        }
    }

    @Override
    public int getOpacity() {
        if (mDrawable != null) {
            return mDrawable.getOpacity();
        }
        return PixelFormat.UNKNOWN;
    }

    public void setDrawable(GifDrawable drawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        drawable.setCallback(this);
        this.mDrawable = drawable;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(who);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(who, what);
        }
    }
}