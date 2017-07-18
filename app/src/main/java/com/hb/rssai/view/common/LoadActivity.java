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
        new Thread(() -> {
            try {
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

                RssSource rssSource4 = new RssSource();
                rssSource4.setId(3);
                rssSource4.setName("善心汇新闻");
                rssSource4.setLink("http://news.baidu.com/ns?word=%C9%C6%D0%C4%BB%E3&tn=newsrss&sr=0&cl=2&rn=20&ct=0");
                LiteOrmDBUtil.insert(rssSource4);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);
        }).start();
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
