package com.hb.rssai.view.subscription;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.ThemeAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResTheme;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.AddSourcesContract;
import com.hb.rssai.event.RssSourceEvent;
import com.hb.rssai.presenter.AddRssPresenter;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.runtimePermissions.PermissionsActivity;
import com.hb.rssai.runtimePermissions.PermissionsChecker;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.FullyGridLayoutManager;
import com.hb.rssai.view.widget.GridSpacingItemDecoration;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.XmlReader;
import com.rss.util.ReadXML;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddSourceActivity extends BaseActivity implements View.OnClickListener, AddSourcesContract.View {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.asa_et_name)
    EditText mAsaEtName;
    @BindView(R.id.asa_et_url)
    EditText mAsaEtUrl;
    @BindView(R.id.asa_btn_save)
    Button mAsaBtnSave;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;
    @BindView(R.id.asa_iv_scan)
    ImageView mAsaIvScan;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.asa_btn_opml)
    Button mAsaBtnOpml;
    @BindView(R.id.asa_tv_opml)
    TextView mAsaTvOpml;
    OpmlTask opmlTask;
    @BindView(R.id.asa_et_key)
    EditText mAsaEtKey;
    @BindView(R.id.asa_btn_key)
    Button mAsaBtnKey;
    @BindView(R.id.aas_recycler_view)
    RecyclerView mAasRecyclerView;
    @BindView(R.id.aas_swipe_layout)
    SwipeRefreshLayout mAasSwipeLayout;
    @BindView(R.id.asa_tv_empty)
    TextView mAsaTvEmpty;
    @BindView(R.id.asa_ll)
    LinearLayout mAsaLl;
    @BindView(R.id.llf_btn_re_try)
    Button mLlfBtnReTry;
    @BindView(R.id.include_no_data)
    View includeNoData;
    @BindView(R.id.include_load_fail)
    View includeLoadFail;

    public final static int REQUESTCODE = 1;
    FullyGridLayoutManager mFullyGridLayoutManager;
    boolean isEnd = false, isLoad = false;
    int page = 1;
    List<ResTheme.RetObjBean.RowsBean> mThemes = new ArrayList<>();
    ThemeAdapter adapter;
    String rssTitle = "";
    String rssLink = "";

    AddSourcesContract.Presenter mPresenter;
    private PermissionsChecker mPermissionsChecker;

    // 所需的全部权限
    static final String[] PERMISSIONS_CAMERA = new String[]{
            Manifest.permission.CAMERA
    };
    private int REQ_SELECT_FILE = 2;
    private OpmlFileTask opmlFileTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionsChecker = new PermissionsChecker(this);
        mPresenter.getList(page);
    }

    @Override
    protected void initView() {
        mFullyGridLayoutManager = new FullyGridLayoutManager(this, 1);
        mAasRecyclerView.setLayoutManager(mFullyGridLayoutManager);
        mAasRecyclerView.setNestedScrollingEnabled(false);
        mAasRecyclerView.setHasFixedSize(true);

        mAasRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(this, 0), false));
        mAasSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mAasSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));


        mAasSwipeLayout.setOnRefreshListener(() -> onRefresh());
        //TODO 设置上拉加载更多
        mAasRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mAasSwipeLayout.setRefreshing(false);
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
                lastVisibleItem = mFullyGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void onRefresh() {
        page = 1;
        isLoad = true;
        isEnd = false;
        if (mThemes != null) {
            mThemes.clear();
        }
        mAasSwipeLayout.setRefreshing(true);
        mPresenter.getList(page);
    }

    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mAasSwipeLayout.setRefreshing(true);
            page++;
            mPresenter.getList(page);
        }
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_add_source;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_asa_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new AddRssPresenter(this, this);
    }

    @OnClick({R.id.asa_btn_save, R.id.asa_iv_scan, R.id.asa_btn_opml, R.id.asa_btn_key})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asa_btn_save:
                //TODO 写入首选
                rssTitle = mAsaEtName.getText().toString().trim();
                rssLink = mAsaEtUrl.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入名称");
                    return;
                }
                if (TextUtils.isEmpty(rssLink)) {
                    T.ShowToast(this, "请输入链接地址");
                    return;
                }
                RssSource rssSource = new RssSource();
                rssSource.setName(rssTitle);
                rssSource.setLink(rssLink);
                LiteOrmDBUtil.insert(rssSource);
                T.ShowToast(this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                mPresenter.addRss(rssLink, rssTitle, getUserID());
                break;
            case R.id.asa_iv_scan:
                if (!isCameraPermissions()) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUESTCODE);
                } else {
                    loadCameraPermissions();
                }
                break;
            case R.id.asa_btn_opml:
                String link2 = mAsaEtUrl.getText().toString().trim();
                opmlTask = new OpmlTask();
                opmlTask.execute(link2);
                break;
            case R.id.asa_btn_key:
                rssTitle = mAsaEtKey.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入关键字");
                    return;
                }
                RssSource keySource = new RssSource();
                keySource.setName(rssTitle);
                try {
                    rssLink = "http://news.baidu.com/ns?word=" + URLEncoder.encode(rssTitle, "UTF-8") + "&tn=newsrss&sr=0&cl=2&rn=20&ct=0";
                    keySource.setLink(rssLink);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LiteOrmDBUtil.insert(keySource);

                mPresenter.addRss(rssLink, rssTitle, getUserID());
                break;
        }
    }

    @Override
    public void showAddSuccess() {
        if (mPop.isShowing()) {
            mPop.dismiss();
        }
    }

    @Override
    public void showListResult(ResTheme resTheme) {
        isLoad = false;
        mAasSwipeLayout.setRefreshing(false);
        //TODO 填充数据
        if (resTheme.getRetCode() == 0) {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.GONE);
            mAasRecyclerView.setVisibility(View.VISIBLE);
            if (resTheme.getRetObj().getRows() != null && resTheme.getRetObj().getRows().size() > 0) {
                this.mThemes.addAll(resTheme.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new ThemeAdapter(this, mThemes);
                    adapter.setItemClickedListener((rowsBean, v) -> {
                        if (Constant.ACTION_BD_KEY.equals(rowsBean.getAction())) {
                            //TODO
                            showPop(1, rowsBean.getName());
                        } else if (Constant.ACTION_INPUT_LINK.equals(rowsBean.getAction())) {
                            //TODO
                            showPop(2, rowsBean.getName());
                        } else if (Constant.ACTION_OPEN_OPML.equals(rowsBean.getAction())) {
                            //TODO
                            showPop(3, rowsBean.getName());
                        }
                    });
                    mAasRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (this.mThemes.size() == resTheme.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else if (resTheme.getRetCode() == 10013) {//暂无数据
            includeNoData.setVisibility(View.VISIBLE);
            includeLoadFail.setVisibility(View.GONE);
            mAasRecyclerView.setVisibility(View.GONE);
        } else {
            includeNoData.setVisibility(View.GONE);
            includeLoadFail.setVisibility(View.VISIBLE);
            mAasRecyclerView.setVisibility(View.GONE);
            T.ShowToast(this, resTheme.getRetMsg());
        }
    }

    @Override
    public void showMsg(String s) {
        T.ShowToast(this, s);
    }


    @Override
    public String getUserID() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }

    public void showPop(int i, String title) {
        showPopView(i, title);
        if (mPop.isShowing()) {
            mPop.dismiss();
        } else {
            mPop.show();
            Window window = mPop.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setContentView(popupView);//自定义布局应该在这里添加，要在dialog.show()的后面
            window.setWindowAnimations(R.style.PopupAnimation);//
            window.setLayout(DisplayUtil.getMobileWidth(this) * 8 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPop.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        }
        backgroundAlpha(0.5f);
        mPop.setOnDismissListener(dialogInterface -> {
            backgroundAlpha(1f);
        });
    }

    @Override
    public void setPresenter(AddSourcesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    ProgressBar mProgressBar;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mProgressBar.setMax(msg.arg1);
            }
        }
    };

    class OpmlFileTask extends AsyncTask<File, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(0);
            mProgressBar.setProgress(0);
            pas_btn_opml_file.setEnabled(false);
            pas_btn_opml.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(File... params) {
            List<Outline> outlines = readOPML(params[0]);
            if (outlines != null && outlines.size() > 0) {
                RssSource rssSource;
                int current = 0;
                Message message = new Message();
                message.what = 0;
                message.arg1 = outlines.size();
                mHandler.sendMessage(message);

                for (Outline outline : outlines) {
                    if (!TextUtils.isEmpty(outline.getXmlUrl())) {
                        rssSource = new RssSource();
                        try {
                            String strUTF = new String(outline.getTitle().getBytes(), "UTF-8");
                            rssSource.setName(strUTF);
                            rssTitle = strUTF;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        rssSource.setLink(outline.getXmlUrl());
                        LiteOrmDBUtil.insert(rssSource);

                        rssLink = outline.getXmlUrl();

                        mPresenter.addOpmlRss(rssLink, rssTitle, getUserID());
                    }
                    for (Outline subOutline : outline.getChildren()) {
                        if (!TextUtils.isEmpty(subOutline.getXmlUrl())) {
                            rssSource = new RssSource();
                            try {
                                String strUTF = new String(subOutline.getTitle().getBytes(), "UTF-8");
                                rssSource.setName(strUTF);

                                rssTitle = strUTF;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            rssSource.setLink(subOutline.getXmlUrl());
                            LiteOrmDBUtil.insert(rssSource);
                            rssLink = subOutline.getXmlUrl();

                            mPresenter.addOpmlRss(rssLink, rssTitle, getUserID());
                        }
                    }
                    current++;
                    publishProgress(current); //这里的参数类型是 AsyncTask<Void, Integer, Void>中的Integer决定的，在onProgressUpdate中可以得到这个值去更新UI主线程，这里是异步线程
                }
                return "success";
            } else {
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String outlines) {
            pas_btn_opml_file.setEnabled(true);
            pas_btn_opml.setVisibility(View.VISIBLE);
            if ("success".endsWith(outlines)) {
                T.ShowToast(AddSourceActivity.this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                finish();
            } else if ("failure".endsWith(outlines)) {
                T.ShowToast(AddSourceActivity.this, "导入遇到错误，请更正OPML文件");
            } else {
                T.ShowToast(AddSourceActivity.this, "未知错误，可反馈给开发者");
            }
        }
    }

    class OpmlTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(0);
            mProgressBar.setProgress(0);
            pas_btn_opml.setEnabled(false);
            pas_btn_opml_file.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            List<Outline> outlines = readOPML(params[0]);
            if (outlines != null && outlines.size() > 0) {
                RssSource rssSource;
                int current = 0;
                Message message = new Message();
                message.what = 0;
                message.arg1 = outlines.size();
                mHandler.sendMessage(message);

                for (Outline outline : outlines) {
                    if (!TextUtils.isEmpty(outline.getXmlUrl())) {
                        rssSource = new RssSource();
                        try {
                            String strUTF = new String(outline.getTitle().getBytes(), "UTF-8");
                            rssSource.setName(strUTF);
                            rssTitle = strUTF;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        rssSource.setLink(outline.getXmlUrl());
                        LiteOrmDBUtil.insert(rssSource);

                        rssLink = outline.getXmlUrl();

                        mPresenter.addOpmlRss(rssLink, rssTitle, getUserID());
                    }
                    for (Outline subOutline : outline.getChildren()) {
                        if (!TextUtils.isEmpty(subOutline.getXmlUrl())) {
                            rssSource = new RssSource();
                            try {
                                String strUTF = new String(subOutline.getTitle().getBytes(), "UTF-8");
                                rssSource.setName(strUTF);

                                rssTitle = strUTF;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            rssSource.setLink(subOutline.getXmlUrl());
                            LiteOrmDBUtil.insert(rssSource);
                            rssLink = subOutline.getXmlUrl();

                            mPresenter.addOpmlRss(rssLink, rssTitle, getUserID());
                        }
                    }
                    current++;
                    publishProgress(current); //这里的参数类型是 AsyncTask<Void, Integer, Void>中的Integer决定的，在onProgressUpdate中可以得到这个值去更新UI主线程，这里是异步线程
                }
                return "success";
            } else {
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String outlines) {
            pas_btn_opml.setEnabled(true);
            pas_btn_opml_file.setVisibility(View.VISIBLE);
            if ("success".endsWith(outlines)) {
                T.ShowToast(AddSourceActivity.this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                finish();
            } else if ("failure".endsWith(outlines)) {
                T.ShowToast(AddSourceActivity.this, "导入遇到错误，请更正OPML文件");
            } else {
                T.ShowToast(AddSourceActivity.this, "未知错误，可反馈给开发者");
            }
        }
    }

    private List<Outline> readOPML(String opmlUrl) {
//        opmlUrl = "http://www.williamlong.info/download/opml.xml";
        URL feedUrl = null;//SyndFeedInput:从远程读到xml结构的内容转成SyndFeedImpl实例
        ReadXML readXML = ReadXML.getInstance();
        try {
            feedUrl = new URL(opmlUrl);
            XmlReader xmlReader = null;
            try {
                xmlReader.setDefaultEncoding("UTF-8");
                xmlReader = new XmlReader(feedUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readXML.readOpml(xmlReader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Outline> readOPML(File file) {
        ReadXML readXML = ReadXML.getInstance();
        XmlReader xmlReader = null;
        try {
            xmlReader.setDefaultEncoding("UTF-8");
            xmlReader = new XmlReader(file);
            return readXML.readOpml(xmlReader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQUESTCODE) {
                mAsaEtUrl.setText(data.getStringExtra("info"));
                pas_et_link.setText(data.getStringExtra("info"));
            } else if (requestCode == REQ_SELECT_FILE) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = StringUtil.getPath(this, uri);
                    if (path != null) {
                        File file = new File(path);
                        if (file.exists()) {
                            System.out.println("asbPath==>" + file.getAbsolutePath());
                            String p = file.getAbsolutePath();
                            if (TextUtils.isEmpty(p)) {
                                T.ShowToast(this, "OPML文件地址无效");
                                return;
                            }
                            if (!p.endsWith(".opml") && !p.endsWith(".xml")) {
                                T.ShowToast(this, "错误的文件后缀");
                                return;
                            }
                            opmlFileTask = new OpmlFileTask();
                            opmlFileTask.execute(file);
                        } else {
                            T.ShowToast(this, "文件不存在");
                        }
                    } else {
                        T.ShowToast(this, "文件不能识别");
                    }
                }
            }

        }
    }

    //////////////////////////////////////弹出层//////////////////////////////////////////////////////////////////
    private View popupView;
    //    private PopupWindow mPop;// 初始化弹出层
    Dialog mPop;

    Button pas_btn_sure;
    Button pas_btn_opml;
    Button pas_btn_opml_file;
    EditText pas_et_link;
    EditText pas_et_name;
    ImageView pas_btn_close;
    TextView pas_tv_title;
    LinearLayout pas_ll_link;
    LinearLayout pas_ll_name;
    ImageView pas_iv_scan;

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    private void showPopView(int flag, String title) {
        if (mPop == null) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            popupView = inflater.inflate(R.layout.pop_add_source, null);

            //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
            mPop = builder.create();

            pas_btn_sure = (Button) popupView.findViewById(R.id.pas_btn_sure);
            mProgressBar = (ProgressBar) popupView.findViewById(R.id.pas_progress_bar);
            pas_tv_title = (TextView) popupView.findViewById(R.id.pas_tv_title);
            pas_btn_opml = (Button) popupView.findViewById(R.id.pas_btn_opml);
            pas_et_link = (EditText) popupView.findViewById(R.id.pas_et_link);
            pas_btn_opml_file = (Button) popupView.findViewById(R.id.pas_btn_opml_file);
            pas_et_name = (EditText) popupView.findViewById(R.id.pas_et_name);
            pas_btn_close = (ImageView) popupView.findViewById(R.id.pas_btn_close);


            pas_ll_link = (LinearLayout) popupView.findViewById(R.id.pas_ll_link);
            pas_ll_name = (LinearLayout) popupView.findViewById(R.id.pas_ll_name);

            pas_iv_scan = (ImageView) popupView.findViewById(R.id.pas_iv_scan);
        }
        pas_tv_title.setText(title);
        if (flag == 1) {
            pas_btn_opml.setVisibility(View.GONE);
            pas_btn_opml_file.setVisibility(View.GONE);
            pas_btn_sure.setVisibility(View.VISIBLE);
            pas_ll_link.setVisibility(View.GONE);
            pas_et_name.setHint("如Rss、人物名等");
            pas_ll_name.setVisibility(View.VISIBLE);

        } else if (flag == 2) {
            pas_btn_opml.setVisibility(View.GONE);
            pas_btn_opml_file.setVisibility(View.GONE);
            pas_btn_sure.setVisibility(View.VISIBLE);
            pas_et_link.setHint("输入源链接");
            pas_et_name.setHint("输入源名称");
            pas_ll_link.setVisibility(View.VISIBLE);
            pas_ll_name.setVisibility(View.VISIBLE);
        } else if (flag == 3) {
            pas_btn_opml.setVisibility(View.VISIBLE);
            pas_et_link.setHint("例如http://xxx.com/opml.xml");
            pas_btn_sure.setVisibility(View.GONE);
            pas_ll_name.setVisibility(View.GONE);
            pas_ll_link.setVisibility(View.VISIBLE);
            pas_btn_opml_file.setVisibility(View.VISIBLE);
        }
        pas_btn_close.setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
        });
        pas_iv_scan.setOnClickListener(v -> {
            if (!isCameraPermissions()) {
                startActivityForResult(new Intent(this, CaptureActivity.class), REQUESTCODE);
            } else {
                loadCameraPermissions();
            }
//            startActivityForResult(new Intent(this, CaptureActivity.class), REQUESTCODE);
        });
        pas_btn_opml.setOnClickListener(v -> {
//            T.ShowToast(this, "功能优化中暂不开放");
//            return;
            //TODO 选择opml文件
            String link2 = pas_et_link.getText().toString().trim();
            if (TextUtils.isEmpty(link2)) {
                T.ShowToast(this, "请输入opml链接地址");
                return;
            }
            opmlTask = new OpmlTask();
            opmlTask.execute(link2);
        });
        pas_btn_opml_file.setOnClickListener(view -> {
            //TODO 打开OPML文件并读取
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQ_SELECT_FILE);
        });
        pas_btn_sure.setOnClickListener(arg0 -> {

            //TODO
            if (flag == 1) {
                rssTitle = pas_et_name.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入关键字");
                    return;
                }
                RssSource keySource = new RssSource();
                keySource.setName(rssTitle);
                try {
                    rssLink = "http://news.baidu.com/ns?word=" + URLEncoder.encode(rssTitle, "UTF-8") + "&tn=newsrss&sr=0&cl=2&rn=20&ct=0";
                    keySource.setLink(rssLink);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LiteOrmDBUtil.insert(keySource);

                mPresenter.addRss(rssLink, rssTitle, getUserID());
            } else if (flag == 2) {
                //TODO 写入首选
                rssTitle = pas_et_name.getText().toString().trim();
                rssLink = pas_et_link.getText().toString().trim();
                if (TextUtils.isEmpty(rssTitle)) {
                    T.ShowToast(this, "请输入名称");
                    return;
                }
                if (TextUtils.isEmpty(rssLink)) {
                    T.ShowToast(this, "请输入链接地址");
                    return;
                }
                RssSource rssSource = new RssSource();
                rssSource.setName(rssTitle);
                rssSource.setLink(rssLink);
                LiteOrmDBUtil.insert(rssSource);
                T.ShowToast(this, "添加成功");
                EventBus.getDefault().post(new RssSourceEvent(0));
                mPresenter.addRss(rssLink, rssTitle, getUserID());
            } else if (flag == 3) {
                String link2 = pas_et_link.getText().toString().trim();
                if (TextUtils.isEmpty(link2)) {
                    T.ShowToast(this, "请输入opml链接地址");
                    return;
                }
                opmlTask = new OpmlTask();
                opmlTask.execute(link2);
            }
        });
    }

    /**
     * 相机权限
     */
    public void loadCameraPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS_CAMERA)) {
                startCameraPermissionsActivity();
            }
        }
    }

    public boolean isCameraPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mPermissionsChecker.lacksPermissions(PERMISSIONS_CAMERA);
        } else {
            return false;
        }
    }

    private final int REQUEST_CODE = 1;

    private void startCameraPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS_CAMERA);
    }
}
