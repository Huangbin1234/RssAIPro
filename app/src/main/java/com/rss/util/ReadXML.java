package com.rss.util;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;

import java.io.Reader;
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

    /**
     * 读取ompl文件
     */
    public List<Outline> readOpml(Reader reader) {
        WireFeedInput input = new WireFeedInput();
        Opml feed = null;
        try {
            feed = (Opml) input.build(reader);
            return feed.getOutlines();
        } catch (FeedException e) {
            e.printStackTrace();
            return null;
        }
    }
}