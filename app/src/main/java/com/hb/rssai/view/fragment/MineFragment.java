package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.me.CollectionActivity;
import com.hb.rssai.view.me.SettingActivity;
import com.hb.rssai.view.subscription.SourceListActivity;
import com.zbar.lib.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class MineFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fm_ll_avatar)
    LinearLayout mFmLlAvatar;
    @BindView(R.id.fm_ll_message)
    RelativeLayout mFmLlMessage;
    @BindView(R.id.fm_ll_collection)
    RelativeLayout mFmLlCollection;
    @BindView(R.id.fm_ll_setting)
    RelativeLayout mFmLlSetting;
    @BindView(R.id.fm_iv_ava)
    ImageView mFmIvAva;
    @BindView(R.id.irs_tv_msg_count)
    TextView mIrsTvMsgCount;
    @BindView(R.id.fm_ll_scan)
    RelativeLayout mFmLlScan;
    @BindView(R.id.fm_tv_account)
    TextView mFmTvAccount;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public final static int REQUESTCODE = 1;
    public final static int REQUEST_LOGIN = 2;

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSysToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mSysToolbar);
        mSysTvTitle.setText(getResources().getString(R.string.str_main_mine));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

  
    @OnClick({R.id.fm_ll_collection, R.id.fm_ll_setting, R.id.fm_ll_scan, R.id.fm_ll_avatar})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_ll_collection:
                getActivity().startActivity(new Intent(getContext(), CollectionActivity.class));
                break;
            case R.id.fm_ll_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fm_ll_scan:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUESTCODE);
                break;
            case R.id.fm_ll_avatar:
                startActivityForResult(new Intent(getContext(), LoginActivity.class), REQUEST_LOGIN);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQUESTCODE) {
                String info = Base64Util.getDecodeStr(data.getStringExtra("info"));
                if (info.startsWith(Constant.FLAG_RSS_SOURCE)) {
                    //打开
                    Intent intent = new Intent(getContext(), SourceListActivity.class);
                    intent.putExtra(SourceListActivity.KEY_LINK, info.replace(Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceListActivity.KEY_TITLE, "分享资讯");
                    getContext().startActivity(intent);

                } else if (info.startsWith(Constant.FLAG_COLLECTION_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "收藏");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_COLLECTION_SOURCE, ""));
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_URL_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_URL_SOURCE, ""));
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_RSS_SOURCE)) {
                    RssSource rssSource = new RssSource();
                    rssSource.setName("订阅");
                    rssSource.setLink(info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    LiteOrmDBUtil.insert(rssSource);
                    // initData();
                    //打开
                    Intent intent = new Intent(getContext(), SourceListActivity.class);
                    intent.putExtra(SourceListActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceListActivity.KEY_TITLE, "分享资讯");
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_COLLECTION_SOURCE)) {
                    UserCollection userCollection = new UserCollection();
                    userCollection.setTitle("收藏");
                    userCollection.setLink(info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE + Constant.FLAG_COLLECTION_SOURCE, ""));
                    LiteOrmDBUtil.insert(userCollection);

                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE + Constant.FLAG_COLLECTION_SOURCE, ""));
                    getContext().startActivity(intent);

                } else if (info.startsWith(Constant.FLAG_PRESS_URL_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_PRESS_URL_SOURCE + Constant.FLAG_URL_SOURCE, ""));
                    getContext().startActivity(intent);
                }
            } else if (requestCode == REQUEST_LOGIN) {
                mFmTvAccount.setText(SharedPreferencesUtil.getString(getContext(), Constant.SP_LOGIN_USER_NAME, ""));
            }
        }
    }
}
