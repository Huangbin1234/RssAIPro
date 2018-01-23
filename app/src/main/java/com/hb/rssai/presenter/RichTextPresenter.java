package com.hb.rssai.presenter;

import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IRichTextView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RichTextPresenter extends BasePresenter<IRichTextView> {

    private IRichTextView iRichTextView;

    public RichTextPresenter(IRichTextView iRichTextView) {
        this.iRichTextView = iRichTextView;
    }

    public void updateCount() {
        informationApi.updateCount(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iRichTextView.setUpdateResult(resBase), iRichTextView::loadError);
    }

    public void getLikeByTitle() {
        informationApi.getLikeByTitle(getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> iRichTextView.setListResult(resInfo), iRichTextView::loadError);
    }

    public void add() {
        collectionApi.add(getAddParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> iRichTextView.setAddResult(resShareCollection), iRichTextView::loadError);
    }

    public void updateEvaluateCount() {
        informationApi.updateEvaluateCount(getUpdateEvaluateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iRichTextView.setUpdateEvaluateResult(resBase), iRichTextView::loadEvaluateError);
    }

    public void getInformation() {
        informationApi.getInformation(getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> iRichTextView.setInfoResult(resInfo), iRichTextView::loadError);
    }

    public void getCollectionByInfoId() {
        collectionApi.getCollectionByInfoId(getCollectionByInfoIdParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollectionBean -> iRichTextView.setCollectionInfoIdResult(resCollectionBean), iRichTextView::loadError);
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String des = "";
        String title = iRichTextView.getInfoTitle();
        String jsonParams = "{\"title\":\"" + title + "\",\"content\":\"" + des + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getAddParams() {
        Map<String, String> map = new HashMap<>();
        String newLink = iRichTextView.getUrl();
        String newTitle = iRichTextView.getInfoTitle();
        String informationId = iRichTextView.getInfoId();
        ResCollectionBean.RetObjBean mRetObjBean = iRichTextView.getRetObjBean();
        boolean isDel;
        if (mRetObjBean == null) {
            isDel = false;
        } else {
            if (mRetObjBean.isDeleteFlag()) {
                isDel = false;
            } else {
                isDel = true;
            }
        }
        String userId = iRichTextView.getUserId();
        String jsonParams = "{\"isDel\":\"" + isDel + "\",\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\",\"link\":\"" + newLink + "\",\"title\":\"" + newTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getUpdateEvaluateParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInfoId();
        String evaluateType = iRichTextView.getEvaluateType();
        Evaluate evaluate = iRichTextView.getEvaluate();
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

    public Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInfoId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getCollectionByInfoIdParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInfoId();
        String userId = iRichTextView.getUserId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getUpdateParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInfoId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
