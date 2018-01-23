package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.ICollectionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        collectionApi.list(iCollectionView.getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollection -> {
                    iCollectionView.setListResult(resCollection);
                }, iCollectionView::loadError);
    }

    public void del() {
        collectionApi.del(iCollectionView.getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iCollectionView.setDelResult(resBase);
                }, iCollectionView::loadError);
    }

    public void getInformation() {
        informationApi.getInformation(iCollectionView.getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    iCollectionView.setInfoResult(resInfo);
                }, iCollectionView::loadError);
    }
}
