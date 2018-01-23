package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IEditSignatureView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class EditSignaturePresenter extends BasePresenter<IEditSignatureView> {
    private IEditSignatureView mIEditSignatureView;

    public EditSignaturePresenter(IEditSignatureView IEditSignatureView) {
        mIEditSignatureView = IEditSignatureView;
    }

    public void updateUserInfo() {
        loginApi.update(mIEditSignatureView.getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mIEditSignatureView.setUpdateResult(resBase);
                }, mIEditSignatureView::loadError);
    }

}
