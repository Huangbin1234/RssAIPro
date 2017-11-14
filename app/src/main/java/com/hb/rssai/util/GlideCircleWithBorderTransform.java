package com.hb.rssai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.hb.rssai.app.ProjectApplication;


/**
 * 下载图片转圆形 带边框
 * Created by rimi on 2016/6/3.
 */
public class GlideCircleWithBorderTransform extends BitmapTransformation {
    public GlideCircleWithBorderTransform(Context context) {
        super(context);
    }

    /**
     * 带边框
     * @param pool
     * @param source
     * @return
     */
    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float r = size / 2f;
        //线宽3dp
        int  mBorder=DisplayUtil.dip2px(  ProjectApplication.getApplication(),2);
        //半径须使用此算法 否则会显示不全
        float m = (size - mBorder * 2 - 4) / 2f;
        Paint mBorderPaint = new Paint();
        //消除锯齿千万记住
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorder);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawCircle(r, r, m, paint);
        canvas.drawCircle(r, r, m, mBorderPaint);

        return result;
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}