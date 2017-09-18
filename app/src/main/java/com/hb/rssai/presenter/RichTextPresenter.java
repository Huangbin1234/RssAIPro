package com.hb.rssai.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.adapter.LikeAdapter;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.IRichTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RichTextPresenter extends BasePresenter<IRichTextView> {
    private Context mContext;
    private IRichTextView iRichTextView;
    private List<ResInformation.RetObjBean.RowsBean> resInfos = new ArrayList<>();
    private RecyclerView recyclerView;
    private LikeAdapter likeAdapter;
    private TextView mRtaTvGood;
    private TextView mRtaTvNotGood;
    private LinearLayout mRtaLlGood;
    private LinearLayout mRtaLlNotGood;

    public RichTextPresenter(Context mContext, IRichTextView iRichTextView) {
        this.mContext = mContext;
        this.iRichTextView = iRichTextView;
        initView();
    }

    private void initView() {
        recyclerView = iRichTextView.getRtaRecyclerView();
        mRtaTvGood = iRichTextView.getTvGood();
        mRtaTvNotGood = iRichTextView.getTvNotGood();

        mRtaLlGood = iRichTextView.getLlGood();
        mRtaLlNotGood = iRichTextView.getLlNotGood();
    }

    public void updateCount() {
        informationApi.updateCount(getUpdateParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateResult(resBase);
                }, this::loadError);
    }

    private void setUpdateResult(ResBase resBase) {
        //TODO 更新数量成功
    }

    private Map<String, String> getUpdateParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInformationId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public void getLikeByTitle() {
        informationApi.getLikeByTitle(getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setListResult(resInfo);
                }, this::loadError);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }


    private void setListResult(ResInformation resInformation) {
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                resInfos.addAll(resInformation.getRetObj().getRows());
                if (likeAdapter == null) {
                    likeAdapter = new LikeAdapter(mContext, resInfos);
                    likeAdapter.setOnItemClickedListener(rowsBean1 -> {
                        //TODO
                        Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
                        intent.putExtra(ContentActivity.KEY_TITLE, rowsBean1.getTitle());
                        intent.putExtra(ContentActivity.KEY_URL, rowsBean1.getLink());
                        intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean1.getId());
                        intent.putExtra("pubDate", rowsBean1.getPubTime());
                        intent.putExtra("whereFrom", rowsBean1.getWhereFrom());
                        intent.putExtra("abstractContent", rowsBean1.getAbstractContent());
                        intent.putExtra("id", rowsBean1.getId());
                        mContext.startActivity(intent);//将Intent传递给Activity
                    });
                    recyclerView.setAdapter(likeAdapter);
                } else {
                    likeAdapter.notifyDataSetChanged();
                }
            }
        } else {
            T.ShowToast(mContext, resInformation.getRetMsg());
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
//        String title = iRichTextView.getNewTitle();
        String title = "创业";
        String jsonParams = "{\"title\":\"" + title + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public void add() {
        collectionApi.add(getAddParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase);
                }, this::loadError);
    }

    private void setAddResult(ResBase resBase) {
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private Map<String, String> getAddParams() {
        Map<String, String> map = new HashMap<>();
        String newLink = iRichTextView.getNewLink();
        String newTitle = iRichTextView.getNewTitle();
        String informationId = iRichTextView.getInformationId();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"userId\":\"" + userId + "\",\"link\":\"" + newLink + "\",\"title\":\"" + newTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    public void updateEvaluateCount() {
        informationApi.updateEvaluateCount(getUpdateEvaluateParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateEvaluateResult(resBase);
                }, this::loadEvaluateError);
    }

    private void loadEvaluateError(Throwable throwable) {
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    public void getInformation() {
        informationApi.getInformation(getInfoParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setInfoResult(resInfo);
                }, this::loadError);
    }

    private void setInfoResult(ResInfo resInfo) {
        if (resInfo.getRetCode() == 0) {
            mRtaTvGood.setText("" + resInfo.getRetObj().getClickGood());
            mRtaTvNotGood.setText("" + resInfo.getRetObj().getClickNotGood());
        } else {
            T.ShowToast(mContext, resInfo.getRetMsg());
        }
    }

    private Map<String, String> getInfoParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInformationId();
        String jsonParams = "{\"informationId\":\"" + informationId + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void setUpdateEvaluateResult(ResBase resBase) {
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
        if (resBase.getRetCode() == 0) {
            //刷新点赞点贬数量
            SharedPreferencesUtil.setBoolean(mContext,iRichTextView.getInformationId(),true);
            getInformation();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private Map<String, String> getUpdateEvaluateParams() {
        Map<String, String> map = new HashMap<>();
        String informationId = iRichTextView.getInformationId();
        String evaluateType = iRichTextView.getEvaluateType();
        String jsonParams = "{\"informationId\":\"" + informationId + "\",\"evaluateType\":\"" + evaluateType + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
