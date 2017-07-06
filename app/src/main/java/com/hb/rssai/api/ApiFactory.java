package com.hb.rssai.api;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();
    static LoginApi loginApiSingleton=null;
    public static LoginApi getLoginApiSingleton(){
        synchronized (monitor) {
            if(loginApiSingleton==null){
                loginApiSingleton=new ApiRetrofit().getLoginApiService();
            }
            return loginApiSingleton;
        }
    }
}
