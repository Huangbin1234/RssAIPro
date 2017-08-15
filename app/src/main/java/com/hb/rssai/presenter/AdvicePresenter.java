package com.hb.rssai.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAdviceView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class AdvicePresenter extends BasePresenter<IAdviceView> {
    private Context mContext;
    private IAdviceView iAdviceView;
    private EditText etContent;

    public AdvicePresenter(Context context, IAdviceView iAdviceView) {
        this.mContext = context;
        this.iAdviceView = iAdviceView;
    }

    public void add() {
        if (iAdviceView != null) {
            etContent = iAdviceView.getEtContent();
            String content = etContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                T.ShowToast(mContext, "请输入意见内容");
                return;
            }

            Map<String, String> params = new HashMap<>();
            String jsonParams = "{\"content\":\"" + content + "\"}";
            params.put("jsonParams", jsonParams);
            adviceApi.doAdd(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resBase -> {
                        setAddResult(resBase);
                    }, this::loadError);
        }
    }

    private void setAddResult(ResBase resBase) {
        T.ShowToast(mContext, resBase.getRetMsg());
        iAdviceView.toFinish();
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
