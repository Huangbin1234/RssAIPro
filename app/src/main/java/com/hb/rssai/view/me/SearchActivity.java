package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.CardAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {


    @BindView(R.id.search_listView)
    RecyclerView mSearchListView;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    List<String> mLocales =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //線性布局
        mSearchListView.setLayoutManager(new LinearLayoutManager(this));
        mLocales.add("asdasd");
        mLocales.add("asdasd2");
        mLocales.add("asdasd3");
        mLocales.add("asdasd4");
        mLocales.add("asdasd5");
        mLocales.add("asdasd6");
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);//指定Toolbar上的视图文件
        final SearchView searchView = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchListView.setAdapter(new LanguageAdapter(mLocales));
                return true;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
//        mSysTvTitle.setText(getResources().getString(R.string.str_search_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

        private List<String> mLocales;

        private LanguageAdapter(List<String> mLocales) {
            this.mLocales = mLocales;
            notifyDataSetChanged();
        }

        @Override
        public LanguageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new LanguageAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_name.setText(mLocales.get(position));
        }

        @Override
        public int getItemCount() {
            return mLocales.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
            TextView tv_count, tv_name;
            Button tv_view;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                tv_view = (Button) itemView.findViewById(R.id.tv_view);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            }
        }
    }

}
