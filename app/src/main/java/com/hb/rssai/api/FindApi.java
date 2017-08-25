package com.hb.rssai.api;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface FindApi {
    @FormUrlEncoded
    @POST("subscription/findMoreList")
    Observable<ResFindMore> findMoreList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/recommendList")
    Observable<ResFindMore> recommendList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/userSubscribeList")
    Observable<ResFindMore> userSubscribeList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/addRssByUser")
    Observable<ResBase> addRssByUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/subscribe")
    Observable<ResBase> subscribe(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/delSubscription")
    Observable<ResBase> delSubscription(@FieldMap Map<String, String> params);

}
