package com.hb.rssai.view.me;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.OfflineAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.OfflineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.OfflinePresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IOfficeView;
import com.hb.rssai.view.service.DownNewsService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class OfflineActivity extends BaseActivity implements IOfficeView {

    @BindView(R.id.offline_list_view)
    ListView mOfflineListView;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.oa_btn_down)
    Button mOaBtnDown;
    @BindView(R.id.oa_chk_all)
    CheckBox mOaChkAll;
    @BindView(R.id.oa_rl_all)
    RelativeLayout mOaRlAll;

    private List<ResDataGroup.RetObjBean.RowsBean> mBeanList = new ArrayList<>();
    private OfflineAdapter mOfflineAdapter;
    private HashMap<Integer, String> groupIds = new HashMap<>();
    private String groupDatas = "";
    private DownNewsService.MyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OfflinePresenter) mPresenter).getChannels();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(OfflineEvent event) {
        runOnUiThread(() -> {
            mOaBtnDown.setText(getName(event.getId()) + " | 进度：" + event.getProgressVal() + " /" + event.getMaxVal());
            setProgress(event.getId(), event.getProgressVal(), event.getMaxVal());
        });
    }

    private String getName(String id) {
        String name = "";
        for (ResDataGroup.RetObjBean.RowsBean bean : mBeanList) {
            if (id.equals(bean.getId())) {
                name = bean.getName();
                break;
            }
        }
        return name;
    }

    public void setProgress(String id, int currentVal, int maxVal) {
        for (ResDataGroup.RetObjBean.RowsBean row : mBeanList) {
            if (id.equals(row.getId())) {
                row.setMaxVal(maxVal);
                row.setProgressVal(currentVal);
            }
        }
        if (mOfflineAdapter != null) {
            mOfflineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initView() {
        SharedPreferencesUtil.setBoolean(this, "isClickOffline", false);
        mOaRlAll.setOnClickListener(v -> {
            if (mBeanList.size() > 0) {
                mOaChkAll.toggle();
                if (mOaChkAll.isChecked()) {
                    for (int i = 0; i < mBeanList.size(); i++) {
                        OfflineAdapter.getIsSelected().put(i, true);
                    }
                } else {
                    for (int i = 0; i < mBeanList.size(); i++) {
                        OfflineAdapter.getIsSelected().put(i, false);
                    }
                }
                if (mOfflineAdapter != null) {
                    mOfflineAdapter.notifyDataSetChanged();
                }
            } else {
                T.ShowToast(this, "暂时没有可下载的频道");
            }
        });
        mOfflineListView.setOnItemClickListener((parent, v, position, id) -> {
            //点击选择
            // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
            OfflineAdapter.ViewHolder holder = (OfflineAdapter.ViewHolder) v.getTag();
            // 改变CheckBox的状态
            holder.dialog_item_chk.toggle();
            //全部置为false
            OfflineAdapter.getIsSelected().put(position, holder.dialog_item_chk.isChecked());
            // 将CheckBox的选中状况记录下来
            if (mOfflineAdapter != null) {
                mOfflineAdapter.notifyDataSetChanged();
            }
        });
        mOaBtnDown.setOnClickListener(v -> {
            groupDatas = "";
            for (int i = 0; i < mBeanList.size(); i++) {
                if (OfflineAdapter.getIsSelected().get(i)) {
                    if (i == 0) {
                        groupDatas += mBeanList.get(i).getVal();//获取到源图片
                    } else {
                        groupDatas += "," + mBeanList.get(i).getVal();//获取到源图片
                    }
                    groupIds.put(mBeanList.get(i).getVal(), mBeanList.get(i).getId());
                }
            }
            if (TextUtils.isEmpty(groupDatas)) {
                T.ShowToast(OfflineActivity.this, "没有选择任何频道！");
                return;
            }
            if (groupDatas.startsWith(",")) {
                groupDatas = groupDatas.substring(1);
            }
            SharedPreferencesUtil.setBoolean(OfflineActivity.this, "isClickOffline", true);
            //TODO 开始去服务器下载数据到本地数据库
            Intent bindIntent = new Intent(OfflineActivity.this, DownNewsService.class);
            bindService(bindIntent, mConnection, BIND_AUTO_CREATE);
        });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (DownNewsService.MyBinder) service;
            myBinder.execute(groupDatas, groupIds);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_offline;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_offline_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new OfflinePresenter(this);
    }


    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup.getRetCode() == 0 && resDataGroup.getRetObj().getRows().size() > 0) {
            mBeanList.addAll(resDataGroup.getRetObj().getRows());
            if (mOfflineAdapter == null) {
                mOfflineAdapter = new OfflineAdapter(this, mBeanList);
                mOfflineListView.setAdapter(mOfflineAdapter);
            }
            mOfflineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        if (SharedPreferencesUtil.getBoolean(this, "isClickOffline", false)) {
            EventBus.getDefault().post(new MineEvent(1));
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册
        EventBus.getDefault().unregister(this);
    }
}
