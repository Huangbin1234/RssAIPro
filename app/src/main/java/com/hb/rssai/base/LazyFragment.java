package com.hb.rssai.base;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/3/14.
 */

public abstract class LazyFragment extends Fragment {
    protected boolean isVisible;
    /**
     * 实现数据懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();


}
