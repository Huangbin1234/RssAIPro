package com.hb.rssai.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hb.rssai.R;
import com.hb.rssai.adapter.ImageDialogAdapter;
import com.hb.rssai.api.FindApi;
import com.hb.rssai.bean.ResBDJson;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.AddSourcesContract;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.view.widget.FullGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class AddRssPresenter extends BasePresenter<AddSourcesContract.View> implements AddSourcesContract.Presenter {
    Context mContext;
    AddSourcesContract.View mView;
    String subscribeId = "";
    String imgUrl = "";
    List<String> imgs = new ArrayList<>();
    /**
     * 弹出图片选择框
     */
    MaterialDialog materialDialog;
    ImageDialogAdapter dialogAdapter;

    public AddRssPresenter(Context context, AddSourcesContract.View mView) {
        mContext = context;
        this.mView = mView;
    }

    @Override
    public void getList(int page) {
        themeApi.list(getListParams(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resTheme -> {
                    mView.showListResult(resTheme);
                }, this::loadError);
    }


    @Override
    public void addRss(String rssLink,String rssTitle,String userId) {
        findApi.addRssByUser(getParams(rssLink,rssTitle,userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase,rssTitle,userId);
                }, this::loadError);
    }

    @Override
    public void addOpmlRss(String rssLink,String rssTitle,String userId) {
        findApi.addRssByUser(getParams(rssLink,rssTitle, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddOpmlResult(resBase);
                }, this::loadError);
    }

    @Override
    public void updateImage(String rssTitle,String userId) {
        findApi.updateImage(getUpdateImageParams(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateImageResult(resBase,rssTitle);
                }, this::loadError);
    }

    private void setAddOpmlResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            mView.showMsg("数据采集将在5分钟内开始，请稍等片刻！");

        } else {
            mView.showMsg(resBase.getRetMsg());
        }
    }

    private void setUpdateImageResult(ResBase resBase,String rssTitle) {
        if (resBase.getRetCode() == 0) {
            mView.showMsg(rssTitle + "自动配图成功");

        } else {
            mView.showMsg(resBase.getRetMsg());
        }
    }

    private Map<String, String> getListParams(int page) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"page\":\"" + page + "\",\"size\":\"" + Constant.PAGE_SIZE + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getUpdateImageParams(String userId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\",\"imgUrl\":\"" + imgUrl + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getParams(String rssLink,String rssTitle,String userId) {
        Map<String, String> map = new HashMap<>();
        String jsonParams = "{\"userId\":\"" + userId + "\",\"link\":\"" + rssLink + "\",\"title\":\"" + rssTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                mView.showMsg(Constant.MSG_NO_LOGIN);
            } else {
                mView.showMsg(Constant.MSG_NETWORK_ERROR);
            }
        } else {
            mView.showMsg(Constant.MSG_NETWORK_ERROR);
        }
    }

    private void setAddResult(ResBase resBase,String rssTitle,String userId) {
        if (resBase.getRetCode() == 0) {
            subscribeId = resBase.getRetObj().toString();
            EventBus.getDefault().post(new RssSourceEvent(0));
            getBDInfo(rssTitle,userId);
            mView.showAddSuccess();
        }
        mView.showMsg(resBase.getRetMsg());
        mView.showMsg("数据采集将在5分钟内开始，请稍等片刻！");
    }

    private void getBDInfo(String rssTitle, String userId) {
        //开始自动配图
        String url;
        url = "[{\"ct\":\"simi\",\"cv\":[{\"provider\":\"piclist\",\"Https\":\"1\",\"query\":\"" + rssTitle + "\",\"SimiCs\":\"4013908033,1059441773\",\"type\":\"card\",\"pn\":\"0\",\"rn\":\"6\",\"srctype\":\"\",\"bdtype\":\"\",\"os\":\"1462401673,115618024\"}]}]";
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            Log.i("RetrofitLog", "retrofitBack = " + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://image.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        System.out.println("https://image.baidu.com/cardserver/search?para" + url);
        FindApi service = retrofit.create(FindApi.class);
        Call<ResBDJson> callShops = service.getRssImage(url);

        callShops.enqueue(new Callback<ResBDJson>() {
            @Override
            public void onResponse(Call<ResBDJson> call, Response<ResBDJson> response) {
                if (response.isSuccessful()) {
                    ResBDJson result = response.body();
                    if ("0".equals(result.getStatus().getCode())) {

                        if (imgs != null && imgs.size() > 0) {
                            imgs.clear();
                        }
                        if (null != result.getData().getSimi().getXiangshi_info() && null != result.getData().getSimi().getXiangshi_info().getUrl()) {
                            imgs.addAll(result.getData().getSimi().getXiangshi_info().getUrl());
                            openImageSelector( rssTitle,  userId);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResBDJson> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void openImageSelector(String rssTitle, String userId) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.view_filter, null);
            FullGridView listView = (FullGridView) view.findViewById(R.id.dialog_gridView);

            listView.setOnItemClickListener((parent, v, position, id) -> {
                //点击选择
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ImageDialogAdapter.ViewHolder holder = (ImageDialogAdapter.ViewHolder) v.getTag();
                // 改变CheckBox的状态
                holder.dialog_item_chk.toggle();
                //全部置为false
                for (int i = 0; i < imgs.size(); i++) {
                    ImageDialogAdapter.getIsSelected().put(i, false);
                }
                // 将CheckBox的选中状况记录下来
                ImageDialogAdapter.getIsSelected().put(position, holder.dialog_item_chk.isChecked());
                if (dialogAdapter != null) {
                    dialogAdapter.notifyDataSetChanged();
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new ImageDialogAdapter(mContext, imgs);
                listView.setAdapter(dialogAdapter);
            }
            dialogAdapter.notifyDataSetChanged();
            materialDialog.setContentView(view).setTitle(Constant.TIPS_IMAGE_SELECTOR).setNegativeButton("关闭", v -> {
                materialDialog.dismiss();
            }).setPositiveButton("选择", v -> {
                for (int i = 0; i < imgs.size(); i++) {
                    if (ImageDialogAdapter.getIsSelected().get(i)) {
                        imgUrl = imgs.get(i);//获取到源图片
                        break;
                    }
                }
                if (!TextUtils.isEmpty(imgUrl)) {
                    //TODO 更新
                    updateImage(rssTitle,userId);
                } else {
                    mView.showMsg("未选择任何图片，将采用默认图片封面。");
                }
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    @Override
    public void start() {

    }
}
