package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ModifySubscriptionContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator
 * on 2019/5/5
 */
public class ModifySubscriptionPresenter extends BasePresenter<ModifySubscriptionContract.View> implements ModifySubscriptionContract.Presenter {
    ModifySubscriptionContract.View mView;

    public ModifySubscriptionPresenter(ModifySubscriptionContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    public void getDataGroupList() {
        dataGroupApi.getDataGroupList(getDataGroupListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    mView.setDataGroupResult(resDataGroup);
                }, mView::loadError);
    }


    @Override
    public void modifySubscription(Map<String, Object> params) {
        findApi.modifySubscription(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mView.showModifyResult(resBase), mView::loadError);
    }

    private HashMap<String, String> getDataGroupListParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }


    @Override
    public void start() {
        getDataGroupList();
    }
}
