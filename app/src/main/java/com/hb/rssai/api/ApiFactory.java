package com.hb.rssai.api;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();
    static LoginApi loginApiSingleton = null;
    static AdviceApi adviceApiSingleton = null;
    static FindApi findApiSingleton = null;
    static CollectionApi collectionApiSingleton = null;
    static InformationApi informationApiSingleton = null;
    static DataGroupApi dataGroupApiSingleton=null;

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

    public static FindApi getFindApiSingleton() {
        synchronized (monitor) {
            if (findApiSingleton == null) {
                findApiSingleton = new ApiRetrofit().getFindApiService();
            }
            return findApiSingleton;
        }
    }

    public static CollectionApi getCollectionApiSingleton() {
        synchronized (monitor) {
            if (collectionApiSingleton == null) {
                collectionApiSingleton = new ApiRetrofit().getCollectionApiService();
            }
            return collectionApiSingleton;
        }
    }

    public static InformationApi getInformationApiSingleton() {
        synchronized (monitor) {
            if (informationApiSingleton == null) {
                informationApiSingleton = new ApiRetrofit().getInformationApiService();
            }
            return informationApiSingleton;
        }
    }

    public static  DataGroupApi getDataGroupApiSingleton(){
       synchronized (monitor){
           if(dataGroupApiSingleton==null){
               dataGroupApiSingleton=new ApiRetrofit().getDataGroupApiService();
           }
       }
        return dataGroupApiSingleton;
    }
}
