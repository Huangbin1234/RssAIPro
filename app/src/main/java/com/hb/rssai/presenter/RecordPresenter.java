package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IRecordView;

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
        informationApi.findUserInformation(mIRecordView.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUserInformation -> {
                    mIRecordView.setListResult(resUserInformation);
                }, mIRecordView::loadError);
    }

    public void getInformation() {
        informationApi.getInformation(mIRecordView.getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    mIRecordView.setInfoResult(resInfo);
                }, mIRecordView::loadError);
    }
}
