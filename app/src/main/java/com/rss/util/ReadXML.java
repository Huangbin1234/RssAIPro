package com.rss.util;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rss.bean.RssBean;
import com.rss.bean.RssTeamBean;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 读取xml文件
 *
 * @author Longxuan
 */
public class ReadXML {


    private static class LazyHolder {
        private static final ReadXML INSTANCE = new ReadXML();
    }

    private ReadXML() {
    }

    public static final ReadXML getInstance() {
        return LazyHolder.INSTANCE;
    }

    // rss分组订阅列表
    private List<RssTeamBean> rssTeamBeanList = new ArrayList<RssTeamBean>();

    /**
     * 读取ompl文件
     *
     * @param filePath
     */
    public void readRss(String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            // System.out.println("找不到【" + filePath + "】文件");
            // return;
            throw new RuntimeException("找不到【" + filePath + "】文件");
        }

        try {

            // 读取并解析XML文档
            // SAXReader就是一个管道，用一个流的方式，把xml文件读出来
            SAXReader reader = new SAXReader();
            FileInputStream fis = new FileInputStream(file);

            // 下面的是通过解析xml字符串的
            Document doc = reader.read(fis);

            // 获取根节点
            Element rootElt = doc.getRootElement(); // 获取根节点
            // System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

            // 获取head/title节点
            Element titleElt = (Element) rootElt.selectSingleNode("head/title");// 获取head节点下的子节点title

            // 获取分组名称
            String title = titleElt.getTextTrim();

            // 获取body节点
            Element bodyElt = (Element) rootElt.selectSingleNode("body");

            // 获取body下的第一个outline节点
            Element outlineElt = (Element) bodyElt.selectSingleNode("outline");

            // 判断该outlineElt节点的属性数量，>2说明是详细博客订阅信息，否则则是分组信息。
            if (outlineElt.attributes().size() > 2) { // 详细博客订阅信息

                // 实例化 RSS分组实体
                RssTeamBean rssTeamBean = new RssTeamBean();

                // 获取body节点下的outline节点
                Iterator<?> iter = bodyElt.elementIterator("outline");

                // 输出分组名称
                // System.out.println("分组名称:" + title);

                // 记录分组名称
                rssTeamBean.setTitle(title);
                rssTeamBean.setText(title);

                // 实例化订阅列表
                List<RssBean> rssBeanList = new ArrayList<RssBean>();

                // 获取详细博客订阅信息
                ReadBlogsInfo(iter, rssBeanList);

                // 设置订阅列表到分组中
                rssTeamBean.setRssBeanList(rssBeanList);

                // 添加分组到rss分组订阅列表
                rssTeamBeanList.add(rssTeamBean);

            } else { // 分组信息

                // 获取body节点下的outline节点
                Iterator<?> iter = bodyElt.elementIterator("outline");

                while (iter.hasNext()) {

                    // 读取outline节点下的所有outline信息，每条信息都是一条订阅记录
                    Element TeamElt = (Element) iter.next();

                    // 实例化 RSS分组实体
                    RssTeamBean rssTeamBean = new RssTeamBean();

                    // 重新获取分组名称
                    title = TeamElt.attributeValue("title");
                    String text = TeamElt.attributeValue("text");
                    // System.out.println("分组title:" + title + "   text:" +
                    // text);

                    // 记录分组名称和显示名称
                    rssTeamBean.setTitle(title);
                    rssTeamBean.setText(text);

                    // 实例化订阅列表
                    List<RssBean> rssBeanList = new ArrayList<RssBean>();

                    // 获取body节点下的outline节点
                    Iterator<?> iterRss = TeamElt.elementIterator("outline");

                    // 获取详细博客订阅信息
                    ReadBlogsInfo(iterRss, rssBeanList);

                    // 设置订阅列表到分组中
                    rssTeamBean.setRssBeanList(rssBeanList);

                    // 添加分组到rss分组订阅列表
                    rssTeamBeanList.add(rssTeamBean);
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 读取ompl文件
     *
     * @param filePath
     */
    public void readRss(URL url) {


        try {

            // 读取并解析XML文档
            // SAXReader就是一个管道，用一个流的方式，把xml文件读出来
            SAXReader reader = new SAXReader();

            // 下面的是通过解析xml字符串的
            Document doc = reader.read(url);
            SyndFeedInput input = new SyndFeedInput();//rome按SyndFeed类型生成rss和atom的实例,
            try {
                SyndFeed feed = input.build(new XmlReader(url));
            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 获取根节点
            Element rootElt = doc.getRootElement(); // 获取根节点
            // System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

            // 获取head/title节点
            Element titleElt = (Element) rootElt.selectSingleNode("head/title");// 获取head节点下的子节点title

            // 获取分组名称
            String title = titleElt.getTextTrim();

            // 获取body节点
            Element bodyElt = (Element) rootElt.selectSingleNode("body");

            // 获取body下的第一个outline节点
            Element outlineElt = (Element) bodyElt.selectSingleNode("outline");

            // 判断该outlineElt节点的属性数量，>2说明是详细博客订阅信息，否则则是分组信息。
            if (outlineElt.attributes().size() > 2) { // 详细博客订阅信息

                // 实例化 RSS分组实体
                RssTeamBean rssTeamBean = new RssTeamBean();

                // 获取body节点下的outline节点
                Iterator<?> iter = bodyElt.elementIterator("outline");

                // 输出分组名称
                // System.out.println("分组名称:" + title);

                // 记录分组名称
                rssTeamBean.setTitle(title);
                rssTeamBean.setText(title);

                // 实例化订阅列表
                List<RssBean> rssBeanList = new ArrayList<RssBean>();

                // 获取详细博客订阅信息
                ReadBlogsInfo(iter, rssBeanList);

                // 设置订阅列表到分组中
                rssTeamBean.setRssBeanList(rssBeanList);

                // 添加分组到rss分组订阅列表
                rssTeamBeanList.add(rssTeamBean);

            } else { // 分组信息

                // 获取body节点下的outline节点
                Iterator<?> iter = bodyElt.elementIterator("outline");

                while (iter.hasNext()) {

                    // 读取outline节点下的所有outline信息，每条信息都是一条订阅记录
                    Element TeamElt = (Element) iter.next();

                    // 实例化 RSS分组实体
                    RssTeamBean rssTeamBean = new RssTeamBean();

                    // 重新获取分组名称
                    title = TeamElt.attributeValue("title");
                    String text = TeamElt.attributeValue("text");
                    // System.out.println("分组title:" + title + "   text:" +
                    // text);

                    // 记录分组名称和显示名称
                    rssTeamBean.setTitle(title);
                    rssTeamBean.setText(text);

                    // 实例化订阅列表
                    List<RssBean> rssBeanList = new ArrayList<RssBean>();

                    // 获取body节点下的outline节点
                    Iterator<?> iterRss = TeamElt.elementIterator("outline");

                    // 获取详细博客订阅信息
                    ReadBlogsInfo(iterRss, rssBeanList);

                    // 设置订阅列表到分组中
                    rssTeamBean.setRssBeanList(rssBeanList);

                    // 添加分组到rss分组订阅列表
                    rssTeamBeanList.add(rssTeamBean);
                }
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 读取当前组博客订阅信息
     *
     * @param iter        当前节点的子节点迭代器
     * @param rssBeanList 订阅列表
     */
    private void ReadBlogsInfo(Iterator<?> iter, List<RssBean> rssBeanList) {

        // 遍历所有outline节点，每个节点都是一条订阅信息
        while (iter.hasNext()) {

            RssBean rssBean = new RssBean();

            Element outlineElt = (Element) iter.next();
            String htmlUrl = outlineElt.attributeValue("htmlUrl"); // 拿到当前节点的htmlUrl属性值
            String xmlUrl = outlineElt.attributeValue("xmlUrl"); // 拿到当前节点的xmlUrl属性值
            String version = outlineElt.attributeValue("version"); // 拿到当前节点的version属性值
            String type = outlineElt.attributeValue("type"); // 拿到当前节点的type属性值
            String outlineTitle = outlineElt.attributeValue("title"); // 拿到当前节点的title属性值
            String outlineText = outlineElt.attributeValue("text"); // 拿到当前节点的text属性值

            // System.out.print("<outline htmlUrl=\"" + htmlUrl + "\" ");
            // System.out.print("xmlUrl=\"" + xmlUrl + "\" ");
            // System.out.print("version=\"" + version + "\" ");
            // System.out.print("type=\"" + type + "\" ");
            // System.out.print("title=\"" + outlineTitle + "\" ");
            // System.out.println("text=\"" + outlineText + "\" />");

            rssBean.setHtmlUrl(htmlUrl);
            rssBean.setXmlUrl(xmlUrl);
            rssBean.setVersion(version);
            rssBean.setType(type);
            rssBean.setTitle(outlineTitle);
            rssBean.setText(outlineText);
            rssBean.setText(outlineText);

            // 将每条订阅信息，存放到订阅列表中
            rssBeanList.add(rssBean);
        }
    }

    /**
     * 获取Rss分组订阅列表
     *
     * @return
     */
    public List<RssTeamBean> getRssTemBeanList() {
        return rssTeamBeanList;
    }
}