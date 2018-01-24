package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.ISubListView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/17 0017.
 */

public class SubListPresenter extends BasePresenter<ISubListView> {
    private ISubListView mISubListView;

    public SubListPresenter(ISubListView ISubListView) {
        mISubListView = ISubListView;
    }


    public void updateUsSort() {
        findApi.updateUsSort(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mISubListView.setUpdateUsSortResult(resBase);
                }, mISubListView::loadError);
    }


    public void getUserSubscribeList() {
        findApi.userSubscribeList(getUserSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resFindMore -> {
                    mISubListView.setUserSubscribeResult(resFindMore);
                }, mISubListView::loadError);
    }

    public void delSubscription() {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mISubListView.setDelResult(resBase);
                }, mISubListView::loadError);
    }

    private Map<String, String> getUpdateParams() {
        Map<String, String> map = new HashMap<>();
        String usId = mISubListView.getClickBean().getUsId();
        String jsonParams = "{\"usId\":\"" + usId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getUserSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String userId = mISubListView.getUserId();
        Object isTag = mISubListView.getIsTag();
        int page = mISubListView.getPageNum();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"isTag\":\"" + isTag + "\",\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = mISubListView.getUserId();
        String subscribeId = mISubListView.getClickBean().getId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

}
