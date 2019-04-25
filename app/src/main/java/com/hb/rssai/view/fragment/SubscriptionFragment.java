package com.hb.rssai.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SubscriptionPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISubscriptionView;
import com.hb.rssai.view.me.SearchActivity;
import com.hb.rssai.view.subscription.AddSourceActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;
import com.hb.rssai.view.subscription.SubscribeAllActivity;
import com.hb.rssai.view.widget.FullListView;
import com.hb.rssai.view.widget.FullyGridLayoutManager;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;

import static android.app.Activity.RESULT_OK;

public class SubscriptionFragment extends BaseFragment implements View.OnClickListener, RssSourceAdapter.onItemLongClickedListener, ISubscriptionView {
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.sf_recycler_view)
    RecyclerView mSfRecyclerView;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.sys_iv_add)
    ImageView mSysIvAdd;
    @BindView(R.id.sf_swipe)
    SwipeRefreshLayout mSfSwipe;
    @BindView(R.id.tv_sub_label)
    TextView mTvSubLabel;
    @BindView(R.id.sf_iv_all)
    ImageView mSfIvAll;
    @BindView(R.id.rl_ll)
    LinearLayout mRlLl;

    @BindView(R.id.sys_iv_scan)
    ImageView mSysIvScan;
    @BindView(R.id.tv_sub_right_all)
    TextView mTvSubRightAll;
    @BindView(R.id.sub_ll_all)
    LinearLayout mSubLlAll;
    @BindView(R.id.sf_nest_scrollview)
    NestedScrollView mSfNestScrollview;
    @BindView(R.id.tv_sub_label_topic)
    TextView mTvSubLabelTopic;
    @BindView(R.id.tv_sub_right_all_topic)
    TextView mTvSubRightAllTopic;
    @BindView(R.id.sf_iv_all_topic)
    ImageView mSfIvAllTopic;
    @BindView(R.id.sub_ll_all_topic)
    LinearLayout mSubLlAllTopic;
    @BindView(R.id.sf_recycler_view_topic)
    RecyclerView mSfRecyclerViewTopic;
    @BindView(R.id.rl_ll_topic)
    LinearLayout mRlLlTopic;
    @BindView(R.id.fs_ll_root)
    LinearLayout mFsLlRoot;
    Unbinder unbinder;

    private OnFragmentInteractionListener mListener;
    public final static int REQUEST_CODE = 1;
    private FullyGridLayoutManager mFullyGridLayoutManager;
    private FullyGridLayoutManager mFullyGridLayoutManagerTopic;
    private ResFindMore.RetObjBean.RowsBean rowsBean;
    private boolean isPrepared;
    private DialogAdapter dialogAdapter;
    private MaterialDialog materialDialog;

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        ((SubscriptionPresenter) mPresenter).refreshList();
        isPrepared = false;
        System.out.println("====lazyLoad====");

        String token = SharedPreferencesUtil.getString(getContext(), Constant.TOKEN, "");
        boolean isShowPop = SharedPreferencesUtil.getBoolean(getContext(), Constant.KEY_IS_SHOW_POP, false);
        if (!TextUtils.isEmpty(token) && !isShowPop) {
            initShowPop();
        }
    }

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (rView != null) {
            if (hidden) {
                mFsLlRoot.setFitsSystemWindows(false);
            } else {
                mFsLlRoot.setFitsSystemWindows(true);
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                rView.requestApplyInsets();
            } else {
                rView.requestFitSystemWindows();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_subscription));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new SubscriptionPresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_subscription;
    }

    View rView;

    @Override
    protected void initView(View rootView) {
        rView = rootView;
        // 卡片式
        mFullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 3);
        mSfRecyclerView.setLayoutManager(mFullyGridLayoutManager);

        mFullyGridLayoutManagerTopic = new FullyGridLayoutManager(getContext(), 3);
        mSfRecyclerViewTopic.setLayoutManager(mFullyGridLayoutManagerTopic);

        mSfRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 2), false));
        mSfRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mSfRecyclerView.setHasFixedSize(true);

        mSfRecyclerViewTopic.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 2), false));
        mSfRecyclerViewTopic.setNestedScrollingEnabled(false);//解决卡顿
        mSfRecyclerViewTopic.setHasFixedSize(true);

        mSfSwipe.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSfSwipe.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

    }

    View popupView;
    Dialog mPop;

    void initShowPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        popupView = inflater.inflate(R.layout.pop_add_subscription, null);
        mPop = builder.create();
        mPop.show();
        Window window = mPop.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setContentView(popupView);//自定义布局应该在这里添加，要在dialog.show()的后面
        window.setWindowAnimations(R.style.PopupAnimation);//
        window.setLayout(DisplayUtil.getMobileWidth(getActivity()) * 8 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        backgroundAlpha(0.5f);
        mPop.setOnDismissListener(dialogInterface -> backgroundAlpha(1f));
        Button pas_btn_sure = popupView.findViewById(R.id.pas_btn_sure);
        ImageView pas_iv_close = popupView.findViewById(R.id.pas_btn_close);
        pas_iv_close.setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
        });
        pas_btn_sure.setOnClickListener(view -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            Intent intent = new Intent(getActivity(), AddSourceActivity.class);
            getActivity().startActivity(intent);
        });

        SharedPreferencesUtil.setBoolean(getContext(), Constant.KEY_IS_SHOW_POP, true);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("====onActivityCreated====");
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }

    @Subscribe
    public void onEventMainThread(RssSourceEvent event) {
        if (event.getMessage() == 0) {
            ((SubscriptionPresenter) mPresenter).refreshList();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.sys_iv_add, R.id.sys_iv_scan, R.id.sys_iv_search, R.id.sub_ll_all, R.id.sub_ll_all_topic})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_add:
                startActivity(new Intent(getContext(), AddSourceActivity.class));
                break;
            case R.id.sys_iv_scan:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE);
                break;
            case R.id.sys_iv_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.sub_ll_all:
                Bundle bundle = new Bundle();
                bundle.putBoolean(SubscribeAllActivity.KEY_IS_TAG, true);
                startActivity(new Intent(getContext(), SubscribeAllActivity.class).putExtras(bundle));
                break;
            case R.id.sub_ll_all_topic:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean(SubscribeAllActivity.KEY_IS_TAG, false);
                startActivity(new Intent(getContext(), SubscribeAllActivity.class).putExtras(bundle2));
                break;
        }
    }


    @Override
    public void onItemLongClicked(ResFindMore.RetObjBean.RowsBean rowsBean) {
        this.rowsBean = rowsBean;
        openMenu();
    }

    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "置顶");
        map.put("id", 1);
        map.put("url", R.mipmap.ic_top);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "分享");
        map2.put("id", 2);
        map2.put("url", R.mipmap.ic_share);
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("name", "删除");
        map3.put("id", 3);
        map3.put("url", R.mipmap.ic_delete);
        list.add(map3);
        return list;
    }

    /**
     * 菜单对话框
     *
     * @return
     */


    private void openMenu() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("id").equals(1)) {
                    //TODO 置顶
                    materialDialog.dismiss();
                    ((SubscriptionPresenter) mPresenter).updateUsSort();
                } else if (list.get(position).get("id").equals(2)) {//分享
                    materialDialog.dismiss();
                    Intent intent = new Intent(getContext(), QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
                    intent.putExtra(QrCodeActivity.KEY_SUBSCRIBE_ID, rowsBean.getId());
                    intent.putExtra(QrCodeActivity.KEY_TITLE, rowsBean.getName());
                    intent.putExtra(QrCodeActivity.KEY_SUBSCRIBE_IMAGE, rowsBean.getImg());
                    intent.putExtra(QrCodeActivity.KEY_SUBSCRIBE_DEC, rowsBean.getAbstractContent());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + rowsBean.getLink()));
                    startActivity(intent);
                } else if (list.get(position).get("id").equals(3)) {
                    materialDialog.dismiss();
                    ((SubscriptionPresenter) mPresenter).delSubscription();
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new DialogAdapter(getContext(), list);
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
    public RecyclerView getRecyclerView() {
        return mSfRecyclerView;
    }

    @Override
    public RecyclerView getTopicRecyclerView() {
        return mSfRecyclerViewTopic;
    }

    @Override
    public SwipeRefreshLayout getSwipeLayout() {
        return mSfSwipe;
    }

    @Override
    public FullyGridLayoutManager getManager() {
        return mFullyGridLayoutManager;
    }

    @Override
    public Fragment getFragment() {
        return SubscriptionFragment.this;
    }

    @Override
    public String getUsId() {
        return rowsBean.getUsId();
    }

    @Override
    public String getSubscribeId() {
        return rowsBean.getId();
    }

    @Override
    public void update() {
        EventBus.getDefault().post(new RssSourceEvent(0));
    }

    @Override
    public NestedScrollView getNestScrollView() {
        return mSfNestScrollview;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE) {
                String info = Base64Util.getDecodeStr(data.getStringExtra("info"));
                if (info.startsWith(Constant.FLAG_RSS_SOURCE)) {
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, "分享资讯");
                    getContext().startActivity(intent);

                } else if (info.startsWith(Constant.FLAG_COLLECTION_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "收藏");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_COLLECTION_SOURCE, ""));
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_URL_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_URL_SOURCE, ""));
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_RSS_SOURCE)) {
                    RssSource rssSource = new RssSource();
                    rssSource.setName("订阅");
                    rssSource.setLink(info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    LiteOrmDBUtil.insert(rssSource);
                    // initData();
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, "分享资讯");
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_COLLECTION_SOURCE)) {
                    UserCollection userCollection = new UserCollection();
                    userCollection.setTitle("收藏");
                    userCollection.setLink(info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE + Constant.FLAG_COLLECTION_SOURCE, ""));
                    LiteOrmDBUtil.insert(userCollection);

                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE + Constant.FLAG_COLLECTION_SOURCE, ""));
                    getContext().startActivity(intent);

                } else if (info.startsWith(Constant.FLAG_PRESS_URL_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_PRESS_URL_SOURCE + Constant.FLAG_URL_SOURCE, ""));
                    getContext().startActivity(intent);
                }
            }
        }
    }
}
