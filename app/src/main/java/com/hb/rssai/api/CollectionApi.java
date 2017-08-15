package com.hb.rssai.api;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface CollectionApi {
    @FormUrlEncoded
    @POST("collection/list")
    Observable<ResCollection> list(@FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("collection/add")
    Observable<ResBase> add(@FieldMap Map<String, String> params);
}
