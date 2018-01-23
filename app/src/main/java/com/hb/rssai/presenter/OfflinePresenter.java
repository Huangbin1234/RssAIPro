package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IOfficeView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public class OfflinePresenter extends BasePresenter<IOfficeView> {

    private IOfficeView iOfficeView;

    public OfflinePresenter(IOfficeView iOfficeView) {
        this.iOfficeView = iOfficeView;
    }

    public void getChannels() {
        dataGroupApi.getDataGroupList(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    iOfficeView.setDataGroupResult(resDataGroup);
                }, iOfficeView::loadError);
    }

    public Map<String, String> getParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
