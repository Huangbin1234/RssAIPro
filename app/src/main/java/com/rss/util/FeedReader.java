package com.rss.util;

import com.hb.rssai.bean.RssChannel;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rss.bean.RSSItemBean;
import com.rss.bean.Website;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It Reads and prints any RSS/Atom feed type.
 */
public class FeedReader {

    private static BufferedReader getResponseRSS(String dataSource) throws IOException {
        BufferedReader reader;
        reader = CustomHttpUtils.httpGETReader(dataSource);
        return reader;
    }

    /**
     * @param url rss 网站地址  比如：http://www.ithome.com/rss/
     * @return 所有文章对象
     * @throws Exception
     */
//    public List<RSSItemBean> getRss(String url) throws Exception {
    public RssChannel getRss(String url) throws Exception {
        SyndFeedInput input = new SyndFeedInput();//rome按SyndFeed类型生成rss和atom的实例,
        URL feedUrl = new URL(url);//SyndFeedInput:从远程读到xml结构的内容转成SyndFeedImpl实例
        SyndFeed feed = input.build(new XmlReader(feedUrl));   //SyndFeed是rss和atom实现类SyndFeedImpl的接口

        List<SyndEntry> entries = feed.getEntries();
        RSSItemBean item = null;
        RssChannel rssChannel = new RssChannel();
        rssChannel.setDescription(feed.getDescription());
        rssChannel.setCopyright(feed.getCopyright());
        rssChannel.setLanguage(feed.getLanguage());
        rssChannel.setLink(feed.getLink());
        rssChannel.setTitle(feed.getTitle() != null ? formatStr(feed.getTitle()) : "");
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
            if (entry.getDescription() != null) {
                item.setDescription(entry.getDescription().getValue());
                item.setImages(getRegexImages(entry.getDescription().getValue()));
            }
            item.setLink(entry.getLink());
            rssItemBeans.add(item);
        }
        rssChannel.setRSSItemBeen(rssItemBeans);
        return rssChannel;
//        return rssItemBeans;
    }

    private String formatStr(String s) {
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(s);

        if (m.matches()) {
            return m.group(1);
        } else {
            return s;
        }
    }

    /**
     * 提取字符串内所有的img标签下的src
     *
     * @param content
     * @return
     */
    public List<String> getRegexImages(String content) {
        String regex;
        List<String> list = new ArrayList<String>();
        //提取字符串中的img标签
        regex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(content);
        while (ma.find()) {
            //提取字符串中的src路径
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(ma.group());
            while (m.find()) {
                if ("http".equals(m.group(1).substring(0, 4))) {//只提取http开头的图片地址
                    //System.out.println(m.group(1));
                    list.add(m.group(1));
                }
            }
        }
        return list;
    }

    /**
     * 从html 中获取 新闻正文
     *
     * @param website 网站对象，我自己定义的
     * @return 加入了新闻正文的 RSS对象  对象链表
     * @throws Exception
     */
//    public List<RSSItemBean> getContent(Website website) throws Exception {
    public RssChannel getContent(Website website) throws Exception {
//        String content;
//        List<RSSItemBean> rssList = getRss(website.getUrl());
        RssChannel rssList = getRss(website.getUrl());
//        FindHtml findHtml = new FindHtml(website.getStartTag(), website.getEndTag(), website.getEncoding());
//        for (RSSItemBean rsItem : rssList) {
//            String link = rsItem.getUri();
//
//            content = findHtml.getContent(link);   //关键方法，获取新闻征文
//            rsItem.setContent(content);
//            rsItem.setFid(Integer.parseInt(website.getFid()));
//        }
        return rssList;
    }
}