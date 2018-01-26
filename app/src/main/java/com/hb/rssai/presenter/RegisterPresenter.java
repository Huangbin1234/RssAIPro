package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.view.iView.IRegisterView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class RegisterPresenter extends BasePresenter<IRegisterView> {
    private IRegisterView mIRegisterView;

    public RegisterPresenter(IRegisterView IRegisterView) {
        mIRegisterView = IRegisterView;
    }

    private String checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "请输入账号";
        }
        return null;
    }

    private String checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "请输入密码";
        }
        if (password.length() < 6 || password.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSurePassword(String sPassword) {
        if (TextUtils.isEmpty(sPassword)) {
            return "请再次输入密码";
        }
        if (sPassword.length() < 6 || sPassword.length() > 16) {
            return "密码长度应为6~16位，请修改";
        }
        return null;
    }

    private String checkSame(String psd, String sPsd) {
        if (!sPsd.equals(psd)) {
            return "两次输入的密码不一致";
        }
        return null;
    }

    public void register() {
        String name = mIRegisterView.getUserName();
        String psd = mIRegisterView.getPassword();
        String sPsd = mIRegisterView.getSurePassword();
        String error;
        if ((error = checkUserName(name)) != null) {
            mIRegisterView.setCheckError(error);
            return;
        }
        if ((error = checkPassword(psd)) != null) {
            mIRegisterView.setCheckError(error);
            return;
        }
        if ((error = checkSurePassword(sPsd)) != null) {
            mIRegisterView.setCheckError(error);
            return;
        }
        if ((error = checkSame(psd, sPsd)) != null) {
            mIRegisterView.setCheckError(error);
            return;
        }
        loginApi.register(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    mIRegisterView.setRegResult(resBase);
                }, mIRegisterView::loadError);
    }

    public Map<String, String> getParams() {
        String name = mIRegisterView.getUserName();
        String psd = mIRegisterView.getPassword();
        String sPsd = mIRegisterView.getSurePassword();
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"userName\":\"" + name + "\",\"password\":\"" + psd + "\",\"sPassword\":\"" + sPsd + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }
}
