package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IEditSignatureView;

import java.util.HashMap;
import java.util.Map;

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

    private String checkEtSignature(String newSignature, String oldSignature) {
        if (TextUtils.isEmpty(newSignature)) {
            return "请输入签名后，再进行保存。";
        }
        if (newSignature.equals(oldSignature)) {
            return "内容没有任何变化，请修改后保存。";
        }
        return null;
    }

    public void updateUserInfo() {
        String error;
        String newSignature = mIEditSignatureView.getNewSignature();
        String oldSignature = mIEditSignatureView.getOldSignature();
        if ((error = checkEtSignature(newSignature, oldSignature)) != null) {
            mIEditSignatureView.setCheckResult(error);
            return;
        }
        loginApi.update(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mIEditSignatureView.setUpdateResult(resBase), mIEditSignatureView::loadError);
    }

    public Map<String, String> getUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String description = mIEditSignatureView.getNewSignature();
        String jsonParams = "{\"description\":\"" + description + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
