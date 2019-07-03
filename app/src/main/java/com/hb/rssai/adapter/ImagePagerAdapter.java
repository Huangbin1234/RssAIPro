package com.hb.rssai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> rows;

    public ImagePagerAdapter(Context mContext, List<String> rows) {
        this.mContext = mContext;
        this.rows = rows;
    }


    @Override
    public int getCount() {
        return rows != null ? rows.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(mContext);
        autoImage(mContext, rows.get(position), imageView);
        container.addView(imageView);
        return imageView;
    }


    /**
     * 自动缩放加载图片
     */
    public void autoImage(Context context, final String imageUrl, final SubsamplingScaleImageView scaleImageView) {
        //使用Glide下载图片,保存到本地
        scaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        scaleImageView.setMinScale(0.0F);
        scaleImageView.setMaxScale(3.0F);//必须设置

        Glide.with(context).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int quality = 80;
                resource.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                byte[] bytes = baos.toByteArray();
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                scaleImageView.setImage(ImageSource.bitmap(bm), new ImageViewState(0.5F, new PointF(0, 0), 0));
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((SubsamplingScaleImageView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
