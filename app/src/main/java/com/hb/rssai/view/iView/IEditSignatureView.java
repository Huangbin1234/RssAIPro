package com.hb.rssai.view.iView;

import android.widget.EditText;

import com.hb.rssai.bean.ResUser;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public interface IEditSignatureView extends IBaseView {

    void setUpdateResult(ResUser resBase);

    Map<String,String> getUpdateParams();

    void loadError(Throwable throwable);

}
