package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IAdviceView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class AdvicePresenter extends BasePresenter<IAdviceView> {
    private IAdviceView iAdviceView;

    public AdvicePresenter(IAdviceView iAdviceView) {
        this.iAdviceView = iAdviceView;
    }

    private String checkContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return "请输入意见内容";
        }
        return null;
    }

    public void list() {
        adviceApi.doList(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resAdviceList -> iAdviceView.showList(resAdviceList), iAdviceView::loadError);
    }

    public void add() {
        String error;
        String content = iAdviceView.getEtContent();
        if ((error = checkContent(content)) != null) {
            iAdviceView.setCheckResult(error);
            return;
        }
        adviceApi.doAdd(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iAdviceView.setAddResult(resBase), iAdviceView::loadError);
    }

    public Map<String, String> getParams() {
        String content = iAdviceView.getEtContent();
        String typeName = iAdviceView.getEtTypeName();
        String type = iAdviceView.getEtType();
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"content\":\"" + content + "\",\"typeName\":\" " + typeName + "\",\"type\":\"" + type + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }

    public Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        int pagNum = iAdviceView.getPageNum();
        String jsonParams = "{\"page\":\"" + pagNum + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
