package com.hb.rssai.presenter;

import android.content.Context;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IMineView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class MinePresenter extends BasePresenter<IMineView> {
    private Context mContext;
    private IMineView iMineView;

    public MinePresenter(Context context, IMineView iMineView) {
        mContext = context;
        this.iMineView = iMineView;
    }

    public void getUser() {
        loginApi.getUserInfo(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> {
                    iMineView.setResult(resUser);
                }, this::loadUserError);
    }

    //添加收藏
    public void addCollection() {
        collectionApi.add(getAddParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> {
                    iMineView.setAddResult(resShareCollection);
                }, this::loadError);
    }

    //添加订阅
    public void addSubscription() {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iMineView.setAddSubscribeResult(resBase);
                }, this::loadError);
    }

    //获取消息数
    public void getMessages() {
        messageApi.list(getMessageParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resMessageList -> {
                    iMineView.setMessageListResult(resMessageList);
                }, this::loadError);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                iMineView.showNoLogin();
            } else {
                iMineView.showNetError();
            }
        } else {
            iMineView.showNetError();
        }
    }

    private void loadUserError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            } else {
                iMineView.showNetError();
            }
        } else {
            iMineView.showNetError();
        }
        iMineView.showLoadUserError();
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iMineView.getUserId();
        String jsonParams = "{\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iMineView.getSubscribeId();
        String userId = iMineView.getUserId();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getAddParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iMineView.getInformationId();
        boolean isDel = false;
        String userId = iMineView.getUserId();
        String jsonParams = "{\"isDel\":\"" + isDel + "\",\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getMessageParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iMineView.getUserId();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
