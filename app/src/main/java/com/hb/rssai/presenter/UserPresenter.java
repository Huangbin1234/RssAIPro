package com.hb.rssai.presenter;

import com.hb.rssai.view.iView.IUserView;

import java.io.File;
import java.util.List;

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
        loginApi.getUserInfo(iUserView.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resUser -> {
                    iUserView.setUserInfoResult(resUser);
                }, iUserView::loadError);
    }

    public void updateUserInfo() {
        loginApi.update(iUserView.getUpdateParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    iUserView.setUpdateResult(resBase);
                }, iUserView::loadError);
    }

    private MultipartBody.Builder getBuilder() {
        String filePath = iUserView.getFilePath();
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
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

}
