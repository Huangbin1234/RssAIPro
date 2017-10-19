package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResShareCollection;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.IMineView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class MinePresenter extends BasePresenter<IMineView> {
    private Context mContext;
    private IMineView iMineView;
    private TextView tvReadCount;
    private TextView tvSubscribeCount;
    private TextView tvAccount;
    private ImageView ivAva;
    private TextView tvMessageFlag;
    private TextView tvSignature;


    public MinePresenter(Context context, IMineView iMineView) {
        mContext = context;
        this.iMineView = iMineView;
        initView();
    }

    private void initView() {
        tvReadCount = iMineView.getTvReadCount();
        tvSubscribeCount = iMineView.getTvSubscribeCount();
        tvAccount = iMineView.getTvAccount();
        ivAva = iMineView.getIvAva();
        tvMessageFlag = iMineView.getTvMessageFlag();
        tvSignature = iMineView.getTvSignature();

    }

    public void getUser() {
        loginApi.getUserInfo(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> {
                    setResult(resUser);
                }, this::loadUserError);
    }

    public void setUpdate() {
        messageApi.selUpdate(getSelUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setSelUpdateResult(resBase);
                }, this::loadError);
    }

    //添加收藏
    public void addCollection() {
        collectionApi.add(getAddParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resShareCollection -> {
                    setAddResult(resShareCollection);
                }, this::loadError);
    }

    //添加订阅
    public void addSubscription() {
        findApi.subscribe(getSubscribeParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddSubscribeResult(resBase);
                }, this::loadError);
    }

    private void setResult(ResUser user) {
        if (user.getRetCode() == 0) {
            tvReadCount.setText("" + user.getRetObj().getReadCount());
            tvSubscribeCount.setText("" + user.getRetObj().getSubscribeCount());
            tvAccount.setText(user.getRetObj().getNickName());

            if (!TextUtils.isEmpty(user.getRetObj().getDescription())) {
                tvSignature.setVisibility(View.VISIBLE);
                tvSignature.setText(user.getRetObj().getDescription());
            } else {
                tvSignature.setVisibility(View.GONE);
            }

            HttpLoadImg.loadCircleImg(mContext, ApiRetrofit.BASE_IMG_URL + user.getRetObj().getAvatar(), ivAva);
        } else {
            T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        }
    }

    private void setAddSubscribeResult(ResBase resBase) {
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private void setAddResult(ResShareCollection resBase) {
        if (resBase.getRetCode() == 0) {
            if (resBase.getRetObj() != null) {

                Intent intent = new Intent(mContext, RichTextActivity.class);
                intent.putExtra("abstractContent", resBase.getRetObj().getAbstractContent());
                intent.putExtra(ContentActivity.KEY_TITLE, resBase.getRetObj().getTitle());
                intent.putExtra("whereFrom", resBase.getRetObj().getWhereFrom());
                intent.putExtra("pubDate", resBase.getRetObj().getPubTime());
                intent.putExtra("url", resBase.getRetObj().getLink());
                intent.putExtra("id", resBase.getRetObj().getId());
                intent.putExtra("clickGood", resBase.getRetObj().getClickGood());
                intent.putExtra("clickNotGood", resBase.getRetObj().getClickNotGood());
                mContext.startActivity(intent);
            } else {
                T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
            }
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private void setSelUpdateResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            if (null != resBase.getRetObj() && ("" + resBase.getRetObj()).length() > 4) {
                tvMessageFlag.setVisibility(View.VISIBLE);
            } else {
                tvMessageFlag.setVisibility(View.GONE);
            }
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            } else {
                T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
            }
        } else {
            T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        }
    }

    private void loadUserError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(mContext, Constant.MSG_NO_LOGIN);
            } else {
                T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
            }
        } else {
            T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        }
        if (TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
            tvReadCount.setText("0");
            tvSubscribeCount.setText("0");
            tvAccount.setText(mContext.getResources().getString(R.string.str_mf_no_login));
            HttpLoadImg.loadCircleImg(mContext, R.mipmap.icon_default_avar, ivAva);
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private Map<String, String> getSelUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String localTime = SharedPreferencesUtil.getString(mContext, Constant.LAST_UPDATE_TIME, Constant.START_TIME);
        String jsonParams = "{\"localTime\":\"" + localTime + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getSubscribeParams() {
        Map<String, String> map = new HashMap<>();
        String subscribeId = iMineView.getSubscribeId();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getAddParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iMineView.getInformationId();
        boolean isDel = false;
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"isDel\":\"" + isDel + "\",\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
