package com.hb.rssai.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IMineView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class MinePresenter extends BasePresenter<IMineView> {
    private Context mContext;
    private IMineView iMineView;
    private TextView tvReadCount;
    private TextView tvSubscribeCount;
    private TextView tvAccount;
    private ImageView ivAva;
    private TextView tvMessageFlag;

    public MinePresenter(Context context, IMineView iMineView) {
        mContext = context;
        this.iMineView = iMineView;
        initView();
    }

    private void initView() {
        tvReadCount = iMineView.getTvReadCount();
        tvSubscribeCount = iMineView.getTvSubscribeCount();
        tvAccount = iMineView.getTvAccount();
        ivAva = iMineView.getIvAva();
        tvMessageFlag = iMineView.getTvMessageFlag();
    }

    public void getUser() {
        loginApi.getUserInfo(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> {
                    setResult(resUser);
                }, this::loadError);
    }

    public void setUpdate() {
        messageApi.selUpdate(getSelUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setSelUpdateResult(resBase);
                }, this::loadError);
    }

    private void setSelUpdateResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            if (null != resBase.getRetObj() && ("" + resBase.getRetObj()).length() > 4) {
                tvMessageFlag.setVisibility(View.VISIBLE);
            }else {
                tvMessageFlag.setVisibility(View.GONE);
            }
        }
    }

    private Map<String, String> getSelUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String localTime = SharedPreferencesUtil.getString(mContext, Constant.LAST_UPDATE_TIME, Constant.START_TIME);
        String jsonParams = "{\"localTime\":\"" + localTime + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setResult(ResUser user) {
        if (user.getRetCode() == 0) {
            tvReadCount.setText("" + user.getRetObj().getReadCount());
            tvSubscribeCount.setText("" + user.getRetObj().getSubscribeCount());
            tvAccount.setText(user.getRetObj().getNickName());
            HttpLoadImg.loadCircleImg(mContext, user.getRetObj().getAvatar(), ivAva);
        } else {
            T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

}
