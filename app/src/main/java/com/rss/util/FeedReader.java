package com.rss.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.T;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rss.bean.Information;
import com.rss.bean.RSSItemBean;
import com.rss.bean.RssChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.findApi;

public class FeedReader {
    List<Information> infoList;


    public RssChannel getContent(String link) {
        RssChannel rssList = getRss(link);
        return rssList;
    }

    public RssChannel getRss(String url) {
        okhttp3.Response response = null;
        try {
            SyndFeedInput input = new SyndFeedInput();//rome按SyndFeed类型生成rss和atom的实例,
            URL feedUrl = new URL(url);//SyndFeedInput:从远程读到xml结构的内容转成SyndFeedImpl实例
            XmlReader xmlReader = null;
            //设置超时
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                    .addInterceptor(new Interceptor() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .removeHeader("User-Agent")//移除旧的
                                    //WebSettings.getDefaultUserAgent(mContext) 是获取原来的User-Agent
                                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36")
                                    .build();
                            return chain.proceed(request);
                        }
                    })
//                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
//                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                    .build();

            Request request = new Request.Builder().url(feedUrl).build();
            response = client.newCall(request).execute();

            //inputStream 转string 过滤特殊字符
            if (url.equals("https://www.leiphone.com/feed/")) {
                InputStream inputStream = StringUtil.streamToString(response.body().byteStream());
                xmlReader = new XmlReader(inputStream);
            } else {
                xmlReader = new XmlReader(response.body().byteStream());
            }
            xmlReader.setDefaultEncoding("UTF-8");
            SyndFeed feed = input.build(xmlReader);   //SyndFeed是rss和atom实现类SyndFeedImpl的接口

            List<SyndEntry> entries = feed.getEntries();
            RSSItemBean item = null;
            RssChannel rssChannel = new RssChannel();
            rssChannel.setDescription(feed.getDescription());
            rssChannel.setCopyright(feed.getCopyright());
            rssChannel.setLanguage(feed.getLanguage());
            rssChannel.setLink(feed.getLink());
            rssChannel.setTitle(feed.getTitle() != null ? StringUtil.formatStr(feed.getTitle()) : "");
            rssChannel.setPubDate(feed.getPublishedDate());
            if (feed.getImage() != null) {
                RssChannel.ImageBean imageBean = new RssChannel.ImageBean();
                imageBean.setTitle(feed.getImage().getTitle());
                imageBean.setUrl(feed.getImage().getUrl());
                imageBean.setLink(feed.getImage().getLink());
                rssChannel.setImage(imageBean);
            }
            List<RSSItemBean> rssItemBeans = new ArrayList<RSSItemBean>();
            for (SyndEntry entry : entries) {
                item = new RSSItemBean();
                item.setTitle(entry.getTitle().trim());
                item.setType(feed.getTitleEx().getValue() != null ? feed.getTitleEx().getValue().trim() : "");
                item.setUri(entry.getUri());
                item.setPubDate(entry.getPublishedDate());
                item.setAuthor(entry.getAuthor());
                if (null != entry.getContents() && entry.getContents().size() > 0) {
                    item.setContent(entry.getContents().get(0).getValue());
                    item.setDescription(entry.getContents().get(0).getValue());
                    item.setImages(StringUtil.getRegexImages(entry.getContents().get(0).getValue()));
                }
                if (entry.getDescription() != null) {
                    String content = item.getContent();
                    String des = entry.getDescription().getValue();
                    if (null != content && null != des && content.length() > des.length()) {
                        item.setDescription(content);
                    } else {
                        item.setDescription(entry.getDescription().getValue());
                    }
                    if (null == item.getImages() || item.getImages().size() <= 0) {
                        item.setImages(StringUtil.getRegexImages(entry.getDescription().getValue()));
                    }
                }
                item.setLink(entry.getLink());
                rssItemBeans.add(item);
            }
            rssChannel.setRSSItemBeen(rssItemBeans);
            return rssChannel;
        } catch (Throwable ex) {
            ex.getMessage();
        } finally {
            if (null != response) {
//                try {
                response.body().close();//even this doesn't work!
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
        return null;
    }

    public void modifySubscription(Map<String, Object> params, final boolean isLogo) {
        findApi.modifySubscription(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResBase>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        T.ShowToast(ProjectApplication.getApplication(), e.getMessage());
                    }

                    @Override
                    public void onNext(ResBase resBase) {
                        if (isLogo) {
                            T.ShowToast(ProjectApplication.getApplication(), "Logo更新" + resBase.getRetMsg());
                        }else{
                            T.ShowToast(ProjectApplication.getApplication(), "未获取到Logo,可手动编辑");
                        }
                    }
                });
    }


    public List<Information> getInfoList(String link, String subscribeId, boolean isTag, String img) {
        infoList = new ArrayList<>();
        RssChannel rssChannel = new FeedReader().getContent(link);
        if (null != rssChannel && null != rssChannel.getRSSItemBeen() && rssChannel.getRSSItemBeen().size() > 0) {
            int size = rssChannel.getRSSItemBeen().size();


            Date date = new Date();
            for (int i = 0; i < size; i++) {
                RSSItemBean rs = rssChannel.getRSSItemBeen().get(i);
                Information information = new Information();
                String infoId = UUID.randomUUID().toString();
                information.setId(infoId);
                information.setAbstractContent(rs.getDescription());
                information.setContent(rs.getContent());
                information.setAuthor(rs.getAuthor());
                information.setImageUrls(StringUtil.listToString(rs.getImages()));
                information.setLink(rs.getLink());
                information.setPubTime(rs.getPubDate() != null ? Constant.sdf.format(rs.getPubDate()) : "");
                information.setOprTime(Constant.sdf.format(date));
                information.setTitle(rs.getTitle().trim());
                information.setWhereFrom(rs.getType());
                information.setClickGood(0);
                information.setClickNotGood(0);
                information.setSubscribeId(subscribeId);
                infoList.add(information);
            }

            if (TextUtils.isEmpty(img)) {
                //TODO 更新LOGO
                Map<String, Object> params = new HashMap<>();
                boolean isLogo = false;
                if (!TextUtils.isEmpty(rssChannel.getDescription())) {
                    params.put("abstractContent", rssChannel.getDescription().trim());
                }
                if (null != rssChannel.getImage() && !TextUtils.isEmpty(rssChannel.getImage().getUrl())) {
                    params.put("img", rssChannel.getImage().getUrl().trim());
                    isLogo = true;
                }
                if (infoList.size() > 0) {
                    params.put("id", subscribeId);
                    params.put("isTag", isTag);
                    String lastTime = rssChannel.getPubDate() != null ? Constant.sdf.format(rssChannel.getPubDate()) : "";
                    if (TextUtils.isEmpty(lastTime) && infoList.size() > 0) {
                        params.put("lastTime", infoList.get(0).getPubTime());
                    } else {
                        params.put("lastTime", rssChannel.getPubDate() != null ? Constant.sdf.format(rssChannel.getPubDate()) : "");
                    }
                    Map<String, Object> map = new HashMap<>();
                    String jsonParams = GsonUtil.toJson(params);
                    map.put(Constant.KEY_JSON_PARAMS, jsonParams);
                    modifySubscription(map, isLogo);
                }
            }
        }
        return infoList;
    }
}