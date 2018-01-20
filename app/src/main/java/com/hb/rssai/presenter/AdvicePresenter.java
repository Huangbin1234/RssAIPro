package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IAdviceView;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class AdvicePresenter extends BasePresenter<IAdviceView> {
    private IAdviceView iAdviceView;

    public AdvicePresenter(IAdviceView iAdviceView) {
        this.iAdviceView = iAdviceView;
    }

    public void add() {
        Map<String, String> params = iAdviceView.getParams();
        if (params == null) {
            return;
        }
        adviceApi.doAdd(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iAdviceView.setAddResult(resBase);
                }, iAdviceView::loadError);
    }
}
