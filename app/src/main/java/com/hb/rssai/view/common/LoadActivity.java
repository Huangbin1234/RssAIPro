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

        RssSource rssSource5 = new RssSource();
        rssSource5.setId(5);
        rssSource5.setName("Engadget 中国版");
        rssSource5.setLink("http://cn.engadget.com/rss.xml");
        LiteOrmDBUtil.insert(rssSource5);

        RssSource rssSource6 = new RssSource();
        rssSource6.setId(6);
        rssSource6.setName("图灵社区");
        rssSource6.setLink("http://feed43.com/3267066686713821.xml");
        LiteOrmDBUtil.insert(rssSource6);


        new Thread(() -> {
            try {
                Thread.sleep(3000);
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
