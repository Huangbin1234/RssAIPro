package com.hb.rssai.presenter;

import android.text.TextUtils;

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
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"content\":\"" + content + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }
}
