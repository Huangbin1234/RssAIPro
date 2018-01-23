package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IOfficeView;

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
        dataGroupApi.getDataGroupList(iOfficeView.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    iOfficeView.setDataGroupResult(resDataGroup);
                }, iOfficeView::loadError);
    }

}
