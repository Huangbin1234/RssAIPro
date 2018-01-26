package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IModifyPasswordView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public class ModifyPasswordPresenter extends BasePresenter<IModifyPasswordView> {
    private IModifyPasswordView mIModifyPasswordView;

    public ModifyPasswordPresenter(IModifyPasswordView IModifyPasswordView) {
        mIModifyPasswordView = IModifyPasswordView;
    }

    private String checkOldPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "请输入旧密码";
        }
        if (password.length() < 6 || password.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkNewPassword(String newPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            return "请输入新密码";
        }
        if (newPassword.length() < 6 || newPassword.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSurePassword(String sNewPassword) {
        if (TextUtils.isEmpty(sNewPassword)) {
            return "请再次输入新密码";
        }
        if (sNewPassword.length() < 6 || sNewPassword.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSame(String psd, String sPsd) {
        if (!sPsd.equals(psd)) {
            return "两次输入的新密码不一致";
        }
        return null;
    }

    public void modify() {
        String oldPsd = mIModifyPasswordView.getOldPsd();
        String newPsd = mIModifyPasswordView.getNewPsd();
        String sNewPsd = mIModifyPasswordView.getNewSPsd();
        String error;

        if ((error = checkOldPassword(oldPsd)) != null) {
            mIModifyPasswordView.setCheckError(error);
            return;
        }
        if ((error = checkNewPassword(newPsd)) != null) {
            mIModifyPasswordView.setCheckError(error);
            return;
        }
        if ((error = checkSurePassword(sNewPsd)) != null) {
            mIModifyPasswordView.setCheckError(error);
            return;
        }
        if ((error = checkSame(newPsd, sNewPsd)) != null) {
            mIModifyPasswordView.setCheckError(error);
            return;
        }
        loginApi.modifyPassword(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mIModifyPasswordView.showModifyResult(resBase), mIModifyPasswordView::loadError);
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"oldPsd\":\"" + mIModifyPasswordView.getOldPsd() + "\",\"newPsd\":\"" + mIModifyPasswordView.getNewPsd() + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
