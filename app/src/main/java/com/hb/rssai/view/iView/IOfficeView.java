package com.hb.rssai.view.iView;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hb.rssai.bean.ResDataGroup;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/3 0003.
 */

public interface IOfficeView {

    Map<String, String> getParams();

    void loadError(Throwable throwable);

    void setDataGroupResult(ResDataGroup resDataGroup);
}
