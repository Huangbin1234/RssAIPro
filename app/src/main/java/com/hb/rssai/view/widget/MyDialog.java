package com.hb.rssai.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hb.rssai.R;
import com.hb.rssai.util.DisplayUtil;

/**
 * Created by Administrator
 * on 2019/4/16
 */
public class MyDialog extends Dialog {
    Context mContext;
    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= View.inflate(getContext(), R.layout.pop_add_source,null);
        setContentView(view);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = DisplayUtil.dip2px(mContext,250);
        lp.width = DisplayUtil.dip2px(mContext,200);
        win.setAttributes(lp);

    }


}
