package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IMessageView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18.
 */

public class MessagePresenter extends BasePresenter<IMessageView> {
    private IMessageView iMessageView;

    public MessagePresenter(IMessageView iMessageView) {
        this.iMessageView = iMessageView;
    }

    public void getList() {
        messageApi.list(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resMessageList -> iMessageView.setListResult(resMessageList), iMessageView::loadError);
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iMessageView.getUserId();
        int pagNum = iMessageView.getPageNum();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + pagNum + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

}
