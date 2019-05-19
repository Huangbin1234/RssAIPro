package com.hb.rssai.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hb.rssai.R;

/**
 * 重写图片加载接口
 */
public class HtmlImageGetterNew implements Html.ImageGetter , Drawable.Callback {

    private Context mContext;
    private Activity mAct;
    private TextView tv;
    private long lastInvalidateTime;


    public HtmlImageGetterNew(Context mContext, Activity mAct, TextView tv) {
        this.mContext = mContext;
        this.mAct = mAct;
        this.tv = tv;
    }

    /**
     * 获取图片
     */
    @Override
    public Drawable getDrawable(String source) {
        final LevelListDrawable[] lld = {new LevelListDrawable()};
        Drawable empty = mContext.getResources().getDrawable(R.drawable.material_card);
        lld[0].addLevel(0, 0, empty);
        lld[0].setBounds(0, 0, DisplayUtil.getMobileWidth(mAct), empty.getIntrinsicHeight());
        Glide.with(mAct).load(source).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d(HtmlImageGetterNew.class.getSimpleName(), e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
                    ((GifDrawable) resource).start();
                }
//                resource.invalidateSelf();

                if (resource != null) {
                    lld[0].addLevel(1, 1, resource);
                    lld[0] = getDrawableAdapter(mAct, lld[0], resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                    lld[0].setLevel(1);
                    CharSequence t = tv.getText();
                    tv.setText(t);
                    tv.invalidate();
                }
                return false;
            }
        }).submit();
        return lld[0];
    }





    public LevelListDrawable getDrawableAdapter(Activity activity, LevelListDrawable drawable, int oldWidth, int oldHeight) {
        LevelListDrawable newDrawable = drawable;
        long newHeight = 0;// 未知数
        int newWidth = DisplayUtil.getMobileWidth(activity) - DisplayUtil.dip2px(activity, 20);// 默认屏幕宽
        newHeight = (newWidth * oldHeight) / oldWidth;
        newDrawable.setBounds(0, 0, newWidth, (int) newHeight);
        return newDrawable;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        System.out.println("=================>invalidateDrawable:");
        if (System.currentTimeMillis() - lastInvalidateTime > 40) {
            lastInvalidateTime = System.currentTimeMillis();
            tv.invalidate();
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long l) {
        System.out.println("=================>scheduleDrawable:");
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
        System.out.println("=================>unscheduleDrawable:");
    }
}
