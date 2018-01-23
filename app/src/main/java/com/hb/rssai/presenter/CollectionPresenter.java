package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.ICollectionView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */

public class CollectionPresenter extends BasePresenter<ICollectionView> {
    private ICollectionView iCollectionView;

    public CollectionPresenter(ICollectionView iCollectionView) {
        this.iCollectionView = iCollectionView;
    }

    public void getList() {
        collectionApi.list(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollection -> iCollectionView.setListResult(resCollection), iCollectionView::loadError);
    }

    public void del() {
        collectionApi.del(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iCollectionView.setDelResult(resBase), iCollectionView::loadError);
    }

    public void getInformation() {
        informationApi.getInformation(getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> iCollectionView.setInfoResult(resInfo), iCollectionView::loadError);
    }


    public Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iCollectionView.getUserId();
        int pagNum = iCollectionView.getPageNum();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"page\":\"" + pagNum + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    public Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iCollectionView.getUserId();
        String id = iCollectionView.getCollectionId();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"id\":\"" + id + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    public Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iCollectionView.getInfoId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
