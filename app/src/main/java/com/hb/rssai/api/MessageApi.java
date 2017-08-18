package com.hb.rssai.api;

import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResMessageList;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface MessageApi {
    @FormUrlEncoded
    @POST("message/list")
    Observable<ResMessageList> list(@FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("message/detail")
    Observable<ResBase> add(@FieldMap Map<String, String> params);
}
