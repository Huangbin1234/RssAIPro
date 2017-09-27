package com.hb.rssai.presenter;

import android.content.Context;
import android.widget.EditText;

import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IEditSignatureView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class EditSignaturePresenter extends BasePresenter<IEditSignatureView> {
    private Context mContext;
    private IEditSignatureView mIEditSignatureView;
    private EditText etContent;

    public EditSignaturePresenter(Context context, IEditSignatureView IEditSignatureView) {
        mContext = context;
        mIEditSignatureView = IEditSignatureView;
        initView();
    }

    private void initView() {
        etContent = mIEditSignatureView.getEtContent();
    }

    public void updateUserInfo() {
        loginApi.update(getUpdateParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateResult(resBase);
                }, this::loadError);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private void setUpdateResult(ResUser resBase) {
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new MineEvent(0));
            EventBus.getDefault().post(new UserEvent(0));
            mIEditSignatureView.toFinish();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private HashMap<String, String> getUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String description = etContent.getText().toString().trim();
        String jsonParams = "{\"description\":\"" + description + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        map.put(Constant.TOKEN, SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""));
        return map;
    }
}
