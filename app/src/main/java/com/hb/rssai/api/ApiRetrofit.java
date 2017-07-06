package com.hb.rssai.api;


import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.util.StateUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ApiRetrofit {
    public LoginApi loginApiService;
    public static final String LOGIN_BASE_URL = "http://182.137.14.194:8080/deviceDataAcquisition/";

    public ApiRetrofit() {
        File httpCacheDirectory = new File(ProjectApplication.mContext.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;//10MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();
        Retrofit retrofit_login = new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        loginApiService = retrofit_login.create(LoginApi.class);
    }

    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();
        Request request = chain.request();
        if (StateUtils.isNetworkAvailable(ProjectApplication.mContext)) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (StateUtils.isNetworkAvailable(ProjectApplication.mContext)) {
            int maxAge = 0;
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;//tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "Public only-if-cached, Max-stale=" + maxStale)
                    .build();
        }
    };

    public LoginApi getLoginApiService() {
        return loginApiService;
    }
}