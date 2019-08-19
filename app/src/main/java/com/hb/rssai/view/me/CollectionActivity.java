package com.hb.rssai.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.rssai.R;
import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.CollectionPresenter;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.iView.ICollectionView;
import com.hb.rssai.view.widget.FullListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;

public class CollectionActivity extends BaseActivity implements CollectionAdapter.onItemLongClickedListener, ICollectionView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.coll_tv_empty)
    TextView mCollTvEmpty;
    @BindView(R.id.coll_ll)
    LinearLayout mCollLl;
    @BindView(R.id.coll_recycler_view)
    RecyclerView mCollRecyclerView;
    @BindView(R.id.coll_swipe_layout)
    SwipeRefreshLayout mCollSwipeLayout;
    @BindView(R.id.include_no_data)
    View includeNoData;
    @BindView(R.id.include_load_fail)
    View includeLoadFail;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;

    private LinearLayoutManager mLayoutManager;
    private String collectionId = "";
    private CollectionAdapter adapter;
    private ResCollection.RetObjBean.RowsBean newRowsBean;
    private List<ResCollection.RetObjBean.RowsBean> resCollections = new ArrayList<>();
    private int pageNum = 1;
    private boolean isEnd = false, isLoad = false;
    private ResCollection.RetObjBean.RowsBean bean;
    private String infoId = "";

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    /**
     * 取消对话框
     *
     * @return
     */
    private DialogAdapter dialogAdapter;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CollectionPresenter) mPresenter).getList();
    }

    @Override
    protected void onRefresh() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;
        mCollSwipeLayout.setRefreshing(true);
        ((CollectionPresenter) mPresenter).getList();
    }

    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mCollSwipeLayout.setRefreshing(true);
            pageNum++;
            ((CollectionPresenter) mPresenter).getList();
        }
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mCollRecyclerView.setLayoutManager(mLayoutManager);
        mCollSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mCollSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mLlfBtnReTry.setOnClickListener(v -> onRefresh());

        mCollSwipeLayout.setOnRefreshListener(() -> onRefresh());
        //TODO 设置上拉加载更多
        mCollRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mCollSwipeLayout.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
                    // 加载更多
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        initShare();
    }

    private void initShare() {
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(CollectionActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                //.addButton("umeng_sharebutton_copy", "umeng_sharebutton_copy", "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
//                            Toast.makeText(CollectionActivity.this, "复制文本按钮", Toast.LENGTH_LONG).show();
//                        } else
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            StringUtil.copy(newRowsBean.getLink(), CollectionActivity.this);
                            Toast.makeText(CollectionActivity.this, "复制链接按钮", Toast.LENGTH_LONG).show();
                        } else if (share_media == SHARE_MEDIA.SMS) {
                            new ShareAction(CollectionActivity.this).withText("来自ZR分享面板")
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        } else {
                            String url = newRowsBean.getLink();
                            UMWeb web = new UMWeb(url);
                            web.setTitle(newRowsBean.getTitle());
                            web.setDescription(newRowsBean.getTitle());
                            web.setThumb(new UMImage(CollectionActivity.this, R.mipmap.ic_launcher));
                            new ShareAction(CollectionActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });


    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<CollectionActivity> mActivity;

        private CustomShareListener(CollectionActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_collection;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_coll_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new CollectionPresenter(this);
    }

    @Override
    public void onItemLongClicked(ResCollection.RetObjBean.RowsBean rowsBean) {
        newRowsBean = rowsBean;
        sureCollection(rowsBean);
    }

    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "分享");
        map.put("collectionId", 1);
        map.put("url", R.mipmap.ic_share);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "删除");
        map2.put("collectionId", 2);
        map2.put("url", R.mipmap.ic_delete);
        list.add(map2);
        return list;
    }

    private void sureCollection(ResCollection.RetObjBean.RowsBean rowsBean) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("collectionId").equals(1)) {
                    //TODO 分享
                    materialDialog.dismiss();
//                    Intent intent = new Intent(CollectionActivity.this, QrCodeActivity.class);
//                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[1]);
//                    intent.putExtra(QrCodeActivity.KEY_TITLE, newRowsBean.getTitle());
//                    intent.putExtra(QrCodeActivity.KEY_INFO_ID, newRowsBean.getInformationId());
//                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_COLLECTION_SOURCE + newRowsBean.getLink()));
//                    startActivity(intent);
                    ShareBoardConfig config = new ShareBoardConfig();
                    config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                    mShareAction.open(config);
                } else if (list.get(position).get("collectionId").equals(2)) {
                    materialDialog.dismiss();
                    LiteOrmDBUtil.deleteWhere(UserCollection.class, "collectionId", new String[]{"" + newRowsBean.getId()});
                    //adapter.notifyDataSetChanged();
                    collectionId = newRowsBean.getId();
                    ((CollectionPresenter) mPresenter).del();
                    //T.ShowToast(CollectionActivity.this, "删除成功！");
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new DialogAdapter(this, list);
                listView.setAdapter(dialogAdapter);
            }
            dialogAdapter.notifyDataSetChanged();
            materialDialog.setContentView(view).setTitle(Constant.TIPS_SYSTEM).setNegativeButton("关闭", v -> {
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    @Override
    public void loadError(Throwable throwable) {
        includeLoadFail.setVisibility(View.VISIBLE);
        includeNoData.setVisibility(View.GONE);
        mCollRecyclerView.setVisibility(View.GONE);

        mCollSwipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void setDelResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            //TODO 确认删除
            onRefresh();
        } else {
            T.ShowToast(this, resBase.getRetMsg());
        }
    }

    @Override
    public void setListResult(ResCollection resCollection) {
        if (resCollections != null && pageNum == 1) {
            resCollections.clear();
        }
        isLoad = false;
        mCollSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resCollection.getRetCode() == 0) {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.GONE);
            mCollRecyclerView.setVisibility(View.VISIBLE);

            if (resCollection.getRetObj().getRows() != null && resCollection.getRetObj().getRows().size() > 0) {
                resCollections.addAll(resCollection.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new CollectionAdapter(this, resCollections);
                    mCollRecyclerView.setAdapter(adapter);
                    adapter.setMyOnItemClickedListener(new CollectionAdapter.MyOnItemClickedListener() {
                        @Override
                        public void onItemClicked(ResCollection.RetObjBean.RowsBean rowsBean) {
                            bean = rowsBean;
                            if (!TextUtils.isEmpty(rowsBean.getInformationId())) {
                                infoId = rowsBean.getInformationId();
                                ((CollectionPresenter) mPresenter).getInformation(); //获取消息
                            } else {
                                Intent intent = new Intent(CollectionActivity.this, ContentActivity.class);
                                intent.putExtra(ContentActivity.KEY_URL, rowsBean.getLink());
                                intent.putExtra(ContentActivity.KEY_TITLE, rowsBean.getTitle());
                                intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean.getInformationId());
                                startActivity(intent);
//                                T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
                            }
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (resCollections.size() == resCollection.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resCollection.getRetCode() == 10013) {//暂无数据
            includeNoData.setVisibility(View.VISIBLE);
            includeLoadFail.setVisibility(View.GONE);
            mCollRecyclerView.setVisibility(View.GONE);
        } else {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.VISIBLE);
            mCollRecyclerView.setVisibility(View.GONE);
            T.ShowToast(this, resCollection.getRetMsg());
        }
    }

    @Override
    public void setInfoResult(ResInfo resInfo) {
        if (resInfo.getRetCode() == 0) {
            Intent intent = new Intent(this, RichTextActivity.class);
            intent.putExtra("abstractContent", resInfo.getRetObj().getAbstractContent());
            intent.putExtra(ContentActivity.KEY_TITLE, resInfo.getRetObj().getTitle());
            intent.putExtra("whereFrom", resInfo.getRetObj().getWhereFrom());
            intent.putExtra("pubDate", resInfo.getRetObj().getPubTime());
            intent.putExtra("url", resInfo.getRetObj().getLink());
            intent.putExtra("id", resInfo.getRetObj().getId());
            intent.putExtra("clickGood", resInfo.getRetObj().getClickGood());
            intent.putExtra("clickNotGood", resInfo.getRetObj().getClickNotGood());
            startActivity(intent);
        } else {
//            T.ShowToast(mContext, "抱歉，文章链接已失效，无法打开！");
            Intent intent = new Intent(this, ContentActivity.class);
            intent.putExtra(ContentActivity.KEY_URL, bean.getLink());
            intent.putExtra(ContentActivity.KEY_TITLE, bean.getTitle());
            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, bean.getInformationId());
            startActivity(intent);
        }
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public String getInfoId() {
        return infoId;
    }

    @Override
    public String getCollectionId() {
        return collectionId;
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }
}
