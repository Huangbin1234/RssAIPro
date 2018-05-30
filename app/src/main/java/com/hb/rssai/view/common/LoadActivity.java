package com.hb.rssai.view.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.generalupdate.InitUpdateInterface;
import com.hb.rssai.R;
import com.hb.rssai.api.ApiFactory;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.bean.ResAdvertisement;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.T;
import com.hb.rssai.view.IndexNavActivity;
import com.hb.update.UpdateManager;
import com.hb.util.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.generalupdate.TestTwoActivity.SAVE_ISUPDATE;
import static com.hb.update.UpdateManager.SAVE_VER_CODE;
import static com.hb.update.UpdateManager.SAVE_VER_CONTENT;
import static com.hb.update.UpdateManager.SAVE_VER_UPDATEURL;
import static com.hb.update.UpdateManager.SAVE_VER_VERNAME;

public class LoadActivity extends AppCompatActivity implements InitUpdateInterface {

    @BindView(R.id.la_text)
    TextView mSampleText;
    @BindView(R.id.load_ad_iv)
    ImageView mLoadAdIv;
    @BindView(R.id.la_iv)
    ImageView mLaIv;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);
        mContext = this;
        getAd();
    }


    @Override
    public void checkUpdate() {
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            CheckVerRunnable checkRunnable = new CheckVerRunnable();
            Thread checkThread = new Thread(checkRunnable);
            checkThread.start();
        } else {
            Log.d("GeneralUpdateLib", "Network connection failed, pleaseheck the network.");
            T.ShowToast(mContext, Constant.FAILED_NETWORK);
            //弹出更新对话框
            startActivity(new Intent(LoadActivity.this, IndexNavActivity.class));
            finish();
        }
    }

    /**
     * request server checkvercode.json file.
     */
    class CheckVerRunnable implements Runnable {
        @Override
        public void run() {
            // replace your .json file url.
            boolean isUpdate = UpdateManager.getUpdateInfo(mContext, ApiRetrofit.JSON_URL);
            if (isUpdate) {
                updateHandler.sendEmptyMessage(1);
            } else {
                updateHandler.sendEmptyMessage(0);
            }
        }
    }

    /**
     * handler rec.
     */
    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // To do something.
                    SharedPreferencesUtil.setBoolean(mContext, SAVE_ISUPDATE, false);//置为更新标记false
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    break;
                case 1:
                    // Find new version.
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_UPDATEURL, UpdateManager.getVerUpdateURL());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_VERNAME, UpdateManager.getVerVerName());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_CODE, UpdateManager.getVerVerCode());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_CONTENT, UpdateManager.getVerContent());
                    SharedPreferencesUtil.setBoolean(mContext, SAVE_ISUPDATE, true);//置为更新标记true
                    break;
                default:
                    break;
            }
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(1);
            }).start();

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                startActivity(new Intent(LoadActivity.this, IndexNavActivity.class));
                finish();
            }
        }
    };

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"type\":\"1\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void getAd() {
        Observable<ResAdvertisement> retrofitService = ApiFactory.getAdvertisementApiSingleton().getAdvertisement(getParams());
        retrofitService.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resAdvertisement -> {
                    reqResult(resAdvertisement);
                }, this::loadError);
    }

    private void reqResult(ResAdvertisement resAdvertisement) {
        if (resAdvertisement.getRetCode() == 0) {
            HttpLoadImg.loadImg(this, resAdvertisement.getRetObj().getImg(), mLoadAdIv);
            if (null != resAdvertisement.getRetObj() && null != resAdvertisement.getRetObj().getLink()) {
                if (resAdvertisement.getRetObj().getLink().startsWith("alipays")) {
                    SharedPreferencesUtil.setString(this, Constant.AlipaysUrl, resAdvertisement.getRetObj().getLink());
                }
                mLoadAdIv.setOnClickListener(v -> {
                    String alipayUrl = SharedPreferencesUtil.getString(this, Constant.AlipaysUrl, "");
                    if (alipayUrl.startsWith("alipays")) {
                        try {
                            //利用Intent打开支付宝
                            Uri uri = Uri.parse(resAdvertisement.getRetObj().getLink());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (Exception e) {
                            //若无法正常跳转，在此进行错误处理
                            T.ShowToast(mContext, "无法跳转到支付宝领红包，请检查您是否安装了支付宝！");
                        }
                    } else {
                        Intent intent = new Intent(this, ContentActivity.class);
                        intent.putExtra(ContentActivity.KEY_URL, resAdvertisement.getRetObj().getLink());
                        intent.putExtra(ContentActivity.KEY_TITLE, resAdvertisement.getRetObj().getTitle());
                        intent.putExtra(ContentActivity.KEY_INFORMATION_ID, resAdvertisement.getRetObj().getId());
                        startActivity(intent);
                    }
                });
            }

        } else {
            System.out.println("暂无广告");
        }
        checkUpdate();
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        checkUpdate();
    }
}
