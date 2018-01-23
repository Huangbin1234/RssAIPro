package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IRecordView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class RecordPresenter extends BasePresenter<IRecordView> {
    private IRecordView mIRecordView;

    public RecordPresenter(IRecordView IRecordView) {
        mIRecordView = IRecordView;
    }

    public void getList() {
        informationApi.findUserInformation(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUserInformation -> {
                    mIRecordView.setListResult(resUserInformation);
                }, mIRecordView::loadError);
    }

    public void getInformation() {
        informationApi.getInformation(getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    mIRecordView.setInfoResult(resInfo);
                }, mIRecordView::loadError);
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = mIRecordView.getUserId();
        int pageNum = mIRecordView.getPageNum();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + pageNum + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    public Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = mIRecordView.getInfoId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

}
