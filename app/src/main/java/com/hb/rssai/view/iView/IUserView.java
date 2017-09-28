package com.hb.rssai.view.iView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public interface IUserView {
    TextView getTvNickName();

    TextView getTvDescription();

    TextView getTvSex();

    TextView getTvBirth();

    ImageView getTvAvatar();

    String getEditType();

    String getEtContent();

    String getFilePath();
}
