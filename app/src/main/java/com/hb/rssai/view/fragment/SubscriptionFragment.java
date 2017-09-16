package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.CardAdapter;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.adapter.RssSourceAdapter;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.bean.RssChannel;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SubscriptionPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ISubscriptionView;
import com.hb.rssai.view.subscription.AddSourceActivity;
import com.hb.rssai.view.subscription.SourceListActivity;
import com.hb.rssai.view.subscription.SubListActivity;
import com.hb.rssai.view.widget.FullListView;
import com.hb.rssai.view.widget.FullyGridLayoutManager;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.rss.bean.Website;
import com.rss.util.FeedReader;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;

import static android.app.Activity.RESULT_OK;

public class SubscriptionFragment extends BaseFragment implements View.OnClickListener, RssSourceAdapter.onItemLongClickedListener, ISubscriptionView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    Unbinder unbinder;
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
    //@BindView(R.id.index_function_gridview)
    // FullGridView mIndexFunctionGridView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //    private GridLayoutManager mGridLayoutManager;
    private RssSourceAdapter mRssSourceAdapter;
    private List<RssSource> list = new ArrayList<>();

    CardAdapter mCardAdapter;
    public final static int REQUEST_CODE = 1;
    private FullyGridLayoutManager mFullyGridLayoutManager;
    private FullyGridLayoutManager mFullyGridLayoutManagerTopic;
    ResFindMore.RetObjBean.RowsBean rowsBean;

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // 注册
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
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

    @Override
    protected void initView(View rootView) {
        // 卡片式
        // mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mFullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 3);
        mSfRecyclerView.setLayoutManager(mFullyGridLayoutManager);

        mFullyGridLayoutManagerTopic = new FullyGridLayoutManager(getContext(), 3);
        mSfRecyclerViewTopic.setLayoutManager(mFullyGridLayoutManagerTopic);

        mSfRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 10), false));
        mSfRecyclerView.setNestedScrollingEnabled(false);//解决卡顿
        mSfRecyclerView.setHasFixedSize(true);

        mSfRecyclerViewTopic.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(getContext(), 10), false));
        mSfRecyclerViewTopic.setNestedScrollingEnabled(false);//解决卡顿
        mSfRecyclerViewTopic.setHasFixedSize(true);

        mSfSwipe.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSfSwipe.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //TODO 设置下拉刷新
//        mSfSwipe.setOnRefreshListener(() -> initData());

//        mIndexFunctionGridView.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(getContext(), SourceListActivity.class);
//            intent.putExtra(SourceListActivity.KEY_LINK, list.get(position).getLink());
//            intent.putExtra(SourceListActivity.KEY_TITLE, list.get(position).getName());
//            getContext().startActivity(intent);
//        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SubscriptionPresenter) mPresenter).refreshList();
//        initData();
    }

//    private void initData() {
//        if (list != null && list.size() > 0) {
//            list.clear();
//        }
//        if (mRssSourceAdapter != null) {
//            mRssSourceAdapter.notifyDataSetChanged();
//        }
////        List<RssSource> dbList = LiteOrmDBUtil.getQueryAllLengthSort(RssSource.class,0,6,"sort");
//        List<RssSource> dbList = LiteOrmDBUtil.getQueryAllSort(RssSource.class, "sort");
//        if (dbList == null || dbList.size() <= 0) {
//            return;
//        }
//        list.addAll(dbList);
//        new ReadRssTask().execute();
//    }

    @Subscribe
    public void onEventMainThread(RssSourceEvent event) {
        if (event.getMessage() == 0) {
            ((SubscriptionPresenter) mPresenter).refreshList();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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


    @OnClick({R.id.sys_iv_add, R.id.sys_iv_scan, R.id.sub_ll_all, R.id.sub_ll_all_topic})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sys_iv_add:
//                startActivity(new Intent(getContext(), HotTagActivity.class));
                startActivity(new Intent(getContext(), AddSourceActivity.class));
                break;
            case R.id.sys_iv_scan:
//                startActivity(new Intent(getContext(), AddSourceActivity.class));
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE);
                break;
            case R.id.sub_ll_all:
                Bundle bundle = new Bundle();
                bundle.putBoolean(SubListActivity.KEY_IS_TAG, true);
                startActivity(new Intent(getContext(), SubListActivity.class).putExtras(bundle));
                break;
            case R.id.sub_ll_all_topic:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean(SubListActivity.KEY_IS_TAG, false);
                startActivity(new Intent(getContext(), SubListActivity.class).putExtras(bundle2));
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
    DialogAdapter dialogAdapter;
    MaterialDialog materialDialog;

    private void openMenu() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = (FullListView) view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("id").equals(1)) {
                    //TODO 置顶
                    materialDialog.dismiss();
                    rowsBean.setSort(new Date().getTime());
//                    LiteOrmDBUtil.update(rowsBean);
//                    ((SubscriptionPresenter)mPresenter).getUserSubscribeList();
                } else if (list.get(position).get("id").equals(2)) {
                    materialDialog.dismiss();
                    Intent intent = new Intent(getContext(), QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[0]);
                    intent.putExtra(QrCodeActivity.KEY_TITLE, rowsBean.getName());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_RSS_SOURCE + rowsBean.getLink()));
                    startActivity(intent);
                } else if (list.get(position).get("id").equals(3)) {
                    materialDialog.dismiss();
//                    LiteOrmDBUtil.deleteWhere(RssSource.class, "id", new String[]{"" + rowsBean.getCollectionId()});
//                    initData();
                    ((SubscriptionPresenter) mPresenter).delSubscription();
//                    T.ShowToast(getContext(), "删除成功！");
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

    @Override
    protected void lazyLoad() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 取消注册
        EventBus.getDefault().unregister(this);
        mListener = null;
    }

//    class ReadRssTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            readRssXml();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            mSfSwipe.setRefreshing(false);
////            if (list.size() < 1) {
////                if (mCardAdapter == null) {
////                    mCardAdapter = new CardAdapter(getContext(), list);
////                    mSfRecyclerView.setAdapter(mCardAdapter);
////                } else {
////                    mCardAdapter.notifyDataSetChanged();
////                }
////                cardSetting();
////            } else {
//            if (mRssSourceAdapter == null) {
//                mRssSourceAdapter = new RssSourceAdapter(getContext(), list, SubscriptionFragment.this);
//                mSfRecyclerView.setAdapter(mRssSourceAdapter);
//            } else {
//                mRssSourceAdapter.notifyDataSetChanged();
//            }
////            }
//        }
//    }


    private void readRssXml() {
        List<Website> websiteList = new ArrayList<>();
        for (RssSource rssSource : list) {
            Website website = new Website();
            website.setUrl(rssSource.getLink());
            website.setName(rssSource.getName());
            website.setOpen("true");
            website.setEncoding("UTF-8");
            website.setStartTag("");
            website.setEndTag("");
            website.setFid("" + rssSource.getId());
            websiteList.add(website);
        }
        for (Website we : websiteList) {
            rssInsert(we);
        }
    }


    /**
     * 可以选择插入到数据库
     *
     * @param website
     */
    String[] urls = {"http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170531/home/20170531103230.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg", "http://icon.nipic.com/BannerPic/20170509/home/20170509164717.jpg", "http://icon.nipic.com/BannerPic/20170619/home/20170619151644.jpg"};

    public void rssInsert(Website website) {
        try {
//            List<RSSItemBean> rssTempList = new FeedReader().getContent(website);
            RssChannel rssTempList = new FeedReader().getContent(website);
            if (rssTempList != null && rssTempList.getRSSItemBeen().size() > 0) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    if (website.getFid().equals("" + list.get(i).getId())) {
                        list.get(i).setCount(rssTempList.getRSSItemBeen().size());
//                        list.get(i).setImgUrl(urls[i]);
                        if (rssTempList.getImage() != null && rssTempList.getImage().getUrl() != null) {
                            list.get(i).setImgUrl(rssTempList.getImage().getUrl());
                            if (rssTempList.getImage().getTitle() != null) {
                                list.get(i).setName(rssTempList.getTitle());
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Intent intent = new Intent(getContext(), SourceListActivity.class);
                    intent.putExtra(SourceListActivity.KEY_LINK, info.replace(Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceListActivity.KEY_TITLE, "分享资讯");
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
                    Intent intent = new Intent(getContext(), SourceListActivity.class);
                    intent.putExtra(SourceListActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceListActivity.KEY_TITLE, "分享资讯");
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
