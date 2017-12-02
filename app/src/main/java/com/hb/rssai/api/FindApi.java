package com.hb.rssai.api;

import com.hb.rssai.bean.ResBDJson;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.ResSubscription;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface FindApi {
    @FormUrlEncoded
    @POST("subscription/findMoreList")
    Observable<ResFindMore> findMoreList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/findMoreListById")
    Observable<ResSubscription> findMoreListById(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/getListByType")
    Observable<ResFindMore> getListByType(@FieldMap Map<String, String> params);

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

    @FormUrlEncoded
    @POST("subscription/updateUsSort")
    Observable<ResBase> updateUsSort(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/updateImage")
    Observable<ResBase> updateImage(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("subscription/getLikeByName")
    Observable<ResFindMore> getLikeByName(@FieldMap Map<String, String> params);

    @GET("cardserver/search")
    Call<ResBDJson> getRssImage(@Query("para") String params);
}



