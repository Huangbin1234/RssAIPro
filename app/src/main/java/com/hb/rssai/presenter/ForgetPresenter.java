package com.hb.rssai.presenter;

import android.text.TextUtils;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.view.iView.IForgetView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public class ForgetPresenter extends BasePresenter<IForgetView> {
    private IForgetView mIForgetView;

    public ForgetPresenter(IForgetView IForgetView) {
        mIForgetView = IForgetView;
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

    public void findPsd() {
        String type = mIForgetView.getType();
        String email = mIForgetView.getEmail();
        String userName = mIForgetView.getUserName();
        String mobile = mIForgetView.getMobile();
        String error;
        if (Constant.TYPE_EMAIL.equals(type)) {
            if ((error = checkEmail(email)) != null) {
                mIForgetView.setCheckError(error);
                return;
            }
            if ((error = checkUserName(userName)) != null) {
                mIForgetView.setCheckError(error);
                return;
            }

        } else if (Constant.TYPE_REPRESENTATION.equals(type)) {

            if ((error = checkUserName(userName)) != null) {
                mIForgetView.setCheckError(error);
                return;
            }
            if ((error = checkMobile(mobile)) != null) {
                mIForgetView.setCheckError(error);
                return;
            }
        } else {
            mIForgetView.setCheckError("跑飞了");
            return;
        }

        loginApi.forgetPassword(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> mIForgetView.showFindResult(resBase), mIForgetView::loadError);
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String type = mIForgetView.getType();
        String email = mIForgetView.getEmail();
        String userName = mIForgetView.getUserName();
        String mobile = mIForgetView.getMobile();

        String jsonParams = "{\"type\":\"" + type + "\",\"email\":\"" + email + "\",\"userName\":\"" + userName + "\",\"mobile\":\"" + mobile + "\"}";
        System.out.println(jsonParams);
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}