package com.hb.rssai.api;

import com.hb.rssai.bean.ResAdvertisement;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface AdvertisementApi {

    @FormUrlEncoded
    @POST("adver/getAdver")
    Observable<ResAdvertisement> getAdvertisement(@FieldMap Map<String, String> params);
}
