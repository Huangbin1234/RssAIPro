package com.hb.rssai.presenter;

import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.RichTextContract;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RichTextPresenter extends BasePresenter<RichTextContract.View> implements RichTextContract.Presenter {

    private RichTextContract.View mView;

    public RichTextPresenter(RichTextContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void updateCount(String informationId) {
        informationApi.updateCount(getInfoParams(informationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mView.showUpdateResult(resBase), mView::loadError);
    }

    @Override
    public void getLikeByTitle(String title) {
        informationApi.getLikeByTitle(getParams(title)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> mView.showListResult(resInfo), mView::loadError);
    }

    @Override
    public void add(String newTitle, String newLink, String informationId, ResCollectionBean.RetObjBean mRetObjBean, String userId) {
        collectionApi.add(getAddParams(newTitle, newLink, informationId, mRetObjBean, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> mView.showAddResult(resShareCollection), mView::loadError);
    }

    @Override
    public void updateEvaluateCount(String informationId, String evaluateType, Evaluate evaluate) {
        informationApi.updateEvaluateCount(getUpdateEvaluateParams(informationId, evaluateType, evaluate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mView.showUpdateEvaluateResult(resBase), mView::loadEvaluateError);
    }

    @Override
    public void getInformation(String informationId) {
        informationApi.getInformation(getInfoParams(informationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> mView.showInfoResult(resInfo), mView::loadError);
    }

    @Override
    public void getCollectionByInfoId(String informationId, String userId) {
        collectionApi.getCollectionByInfoId(getCollectionByInfoIdParams(informationId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollectionBean -> mView.showCollectionInfoIdResult(resCollectionBean), mView::loadError);
    }

    public Map<String, String> getParams(String title) {
        Map<String, String> map = new HashMap<>();
        String des = "";
        String jsonParams = "{\"title\":\"" + title + "\",\"content\":\"" + des + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getAddParams(String newTitle, String newLink, String informationId, ResCollectionBean.RetObjBean mRetObjBean, String userId) {
        Map<String, String> map = new HashMap<>();
        boolean isDel;
        if (mRetObjBean == null) {
            isDel = false;
        } else {
            isDel = !mRetObjBean.isDeleteFlag();
        }
        String jsonParams = "{\"isDel\":\"" + isDel + "\",\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\",\"link\":\"" + newLink + "\",\"title\":\"" + newTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getUpdateEvaluateParams(String informationId, String evaluateType, Evaluate evaluate) {
        Map<String, String> map = new HashMap<>();
        String isOpr = "";
        if (evaluate != null) {
            if ("1".equals(evaluateType)) {
                isOpr = evaluate.getClickGood();
            } else if ("0".equals(evaluateType)) {
                isOpr = evaluate.getClickNotGood();
            }
        }
        //为“”执行+1
        //为1 执行 -1
        //为2 执行 +1
        if ("1".equals(isOpr)) {
            isOpr = "2";
        } else if ("2".equals(isOpr)) {
            isOpr = "1";
        } else {
            isOpr = "";
        }
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"isOpr\":\"" + isOpr + "\",\"evaluateType\":\"" + evaluateType + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getInfoParams(String informationId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getCollectionByInfoIdParams(String informationId, String userId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    @Override
    public void start() {

    }
}
