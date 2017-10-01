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
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAddRssView;
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

                        if (imgs != null && imgs.size() > 0) {
                            imgs.clear();
                        }
                        if(null!=result.getData().getSimi().getXiangshi_info().getUrl()){
                            imgs.addAll(result.getData().getSimi().getXiangshi_info().getUrl());
                            openImageSelector();
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

    List<String> imgs = new ArrayList<>();
    /**
     * 弹出图片选择框
     */
    MaterialDialog materialDialog;
    ImageDialogAdapter dialogAdapter;

    private void openImageSelector() {
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
                if(!TextUtils.isEmpty(imgUrl)){
                    //TODO 更新
                    updateImage();
                }else{
                    T.ShowToast(mContext,"未选择任何图片，将采用默认图片封面。");
                }
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }
}
