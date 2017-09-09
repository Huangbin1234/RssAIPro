package com.hb.rssai.view.subscription.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.TabContentPresenter;
import com.hb.rssai.view.iView.ITabContentView;
import com.hb.rssai.view.widget.MyDecoration;

import butterknife.BindView;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 17/1/28 17:36
 */
public class TabResourceFragment extends BaseFragment implements ITabContentView {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private LinearLayoutManager manager;

    public static TabResourceFragment getInstance(String title) {
        TabResourceFragment fra = new TabResourceFragment();
        Bundle bundle = new Bundle();
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void setAppTitle() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return new TabContentPresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View rootView) {
        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //加载数据
//        ((TabContentPresenter) mPresenter).getListData(0);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public LinearLayoutManager getManager() {
        return manager;
    }
}