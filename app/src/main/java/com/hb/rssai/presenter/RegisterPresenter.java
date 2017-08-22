package com.hb.rssai.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IRegisterView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class RegisterPresenter extends BasePresenter<IRegisterView> {
    private Context mContext;
    private IRegisterView mIRegisterView;

    private EditText uName, uPsd, uSpsd;

    public RegisterPresenter(Context context, IRegisterView IRegisterView) {
        mContext = context;
        mIRegisterView = IRegisterView;
        initView();
    }

    private void initView() {
        uName = mIRegisterView.getEtName();
        uPsd = mIRegisterView.getEtPsd();
        uSpsd = mIRegisterView.getEtSpsd();
    }

    public void register() {
        String name = uName.getText().toString().trim();
        String psd = uPsd.getText().toString().trim();
        String spsd = uSpsd.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            T.ShowToast(mContext, "请输入账号");
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            T.ShowToast(mContext, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(spsd)) {
            T.ShowToast(mContext, "请再次输入密码");
            return;
        }

        if (!spsd.equals(psd)) {
            T.ShowToast(mContext, "两次输入的密码不一致");
            return;
        }

        loginApi.register(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setRegResult(resBase);
                }, this::loadError);
    }

    private Map<String, String> getParams() {
        String name = uName.getText().toString().trim();
        String psd = uPsd.getText().toString().trim();
        String spsd = uSpsd.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        String jsonParams = "{\"userName\":\"" + name + "\",\"password\":\"" + psd + "\",\"sPassword\":\"" + spsd + "\"}";
        params.put("jsonParams", jsonParams);
        return params;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setRegResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            String name = uName.getText().toString().trim();
            String psd = uSpsd.getText().toString().trim();
            SharedPreferencesUtil.setString(mContext, Constant.SP_LOGIN_USER_NAME, name);
            SharedPreferencesUtil.setString(mContext, Constant.SP_LOGIN_PSD, psd);

            mIRegisterView.toFinish();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }
}
