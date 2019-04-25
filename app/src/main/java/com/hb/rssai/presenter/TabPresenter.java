package com.hb.rssai.presenter;

import android.content.Context;

import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IInformationView;
import com.hb.rssai.view.iView.ITabView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class TabPresenter extends BasePresenter<IInformationView> {
    private Context mContext;
    private ITabView iTabView;

    List<ResDataGroup.RetObjBean.RowsBean> mRowsBeanList = new ArrayList<>();
    List<ResDataGroup.RetObjBean.RowsBean> mMeRowsBeanList = new ArrayList<>();

    public TabPresenter(Context context, ITabView iTabView) {
        mContext = context;
        this.iTabView = iTabView;
    }


    public void getDataGroupList() {
        dataGroupApi.getDataGroupList(getDataGroupListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    iTabView.cacheDataGroup(resDataGroup);
                    setDataGroupResult(resDataGroup);
                }, this::loadError);
    }


    public void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup != null && resDataGroup.getRetCode() == 0) {
            if (mRowsBeanList.size() > 0) {
                mRowsBeanList.clear();
            }
            if (mMeRowsBeanList.size() > 0) {
                mMeRowsBeanList.clear();
            }

            for (ResDataGroup.RetObjBean.RowsBean bean : resDataGroup.getRetObj().getRows()) {
                if (bean.getGroupType() == 0) {
                    mRowsBeanList.add(bean);
                } else if (bean.getGroupType() == 1) {
                    mMeRowsBeanList.add(bean);
                }
            }
        }
        iTabView.loadGroupDown();
    }

    public List<ResDataGroup.RetObjBean.RowsBean> getGroupList() {
        return mRowsBeanList;
    }

    public List<ResDataGroup.RetObjBean.RowsBean> getMeGroupList() {
        return mMeRowsBeanList;
    }

    private HashMap<String, String> getDataGroupListParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
