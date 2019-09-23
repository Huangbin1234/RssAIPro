package com.hb.rssai.contract;

import android.view.View;

import com.hb.rssai.bean.ResFindMore;

/**
 * Created by Administrator
 * on 2019/9/23
 */
public interface AddSourceLikeContract {
    interface  View extends  BaseView<Presenter>{
       void showSubscribeLike(ResFindMore resBase);

    }
    interface  Presenter extends  BasePresenter{
      void  getSubscribeLike(String key,int page);

        void findMoreListById(android.view.View v);

    }
}
