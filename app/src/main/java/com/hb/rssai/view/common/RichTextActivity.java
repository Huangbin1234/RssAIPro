package com.hb.rssai.view.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
import com.hb.rssai.util.CommonHandler;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.HtmlImageGetter;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.StatusBarUtil;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.widget.MyDecoration;
import com.hb.rssai.view.widget.WordWrapView;
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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.rss.util.StringUtil.getAllRegexImages;

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
    @BindView(R.id.item_iv_logo)
    ImageView mItemIvLogo;
    @BindView(R.id.wwv_his_word)
    WordWrapView mWwvHisWord;


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
    private String subscribeImg;

    private ResCollectionBean.RetObjBean mRetObjBean;

    private List<ResInformation.RetObjBean.RowsBean> resInfoList = new ArrayList<>();
    private LikeAdapter likeAdapter;
    private Evaluate evaluate = new Evaluate();

    RichTextContract.Presenter mPresenter;
    boolean isFirst = false;
    private long count;//阅读数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        // 允许使用transitions
        super.onCreate(savedInstanceState);
        isFirst = true;
        mPresenter.getLikeByTitle(title);
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.TOKEN, ""))) {
            mPresenter.updateCount(id);
        }
        mPresenter.getInformation(id);
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            try {
                //mOverhangSize属性，意思就是左菜单离右边屏幕边缘的距离
                Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                f_overHang.setAccessible(true);
                //设置左菜单离右边屏幕边缘的距离为0，设置全屏
                f_overHang.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
            // 左侧的透明视图
            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            slidingPaneLayout.addView(leftView, 0);  //添加到SlidingPaneLayout中
            // 右侧的内容视图
            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
            decor.removeView(decorChild);
            decor.addView(slidingPaneLayout);
            // 为 SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1);
        }
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

            if (bundle.containsKey("subscribeImg")) {
                subscribeImg = bundle.getString("subscribeImg", "");
            }
            if (bundle.containsKey("count")) {
                count = bundle.getLong("count", 0);
            }
            try {
                abstractContentFormat = getNewContent(abstractContent);
            } catch (Exception e) {
                e.printStackTrace();
                abstractContentFormat = abstractContent;
            }
        }
    }

    WebSettings settings;

    private void initWebView() {
        settings = mWebView.getSettings();
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
        mWebView.addJavascriptInterface(new JsToJava(), "imageListener");
        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false);
        if (isNight) {
            mWebView.setBackgroundColor(0); // 设置背景色   xml 一定要设置background 否则此处会报空指针
            mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#9C9C9C\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"word-break\", \"break-all\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"word-wrap\", \"break-word\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"text-align\", \"justify\");");
                    // web 页面加载完成，添加监听图片的点击 js 函数
                    // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                    mWebView.loadUrl("javascript:(function(){" +
                            "var objs = document.getElementsByTagName(\"img\"); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "    objs[i].onclick=function()  " +
                            "    {  "
                            + "        window.imageListener.startShowImageActivity(this.src);  " +
                            "    }  " +
                            "}" +
                            "})()");
                }
            });
        } else {
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#4D4D4D\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"word-break\", \"break-all\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"word-wrap\", \"break-word\");");
                    mWebView.loadUrl("javascript:document.body.style.setProperty(\"text-align\", \"justify\");");
                    // web 页面加载完成，添加监听图片的点击 js 函数
                    // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                    mWebView.loadUrl("javascript:(function(){" +
                            "var objs = document.getElementsByTagName(\"img\"); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "    objs[i].onclick=function()  " +
                            "    {  "
                            + "        window.imageListener.startShowImageActivity(this.src);  " +
                            "    }  " +
                            "}" +
                            "})()");
                }
            });
        }
        mWebView.loadDataWithBaseURL(null, abstractContentFormat, "text/html", "utf-8", null);
    }


    /**
     * 点击图片启动新的 ShowImageFromWebActivity，并传入点击图片对应的 url 和页面所有图片
     * 对应的 url
     *
     * @param url 点击图片对应的 url
     */
    List<String> arrayList = new ArrayList<>();

    /**
     * js方法回调
     */
    private class JsToJava {
        @JavascriptInterface
        public void startShowImageActivity(String url) {
            Intent intent = new Intent(RichTextActivity.this, ImageShowActivity.class);
            for (int i = 0; i < arrayList.size(); i++) {
                if (url.equals(arrayList.get(i))) {
                    intent.putExtra(ImageShowActivity.KEY_IMAGE_POS, i);
                    break;
                }
            }
            intent.putStringArrayListExtra(ImageShowActivity.KEY_IMAGE_BEAN, (ArrayList<String>) arrayList);
            startActivity(intent);
        }
    }

    @Override
    protected void initView() {

        mRtaTvTitle.setText(title.trim());
        if (TextUtils.isEmpty(url)) {
            mRtaTvView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(subscribeImg)) {
            mItemIvLogo.setVisibility(View.GONE);
        } else {
            mItemIvLogo.setVisibility(View.VISIBLE);
            HttpLoadImg.loadCircleImg(this, subscribeImg, mItemIvLogo);
        }

        String eId = SharedPreferencesUtil.getString(this, id, "");
        if (null != GsonUtil.getGsonUtil().getBean(eId, Evaluate.class)) {
            evaluate = GsonUtil.getGsonUtil().getBean(eId, Evaluate.class);
        }

        try {
            if (!TextUtils.isEmpty(pubDate)) {
                if (count > 0) {
                    mRtaTvWhereFrom.setText(whereFrom + " " + DateUtil.showDate(Constant.sdf.parse(pubDate), Constant.DATE_LONG_PATTERN) + " 浏览" + count + "");
                } else {
                    mRtaTvWhereFrom.setText(whereFrom + " " + DateUtil.showDate(Constant.sdf.parse(pubDate), Constant.DATE_LONG_PATTERN));
                }
            } else {
                mRtaTvWhereFrom.setText(whereFrom);
            }
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
            initShowPop();
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
            if (null != arrayList && arrayList.size() > 0) {
                arrayList.clear();
            }
            arrayList.addAll(getAllRegexImages(htmlText));

            htmlText = htmlText.replace("<figure", "</figure");
            htmlText = htmlText.replace("&nbsp;", "\t");
            htmlText = htmlText.replace("&#160;", "\t");
            htmlText = htmlText.replace("阅读原文", "\t");
            htmlText = htmlText.replace("<pre", "");
            htmlText = htmlText.replace("</pre>", "");
            htmlText = htmlText.replace("<code", "<span");
            htmlText = htmlText.replace("</code>", "</span>");
            htmlText = htmlText.replace("\"//files.", "\"http://files.");
            htmlText = htmlText.replace("\"//player.", "\"http://player.");
            htmlText = htmlText.replace("———", "");
            htmlText = htmlText.replace("<mark", "<span");
            htmlText = htmlText.replace("</mark>", "</span>");

            Document doc = Jsoup.parse(htmlText);

            String resStr = doc.toString();
            if (resStr.contains("&lt;") && resStr.contains("&gt;")) {
                resStr = resStr.replace("&lt;", "<");
                resStr = resStr.replace("&gt;", ">");
                doc = Jsoup.parse(resStr);
            }
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%")
                        .attr("height", "auto")
                        .attr("data-w", "100%")
                        .attr("data-h", "auto")
                        .attr("data-width", "100%")
                        .attr("data-height", "auto")
                        .attr("data-rawwidth", "100%")
                        .attr("data-rawheight", "auto")
                        .attr("style", cssStr(element.attr("style"), "width", "100%"))
                        .attr("style", cssStr(element.attr("style"), "height", "auto"))
                        .attr("style", cssStr(element.attr("style"), "max-width", "100%"))
                        .attr("style", addAttr(element.attr("style"), "border-radius", "8px"));
            }
            Elements elements1 = doc.getElementsByTag("table");
            for (Element element : elements1) {
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
                element.attr("style", cssStr(element.attr("style"), "font-size", DisplayUtil.dip2px(this, 17) + "px"));
                element.attr("style", cssStr(element.attr("style"), "color", "#4D4D4D"));
                element.attr("style", cssStr(element.attr("style"), "background-color", "rgba(0,0,0,0)"));
                element.attr("style", cssStr(element.attr("style"), "text-indent", "2em"));
            }
            Elements elements3 = doc.getElementsByTag("a");
            for (Element element : elements3) {
                element.attr("style", "color:#9c9c9c;word-wrap:break-word;text-decoration:none;border-bottom:4px dashed #9c9c9c;");
            }
            Elements elements4 = doc.getElementsByTag("iframe");
            for (Element element : elements4) {
                element.attr("width", "100%")
                        .attr("height", "600px");
            }
            Elements elements5 = doc.getElementsByTag("div");
            for (Element element : elements5) {
                element.attr("style", cssStr(element.attr("style"), "font-size", DisplayUtil.dip2px(this, 17) + "px"));
                element.attr("style", cssStr(element.attr("style"), "line-height", "normal"));
                element.attr("style", cssStr(element.attr("style"), "width", "100%"));
            }
            Elements elements6 = doc.getElementsByTag("p");
            for (Element element : elements6) {
                element.attr("style", cssStr(element.attr("style"), "line-height", "normal"));
//                element.attr("style", cssStr(element.attr("style"), "text-indent", "2em"));
            }
            Elements elements7 = doc.getElementsByTag("font");
            for (Element element : elements7) {
                element.attr("style", cssStr(element.attr("style"), "font-size", DisplayUtil.dip2px(this, 17) + "px"));
                element.attr("style", cssStr(element.attr("style"), "color", "#4D4D4D"));
                element.attr("style", cssStr(element.attr("style"), "background-color", "rgba(0,0,0,0)"));
                element.attr("style", cssStr(element.attr("style"), "text-indent", "2em"));
            }
            Elements elements8 = doc.getElementsByTag("video");
            for (Element element : elements8) {
                element.attr("width", "100%")
                        .attr("height", "auto")
                        .attr("data-w", "100%")
                        .attr("data-h", "auto")
                        .attr("data-width", "100%")
                        .attr("data-height", "auto")
                        .attr("data-rawwidth", "100%")
                        .attr("data-rawheight", "auto")
                        .attr("style", cssStr(element.attr("style"), "width", "100%"))
                        .attr("style", cssStr(element.attr("style"), "height", "auto"))
                        .attr("style", cssStr(element.attr("style"), "max-width", "100%"))
                        .attr("style", addAttr(element.attr("style"), "border-radius", "8px"));
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

    // style="a:123";
    private String addAttr(String sourceStr, String key, String value) {
        if (TextUtils.isEmpty(sourceStr)) {
            String tempStr = "" + key + ":" + value + "";
            return tempStr;
        } else {
            String tempStr = key + ":" + value + "; " + sourceStr;
            return tempStr;
        }
    }

    /**
     * TextView 加载
     */
    @Deprecated
    private void loadTextView() {
        HtmlImageGetter htmlImageGetter = new HtmlImageGetter(this, this, mRtaTvContent);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(abstractContent, Html.FROM_HTML_MODE_LEGACY, htmlImageGetter, null);
        } else {
            spanned = Html.fromHtml(abstractContent, htmlImageGetter, null); // or for older api
        }
        mRtaTvContent.setText(spanned);
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    private void initShare() {
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/ //SHARE_MEDIA.SINA, SHARE_MEDIA.MORE
        mShareAction = new ShareAction(RichTextActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .addButton("umeng_view_content", "umeng_view_content", "umeng_view_content", "umeng_view_content")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            StringUtil.copy(url, RichTextActivity.this);
                            Toast.makeText(RichTextActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();
                        } else if (snsPlatform.mShowWord.equals("umeng_view_content")) {
                            Intent intent = new Intent(RichTextActivity.this, ContentActivity.class);//创建Intent对象
                            intent.putExtra(ContentActivity.KEY_TITLE, title);
                            intent.putExtra(ContentActivity.KEY_URL, url);
                            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, id);
                            startActivity(intent);
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

    @Override
    public void showFail(Throwable throwable) {
        CommonHandler.actionThrowable(throwable);
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
            case R.id.toolbar_font:
                //TODO 打开设置
                showThemePop();
                break;
        }
        return false;
    }


    private void showThemePop() {
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
//        backgroundAlpha(0.5f);
//        mPop.setOnDismissListener(dialogInterface -> {
//            //Log.v("List_noteTypeActivity:", "我是关闭事件");
//            backgroundAlpha(1f);
//        });
    }

    View popupView;
    AlertDialog mPop;

    void initShowPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        popupView = inflater.inflate(R.layout.layout_font, null);
        mPop = builder.create();

        SeekBar sb = popupView.findViewById(R.id.seekBar);
        TextView ts = popupView.findViewById(R.id.tvSize);
        int prgValue = SharedPreferencesUtil.getInt(RichTextActivity.this, Constant.KEY_FONT_SIZE, 0);
        if (prgValue > 0) {
            ts.setText("设置字体大小（" + prgValue + ")");
            settings.setTextZoom(prgValue);
            int size = DisplayUtil.dip2px(RichTextActivity.this, 17 * (1 + (prgValue / 100f)));
            settings.setDefaultFontSize(size);
            sb.setProgress(prgValue);
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.setTextZoom(progress);
                int size = DisplayUtil.dip2px(RichTextActivity.this, 17 * (1 + (progress / 100f)));
                settings.setDefaultFontSize(size);
                ts.setText("设置字体大小（" + progress + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferencesUtil.setInt(RichTextActivity.this, Constant.KEY_FONT_SIZE, seekBar.getProgress());
            }
        });
        sb.setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
        });


//        SharedPreferencesUtil.setBoolean(this, Constant.KEY_IS_SHOW_POP, true);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
//    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getWindow().setAttributes(lp);
//    }
    @Override
    protected BasePresenter createPresenter() {
        return new RichTextPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.destroy();
    }


    @Override
    public void loadError(Throwable throwable) {
        mRtaLlGood.setEnabled(true);
        mRtaLlNotGood.setEnabled(true);
        throwable.printStackTrace();
//        if (throwable instanceof HttpException) {
        CommonHandler.actionThrowable(throwable);
//            if (((HttpException) throwable).response().code() == 401) {
//                T.ShowToast(this, Constant.MSG_NO_LOGIN);
//            } else {
//                T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
//            }
//        } else {
//            T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
//        }
    }

    @Override
    public void showUpdateResult(ResBase resBase) {
        //TODO 更新数量成功
    }


    @Override
    public void showListResult(ResInformation resInformation) {
        if (resInformation.getRetCode() == 0) {
            if (resInformation.getRetObj().getRows() != null && resInformation.getRetObj().getRows().size() > 0) {
                String keyWord = resInformation.getKeyWord();//标签云

                if (!TextUtils.isEmpty(keyWord)) {
                    String[] words = keyWord.split(",");
                    for (String w : words) {
                        w = w.replace("%", "");
                        w = w.replace("'", "");
                        if (!TextUtils.isEmpty(w) && !"null".equals(w)) {
                            w = "#" + w;
                            TextView textView = new TextView(this);
                            textView.setText(w);
                            textView.setTextColor(getResources().getColor(R.color.color_cloud_label));
                            mWwvHisWord.addView(textView);
                        }
                    }
                }
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
