package com.hb.rssai.api;


import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResLogin;
import com.hb.rssai.bean.ResUser;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface LoginApi {
    @FormUrlEncoded
    @POST("ic_user_black_24dp/login")
    Observable<ResLogin> doLogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ic_user_black_24dp/register")
    Observable<ResBase> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ic_user_black_24dp/getUserInfo")
    Observable<ResUser> getUserInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ic_user_black_24dp/update")
    Observable<ResUser> update(@FieldMap Map<String, String> params);

    @Multipart
    @POST("ic_user_black_24dp/upload")
    Observable<ResBase> upload(@Part List<MultipartBody.Part> partList);
}
