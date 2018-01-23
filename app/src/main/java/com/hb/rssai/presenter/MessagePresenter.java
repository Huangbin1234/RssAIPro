package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IMessageView;

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

    //重载父类数据上下拉刷新接口
    @Override
    public void onRefresh(int pageSize, int pageNum) {
        messageApi.list(iMessageView.getParams(pageSize, pageNum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resMessageList -> {
                    iMessageView.setListResult(resMessageList);
                }, iMessageView::loadError);
    }

}
