package com.hb.rssai.contract;

public interface BaseView<T>{
    void setPresenter(T t);
    void showFail(Throwable throwable);
}
