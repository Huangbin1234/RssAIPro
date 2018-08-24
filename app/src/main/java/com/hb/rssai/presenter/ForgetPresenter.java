package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ForgetContract;
import com.hb.rssai.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public class ForgetPresenter extends BasePresenter<ForgetContract.View> implements ForgetContract.Presenter {
    private ForgetContract.View mView;

    public ForgetPresenter(ForgetContract.View mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    private String checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "请输入与账户绑定的邮箱";
        }
        return null;
    }

    private String checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "请输入登录账户";
        }
        return null;
    }

    private String checkMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "请输入联系手机号";
        }
        if (!StringUtil.isMobileNO(mobile)) {
            return "请输入正确的手机号";
        }
        return null;
    }

    private Map<String, String> getParams(String type, String email, String userName, String mobile) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"type\":\"" + type + "\",\"email\":\"" + email + "\",\"userName\":\"" + userName + "\",\"mobile\":\"" + mobile + "\"}";
        System.out.println(jsonParams);
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    @Override
    public void retrievePassword(String type, String email, String userName, String mobile) {
        String error;
        if (Constant.TYPE_EMAIL.equals(type)) {
            if ((error = checkEmail(email)) != null) {
                mView.showCheckError(error);
                return;
            }
            if ((error = checkUserName(userName)) != null) {
                mView.showCheckError(error);
                return;
            }

        } else if (Constant.TYPE_REPRESENTATION.equals(type)) {

            if ((error = checkUserName(userName)) != null) {
                mView.showCheckError(error);
                return;
            }
            if ((error = checkMobile(mobile)) != null) {
                mView.showCheckError(error);
                return;
            }
        } else {
            mView.showCheckError("跑飞了");
            return;
        }

        loginApi.forgetPassword(getParams(type, email, userName, mobile))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mView.showRetrieveSuccess(resBase), mView::showRetrieveFailed);
    }

    @Override
    public void start() {

    }
}