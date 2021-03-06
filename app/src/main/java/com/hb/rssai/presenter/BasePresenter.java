package com.hb.rssai.presenter;


import com.hb.rssai.api.AdviceApi;
import com.hb.rssai.api.ApiFactory;
import com.hb.rssai.api.CollectionApi;
import com.hb.rssai.api.DataGroupApi;
import com.hb.rssai.api.FindApi;
import com.hb.rssai.api.InformationApi;
import com.hb.rssai.api.LoginApi;
import com.hb.rssai.api.MessageApi;
import com.hb.rssai.api.ThemeApi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/4/25.
 */

public abstract class BasePresenter<V> {
    protected Reference<V> mViewRef;
    public static final LoginApi loginApi = ApiFactory.getLoginApiSingleton();
    public static final AdviceApi adviceApi = ApiFactory.getAdviceApiSingleton();
    public static final FindApi findApi = ApiFactory.getFindApiSingleton();
    public static final CollectionApi collectionApi = ApiFactory.getCollectionApiSingleton();
    public static final InformationApi informationApi = ApiFactory.getInformationApiSingleton();
    public static final DataGroupApi dataGroupApi = ApiFactory.getDataGroupApiSingleton();
    public static final MessageApi messageApi = ApiFactory.getMessageApiSingleton();
    public static final ThemeApi themeApi = ApiFactory.getThemeApiSingleton();

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
