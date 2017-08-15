package com.hb.rssai.presenter;

import android.content.Context;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IContentView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ContentPresenter extends BasePresenter<IContentView> {
    private Context mContext;
    private IContentView iContentView;


    public ContentPresenter(Context mContext, IContentView iContentView) {
        this.mContext = mContext;
        this.iContentView = iContentView;

    }


    public void add() {
        collectionApi.add(getAddParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setListResult(resBase);
                }, this::loadError);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setListResult(ResBase resBase) {
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private Map<String, String> getAddParams() {
        Map<String, String> map = new HashMap<>();
        String newLink = iContentView.getNewLink();
        String newTitle = iContentView.getNewTitle();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"link\":\"" + newLink + "\",\"title\":\"" + newTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
