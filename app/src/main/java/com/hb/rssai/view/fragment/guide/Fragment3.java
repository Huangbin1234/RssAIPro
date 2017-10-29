package com.hb.rssai.view.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.common.LoadActivity;


/**
 * Created by zq on 2016/11/12.
 */


public class Fragment3 extends Fragment {
    private View view;
    private RelativeLayout mRelativeLayout;
    private TextView mTextView;

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
        mRelativeLayout.setBackgroundResource(R.mipmap.g3);
        mTextView = (TextView) view.findViewById(R.id.fragment_text);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.setString(getContext(), Constant.KEY_GUIDE, "1");
                Intent intent = new Intent(getActivity(), LoadActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
