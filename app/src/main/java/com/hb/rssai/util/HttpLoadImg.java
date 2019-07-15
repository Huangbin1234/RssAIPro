package com.hb.rssai.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hb.rssai.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * TODO : 图片加载器(封装图片加载,在以后需要的时候可以随时换)
 * Created by hb on 2016-05-14.
 */
public class HttpLoadImg {

    /**
     * 兼容 CircleImageView有冲突  去掉動畫 dontAnimate
     */
    public static void loadImgNoAnimate(Activity activity, byte[] bitmap, ImageView imageView) {
//        Glide.with(activity).load(bitmap).apply(new RequestOptions().transform(new CircleCrop()).centerCrop()).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.ALL).
//                priority(Priority.LOW).
//                fallback(R.mipmap.ic_error).
//                dontAnimate().
//                into(imageView);

        Glide.with(activity).load(bitmap).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

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
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
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
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 下载图片转圆形
     */
    public static void loadCircleImg(Context context, Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * ic_error.png
     * 下载图片转圆形带边框2dp
     */
    public static void loadCircleWithBorderImg(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleWithBorderTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleTransformWithBorder(context, 1, context.getResources().getColor(R.color.white))).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * ic_error.png
     * 下载图片转圆形带边框2dp
     */
    public static void loadCircleWithBorderImg(Context context, byte[] bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleTransformWithBorder(context, 2, context.getResources().getColor(R.color.white))).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    /**
     * 下载图片转圆形带边框2dp
     */
//    public static void loadCircleWithBorderImg(Context context, Uri url, ImageView imageView) {
//        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new GlideCircleWithBorderTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//    }

    /**
     * 下载图片转圆角
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).thumbnail(0.1f).apply(new RequestOptions().centerCrop()).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        Glide.with(context).load(url).apply(
                new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10)))
                .error(R.mipmap.ic_error)
                .skipMemoryCache(true)//实践得出更省内存
                .placeholder(R.mipmap.ic_place)
                .disallowHardwareConfig()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);

    }

    public static void loadRoundGifImg(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).thumbnail(0.1f).apply(new RequestOptions().centerCrop()).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        Glide.with(context).load(url).apply(
                new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.DATA))
                .error(R.mipmap.ic_error)
                .skipMemoryCache(true)//实践得出更省内存
                .placeholder(R.mipmap.ic_place)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)// DiskCacheStrategy.NONE
                .into(imageView);

    }

    /**
     * 下载图片转圆角
     */
    public static void loadRoundImg(Context context, Integer url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).transform(new RoundedCorners(10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }

    /**
     * 下载图片转圆角
     */
    public static void loadVideoImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(Uri.fromFile(new File(url))).placeholder(R.mipmap.ic_launcher).into(imageView);
    }

    /**
     * 自动缩放加载图片
     */
    public static void autoImage(Context context, final String imageUrl, final SubsamplingScaleImageView scaleImageView) {
        //使用Glide下载图片,保存到本地
        scaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        scaleImageView.setMinScale(0.0F);
        scaleImageView.setMaxScale(3.0F);//必须设置
        final File downDir = Environment.getExternalStorageDirectory();
        new Thread(() -> {
            Looper.prepare();//增加部分
            try {

                RequestBuilder builder = Glide.with(context).load(imageUrl);
                FutureTarget futureTarget = builder.submit();
//                String t = "" + new Date().getTime(); //200, 200
                Bitmap bitmap = null;
                if (futureTarget.get() instanceof Bitmap) {
                    bitmap = (Bitmap) futureTarget.get();
                } else if (futureTarget.get() instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) futureTarget.get()).getBitmap();
                } else {
                    return;
                }
                FileOutputStream fout = null;
                try {
                    File fileDir = new File(downDir + "/zr/download/");
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File file = new File(downDir, "/zr/download/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //保存图片
                    fout = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                    // 将保存的地址给SubsamplingScaleImageView,这里注意设置ImageViewState
                    scaleImageView.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0.5F, new PointF(0, 0), 0));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fout != null) fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Looper.loop();//增加部分
        }).start();
    }
}
