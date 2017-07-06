package com.hb.rssai.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.fragment.HomeFragment;
import com.hb.rssai.view.fragment.SubscriptionFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener, HomeFragment.OnFragmentInteractionListener, SubscriptionFragment.OnFragmentInteractionListener {

    @BindView(R.id.main_bottom_home_iv)
    ImageView mMainBottomHomeIv;
    @BindView(R.id.main_bottom_home_tv)
    TextView mMainBottomHomeTv;
    @BindView(R.id.main_bottom_layout_home)
    LinearLayout mMainBottomLayoutHome;
    @BindView(R.id.main_bottom_subscription_iv)
    ImageView mMainBottomSubscriptionIv;
    @BindView(R.id.main_bottom_subscription_tv)
    TextView mMainBottomSubscriptionTv;
    @BindView(R.id.main_bottom_layout_subscription)
    LinearLayout mMainBottomLayoutSubscription;
    @BindView(R.id.main_bottom_layout)
    LinearLayout mMainBottomLayout;
    @BindView(R.id.ma_view)
    View mMaView;
    @BindView(R.id.ma_frame_layout)
    FrameLayout mMaFrameLayout;
    @BindView(R.id.rl_root_view)
    RelativeLayout mRlRootView;
    private Context mContext;
    private HomeFragment homeFragment;
    private SubscriptionFragment subscriptionFragment;
    private int positionId;//positionId 关闭应用时的Fragment ID
    // 退出程序
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        homeFragment = new HomeFragment();
        subscriptionFragment = new SubscriptionFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.ma_frame_layout, homeFragment);
        fragmentTransaction.add(R.id.ma_frame_layout, subscriptionFragment);
        fragmentTransaction.commit();
        showFragment(R.id.main_bottom_layout_home);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setAppTitle() {
        mContext = this;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * TODO: 控制fragment 显示
     *
     * @param id
     */
    private void showFragment(int id) {
        positionId = id;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.hide(homeFragment);
        mTransaction.hide(subscriptionFragment);
        // 设置按钮 图片为普通颜色
        mMainBottomHomeTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomHomeIv.setImageResource(R.mipmap.main_home);
        mMainBottomSubscriptionTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomSubscriptionIv.setImageResource(R.mipmap.main_subscription);
        switch (id) {
            case R.id.main_bottom_layout_home:
                mTransaction.show(homeFragment);
                mMainBottomHomeTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomHomeIv.setImageResource(R.mipmap.main_home_active);
                break;
            case R.id.main_bottom_layout_subscription:
                mTransaction.show(subscriptionFragment);
                mMainBottomSubscriptionTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomSubscriptionIv.setImageResource(R.mipmap.main_task_active);
                break;
        }
        mTransaction.commit();
    }


    @OnClick({R.id.main_bottom_layout_home, R.id.main_bottom_layout_subscription})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_bottom_layout_home:
                showFragment(R.id.main_bottom_layout_home);
                break;
            case R.id.main_bottom_layout_subscription:
                showFragment(R.id.main_bottom_layout_subscription);
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    // 如果两次按键时间间隔大于2秒，则不退出
                    T.ShowToast(mContext, "再按一次退出程序", 0);
                    // 更新firstTime
                    firstTime = secondTime;
                    return true;
                } else {
                    finish();
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 下面两个方法解决缓存应用回来后防止Fragment重叠
     * positionId 关闭应用时的Fragment ID
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        positionId = savedInstanceState.getInt("positionId");
        showFragment(positionId);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //记录当前的position
        outState.putInt("positionId", positionId);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
