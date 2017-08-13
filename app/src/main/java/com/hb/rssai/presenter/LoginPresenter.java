package com.hb.rssai.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.hb.rssai.bean.ResLogin;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ILoginView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {
    private Context mContext;
    private ILoginView iLoginView;
    private EditText etName;
    private EditText etPsd;

    public LoginPresenter(Context context, ILoginView iLoginView) {
        mContext = context;
        this.iLoginView = iLoginView;
    }

    public void login() {

        if (iLoginView != null) {
            etName = iLoginView.getEtUserName();
            etPsd = iLoginView.getEtPassword();
            String uName = etName.getText().toString().trim();
            String uPsd = etPsd.getText().toString().trim();
            if (TextUtils.isEmpty(uName)) {
                T.ShowToast(mContext, "请输入账号");
                return;
            }
            if (TextUtils.isEmpty(uPsd)) {
                T.ShowToast(mContext, "请输入密码");
                return;
            }
            Map<String, String> params = new HashMap<>();
            String jsonParams = "{\"userName\":\"" + uName + "\",\"password\":\"" + uPsd + "\"}";
            params.put("jsonParams", jsonParams);
            loginApi.doLogin(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resLogin -> {
                        setLoginResult(resLogin);
                    }, this::loadError);
        }
    }

    private void setLoginResult(ResLogin bean) {
        if (bean.getRetCode() == 0) {
            String uName = etName.getText().toString().trim();
            String uPsd = etPsd.getText().toString().trim();
            SharedPreferencesUtil.setString(mContext, Constant.SP_LOGIN_USER_NAME, uName);
            SharedPreferencesUtil.setString(mContext, Constant.SP_LOGIN_PSD, uPsd);
            SharedPreferencesUtil.setString(mContext, Constant.TOKEN, bean.getRetObj() != null ? bean.getRetObj().getToken(): "");
            iLoginView.toFinish();
        }
        T.ShowToast(mContext, bean.getRetMsg());

    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
