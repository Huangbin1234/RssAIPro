package com.hb.rssai.view.iView;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface IRichTextView {
    RecyclerView getRtaRecyclerView();

    String getNewTitle();

    String getNewLink();


    String getInformationId();

    String getEvaluateType();

    TextView getTvNotGood();

    TextView getTvGood();

    LinearLayout getLlNotGood();

    LinearLayout getLlGood();
}
