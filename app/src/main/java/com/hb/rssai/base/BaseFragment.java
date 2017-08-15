package com.hb.rssai.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.presenter.BasePresenter;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/2/9.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends LazyFragment {

    public T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mPresenter = createPresenter();
//        if(mPresenter!=null){
//            mPresenter.attachView((V) this);
//        }
        View rootView = inflater.inflate(providerContentViewId(), container, false);
        ButterKnife.bind(this,rootView);
        setAppTitle();
        initView(rootView);
        mPresenter = createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V) this);
        }
        return rootView;
    }

    protected abstract void setAppTitle();

    protected abstract T createPresenter();

    //设置布局
    protected abstract int providerContentViewId();

    //初始化控件信息
    protected abstract void initView(View rootView);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
