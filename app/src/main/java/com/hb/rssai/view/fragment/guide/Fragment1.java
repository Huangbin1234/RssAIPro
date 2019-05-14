package com.hb.rssai.view.fragment.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;


/**
 * Created by zq on 2016/11/12.
 */

public class Fragment1 extends Fragment {
    private View view;
    private ImageView gf_iv;
    private RelativeLayout mRelativeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_background);
        gf_iv = (ImageView) view.findViewById(R.id.gf_iv);
        Glide.with(getContext()).load(ApiRetrofit.BASE + "app_update/pic/g1.jpg").thumbnail(0.1f).error(R.mipmap.ic_error).placeholder(R.mipmap.ic_place).diskCacheStrategy(DiskCacheStrategy.NONE).into(gf_iv);
    }
}
