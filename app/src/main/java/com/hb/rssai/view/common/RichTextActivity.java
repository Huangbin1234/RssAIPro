package com.hb.rssai.view.common;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.rssai.R;
import com.hb.rssai.adapter.LikeAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.Evaluate;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResCollectionBean;
import com.hb.rssai.bean.ResInfo;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.bean.ResShareCollection;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.RichTextContract;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.RichTextPresenter;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.HtmlImageGetter;
import com.hb.rssai.util.HtmlImageGetterNew;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.StatusBarUtil;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.MyDecoration;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;

import static com.google.common.base.Preconditions.checkNotNull;

//import com.zzhoujay.richtext.RichText;

public class RichTextActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, RichTextContract.View {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ca_load_progress)
    ProgressBar mCaLoadProgress;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rta_tv_content)
    TextView mRtaTvContent;
    @BindView(R.id.activity_add_source)
    LinearLayout mActivityAddSource;
    @BindView(R.id.rta_tv_title)
    TextView mRtaTvTitle;
    @BindView(R.id.rta_tv_date)
    TextView mRtaTvDate;
    @BindView(R.id.rta_tv_whereFrom)
    TextView mRtaTvWhereFrom;
    @BindView(R.id.rta_tv_view)
    TextView mRtaTvView;
    @BindView(R.id.rta_recycler_view)
    RecyclerView mRtaRecyclerView;
    @BindView(R.id.rta_iv_good)
    ImageView mRtaIvGood;
    @BindView(R.id.rta_tv_good)
    TextView mRtaTvGood;
    @BindView(R.id.rta_ll_good)
    LinearLayout mRtaLlGood;
    @BindView(R.id.rta_iv_not_good)
    ImageView mRtaIvNotGood;
    @BindView(R.id.rta_tv_not_good)
    TextView mRtaTvNotGood;
    @BindView(R.id.rta_ll_not_good)
    LinearLayout mRtaLlNotGood;
    @BindView(R.id.ff_find_hot_label)
    TextView mFfFindHotLabel;


    private LinearLayoutManager linearLayoutManager;

    private String abstractContent = "";
    private String abstractContentFormat = "";
    private String pubDate = "";
    private String title = "";
    private String whereFrom = "";
    private String url = "";
    private String id = "";
    private String evaluateType = "";
    private long clickGood;
    private long clickNotGood;

    private ResCollectionBean.RetObjBean mRetObjBean;

    private List<ResInformation.RetObjBean.RowsBean> resInfoList = new ArrayList<>();
    private LikeAdapter likeAdapter;
    private Evaluate evaluate = new Evaluate();

    RichTextContract.Presenter mPresenter;
    boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
        mPresenter.getLikeByTitle(title);
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.TOKEN, ""))) {
            mPresenter.updateCount(id);
        }
        mPresenter.getInformation(id);
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysToolbar.setNavigationIcon(R.mipmap.ic_back);
        mSysToolbar.setNavigationOnClickListener(v -> finish());
        mSysToolbar.setOnMenuItemClickListener(this);
        //修改状态栏文字图标为深色
        StatusBarUtil.StatusBarLightMode(this);
    }

    public void setSelfTheme(Activity activity) {
        setTranslucentStatus2(activity);
    }

    @Override
    protected void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            abstractContent = null == bundle.getString("abstractContent") ? "" : bundle.getString("abstractContent");
            title = bundle.getString(ContentActivity.KEY_TITLE);
            whereFrom = bundle.getString("whereFrom");
            pubDate = bundle.getString("pubDate");
            url = bundle.getString("url");
            id = bundle.getString("id");

            clickGood = bundle.getLong("clickGood");
            clickNotGood = bundle.getLong("clickNotGood");

            try {
                abstractContentFormat = getNewContent(abstractContent);
            } catch (Exception e) {
                e.printStackTrace();
                abstractContentFormat = abstractContent;
            }
        }
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //扩大比例的缩放
        settings.setJavaScriptEnabled(true);
        int size = DisplayUtil.dip2px(this, 17);
        settings.setDefaultFontSize(size);
        settings.setMinimumFontSize(14);//设置 WebView 支持的最小字体大小，默认为 8
//        settings.setTextZoom(300); // 通过百分比来设置文字的大小，默认值是100

        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false);
        if (isNight) {
            mWebView.setBackgroundColor(0); // 设置背景色   xml 一定要设置background 否则此处会报空指针
            mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#9C9C9C\");"
                    );
                }
            });
        } else {
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#555555\");"
                    );
                }
            });
        }
        mWebView.loadDataWithBaseURL(null, abstractContentFormat, "text/html", "utf-8", null);
    }

    @Override
    protected void initView() {
        mRtaTvTitle.setText(title.trim());
        if (TextUtils.isEmpty(url)) {
            mRtaTvView.setVisibility(View.GONE);
        }

        String eId = SharedPreferencesUtil.getString(this, id, "");
        if (null != GsonUtil.getGsonUtil().getBean(eId, Evaluate.class)) {
            evaluate = GsonUtil.getGsonUtil().getBean(eId, Evaluate.class);
        }

        try {
            if (!TextUtils.isEmpty(pubDate))
                mRtaTvWhereFrom.setText(whereFrom + " " + DateUtil.showDate(Constant.sdf.parse(pubDate), Constant.DATE_LONG_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mRtaTvNotGood.setText("" + clickNotGood);
        mRtaTvGood.setText("" + clickGood);

        boolean isOldRec = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_OLD_REC_MODE, false);
        if (isOldRec) {
            mRtaTvContent.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            loadTextView();
        } else {
            initWebView();
            mRtaTvContent.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }


        linearLayoutManager = new LinearLayoutManager(this);
        mRtaRecyclerView.setLayoutManager(linearLayoutManager);
        mRtaRecyclerView.setNestedScrollingEnabled(false);
        mRtaRecyclerView.setHasFixedSize(true);
        mRtaRecyclerView.addItemDecoration(new MyDecoration(this, LinearLayoutManager.VERTICAL));

        //初始化
        String eStr = SharedPreferencesUtil.getString(this, id, "");
        if (!TextUtils.isEmpty(eStr)) {//如果不是空
            Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
            if ("1".equals(eva.getClickGood())) {
                mRtaIvGood.setImageResource(R.mipmap.ic_good_press);
            } else if ("2".equals(eva.getClickGood())) {
                mRtaIvGood.setImageResource(R.mipmap.ic_good);
            }
            if ("1".equals(eva.getClickNotGood())) {
                mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good_press);
            } else if ("2".equals(eva.getClickNotGood())) {
                mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good);
            }
        }
        initShare();
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public String getNewContent(String htmlText) {
        try {
            htmlText = htmlText.replace("<figure", "</figure");
            htmlText = htmlText.replace("&nbsp;", "\t");
            htmlText = htmlText.replace("&#160;", "\t");
            Document doc = Jsoup.parse(htmlText);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%")
                        .attr("height", "auto")
                        .attr("data-w", "100%")
                        .attr("data-h", "auto")
                        .attr("style", cssStr(element.attr("style"), "width", "100%"))
                        .attr("style", cssStr(element.attr("style"), "height", "auto"))
                        .attr("style", cssStr(element.attr("style"), "max-width", "100%"));
            }
            Elements elements2 = doc.getElementsByTag("span");
            for (Element element : elements2) {
                element.attr("style", cssStr(element.attr("style"), "font-size", "" + DisplayUtil.dip2px(this, 16)));
                element.attr("style", cssStr(element.attr("style"), "color", "#555555"));
            }
            Elements elements3 = doc.getElementsByTag("a");
            for (Element element : elements3) {
                element.attr("style", "color:#9c9c9c;word-wrap:break-word;");
            }
            return doc.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return htmlText;
        }
    }

    /**
     * 替换style 中的宽度高度属性
     *
     * @param sourceStr
     * @param key
     * @param value
     * @return
     */
    private String cssStr(String sourceStr, String key, String value) {
        if (!sourceStr.contains(key)) {
            return sourceStr;
        }
        String s1 = sourceStr.substring(0, sourceStr.indexOf(key));
        String s2 = sourceStr.substring(sourceStr.indexOf(key));
        String s3 = "";
        if (-1 == s2.indexOf(";")) {
            s3 = s2.substring(s2.length());
        } else {
            s3 = s2.substring(s2.indexOf(";"));
        }
        return s1 + "" + key + ":" + value + s3;
    }

    /**
     * TextView 加载
     */
    @Deprecated
    private void loadTextView() {
//        mRtaTvContent.setRichText(abstractContent);
        HtmlImageGetter htmlImageGetter = new HtmlImageGetter(this, this, mRtaTvContent);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(abstractContent, Html.FROM_HTML_MODE_LEGACY, htmlImageGetter, null);
        } else {
            spanned = Html.fromHtml(abstractContent, htmlImageGetter, null); // or for older api
        }
        mRtaTvContent.setText(spanned);
//        mRtaTvContent.setMovementMethod(LinkMovementMethod.getInstance());
//        CharSequence text = mRtaTvContent.getText();
//        if (text instanceof Spannable) {
//            int end = text.length();
//            Spannable sp = (Spannable) mRtaTvContent.getText();
//            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            ImageSpan[] imgs = sp.getSpans(0, end, ImageSpan.class);
//            StyleSpan[] styleSpens = sp.getSpans(0, end, StyleSpan.class);
//            ForegroundColorSpan[] colorSpans = sp.getSpans(0, end, ForegroundColorSpan.class);
//            SpannableStringBuilder style = new SpannableStringBuilder(text);
//            style.clearSpans();
//            for (URLSpan url : urls) {
//                style.setSpan(url, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF12ADFA"));
//                style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            for (ImageSpan url : imgs) {
//                HtmlImageGetterNew imageGetter = new HtmlImageGetterNew(RichTextActivity.this, RichTextActivity.this, mRtaTvContent);
//
////                ImageSpan span = new ImageSpan(htmlImageGetter.getDrawable(url.getSource()));
//                ImageSpan span = new ImageSpan(imageGetter.getDrawable(url.getSource()));
//                Drawable drawable = span.getDrawable();
//                if (drawable != null) {
//                    drawable.setCallback(htmlImageGetter);
//                }
//                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            for (StyleSpan styleSpan : styleSpens) {
//                style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            for (ForegroundColorSpan colorSpan : colorSpans) {
//                style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            mRtaTvContent.setText(style);
//        }
    }

    public Drawable getUrlDrawable(String source, TextView mTextView) {
        HtmlImageGetterNew imageGetter = new HtmlImageGetterNew(RichTextActivity.this, RichTextActivity.this, mTextView);
        return imageGetter.getDrawable(source);
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    private void initShare() {
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/ //SHARE_MEDIA.SINA, SHARE_MEDIA.MORE
        mShareAction = new ShareAction(RichTextActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            StringUtil.copy(url, RichTextActivity.this);
                            Toast.makeText(RichTextActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();
                        } else if (share_media == SHARE_MEDIA.SMS) {
                            new ShareAction(RichTextActivity.this).withText("来自ZR分享面板")
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        } else {
                            String url = RichTextActivity.this.url;
                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);
                            web.setDescription(abstractContent);
                            web.setThumb(new UMImage(RichTextActivity.this, R.mipmap.load_logo));
                            new ShareAction(RichTextActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });


    }

    @Override
    public void setPresenter(RichTextContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<RichTextActivity> mActivity;

        private CustomShareListener(RichTextActivity activity) {
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
        return R.layout.activity_rich_text;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        MenuItem item = menu.findItem(R.id.toolbar_add_collection);
        this.item = item;
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.TOKEN, ""))) {
            mPresenter.getCollectionByInfoId(id, getUserID());
        }
        return super.onCreateOptionsMenu(menu);
    }

    private MenuItem item;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add_collection:
                this.item = item;
                if (!TextUtils.isEmpty(url)) {
                    String dateTime = DateUtil.format(new Date(), Constant.DATE_LONG_PATTERN);
                    UserCollection collection = new UserCollection();
                    collection.setLink(url);
                    collection.setTime(dateTime);
                    collection.setTitle(title);
                    LiteOrmDBUtil.insert(collection);
                    T.ShowToast(RichTextActivity.this, "收藏成功！");

                    String userId = getUserID();
                    mPresenter.add(title, url, id, mRetObjBean, userId);

                } else {
                    T.ShowToast(this, "收藏失败，链接错误！");
                }
                break;
            case R.id.toolbar_add_share:
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                mShareAction.open(config);
                break;
//            case R.id.toolbar_old_rec:
//                mRtaTvContent.setVisibility(View.VISIBLE);
//                mWebView.setVisibility(View.GONE);
//                loadTextView();
//                T.ShowToast(this, "已切换到老版本解析");
//                break;
        }
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return new RichTextPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RichText.recycle();
        mWebView.stopLoading();
        mWebView.destroy();
    }


    @Override
    public void loadError(Throwable throwable) {
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
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
    public void showUpdateResult(ResBase resBase) {
        //TODO 更新数量成功
    }


    @Override
    public void showListResult(ResInformation resInformation) {
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                resInfoList.addAll(resInformation.getRetObj().getRows());
                if (likeAdapter == null) {
                    likeAdapter = new LikeAdapter(this, resInfoList);
                    likeAdapter.setOnItemClickedListener(rowsBean1 -> {
                        //TODO
                        Intent intent = new Intent(this, RichTextActivity.class);//创建Intent对象
                        intent.putExtra(ContentActivity.KEY_TITLE, rowsBean1.getTitle());
                        intent.putExtra(ContentActivity.KEY_URL, rowsBean1.getLink());
                        intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean1.getId());
                        intent.putExtra("pubDate", rowsBean1.getPubTime());
                        intent.putExtra("whereFrom", rowsBean1.getWhereFrom());
                        intent.putExtra("abstractContent", rowsBean1.getAbstractContent());
                        intent.putExtra("id", rowsBean1.getId());
                        startActivity(intent);//将Intent传递给Activity
                    });
                    mRtaRecyclerView.setAdapter(likeAdapter);
                } else {
                    likeAdapter.notifyDataSetChanged();
                }
            }
        } else {
            T.ShowToast(this, resInformation.getRetMsg());
        }
    }

    @Override
    public void showAddResult(ResShareCollection resShareCollection) {
        mPresenter.getCollectionByInfoId(id, getUserID());
        T.ShowToast(this, resShareCollection.getRetMsg());
    }

    @Override
    public void loadEvaluateError(Throwable throwable) {
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }


    @Override
    public void showUpdateEvaluateResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            mPresenter.getInformation(id);
        } else {
            T.ShowToast(this, resBase.getRetMsg());
        }
    }


    @Override
    public void showInfoResult(ResInfo resInfo) {
        if (resInfo.getRetCode() == 0) {
            mRtaTvGood.setText("" + resInfo.getRetObj().getClickGood());
            mRtaTvNotGood.setText("" + resInfo.getRetObj().getClickNotGood());
            //刷新
            if ("1".equals(evaluateType)) {
                String eStr = SharedPreferencesUtil.getString(this, id, "");
                if (TextUtils.isEmpty(eStr)) {//如果是空那么没操作过直接设置1
                    evaluate.setClickGood("1");
                    evaluate.setInformationId(id);
                    SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    mRtaIvGood.setImageResource(R.mipmap.ic_good_press);
                } else {
                    Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
                    if ("1".equals(eva.getClickGood())) {
                        mRtaIvGood.setImageResource(R.mipmap.ic_good);
                        evaluate.setClickGood("2");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    } else if ("2".equals(eva.getClickGood())) {
                        mRtaIvGood.setImageResource(R.mipmap.ic_good_press);
                        evaluate.setClickGood("1");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    } else {
                        evaluate.setClickGood("1");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                        mRtaIvGood.setImageResource(R.mipmap.ic_good_press);
                    }
                }
            } else if ("0".equals(evaluateType)) {

                String eStr = SharedPreferencesUtil.getString(this, id, "");
                if (TextUtils.isEmpty(eStr)) {//如果是空那么没操作过直接设置1
                    evaluate.setClickNotGood("1");
                    evaluate.setInformationId(id);
                    SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good_press);
                } else {
                    Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
                    if ("1".equals(eva.getClickNotGood())) {
                        mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good);
                        evaluate.setClickNotGood("2");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    } else if ("2".equals(eva.getClickNotGood())) {
                        mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good_press);
                        evaluate.setClickNotGood("1");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                    } else {
                        evaluate.setClickNotGood("1");
                        evaluate.setInformationId(id);
                        SharedPreferencesUtil.setString(this, id, GsonUtil.toJson(evaluate));
                        mRtaIvNotGood.setImageResource(R.mipmap.ic_not_good_press);
                    }
                }
            }
        } else {

            if (!isFirst) {
                boolean isOffline = SharedPreferencesUtil.getBoolean(this, Constant.KEY_IS_OFFLINE_MODE, false);
                if (isOffline) {
                    T.ShowToast(this, getResources().getString(R.string.str_offline_notice));
                } else {
                    T.ShowToast(this, resInfo.getRetMsg());
                }
            }
            isFirst = false;
        }
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
    }


    @Override
    public void showCollectionInfoIdResult(ResCollectionBean resCollectionBean) {
        mRetObjBean = resCollectionBean.getRetObj();
        if (resCollectionBean.getRetCode() == 0) {
            //设置收藏图标
            if (!resCollectionBean.getRetObj().isDeleteFlag()) {
                item.setIcon(R.mipmap.ic_collection_press);
            } else {
                item.setIcon(R.mipmap.ic_collection_normal);
            }
        } else {
            item.setIcon(R.mipmap.ic_collection_normal);
        }
    }


    @OnClick({R.id.rta_ll_good, R.id.rta_ll_not_good, R.id.rta_tv_view})
    @Override
    public void onClick(View v) {
        String eStr = SharedPreferencesUtil.getString(this, id, "");
        Evaluate eva = GsonUtil.getGsonUtil().getBean(eStr, Evaluate.class);
        switch (v.getId()) {
            case R.id.rta_tv_view:
                Intent intent = new Intent(RichTextActivity.this, ContentActivity.class);//创建Intent对象
                intent.putExtra(ContentActivity.KEY_TITLE, title);
                intent.putExtra(ContentActivity.KEY_URL, url);
                intent.putExtra(ContentActivity.KEY_INFORMATION_ID, id);
                startActivity(intent);
                break;
            case R.id.rta_ll_good:
                evaluateType = "1";
                if (null != eva && "1".equals(eva.getClickNotGood())) {
                    T.ShowToast(this, "您已踩过了，请先取消！");
                } else {
                    mRtaLlGood.setEnabled(false);
                    mRtaLlNotGood.setEnabled(false);
                    mPresenter.updateEvaluateCount(id, evaluateType, evaluate);
                }
                break;
            case R.id.rta_ll_not_good:
                evaluateType = "0";
                if (null != eva && "1".equals(eva.getClickGood())) {
                    T.ShowToast(this, "您已点过赞了，请先取消！");
                } else {
                    mRtaLlGood.setEnabled(false);
                    mRtaLlNotGood.setEnabled(false);
                    mPresenter.updateEvaluateCount(id, evaluateType, evaluate);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

}
