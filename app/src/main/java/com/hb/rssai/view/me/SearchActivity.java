package com.hb.rssai.view.me;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.SearchFragmentAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SearchPresenter;
import com.hb.rssai.util.KeyboardUtil;
import com.hb.rssai.util.SearchTextWatcher;
import com.hb.rssai.util.StatusBarUtil;
import com.hb.rssai.view.iView.ISearchView;
import com.hb.rssai.view.widget.MyDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements ISearchView, View.OnClickListener, SearchSubscribeFragment.OnFragmentInteractionListener, SearchInfoFragment.OnFragmentInteractionListener {


    @BindView(R.id.search_listView)
    RecyclerView mSearchListView;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.its_et_key)
    EditText mItsEtKey;
    @BindView(R.id.its_iv_clear)
    ImageView mItsIvClear;
    @BindView(R.id.its_btn_search)
    Button mItsBtnSearch;
    @BindView(R.id.its_iv_search)
    ImageView mItsIvSearch;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    List<String> mLocales = new ArrayList<>();
    private LinearLayoutManager infoManager;
    private String keyWord = "";
    private SearchFragmentAdapter adapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SearchListener searchListener;

    public SearchListener getSearchListener() {
        return searchListener;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public interface SearchListener {
        void search(String val);
    }

    @Override
    protected void initView() {
        //線性布局
        initPager();
        infoManager = new LinearLayoutManager(this);
        mSearchListView.setLayoutManager(infoManager);
        mSearchListView.addItemDecoration(new MyDecoration(this, LinearLayoutManager.VERTICAL));
        mLocales.add("asdasd");
        mLocales.add("asdasd2");
        mLocales.add("asdasd3");
        mLocales.add("asdasd4");
        mLocales.add("asdasd5");
        mLocales.add("asdasd6");

        mItsEtKey.addTextChangedListener(new SearchTextWatcher(mItsEtKey, mItsIvClear));
        mItsEtKey.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                keyWord = v.getText().toString();
                if (!TextUtils.isEmpty(keyWord)) {
                    searchListener.search(keyWord);
                    KeyboardUtil.hideSoftKeyboard(SearchActivity.this);
                }
                return true;
            }
            return false;
        });
        mItsIvClear.setOnClickListener(v -> mItsEtKey.setText(""));
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_search;
    }

    private void initPager() {
        //使用适配器将ViewPager与Fragment绑定在一起
        adapter = new SearchFragmentAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        //不预加载数据
        mViewPager.setOffscreenPageLimit(0);
        //解决ViewPage与SwipeReFreshLayout 滑动冲突
        mTabLayout.setupWithViewPager(mViewPager);
        //指定位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 4) {
                    mViewPager.setCurrentItem(tab.getPosition() - 1);
                } else {
                    mViewPager.setCurrentItem(tab.getPosition());
                    adapter.getItem(tab.getPosition()).setUserVisibleHint(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("d", "onTabUnselected2：" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("d", "onTabReselected3：" + tab.getPosition());
            }
        });


    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysToolbar.setNavigationIcon(R.mipmap.ic_back);
        mSysToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //修改状态栏文字图标为深色
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new SearchPresenter(this, this);
    }

    @Override
    public String getKeyWords() {
        return keyWord;
    }

    @Override
    public RecyclerView getInfoRecyclerView() {
        return mSearchListView;
    }

    @Override
    public RecyclerView getSubscribeRecyclerView() {
        return null;
    }

    @Override
    public LinearLayoutManager getInfoManager() {
        return infoManager;
    }

    @OnClick({R.id.its_iv_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.its_iv_search:
                keyWord = mItsEtKey.getText().toString();
                searchListener.search(keyWord);
                KeyboardUtil.hideSoftKeyboard(this);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
