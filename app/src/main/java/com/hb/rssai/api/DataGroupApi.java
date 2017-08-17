package com.hb.rssai.api;

import com.hb.rssai.bean.ResDataGroup;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface DataGroupApi {
    @FormUrlEncoded
    @POST("dataGroup/list")
    Observable<ResDataGroup> getDataGroupList(@FieldMap Map<String, String> params);
}
