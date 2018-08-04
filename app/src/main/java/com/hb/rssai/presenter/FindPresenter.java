package com.hb.rssai.presenter;

import android.text.TextUtils;
import android.view.View;

import com.hb.rssai.bean.ResSubscription;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IFindView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class FindPresenter extends BasePresenter<IFindView> {
    private IFindView iFindView;

    public FindPresenter(IFindView iFindView) {
        this.iFindView = iFindView;
    }

    public void findMoreList() {
        if (iFindView != null) {
            findApi.findMoreList(getFindMoreParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> iFindView.setFindMoreResult(resFindMore), this::loadFindError);
        }
    }

    public void recommendList() {
        if (iFindView != null) {
            findApi.recommendList(getRecommendParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resFindMore -> iFindView.setRecommendResult(resFindMore), this::loadError);
        }
    }

    public void findMoreListById(View v, boolean isRecommend) {
        if (iFindView != null) {
            findApi.findMoreListById(getFindMoreByIdParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resSubscription -> setFindMoreByIdResult(resSubscription, v, isRecommend), this::loadError);
        }
    }

    public void addSubscription(View v, boolean isRecommend) {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iFindView.setAddResult(resBase, v, isRecommend), this::loadError);
    }

    public void delSubscription(View v, boolean isRecommend) {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iFindView.setDelResult(resBase, v, isRecommend), this::loadError);
    }

    public void setFindMoreByIdResult(ResSubscription resSubscription, View v, boolean isRecommend) {
        if (resSubscription.getRetCode() == 0) {
            if (resSubscription.getRetObj().isDeleteFlag()) {
                addSubscription(v, isRecommend);
            } else { //如果发现没有被删除
                if (TextUtils.isEmpty(resSubscription.getRetObj().getUserId())) {//如果也没有被添加过
                    addSubscription(v, isRecommend);
                } else {//如果被添加过
                    String userId = iFindView.getUserID();
                    if (userId.equals(resSubscription.getRetObj().getUserId())) {//如果是等于当前登录ID
                        delSubscription(v, isRecommend);
                    } else {//不等于
                        addSubscription(v, isRecommend);
                    }
                }
            }
        } else if (resSubscription.getRetCode() == 10013) {
            //从来没订阅过
            addSubscription(v, isRecommend);
        } else {
            iFindView.showToast(resSubscription.getRetMsg());
        }
    }


    private void loadFindError(Throwable throwable) {
        throwable.printStackTrace();
        iFindView.showFindError();

    }

    private void loadError(Throwable throwable) {
        iFindView.showLoadError();
        throwable.printStackTrace();
    }


    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iFindView.getUserID();
        String subscribeId = iFindView.getRowsBeanId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getFindMoreParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + iFindView.getPage() + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getFindMoreByIdParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iFindView.getRowsBeanId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getRecommendParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + iFindView.getRecommendPage() + "\",\"size\":\"" + Constant.RECOMMEND_PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iFindView.getRowsBeanId();
        String userId = iFindView.getUserID();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
