package com.hb.rssai.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 搜索框观察器
 * Created by Administrator on 2017/3/20.
 */

public class SearchTextWatcher implements TextWatcher {
    private boolean check = false, firstCheck = true;
    private EditText itsEtKey;
    private ImageView itsIvClear;

    public SearchTextWatcher(EditText itsEtKey, ImageView itsIvClear) {
        this.itsEtKey = itsEtKey;
        this.itsIvClear = itsIvClear;
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(itsEtKey.getText().toString().trim())) {
            check = true;
        } else {
            check = false;
        }
        if (firstCheck) {
            firstCheck = false;
            if (check) {
                itsIvClear.setVisibility(View.VISIBLE);
            }
        } else {
            if (!check) {
                itsIvClear.setVisibility(View.GONE);
            } else {
                itsIvClear.setVisibility(View.VISIBLE);
            }
        }
    }
}
