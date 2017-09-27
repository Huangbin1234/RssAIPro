package com.hb.rssai.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IUserView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class UserPresenter extends BasePresenter<IUserView> {
    private Context mContext;
    private IUserView iUserView;

    private TextView tvNickName;
    private TextView tvDesc;
    private ImageView ivAvatar;
    private TextView tvBirth;
    private TextView tvSex;

    public UserPresenter(Context context, IUserView iUserView) {
        mContext = context;
        this.iUserView = iUserView;
        initView();
    }

    private void initView() {
        tvNickName = iUserView.getTvNickName();
        tvDesc = iUserView.getTvDescription();
        ivAvatar = iUserView.getTvAvatar();
        tvSex = iUserView.getTvSex();
        tvBirth = iUserView.getTvBirth();
    }

    public void getUserInfo() {
        loginApi.getUserInfo(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> {
                    setUserInfoResult(resUser);
                }, this::loadError);
    }

    private void setUserInfoResult(ResUser resUser) {
        if (resUser.getRetCode() == 0) {
            tvNickName.setText(resUser.getRetObj().getNickName());
            tvDesc.setText(resUser.getRetObj().getDescription());
            if (resUser.getRetObj().getSex() == -1) {
                tvSex.setText("单击设置性别");
            } else {
                tvSex.setText(resUser.getRetObj().getSex() == 1 ? "男" : "女");
            }
            tvBirth.setText(resUser.getRetObj().getBirth());
            HttpLoadImg.loadCircleImg(mContext, resUser.getRetObj().getAvatar(), ivAvatar);
        } else {
            T.ShowToast(mContext, resUser.getRetMsg());
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
        if (TextUtils.isEmpty(SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""))) {
            tvNickName.setText("点击设置昵称");
            tvDesc.setText("点击设置个性签名");
            tvSex.setText("点击设置性别");
            tvBirth.setText("点击设置生日");
            HttpLoadImg.loadCircleImg(mContext, R.mipmap.icon_default_avar, ivAvatar);
        }
    }

    public void updateUserInfo() {
        loginApi.update(getUpdateParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateResult(resBase);
                }, this::loadError);
    }

    private void setUpdateResult(ResUser resBase) {
        if(resBase.getRetCode()==0){
            getUserInfo();
            EventBus.getDefault().post(new MineEvent(0));
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private HashMap<String, String> getUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "";
        if ("1".equals(iUserView.getEditType())) {
            String gender = tvSex.getText().toString().trim();
            String sex = "0";//默认
            if ("男".equals(gender)) {
                sex = "1";
            } else if ("女".equals(gender)) {
                sex = "2";
            }
            jsonParams = "{\"sex\":\"" + sex + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        } else if ("2".equals(iUserView.getEditType())) {
            String birth = tvBirth.getText().toString().trim();
            jsonParams = "{\"birth\":\"" + birth + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        } else if ("3".equals(iUserView.getEditType())) {
            jsonParams = "{\"nickName\":\"" + iUserView.getEtContent() + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        }
        map.put(Constant.TOKEN, SharedPreferencesUtil.getString(mContext, Constant.TOKEN, ""));
        return map;
    }
}
