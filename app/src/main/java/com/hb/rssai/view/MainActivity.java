package com.hb.rssai.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MainEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.runtimePermissions.PermissionsActivity;
import com.hb.rssai.runtimePermissions.PermissionsChecker;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.fragment.FindFragment;
import com.hb.rssai.view.fragment.HomeFragment;
import com.hb.rssai.view.fragment.MineFragment;
import com.hb.rssai.view.fragment.SubscriptionFragment;
import com.hb.update.UpdateManager;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener, HomeFragment.OnFragmentInteractionListener, SubscriptionFragment.OnFragmentInteractionListener, MineFragment.OnFragmentInteractionListener, FindFragment.OnFragmentInteractionListener {

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
    @BindView(R.id.main_bottom_mine_iv)
    ImageView mMainBottomMineIv;
    @BindView(R.id.main_bottom_mine_tv)
    TextView mMainBottomMineTv;
    @BindView(R.id.main_bottom_layout_mine)
    LinearLayout mMainBottomLayoutMine;
    @BindView(R.id.main_bottom_find_iv)
    ImageView mMainBottomFindIv;
    @BindView(R.id.main_bottom_find_tv)
    TextView mMainBottomFindTv;
    @BindView(R.id.main_bottom_layout_find)
    LinearLayout mMainBottomLayoutFind;
    private Context mContext;
    private HomeFragment homeFragment;
    private SubscriptionFragment subscriptionFragment;
    private MineFragment mineFragment;
    private FindFragment findFragment;
    private int positionId;//positionId 关闭应用时的Fragment ID
    // 退出程序
    private long firstTime = 0;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private PermissionsChecker mPermissionsChecker;
    private final int REQUEST_CODE = 1;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        mPermissionsChecker = new PermissionsChecker(this);
        //进入对应的页面判断标记是否有更新在进行调用此方法
        if (SharedPreferencesUtil.getBoolean(mContext, Constant.SAVE_IS_UPDATE, false)) {
            UpdateManager.update(mContext);
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (event.getMessage() == 1) {
            if (SharedPreferencesUtil.hasKey(this, Constant.KEY_SYS_NIGHT_MODE_TIME) && ProjectApplication.sys_night_mode_time != SharedPreferencesUtil.getLong(this, Constant.KEY_SYS_NIGHT_MODE_TIME, 0)) {
                if (SharedPreferencesUtil.hasKey(this, Constant.KEY_SYS_NIGHT_MODE)) {
                    if (SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false)) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
            }
        }
    }

    @Override
    protected void initView() {
        if (savedInstanceState != null) {//恢复现场
            int id = savedInstanceState.getInt("positionId");
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = (HomeFragment) fm.getFragment(savedInstanceState, HomeFragment.class.getSimpleName());
            subscriptionFragment = (SubscriptionFragment) fm.getFragment(savedInstanceState, SubscriptionFragment.class.getSimpleName());
            findFragment = (FindFragment) fm.getFragment(savedInstanceState, FindFragment.class.getSimpleName());
            mineFragment = (MineFragment) fm.getFragment(savedInstanceState, MineFragment.class.getSimpleName());
            showFragment(id);
        } else {
            homeFragment = new HomeFragment();
            findFragment = new FindFragment();
            subscriptionFragment = new SubscriptionFragment();
            mineFragment = new MineFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.ma_frame_layout, homeFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, findFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, subscriptionFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, mineFragment);
            fragmentTransaction.commit();
            showFragment(R.id.main_bottom_layout_home);
        }
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
        mTransaction.hide(findFragment);
        mTransaction.hide(subscriptionFragment);
        mTransaction.hide(mineFragment);
        // 设置按钮 图片为普通颜色
        mMainBottomHomeTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomHomeIv.setImageResource(R.mipmap.main_home);

        mMainBottomFindTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomFindIv.setImageResource(R.mipmap.main_find);

        mMainBottomSubscriptionTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomSubscriptionIv.setImageResource(R.mipmap.main_subscription);

        mMainBottomMineTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_normal));
        mMainBottomMineIv.setImageResource(R.mipmap.main_user_male);
        switch (id) {
            case R.id.main_bottom_layout_home:
                mTransaction.show(homeFragment);
                mMainBottomHomeTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomHomeIv.setImageResource(R.mipmap.main_home_active);
                homeFragment.setUserVisibleHint(true);
                break;
            case R.id.main_bottom_layout_find:
                mTransaction.show(findFragment);
                mMainBottomFindTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomFindIv.setImageResource(R.mipmap.main_find_active);
                findFragment.setUserVisibleHint(true);
                break;
            case R.id.main_bottom_layout_subscription:
                mTransaction.show(subscriptionFragment);
                mMainBottomSubscriptionTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomSubscriptionIv.setImageResource(R.mipmap.main_task_active);
                subscriptionFragment.setUserVisibleHint(true);
                break;
            case R.id.main_bottom_layout_mine:
                mTransaction.show(mineFragment);
                mMainBottomMineTv.setTextColor(getResources().getColor(R.color.main_bottom_txt_press));
                mMainBottomMineIv.setImageResource(R.mipmap.main_user_active);
                mineFragment.setUserVisibleHint(true);
                break;
        }
        mTransaction.commit();
    }


    @OnClick({R.id.main_bottom_layout_home, R.id.main_bottom_layout_find, R.id.main_bottom_layout_subscription, R.id.main_bottom_layout_mine})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_bottom_layout_home:
                showFragment(R.id.main_bottom_layout_home);
                break;
            case R.id.main_bottom_layout_find:
                showFragment(R.id.main_bottom_layout_find);
                break;
            case R.id.main_bottom_layout_subscription:
                showFragment(R.id.main_bottom_layout_subscription);
                break;
            case R.id.main_bottom_layout_mine:
                showFragment(R.id.main_bottom_layout_mine);
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
                    RichText.recycle();
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
        //TODO 此句必须调用，不然数据无法存储数据，因为需要传递所需要的一些初始参数
        super.onSaveInstanceState(outState);
        //记录当前的position
        outState.putInt("positionId", positionId);

        FragmentManager fm = getSupportFragmentManager();
        if (homeFragment.isAdded()) {
            fm.putFragment(outState, HomeFragment.class.getSimpleName(), homeFragment);
        }
        if (subscriptionFragment.isAdded()) {
            fm.putFragment(outState, SubscriptionFragment.class.getSimpleName(), subscriptionFragment);
        }
        if (findFragment.isAdded()) {
            fm.putFragment(outState, FindFragment.class.getSimpleName(), findFragment);
        }
        if (mineFragment.isAdded()) {
            fm.putFragment(outState, MineFragment.class.getSimpleName(), mineFragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
            }
        }

    }


    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拒绝时，关闭页面，缺少主要权限无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
