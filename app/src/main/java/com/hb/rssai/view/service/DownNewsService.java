package com.hb.rssai.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.OfflineEvent;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.T;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.informationApi;

public class DownNewsService extends Service {

    private MyBinder mBinder = new MyBinder();
    private HashMap<Integer, String> groupIds = new HashMap<>();
    private OfflineEvent mOfflineEvent = new OfflineEvent();
    private ResInformation.RetObjBean.RowsBean info;

    public DownNewsService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

        public void execute(String params, HashMap<Integer, String> groupIds) {
            //TODO 开始执行下载任务
            LiteOrmDBUtil.deleteAll(Information.class);
            downLoadData(params, groupIds);
        }
    }


    private void downLoadData(String groupData, HashMap<Integer, String> groupIds) {
        this.groupIds = groupIds;
        if (!TextUtils.isEmpty(groupData)) {
            //开始写入数据
            String[] groups = groupData.split(",");
            for (int i = 0; i < groups.length; i++) {
                getList(groups[i]);
            }
        } else {
            T.ShowToast(this, "没有选择任何频道");
        }
    }

    public void getList(String group) {
        informationApi.getList(getListParams(group))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInformation -> setListResult(resInformation, group), this::loadError);
    }


    private void setListResult(ResInformation resInformation, String group) {
        ResInformation.RetObjBean bean = resInformation.getRetObj();
        if (bean == null || bean.getRows() == null) {
            return;
        }
        //先清除所有数据在插入
        LiteOrmDBUtil.deleteAll(Information.class);
        int len = bean.getRows().size();
        for (int i = 0; i < len; i++) {
            info = bean.getRows().get(i);
            Information rowBean = new Information();
            rowBean.setAuthor(info.getAuthor());
            rowBean.setPubTime(info.getPubTime());
            rowBean.setDataType(info.getDataType());
            rowBean.setAbstractContent(info.getAbstractContent());
            rowBean.setCount(info.getCount());
            rowBean.setLink(info.getLink());
            rowBean.setWhereFrom(info.getWhereFrom());
            rowBean.setTitle(info.getTitle());
            rowBean.setContent(info.getContent());
            rowBean.setDeleteFlag(info.isDeleteFlag());
            rowBean.setImageUrls(info.getImageUrls());
            rowBean.setId(info.getId());
            rowBean.setOprTime(info.getOprTime());
            rowBean.setClickGood(info.getClickGood());
            rowBean.setClickNotGood(info.getClickNotGood());
            if (i == 5 || i == 10 || i == 15 || i == len - 1) {
                mOfflineEvent.setMaxVal(len);
                mOfflineEvent.setProgressVal(i + 1);
                mOfflineEvent.setId(groupIds.get(Integer.valueOf(group)));
                EventBus.getDefault().post(mOfflineEvent);
            }
            LiteOrmDBUtil.insert(rowBean);
        }
    }

    private Map<String, String> getListParams(String group) {
        Map<String, String> map = new HashMap<>();
        int dataType = Integer.valueOf(group);
        String jsonParams = "{\"dataType\":\"" + dataType + "\",\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }
}
