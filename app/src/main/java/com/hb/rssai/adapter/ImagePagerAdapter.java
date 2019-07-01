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
    //    private File downDir = Environment.getExternalStorageDirectory();
    private int p = 0;

    public ImagePagerAdapter(Context mContext, List<String> rows, int pos) {
        this.mContext = mContext;
        this.rows = rows;
        this.p = pos;
    }


    @Override
    public int getCount() {
        return rows != null ? rows.size() : 0;
    }

    SubsamplingScaleImageView imageView;


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        imageView = new SubsamplingScaleImageView(mContext);
        autoImage(mContext, rows.get(position), imageView, position);
        container.addView(imageView);
        return imageView;
    }


    /**
     * 自动缩放加载图片
     */
    public void autoImage(Context context, final String imageUrl, final SubsamplingScaleImageView scaleImageView, int pos) {
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
                Log.i("wechat", "压缩后图片的大小" + (bm.getByteCount() / 1024 / 1024)
                        + "M宽度为" + bm.getWidth() + "高度为" + bm.getHeight()
                        + "bytes.length=  " + (bytes.length / 1024) + "KB"
                        + "quality=" + quality);
                imageView.setImage(ImageSource.bitmap(bm), new ImageViewState(0.5F, new PointF(0, 0), 0));
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((SubsamplingScaleImageView) object);
    }

//    private static ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(4);

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                final String imgUrl = msg.obj.toString();
////                singleThreadExecutor.execute(() -> {
////                    try {
//
//                Glide.with(mContext).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        int quality = 80;
//                        resource.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//                        byte[] bytes = baos.toByteArray();
//                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        Log.i("wechat", "压缩后图片的大小" + (bm.getByteCount() / 1024 / 1024)
//                                + "M宽度为" + bm.getWidth() + "高度为" + bm.getHeight()
//                                + "bytes.length=  " + (bytes.length / 1024) + "KB"
//                                + "quality=" + quality);
//
//
//                        imageView.setImage(ImageSource.bitmap(bm), new ImageViewState(0.5F, new PointF(0, 0), 0));
//
//                    }
//                });


//                        FileOutputStream fout = null;
//                        RequestBuilder builder = Glide.with(mContext).load(imgUrl);
//                        FutureTarget futureTarget = builder.submit();
//                        Bitmap bitmap = null;
//                        if (futureTarget.get() instanceof Bitmap) {
//                            bitmap = (Bitmap) futureTarget.get();
//                        } else if (futureTarget.get() instanceof BitmapDrawable) {
//                            bitmap = ((BitmapDrawable) futureTarget.get()).getBitmap();
//                        } else {
//                            return;
//                        }
//                        File fileDir = new File(downDir + "/zr/download/");
//                        if (!fileDir.exists()) {
//                            fileDir.mkdirs();
//                        }
//                        File file = new File(downDir, "/zr/download/" + imgUrl.substring(imgUrl.lastIndexOf("/") + 1));
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        //保存图片
//                        fout = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
//                        Message message = new Message();
//                        message.what = 0;
//                        message.arg2 = msg.arg2;
//                        message.obj = file.getAbsolutePath();
//                        upHandler.sendMessage(message);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//    };

//    Handler upHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0) {
//                if (null != msg.obj) {
////                    imageView.setImage(ImageSource.uri(msg.obj.toString()), new ImageViewState(0.5F, new PointF(0, 0), 0));
//                    imageView.setImage(ImageSource.bitmap((Bitmap) msg.obj), new ImageViewState(0.5F, new PointF(0, 0), 0));
//                }
//            }
//        }
//    };

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
