package com.hb.rssai.view.iView;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public interface IOfficeView {
    ListView getListView();
    CheckBox getChkAll();
    RelativeLayout getOaRlAll();
    Button getBtnDown();
}
