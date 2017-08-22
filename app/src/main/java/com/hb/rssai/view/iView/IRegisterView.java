package com.hb.rssai.view.iView;

import android.widget.EditText;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public interface IRegisterView {
    EditText getEtPsd();

    EditText getEtSpsd();

    EditText getEtName();

    void toFinish();
}
