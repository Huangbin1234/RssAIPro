package com.rss.util;

import com.hb.rssai.constants.Constant;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rss.bean.Information;
import com.rss.bean.RSSItemBean;
import com.rss.bean.RssChannel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FeedReader {
    List<Information> infoList;


    public RssChannel getContent(String link) {
        RssChannel rssList = getRss(link);
        return rssList;
    }

    public RssChannel getRss(String url) {
        Response response = null;
        try {
            SyndFeedInput input = new SyndFeedInput();//rome按SyndFeed类型生成rss和atom的实例,
            URL feedUrl = new URL(url);//SyndFeedInput:从远程读到xml结构的内容转成SyndFeedImpl实例
            XmlReader xmlReader = null;
            //设置超时
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(30 * 1000, TimeUnit.MILLISECONDS);
            Request request = new Request.Builder().url(feedUrl).build();
            response = httpClient.newCall(request).execute();
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
                try {
                    response.body().close();//even this doesn't work!
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public List<Information> getInfoList(String link, String subscribeId) {
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
        }
        return infoList;
    }
}