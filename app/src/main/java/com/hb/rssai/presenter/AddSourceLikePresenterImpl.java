package com.hb.rssai.presenter;

import android.view.View;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.AddSourceLikeContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator
 * on 2019/9/23
 */
public class AddSourceLikePresenterImpl extends BasePresenter<AddSourceLikeContract.View> implements AddSourceLikeContract.Presenter {
    AddSourceLikeContract.View mView;

    public AddSourceLikePresenterImpl(AddSourceLikeContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void getSubscribeLike(String key, int page) {
        findApi.getLikeByName(getSubscribeParams(key, page)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    mView.showSubscribeLike(resFindMore);
                }, mView::showFail);
    }

    @Override
    public void findMoreListById(View v) {

    }

    private Map<String, String> getSubscribeParams(String key, int page) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"name\":\"" + key + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    @Override
    public void start() {

    }
}
