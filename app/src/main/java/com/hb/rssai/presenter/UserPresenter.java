package com.hb.rssai.presenter;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.iView.IUserView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class UserPresenter extends BasePresenter<IUserView> {
    private IUserView iUserView;

    public UserPresenter(IUserView iUserView) {
        this.iUserView = iUserView;
    }

    public void getUserInfo() {
        loginApi.getUserInfo(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> iUserView.setUserInfoResult(resUser), iUserView::loadError);
    }

    public void updateUserInfo() {
        loginApi.update(getUpdateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> iUserView.setUpdateResult(resBase), iUserView::loadError);
    }

    private MultipartBody.Builder getBuilder() {
        String filePath = iUserView.getFilePath();
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder() .setType(MultipartBody.FORM);//表单类型
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//file 后台接收图片流的参数名
        return builder;
    }

    //上传头像
    public void uploadAvatar() {
        List<MultipartBody.Part> parts = getBuilder().build().parts();
        loginApi.upload(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iUserView.setAvatarResult(resBase);
                }, iUserView::loadError);
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String userId = iUserView.getUserId();
        String jsonParams = "{\"userId\":\"" + userId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public Map<String, String> getUpdateParams() {
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "";
        String editType = iUserView.getEtType();
        String gender = iUserView.getSex();
        String birth = iUserView.getBirth();
        String nickName = iUserView.getNickName();
        String email = iUserView.getEmail();

        if ("1".equals(editType)) {
            String sex = "0";//默认
            if ("男".equals(gender)) {
                sex = "1";
            } else if ("女".equals(gender)) {
                sex = "2";
            }
            jsonParams = "{\"sex\":\"" + sex + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        } else if ("2".equals(editType)) {
            jsonParams = "{\"birth\":\"" + birth + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        } else if ("3".equals(editType)) {
            jsonParams = "{\"nickName\":\"" + nickName + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        }else if("4".equals(editType)){
            jsonParams = "{\"email\":\"" + email + "\"}";
            map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        }
        return map;
    }
}
