package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.HomeSourceEvent;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.UserPresenter;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.IUserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class UserActivity extends BaseActivity implements IUserView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ama_iv_user_photo)
    ImageView mAmaIvUserPhoto;
    @BindView(R.id.ama_rl_head_portrait)
    RelativeLayout mAmaRlHeadPortrait;
    @BindView(R.id.iv_nick_name)
    ImageView mIvNickName;
    @BindView(R.id.ama_iv_nick_name)
    ImageView mAmaIvNickName;
    @BindView(R.id.ama_tv_nick_name)
    TextView mAmaTvNickName;
    @BindView(R.id.ama_rl_nick_name)
    RelativeLayout mAmaRlNickName;
    @BindView(R.id.iv_sex)
    ImageView mIvSex;
    @BindView(R.id.ama_iv_sex)
    ImageView mAmaIvSex;
    @BindView(R.id.ama_tv_sex)
    TextView mAmaTvSex;
    @BindView(R.id.ama_rl_sex)
    RelativeLayout mAmaRlSex;
    @BindView(R.id.iv_birth)
    ImageView mIvBirth;
    @BindView(R.id.ama_iv_birth)
    ImageView mAmaIvBirth;
    @BindView(R.id.ama_tv_birth)
    TextView mAmaTvBirth;
    @BindView(R.id.ama_rl_birth)
    RelativeLayout mAmaRlBirth;
    @BindView(R.id.iv_signature)
    ImageView mIvSignature;
    @BindView(R.id.ama_iv_signature)
    ImageView mAmaIvSignature;
    @BindView(R.id.ama_tv_signature)
    TextView mAmaTvSignature;
    @BindView(R.id.ama_rl_phone)
    RelativeLayout mAmaRlPhone;
    @BindView(R.id.ama_btn_login)
    Button mAmaBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserPresenter) mPresenter).getUserInfo();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(HomeSourceEvent event) {
        if (event.getMessage() == 0) {
            ((UserPresenter) mPresenter).getUserInfo();
        }
    }

    @Override
    protected void initView() {
        mAmaBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.setString(UserActivity.this, Constant.SP_LOGIN_USER_NAME, "");
                SharedPreferencesUtil.setString(UserActivity.this, Constant.SP_LOGIN_PSD, "");
                SharedPreferencesUtil.setString(UserActivity.this, Constant.TOKEN, "");
                SharedPreferencesUtil.setString(UserActivity.this, Constant.USER_ID, "");

                ((UserPresenter) mPresenter).getUserInfo();
                EventBus.getDefault().post(new MineEvent(0));
            }
        });
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_user;
    }

    @Override
    protected void setAppTitle() {
        mSysToolbar.setTitle("");
        setSupportActionBar(mSysToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mSysTvTitle.setText(getResources().getString(R.string.str_user_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new UserPresenter(this, this);
    }


    @Override
    public TextView getTvNickName() {
        return mAmaTvNickName;
    }

    @Override
    public TextView getTvDescription() {
        return mAmaTvSignature;
    }

    @Override
    public TextView getTvSex() {
        return mAmaTvSex;
    }

    @Override
    public TextView getTvBirth() {
        return mAmaTvBirth;
    }

    @Override
    public ImageView getTvAvatar() {
        return mAmaIvUserPhoto;
    }
}
