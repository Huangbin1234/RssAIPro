package com.hb.rssai.api;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public interface InformationApi {
    @FormUrlEncoded
    @POST("info/list")
    Observable<ResInformation> getList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/listById")
    Observable<ResInformation> getListById(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/listCardById")
    Observable<ResCardSubscribe> listCardById(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/updateCount")
    Observable<ResBase> updateCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/updateEvaluateCount")
    Observable<ResBase> updateEvaluateCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/getLikeByTitle")
    Observable<ResInformation> getLikeByTitle(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/getUserInformation")
    Observable<ResInformation> getUserInformation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/getInformation")
    Observable<ResInfo> getInformation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("info/getSearchInfo")
    Observable<ResInformation> getSearchInfo(@FieldMap Map<String, String> params);
}
