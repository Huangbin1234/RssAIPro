package com.hb.rssai.presenter;

import android.content.Context;

import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.ITabResourceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class TabResourcePresenter extends BasePresenter<ITabResourceView> {
    private Context mContext;
    private ITabResourceView mITabResourceView;

    public TabResourcePresenter(Context context, ITabResourceView ITabResourceView) {
        mContext = context;
        mITabResourceView = ITabResourceView;
    }

    public void getGroupType() {
        dataGroupApi.getDataGroupList(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    setResult(resDataGroup);
                }, this::loadError);
    }

    List<ResDataGroup.RetObjBean.RowsBean> mRowsBeanList = new ArrayList<>();

    public List<ResDataGroup.RetObjBean.RowsBean> getGroupList() {
        return mRowsBeanList;
    }

    private void setResult(ResDataGroup resDataGroup) {
        if (resDataGroup != null && resDataGroup.getRetCode() == 0) {
            for (ResDataGroup.RetObjBean.RowsBean bean : resDataGroup.getRetObj().getRows()) {
                if (bean.getGroupType() == 0) {
                    mRowsBeanList.add(bean);
                }
            }
            mITabResourceView.setUi(mRowsBeanList);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }


    private Map<String, String> getParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
