package com.hb.rssai.api;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();
    static LoginApi loginApiSingleton = null;
    static AdviceApi adviceApiSingleton = null;

    public static LoginApi getLoginApiSingleton() {
        synchronized (monitor) {
            if (loginApiSingleton == null) {
                loginApiSingleton = new ApiRetrofit().getLoginApiService();
            }
            return loginApiSingleton;
        }
    }

    public static AdviceApi getAdviceApiSingleton() {
        synchronized (monitor) {
            if (adviceApiSingleton == null) {
                adviceApiSingleton = new ApiRetrofit().getAdviceApiService();
            }
            return adviceApiSingleton;
        }
    }
}
