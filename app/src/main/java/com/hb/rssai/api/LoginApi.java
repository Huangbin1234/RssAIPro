package com.hb.rssai.api;


import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResLogin;
import com.hb.rssai.bean.ResUser;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface LoginApi {
    @FormUrlEncoded
    @POST("user/login")
    Observable<ResLogin> doLogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/register")
    Observable<ResBase> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/getUserInfo")
    Observable<ResUser> getUserInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/update")
    Observable<ResUser> update(@FieldMap Map<String, String> params);
}
