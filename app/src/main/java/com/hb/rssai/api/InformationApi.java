package com.hb.rssai.api;

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
}
