package com.hb.rssai.view.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.hb.generalupdate.InitUpdateInterface;
import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.MainActivity;
import com.hb.update.UpdateManager;
import com.hb.util.SharedPreferencesUtil;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hb.generalupdate.TestTwoActivity.SAVE_ISUPDATE;
import static com.hb.update.UpdateManager.SAVE_VER_CODE;
import static com.hb.update.UpdateManager.SAVE_VER_CONTENT;
import static com.hb.update.UpdateManager.SAVE_VER_UPDATEURL;
import static com.hb.update.UpdateManager.SAVE_VER_VERNAME;

public class LoadActivity extends AppCompatActivity  implements InitUpdateInterface {

    @BindView(R.id.la_text)
    TextView mSampleText;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);
        mContext = this;
        checkUpdate();
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
//            startActivity(new Intent(LoadActivity.this, MainActivity.class));
            startActivity(new Intent(LoadActivity.this, TestNavActivity.class));
            finishAct();
        }
    }
    private void finishAct() {
        finish();
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(1);
                }
            }).start();

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                startActivity(new Intent(LoadActivity.this, MainActivity.class));
                startActivity(new Intent(LoadActivity.this, TestNavActivity.class));
                finishAct();
            }
        }
    };
}
