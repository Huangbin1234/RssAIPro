package com.hb.rssai.presenter;



import com.hb.rssai.api.ApiFactory;
import com.hb.rssai.api.LoginApi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/4/25.
 */

public abstract class BasePresenter<V> {
    protected Reference<V> mViewRef;
    public static final LoginApi loginAPi = ApiFactory.getLoginApiSingleton();

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
