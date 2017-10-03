package com.hb.rssai.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hb.rssai.adapter.OfflineAdapter;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IOfficeView;
import com.hb.rssai.view.service.DownNewsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public class OfflinePresenter extends BasePresenter<IOfficeView> {
    private Context mContext;
    private IOfficeView iOfficeView;
    private ListView mListView;
    private Button btnDown;

    public OfflinePresenter(Context context, IOfficeView iOfficeView) {
        mContext = context;
        this.iOfficeView = iOfficeView;
        initView();
    }

    CheckBox chkAll;
    RelativeLayout rlAll;

    private void initView() {
        mListView = iOfficeView.getListView();
        chkAll = iOfficeView.getChkAll();
        rlAll = iOfficeView.getOaRlAll();
        btnDown = iOfficeView.getBtnDown();

        rlAll.setOnClickListener(v -> {
            if (mBeanList.size() > 0) {
                chkAll.toggle();
                if (chkAll.isChecked()) {
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
                T.ShowToast(mContext, "暂时没有可下载的频道");
            }
        });
        mListView.setOnItemClickListener((parent, v, position, id) -> {
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

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupDatas = "";
                for (int i = 0; i < mBeanList.size(); i++) {
                    if (OfflineAdapter.getIsSelected().get(i)) {
                        if (i == 0) {
                            groupDatas += mBeanList.get(i).getVal();//获取到源图片
                        } else {
                            groupDatas += "," + mBeanList.get(i).getVal();//获取到源图片
                        }
                    }
                }
                T.ShowToast(mContext, groupDatas);
                //TODO 开始去服务器下载数据到本地数据库
                Intent bindIntent = new Intent(mContext, DownNewsService.class);
                mContext.bindService(bindIntent, mConnection, mContext.BIND_AUTO_CREATE);
            }
        });
    }

    private DownNewsService.MyBinder myBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (DownNewsService.MyBinder) service;
            myBinder.execute(groupDatas);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    private String groupDatas = "";

    public void getChannels() {
        dataGroupApi.getDataGroupList(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resDataGroup -> {
                    setDataGroupResult(resDataGroup);
                }, this::loadError);
    }

    private OfflineAdapter mOfflineAdapter;

    private List<ResDataGroup.RetObjBean.RowsBean> mBeanList = new ArrayList<>();

    private void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup.getRetCode() == 0 && resDataGroup.getRetObj().getRows().size() > 0) {
            mBeanList.addAll(resDataGroup.getRetObj().getRows());
            if (mOfflineAdapter == null) {
                mOfflineAdapter = new OfflineAdapter(mContext, mBeanList);
                mListView.setAdapter(mOfflineAdapter);
            }
            mOfflineAdapter.notifyDataSetChanged();
        }
    }

    private Map<String, String> getParams() {
        //参数可以不要
        HashMap<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }
}
