package com.hb.rssai.presenter;

import android.content.Context;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAddRssView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class AddRssPresenter extends BasePresenter<IAddRssView> {
    private Context mContext;
    private IAddRssView iAddRssView;

    public AddRssPresenter(Context context, IAddRssView iAddRssView) {
        mContext = context;
        this.iAddRssView = iAddRssView;
    }

    public void addRss() {
        findApi.addRssByUser(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase);
                }, this::loadError);
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String rssLink = iAddRssView.getRssLink();
        String rssTitle = iAddRssView.getRssTitle();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"link\":\"" + rssLink + "\",\"title\":\"" + rssTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setAddResult(ResBase resBase) {
        T.ShowToast(mContext, resBase.getRetMsg());
    }
}
