package com.hb.rssai.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hb.rssai.R;

import java.io.File;


/**
 * TODO : 图片加载器(封装图片加载,在以后需要的时候可以随时换)
 * Created by hb on 2016-05-14.
 */
public class HttpLoadImg {
    /**
     * TODO: 加载图片,
     *
     * @param activity  传acitivity是为了在 onstop方法的时候停止加载, 在onresume方法中继续加载
     * @param url
     * @param imageView
     */
    public static void loadImg(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * TODO: 加载图片,
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载本地图片资源ID
     */
    public static void loadImg(Context context, Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载本地图片资源ID
     */
    public static void loadImg(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载本地图片资源ID
     */
    public static void loadImgNoCache(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    /**
     * 下载图片转圆形
     */
    public static void loadCircleImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    /**
     * 下载图片转圆形
     */
    public static void loadCircleImg(Context context, Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    /**ic_error.png
     * 下载图片转圆形带边框2dp
     */
    public static void loadCircleWithBorderImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleWithBorderTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    /**ic_error.png
     * 下载图片转圆形带边框2dp
     */
    public static void loadCircleWithBorderImg(Context context, Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleWithBorderTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    /**
     * 下载图片转圆形带边框2dp
     */
    public static void loadCircleWithBorderImg(Context context, Uri url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleWithBorderTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 下载图片转圆角
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideRoundTransform(context, 10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }
    /**
     * 下载图片转圆角
     */
    public static void loadRoundImg(Context context, Integer url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideRoundTransform(context, 10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }
    /**
     * 下载图片转圆角
     */
    public static void loadVideoImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(Uri.fromFile(new File(url))).placeholder(R.mipmap.ic_launcher).into(imageView);

    }

}
