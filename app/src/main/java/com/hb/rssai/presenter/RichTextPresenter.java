package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IRichTextView;

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
        informationApi.updateCount(iRichTextView.getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iRichTextView.setUpdateResult(resBase);
                }, iRichTextView::loadError);
    }

    public void getLikeByTitle() {
        informationApi.getLikeByTitle(iRichTextView.getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    iRichTextView.setListResult(resInfo);
                }, iRichTextView::loadError);
    }

    public void add() {
        collectionApi.add(iRichTextView.getAddParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> {
                    iRichTextView.setAddResult(resShareCollection);
                }, iRichTextView::loadError);
    }

    public void updateEvaluateCount() {
        informationApi.updateEvaluateCount(iRichTextView.getUpdateEvaluateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iRichTextView.setUpdateEvaluateResult(resBase);
                }, iRichTextView::loadEvaluateError);
    }

    public void getInformation() {
        informationApi.getInformation(iRichTextView.getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    iRichTextView.setInfoResult(resInfo);
                }, iRichTextView::loadError);
    }

    public void getCollectionByInfoId() {
        collectionApi.getCollectionByInfoId(iRichTextView.getCollectionByInfoIdParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCollectionBean -> {
                    iRichTextView.setCollectionInfoIdResult(resCollectionBean);
                }, iRichTextView::loadError);
    }

}
