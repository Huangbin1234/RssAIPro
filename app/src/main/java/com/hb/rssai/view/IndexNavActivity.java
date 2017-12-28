package com.hb.rssai.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hb.rssai.R;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MainEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.runtimePermissions.PermissionsActivity;
import com.hb.rssai.runtimePermissions.PermissionsChecker;
import com.hb.rssai.util.BottomNavigationViewHelper;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.fragment.FindFragment;
import com.hb.rssai.view.fragment.HomeFragment;
import com.hb.rssai.view.fragment.MineFragment;
import com.hb.rssai.view.fragment.SubscriptionFragment;
import com.hb.rssai.view.fragment.TabFragment;
import com.hb.update.UpdateManager;
import com.jaeger.library.StatusBarUtil;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class IndexNavActivity extends BaseActivity implements TabFragment.OnFragmentInteractionListener, SubscriptionFragment.OnFragmentInteractionListener, MineFragment.OnFragmentInteractionListener, FindFragment.OnFragmentInteractionListener {


    @BindView(R.id.ma_frame_layout)
    FrameLayout mMaFrameLayout;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.container)
    LinearLayout mContainer;


    private Context mContext;

    private PermissionsChecker mPermissionsChecker;
    private Bundle savedInstanceState;

//    private HomeFragment homeFragment;
    private TabFragment tabFragment;
    private SubscriptionFragment subscriptionFragment;
    private MineFragment mineFragment;
    private FindFragment findFragment;

    // 退出程序
    private long firstTime = 0;
    private final int REQUEST_CODE = 1;
    private int positionId;//positionId 关闭应用时的Fragment ID

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(R.id.navigation_home);
                    return true;
                case R.id.navigation_subscribe:
                    showFragment(R.id.navigation_subscribe);
                    return true;
                case R.id.navigation_find:
                    showFragment(R.id.navigation_find);
                    return true;
                case R.id.navigation_mine:
                    showFragment(R.id.navigation_mine);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void initStatusBar() {
//        super.initStatusBar();
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

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

    @Override
    protected void initView() {
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {//恢复现场
            int id = savedInstanceState.getInt("positionId");
            FragmentManager fm = getSupportFragmentManager();
//            homeFragment = (HomeFragment) fm.getFragment(savedInstanceState, HomeFragment.class.getSimpleName());
            tabFragment = (TabFragment) fm.getFragment(savedInstanceState, HomeFragment.class.getSimpleName());
            subscriptionFragment = (SubscriptionFragment) fm.getFragment(savedInstanceState, SubscriptionFragment.class.getSimpleName());
            findFragment = (FindFragment) fm.getFragment(savedInstanceState, FindFragment.class.getSimpleName());
            mineFragment = (MineFragment) fm.getFragment(savedInstanceState, MineFragment.class.getSimpleName());
            showFragment(id);
        } else {
//            homeFragment = new HomeFragment();
            tabFragment = new TabFragment();
            findFragment = new FindFragment();
            subscriptionFragment = new SubscriptionFragment();
            mineFragment = new MineFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.add(R.id.ma_frame_layout, homeFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, tabFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, findFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, subscriptionFragment);
            fragmentTransaction.add(R.id.ma_frame_layout, mineFragment);
            fragmentTransaction.commit();
            showFragment(R.id.navigation_home);
        }
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
    protected int providerContentViewId() {
        return R.layout.activity_index_nav;
    }

    @Override
    protected void setAppTitle() {
        mContext = this;
        BottomNavigationViewHelper.disableShiftMode(mNavigation);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * TODO: 控制fragment 显示
     *
     * @param id
     */
    private void showFragment(int id) {
        positionId = id;
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
//        mTransaction.hide(homeFragment);
        mTransaction.hide(tabFragment);
        mTransaction.hide(findFragment);
        mTransaction.hide(subscriptionFragment);
        mTransaction.hide(mineFragment);
        // 设置按钮 图片为普通颜色

        switch (id) {
            case R.id.navigation_home:
//                mTransaction.show(homeFragment);
                mTransaction.show(tabFragment);
//                homeFragment.setUserVisibleHint(true);
                tabFragment.setUserVisibleHint(true);
                break;
            case R.id.navigation_find:
                mTransaction.show(findFragment);
                findFragment.setUserVisibleHint(true);
                break;
            case R.id.navigation_subscribe:
                mTransaction.show(subscriptionFragment);
                subscriptionFragment.setUserVisibleHint(true);
                break;
            case R.id.navigation_mine:
                mTransaction.show(mineFragment);
                mineFragment.setUserVisibleHint(true);
                break;
        }
        mTransaction.commit();
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
//        if (homeFragment.isAdded()) {
//            fm.putFragment(outState, HomeFragment.class.getSimpleName(), homeFragment);
//        }
        if (tabFragment.isAdded()) {
            fm.putFragment(outState, HomeFragment.class.getSimpleName(), tabFragment);
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
