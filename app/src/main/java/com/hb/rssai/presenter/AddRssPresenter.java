package com.hb.rssai.presenter;

import android.content.Context;
import android.util.Log;

import com.hb.rssai.api.FindApi;
import com.hb.rssai.bean.ResBDJson;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAddRssView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class AddRssPresenter extends BasePresenter<IAddRssView> {
    private Context mContext;
    private IAddRssView iAddRssView;

    public AddRssPresenter(Context context, IAddRssView iAddRssView) {
        mContext = context;
        this.iAddRssView = iAddRssView;
    }

    public void addRss() {
        findApi.addRssByUser(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setAddResult(resBase);
                }, this::loadError);
    }

    public void updateImage() {
        findApi.updateImage(getUpdateImageParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resBase -> {
                    setUpdateImageResult(resBase);
                }, this::loadError);
    }

    private void setUpdateImageResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            String rssTitle = iAddRssView.getRssTitle();
            T.ShowToast(mContext, rssTitle + "自动配图成功");
        } else {
            T.ShowToast(mContext, resBase.getRetMsg());
        }
    }

    private Map<String, String> getUpdateImageParams() {
        Map<String, String> map = new HashMap<>();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"subscribeId\":\"" + subscribeId + "\",\"imgUrl\":\"" + imgUrl + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String rssLink = iAddRssView.getRssLink();
        String rssTitle = iAddRssView.getRssTitle();
        String userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
        String jsonParams = "{\"userId\":\"" + userId + "\",\"link\":\"" + rssLink + "\",\"title\":\"" + rssTitle + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);

        return map;
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private String subscribeId = "";
    private String imgUrl = "";

    private void setAddResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            subscribeId = resBase.getRetObj().toString();
            EventBus.getDefault().post(new RssSourceEvent(0));
            getBDInfo();
        }
        T.ShowToast(mContext, resBase.getRetMsg());
    }

    private void getBDInfo() {
        //开始自动配图
        String keyword = iAddRssView.getRssTitle();
        String url = null;
        url = "[{\"ct\":\"simi\",\"cv\":[{\"provider\":\"piclist\",\"Https\":\"1\",\"query\":\"" + keyword + "\",\"SimiCs\":\"4013908033,1059441773\",\"type\":\"card\",\"pn\":\"0\",\"rn\":\"6\",\"srctype\":\"\",\"bdtype\":\"\",\"os\":\"1462401673,115618024\"}]}]";
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
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
                        imgUrl = result.getData().getSimi().getXiangshi_info().getUrl().get(0);//获取到源图片
                        updateImage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResBDJson> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
