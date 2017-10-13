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

    public DownNewsService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

        public void execute(String params) {
            //TODO 开始执行下载任务
            LiteOrmDBUtil.deleteAll(Information.class);
            downLoadData(params);
        }
    }

    private void downLoadData(String groupDatas) {
        if (!TextUtils.isEmpty(groupDatas)) {
            //开始写入数据
            String[] groups = groupDatas.split(",");
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
                .subscribe(resInformation -> {
                    setListResult(resInformation, group);
                }, this::loadError);
    }

    OfflineEvent mOfflineEvent = new OfflineEvent(0);

    private void setListResult(ResInformation resInformation, String group) {
        if (resInformation.getRetObj() == null || resInformation.getRetObj().getRows() == null) {
            return;
        }

        for (int i = 0; i < resInformation.getRetObj().getRows().size(); i++) {
            ResInformation.RetObjBean.RowsBean info = resInformation.getRetObj().getRows().get(i);
            Information rowBean = new Information();
            rowBean.setAuthor(info.getAuthor());
            rowBean.setPubTime(info.getPubTime());
            rowBean.setDataType(info.getDataType());
            rowBean.setAbstractContent(info.getAbstractContent());
            rowBean.setCount((int) info.getCount());
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

            LiteOrmDBUtil.insert(rowBean);
        }

        mOfflineEvent.setMessage(resInformation.getRetObj().getRows().size());
        mOfflineEvent.setContent(group);
        EventBus.getDefault().post(mOfflineEvent);

    }

    private Map<String, String> getListParams(String group) {
        Map<String, String> map = new HashMap<>();
        int dataType = Integer.valueOf(group);
        String jsonParams = "{\"dataType\":\"" + dataType + "\",\"page\":\"" + 1 + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        System.out.println(map);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }
}
