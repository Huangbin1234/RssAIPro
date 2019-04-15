package com.hb.rssai.api;

import com.hb.rssai.bean.ResAdviceList;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResMessageList;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public interface AdviceApi {
    @FormUrlEncoded
    @POST("advice/add")
    Observable<ResBase> doAdd(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("advice/list")
    Observable<ResAdviceList> doList(@FieldMap Map<String, String> params);
}
