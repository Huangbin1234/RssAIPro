package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ContentContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ContentPresenter extends BasePresenter<ContentContract.View> implements ContentContract.Presenter {
    private ContentContract.View mView;


    public ContentPresenter(ContentContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    private Map<String, String> getAddParams(String newTitle, String newLink, String informationId, String userId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\",\"link\":\"" + newLink + "\",\"title\":\"" + newTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getUpdateParams(String informationId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    @Override
    public void updateCount(String informationId) {
        informationApi.updateCount(getUpdateParams(informationId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mView.showUpdateSuccess(resBase);
                }, mView::showUpdateFailed);
    }

    @Override
    public void add(String newTitle, String newLink, String informationId, String userId) {
        collectionApi.add(getAddParams(newTitle, newLink, informationId, userId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> {
                    mView.showAddSuccess(resShareCollection);
                }, mView::showAddFailed);
    }

    @Override
    public void start() {

    }
}
