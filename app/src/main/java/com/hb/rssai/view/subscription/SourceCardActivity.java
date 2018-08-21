package com.hb.rssai.view.subscription;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hb.rssai.R;
import com.hb.rssai.adapter.SourceListCardAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SourceListPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISourceListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.adapter.rxjava.HttpException;

public class SourceCardActivity extends BaseActivity implements ISourceListView {

    public static final String KEY_LINK = "rssLink";
    public static final String KEY_TITLE = "rssTitle";
    public static final String KEY_IS_CHECK = "isCheck";
    public static String KEY_SUBSCRIBE_ID = "subscribeId";
    public static String KEY_IMAGE = "image_url";
    public static String KEY_DESC = "desc";
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.sla_tv_empty)
    TextView mSlaTvEmpty;
    @BindView(R.id.sla_ll)
    LinearLayout mSlaLl;
    @BindView(R.id.sla_recycler_view)
    RecyclerView mSlaRecyclerView;
    @BindView(R.id.sla_swipe_layout)
    SwipeRefreshLayout mSlaSwipeLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.sla_iv_logo)
    ImageView mSlaIvLogo;
    @BindView(R.id.sla_nest_view)
    NestedScrollView mSlaNestView;
    @BindView(R.id.sla_iv_to_bg)
    ImageView mSlaIvToBg;
    @BindView(R.id.sla_tv_title)
    TextView mSlaTvTitle;
    @BindView(R.id.sla_tv_desc)
    TextView mSlaTvDesc;
    @BindView(R.id.sys_iv_share)
    ImageView mSysIvShare;
    @BindView(R.id.sla_iv_subscribe)
    ImageView mSlaIvSubscribe;

    private LinearLayoutManager mLayoutManager;
    private String linkValue;
    private String titleValue = "";
    private String subscribeId = "";
    private String imageLogo = "";
    private String desc = "";
    private boolean isCheck = false;
    private boolean isEnd = false, isLoad = false;
    private int pageNum = 1;

    private SourceListCardAdapter cardAdapter;
    private List<ResInformation.RetObjBean.RowsBean> infoList = new ArrayList<>();
    private List<List<ResCardSubscribe.RetObjBean.RowsBean>> infoListCard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshList();
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_source_card;
    }

    @Override
    public void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void initView() {
        if (isCheck) {
            mSlaIvSubscribe.setImageResource(R.mipmap.ic_subscribe_cancel);
        } else {
            mSlaIvSubscribe.setImageResource(R.mipmap.ic_subscribe_add);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mSlaRecyclerView.setLayoutManager(mLayoutManager);
        mSlaRecyclerView.setHasFixedSize(true);
        mSlaRecyclerView.setNestedScrollingEnabled(false);
        mSlaSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSlaSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mSysIvShare.setOnClickListener(v -> {
            Intent intent = new Intent(SourceCardActivity.this, QrCodeActivity.class);
            intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
            intent.putExtra(QrCodeActivity.KEY_TITLE, titleValue);
            intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + linkValue));
            startActivity(intent);
        });
        mSlaIvSubscribe.setOnClickListener(v -> {
            //TODO 订阅或取消
            ((SourceListPresenter) mPresenter).subscribe(v);
        });

        //TODO 设置下拉刷新
        mSlaSwipeLayout.setOnRefreshListener(() -> refreshList());
        //TODO 设置上拉加载更多
        mSlaNestView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(0) != null && isBottomForNestedScrollView(v, scrollY)) {
                // 加载更多
                if (!isEnd && !isLoad) {
                    mSlaSwipeLayout.setRefreshing(true);
                    pageNum++;
                    ((SourceListPresenter) mPresenter).getListCardById();
                }
            }
        });
    }


    // TODO: 判断是不是在底部
    private boolean isBottomForNestedScrollView(NestedScrollView v, int scrollY) {
        return (scrollY + v.getHeight()) == (v.getChildAt(0).getHeight() + v.getPaddingTop() + v.getPaddingBottom());
    }

    /**
     * 刷新数据
     */
    public void refreshList() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;
        if (infoList != null) {
            infoList.clear();
        }
        if (infoListCard != null) {
            infoListCard.clear();
        }
        ((SourceListPresenter) mPresenter).getListCardById();
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            linkValue = bundle.getString(KEY_LINK, "");
            titleValue = bundle.getString(KEY_TITLE, "");
            subscribeId = bundle.getString(KEY_SUBSCRIBE_ID, "");
            imageLogo = bundle.getString(KEY_IMAGE, "");
            desc = bundle.getString(KEY_DESC, "");
            isCheck = bundle.getBoolean(KEY_IS_CHECK, false);
        }
        HttpLoadImg.loadImg(this, imageLogo, mSlaIvLogo);
        Glide.with(this).load(imageLogo).bitmapTransform(new BlurTransformation(this, 20, 2), new CenterCrop(this)).into(mSlaIvToBg);

        mSysTvTitle.setText(titleValue);
        mSlaTvDesc.setText(desc);
        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);//设置收缩后标题的位置
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.LEFT);////设置展开后标题的位置
        mCollapsingToolbarLayout.setTitle(titleValue);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
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
    }

    @Override
    protected BasePresenter createPresenter() {
        return new SourceListPresenter(this, this);
    }

    @Override
    public String getSubscribeId() {
        return subscribeId;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void loadError(Throwable throwable) {
        mSlaLl.setVisibility(View.GONE);
        mSlaSwipeLayout.setRefreshing(false);
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).response().code() == 401) {
                T.ShowToast(this, Constant.MSG_NO_LOGIN);
            } else {
                T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
            }
        } else {
            T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
        }
    }

    @Override
    public void setListCardResult(ResCardSubscribe resCardSubscribe) {
        //TODO 填充数据
        mSlaLl.setVisibility(View.GONE);
        isLoad = false;
        mSlaSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resCardSubscribe.getRetCode() == 0) {
            if (resCardSubscribe.getRetObj().getRows() != null && resCardSubscribe.getRetObj().getRows().size() > 0) {
                infoListCard.addAll(resCardSubscribe.getRetObj().getRows());
                if (cardAdapter == null) {
                    cardAdapter = new SourceListCardAdapter(this, infoListCard);
                    mSlaRecyclerView.setAdapter(cardAdapter);
                } else {
                    cardAdapter.notifyDataSetChanged();
                }
            }
            int sum = 0;
            for (List<ResCardSubscribe.RetObjBean.RowsBean> rBean : infoListCard) {
                sum += rBean.size();
            }

            if (sum >= resCardSubscribe.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(this, resCardSubscribe.getRetMsg());
        }
    }
}
