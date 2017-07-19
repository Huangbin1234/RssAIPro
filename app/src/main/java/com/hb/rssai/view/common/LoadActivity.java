package com.hb.rssai.view.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadActivity extends AppCompatActivity {

    @BindView(R.id.la_text)
    TextView mSampleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        RssSource rssSource = new RssSource();
        rssSource.setName("环球军事");
        rssSource.setId(1);
        rssSource.setLink("http://rss.huanqiu.com/mil/china.xml");
        LiteOrmDBUtil.insert(rssSource);

        RssSource rssSource2 = new RssSource();
        rssSource2.setId(2);
        rssSource2.setName("新浪军事");
        rssSource2.setLink("http://rss.sina.com.cn/roll/mil/hot_roll.xml");
        LiteOrmDBUtil.insert(rssSource2);

        RssSource rssSource3 = new RssSource();
        rssSource3.setId(3);
        rssSource3.setName("善心汇新闻");
        rssSource3.setLink("http://news.baidu.com/ns?word=%C9%C6%D0%C4%BB%E3&tn=newsrss&sr=0&cl=2&rn=20&ct=0");
        LiteOrmDBUtil.insert(rssSource3);

        RssSource rssSource4 = new RssSource();
        rssSource4.setName("环球军事");
        rssSource4.setId(4);
        rssSource4.setLink("http://rss.huanqiu.com/mil/china.xml");
        LiteOrmDBUtil.insert(rssSource4);

        RssSource rssSource5 = new RssSource();
        rssSource5.setId(5);
        rssSource5.setName("新浪军事");
        rssSource5.setLink("http://rss.sina.com.cn/roll/mil/hot_roll.xml");
        LiteOrmDBUtil.insert(rssSource5);

        RssSource rssSource6 = new RssSource();
        rssSource6.setId(6);
        rssSource6.setName("善心汇新闻");
        rssSource6.setLink("http://news.baidu.com/ns?word=%C9%C6%D0%C4%BB%E3&tn=newsrss&sr=0&cl=2&rn=20&ct=0");
        LiteOrmDBUtil.insert(rssSource6);

        RssSource rssSource7 = new RssSource();
        rssSource7.setName("环球军事");
        rssSource7.setId(7);
        rssSource7.setLink("http://rss.huanqiu.com/mil/china.xml");
        LiteOrmDBUtil.insert(rssSource7);

        RssSource rssSource8 = new RssSource();
        rssSource8.setId(8);
        rssSource8.setName("善心汇新闻");
        rssSource8.setLink("http://news.baidu.com/ns?word=%C9%C6%D0%C4%BB%E3&tn=newsrss&sr=0&cl=2&rn=20&ct=0");
        LiteOrmDBUtil.insert(rssSource8);

//        RssSource rssSource9 = new RssSource();
//        rssSource9.setName("环球军事");
//        rssSource9.setId(9);
//        rssSource9.setLink("http://rss.huanqiu.com/mil/china.xml");
//        LiteOrmDBUtil.insert(rssSource9);
//
//        RssSource rssSource10 = new RssSource();
//        rssSource10.setId(10);
//        rssSource10.setName("新浪军事");
//        rssSource10.setLink("http://rss.sina.com.cn/roll/mil/hot_roll.xml");
//        LiteOrmDBUtil.insert(rssSource10);
//
//        RssSource rssSource11 = new RssSource();
//        rssSource11.setId(11);
//        rssSource11.setName("善心汇新闻");
//        rssSource11.setLink("http://news.baidu.com/ns?word=%C9%C6%D0%C4%BB%E3&tn=newsrss&sr=0&cl=2&rn=20&ct=0");
//        LiteOrmDBUtil.insert(rssSource11);

        handler.sendEmptyMessage(1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(LoadActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        }
    };
}
