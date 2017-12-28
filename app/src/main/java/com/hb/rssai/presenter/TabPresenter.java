package com.hb.rssai.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.rssai.adapter.InfoAdapter;
import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IInformationView;
import com.hb.rssai.view.iView.ITabView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    setDataGroupResult(resDataGroup);
                }, this::loadError);
    }


    private void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup != null && resDataGroup.getRetCode() == 0) {
            if (mRowsBeanList.size() > 0) {
                mRowsBeanList.clear();
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
