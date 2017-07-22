package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.easytagdragview.EasyTipDragView;
import com.hb.rssai.easytagdragview.bean.SimpleTitleTip;
import com.hb.rssai.easytagdragview.bean.Tip;
import com.hb.rssai.easytagdragview.bean.TipDataModel;
import com.hb.rssai.easytagdragview.widget.TipItemView;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.T;

import java.util.ArrayList;

import butterknife.BindView;

public class HotTagActivity extends BaseActivity {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;
    @BindView(R.id.easy_tip_drag_view)
    EasyTipDragView mEasyTipDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //设置已包含的标签数据
        mEasyTipDragView.setAddData(TipDataModel.getAddTips());
        //设置可以添加的标签数据
        mEasyTipDragView.setDragData(TipDataModel.getDragTips());
        //在easyTipDragView处于非编辑模式下点击item的回调（编辑模式下点击item作用为删除item）
        mEasyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
            @Override
            public void onTileSelected(Tip entity, int position, View view) {
                T.ShowToast(HotTagActivity.this, ((SimpleTitleTip) entity).getTip());
            }
        });
        //设置每次数据改变后的回调（例如每次拖拽排序了标签或者增删了标签都会回调）
        mEasyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<Tip> tips) {
                Log.i("heheda", tips.toString());
            }
        });
        //设置点击“确定”按钮后最终数据的回调
        mEasyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                T.ShowToast(HotTagActivity.this, "最终数据：" + tips.toString());
                //   btn.setVisibility(View.VISIBLE);
            }
        });
        mEasyTipDragView.open();
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_hot_tag;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_hta_title));
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
    protected BasePresenter createPresenter() {
        return null;
    }
}
