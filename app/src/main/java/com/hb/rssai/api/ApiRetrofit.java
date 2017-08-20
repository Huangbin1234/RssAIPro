package com.hb.rssai.api;


import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
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
    private AdviceApi adviceService;
    private FindApi findApiService;
    private CollectionApi collectionApiService;
    private InformationApi informationApiService;
    private DataGroupApi dataGroupApiService;
    private MessageApi messageApiService;
//        public static final String BASE_URL = "http://192.168.58.226:8010/";
    public static final String BASE_URL = "http://192.168.0.109:8010/";
//    public static final String BASE_URL = "http://192.168.1.103:8010/";

    public ApiRetrofit() {
        File httpCacheDirectory = new File(ProjectApplication.mContext.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;//10MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();
        Retrofit retrofit_login = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Retrofit retrofit_advice = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Retrofit retrofit_find = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Retrofit retrofit_collection = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Retrofit retrofit_information = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Retrofit retrofit_data_group = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Retrofit retrofit_message = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        loginApiService = retrofit_login.create(LoginApi.class);
        adviceService = retrofit_advice.create(AdviceApi.class);
        findApiService = retrofit_find.create(FindApi.class);
        collectionApiService = retrofit_collection.create(CollectionApi.class);
        informationApiService = retrofit_information.create(InformationApi.class);
        dataGroupApiService = retrofit_data_group.create(DataGroupApi.class);
        messageApiService = retrofit_message.create(MessageApi.class);
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
                    .addHeader("token", SharedPreferencesUtil.getString(ProjectApplication.mContext, Constant.TOKEN, ""))
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

    public AdviceApi getAdviceApiService() {
        return adviceService;
    }

    public FindApi getFindApiService() {
        return findApiService;
    }

    public CollectionApi getCollectionApiService() {
        return collectionApiService;
    }

    public InformationApi getInformationApiService() {
        return informationApiService;
    }

    public DataGroupApi getDataGroupApiService() {
        return dataGroupApiService;
    }

    public MessageApi getMessageApiService() {
        return messageApiService;
    }
}
