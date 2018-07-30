package com.hb.rssai.view.iView;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public interface IMineView {
    TextView getTvReadCount();

    TextView getTvSubscribeCount();

    TextView getTvAccount();

    ImageView getIvAva();

    String getInformationId();

    String getSubscribeId();

    TextView getTvSignature();

    TextView  getTvMsgCount();

    ImageView getMfIvToBg();

}
