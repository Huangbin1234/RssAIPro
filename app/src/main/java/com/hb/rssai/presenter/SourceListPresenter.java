package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hb.rssai.R;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResSubscription;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.iView.ISourceListView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class SourceListPresenter extends BasePresenter<ISourceListView> {
    private Context mContext;
    private ISourceListView iSourceListView;

    public SourceListPresenter(Context context, ISourceListView iSourceListView) {
        mContext = context;
        this.iSourceListView = iSourceListView;
    }


    public void subscribe(View v) {
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
            //TODO 先去查询服务器上此条数据
            findMoreListById(v);
        } else {
            //跳转到登录
            T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ProjectApplication.mContext.startActivity(intent);
        }
    }

    public void findMoreListById(View v) {
        if (iSourceListView != null) {
            findApi.findMoreListById(getFindMoreByIdParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resSubscription -> {
                        setFindMoreByIdResult(resSubscription, v);
                    }, iSourceListView::loadError);
        }
    }

    public void addSubscription(View v) {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase, v);
                }, iSourceListView::loadError);
    }

    public void getListCardById() {
        informationApi.listCardById(getListParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resCardSubscribe -> {
                    iSourceListView.setListCardResult(resCardSubscribe);
                }, iSourceListView::loadError);
    }

    public void delSubscription(View v) {
        findApi.delSubscription(getDelParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setDelResult(resBase, v);
                }, iSourceListView::loadError);
    }

    private void setFindMoreByIdResult(ResSubscription resSubscription, View v) {
        if (resSubscription.getRetCode() == 0) {
            if (resSubscription.getRetObj().isDeleteFlag()) {
                addSubscription(v);
            } else { //如果发现没有被删除
                if (TextUtils.isEmpty(resSubscription.getRetObj().getUserId())) {//如果也没有被添加过
                    addSubscription(v);
                } else {//如果被添加过
                    String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
                    if (userId.equals(resSubscription.getRetObj().getUserId())) {//如果是等于当前登录ID
                        delSubscription(v);
                    } else {//不等于
                        addSubscription(v);
                    }
                }
            }
        } else if (resSubscription.getRetCode() == 10013) {
            //从来没订阅过
            addSubscription(v);
        } else {
            T.ShowToast(mContext, resSubscription.getRetMsg());
        }
    }

    private void setDelResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (resBase.getRetCode() == 0) {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        }
    }

    private void setAddResult(ResBase resBase, View v) {
        T.ShowToast(mContext, resBase.getRetMsg());
        if (resBase.getRetCode() == 0) {
            EventBus.getDefault().post(new RssSourceEvent(0));
        }
        if (resBase.getRetCode() == 0) {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_cancel);
        } else {
            ((ImageView) v).setImageResource(R.mipmap.ic_subscribe_add);
        }
    }

    /**
     * 获取订阅信息 根据ID
     * @param
     */
    public void getSubscription() {
        findApi.getSubscription(getSubscriptionParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resSubscription -> {
                    iSourceListView.setSubscription(resSubscription);
                }, iSourceListView::loadError);
    }
    private Map<String, String> getSubscriptionParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
    private Map<String, String> getFindMoreByIdParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getDelParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"usId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }


    private Map<String, String> getListParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iSourceListView.getSubscribeId();
        String jsonParams = "{\"subscribeId\":\"" + subscribeId + "\",\"page\":\"" + iSourceListView.getPageNum() + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }
}
