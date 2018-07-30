package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.MinePresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DataCleanManager;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.iView.IMineView;
import com.hb.rssai.view.me.CollectionActivity;
import com.hb.rssai.view.me.MessageActivity;
import com.hb.rssai.view.me.OfflineActivity;
import com.hb.rssai.view.me.RecordActivity;
import com.hb.rssai.view.me.SearchActivity;
import com.hb.rssai.view.me.SettingActivity;
import com.hb.rssai.view.me.UserActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;
import com.hb.rssai.view.subscription.SubscribeAllActivity;
import com.zbar.lib.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class MineFragment extends BaseFragment implements View.OnClickListener, IMineView {

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
    @BindView(R.id.mf_tv_read_count)
    TextView mMfTvReadCount;
    @BindView(R.id.mf_tv_subcribe_count)
    TextView mMfTvSubscribeCount;
    @BindView(R.id.fm_ll_data)
    LinearLayout mFmLlData;
    @BindView(R.id.fm_ll_search)
    RelativeLayout mFmLlSearch;
    @BindView(R.id.mf_tv_offline_count)
    TextView mMfTvOfflineCount;
    @BindView(R.id.mf_ll_offline)
    LinearLayout mMfLlOffline;
    @BindView(R.id.mf_ll_subcribe_count)
    LinearLayout mMfLlSubscribeCount;
    @BindView(R.id.fm_tv_signature)
    TextView mFmTvSignature;
    @BindView(R.id.sys_iv_setting)
    ImageView mSysIvSetting;
    @BindView(R.id.mf_ll_record)
    LinearLayout mfLlRecord;
    @BindView(R.id.sla_iv_to_bg)
    ImageView mMfIvToBg;
    @BindView(R.id.mf_ll_clear)
    LinearLayout mFllClean;

    private OnFragmentInteractionListener mListener;
    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_LOGIN = 2;

    private boolean isPrepared;
    private String infoId;
    private String subscribeId;

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        ((MinePresenter) mPresenter).getUser();
        ((MinePresenter) mPresenter).getMessages();
        isPrepared = false;
        System.out.println("====lazyLoad====");
    }

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(MineEvent event) {
        if (event.getMessage() == 0) {
            ((MinePresenter) mPresenter).getUser();
            ((MinePresenter) mPresenter).getMessages();
        } else if (event.getMessage() == 1) {
            mMfTvOfflineCount.setText("" + LiteOrmDBUtil.getQueryAll(Information.class).size());
        } else if (event.getMessage() == 2) {
            //TODO  有点击消息 或 当有加载消息列表时
            long msgTotalCount = SharedPreferencesUtil.getLong(getContext(), Constant.KEY_MESSAGE_TOTAL_COUNT, 0);
            long localMsgCount = SharedPreferencesUtil.getLong(getContext(), Constant.KEY_MESSAGE_COUNT, 0);
            if (msgTotalCount > localMsgCount) {
                mIrsTvMsgCount.setVisibility(View.VISIBLE);
                mIrsTvMsgCount.setText("" + (msgTotalCount - localMsgCount));
            } else {
                mIrsTvMsgCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void setAppTitle() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return new MinePresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        mMfTvOfflineCount.setText("" + LiteOrmDBUtil.getQueryAll(Information.class).size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化UI完成
        isPrepared = true;
        lazyLoad();
    }

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


    @OnClick({R.id.mf_ll_clear, R.id.sla_iv_to_bg, R.id.mf_ll_record, R.id.mf_ll_subcribe_count, R.id.sys_iv_setting, R.id.fm_ll_collection, R.id.fm_ll_setting, R.id.fm_ll_scan, R.id.fm_ll_avatar, R.id.fm_ll_message, R.id.fm_ll_search, R.id.mf_ll_offline})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_ll_collection:
                getActivity().startActivity(new Intent(getContext(), CollectionActivity.class));
                break;
            case R.id.fm_ll_message:
                if (!TextUtils.isEmpty(mFmTvAccount.getText().toString()) && !"登录体验更多功能".equals(mFmTvAccount.getText().toString())) {
//                    mIrsTvMsgCount.setVisibility(View.GONE);
                    getActivity().startActivity(new Intent(getContext(), MessageActivity.class));
                } else {
                    T.ShowToast(getContext(), "请登录成功后查看");
                }
                break;
            case R.id.fm_ll_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fm_ll_scan:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE);
                break;
            case R.id.fm_ll_avatar:
                startActivityForResult(new Intent(getContext(), UserActivity.class), REQUEST_LOGIN);
                break;
            case R.id.sla_iv_to_bg:
                startActivityForResult(new Intent(getContext(), UserActivity.class), REQUEST_LOGIN);
                break;
            case R.id.fm_ll_search:
                getActivity().startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.mf_ll_offline:
                getActivity().startActivity(new Intent(getContext(), OfflineActivity.class));
                break;
            case R.id.mf_ll_subcribe_count:
                startActivity(new Intent(getContext(), SubscribeAllActivity.class));
                break;
            case R.id.sys_iv_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.mf_ll_record:
                getActivity().startActivity(new Intent(getContext(), RecordActivity.class));
                break;
            case R.id.mf_ll_clear:
                try {
                    T.ShowToast(getContext(), "本次清理缓存" + DataCleanManager.getTotalCacheSize(getContext()));
                    DataCleanManager.clearAllCache(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public TextView getTvReadCount() {
        return mMfTvReadCount;
    }

    @Override
    public TextView getTvSubscribeCount() {
        return mMfTvSubscribeCount;
    }

    @Override
    public TextView getTvAccount() {
        return mFmTvAccount;
    }

    @Override
    public ImageView getIvAva() {
        return mFmIvAva;
    }

    @Override
    public String getInformationId() {
        return infoId;
    }

    @Override
    public String getSubscribeId() {
        return subscribeId;
    }

    @Override
    public TextView getTvSignature() {
        return mFmTvSignature;
    }

    @Override
    public TextView getTvMsgCount() {
        return mIrsTvMsgCount;
    }

    @Override
    public ImageView getMfIvToBg() {
        return mMfIvToBg;
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
            if (requestCode == REQUEST_CODE) {
                String info = Base64Util.getDecodeStr(data.getStringExtra("info"));
                if (info.startsWith(Constant.FLAG_RSS_SOURCE)) {
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, "分享资讯");
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
                    //TODO 订阅 写入服务器
                    subscribeId = info.replace(Constant.FLAG_PRESS_RSS_SOURCE, "");
                    ((MinePresenter) mPresenter).addSubscription();
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, "分享主题");
                    intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, subscribeId);
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_COLLECTION_SOURCE)) {
                    UserCollection userCollection = new UserCollection();
                    userCollection.setTitle("收藏");
                    userCollection.setLink(info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE + Constant.FLAG_COLLECTION_SOURCE, ""));
                    LiteOrmDBUtil.insert(userCollection);

                    //TODO 收藏 写入服务器
                    infoId = info.replace(Constant.FLAG_PRESS_COLLECTION_SOURCE, "");//获取资讯ID
                    ((MinePresenter) mPresenter).addCollection();

                } else if (info.startsWith(Constant.FLAG_PRESS_URL_SOURCE)) {
                    Intent intent = new Intent(getContext(), ContentActivity.class);
                    intent.putExtra(ContentActivity.KEY_TITLE, "访问");
                    intent.putExtra(ContentActivity.KEY_URL, info.replace(Constant.FLAG_PRESS_URL_SOURCE + Constant.FLAG_URL_SOURCE, ""));
                    getContext().startActivity(intent);
                }
            } else if (requestCode == REQUEST_LOGIN) {
                mFmTvAccount.setText(SharedPreferencesUtil.getString(getContext(), Constant.SP_LOGIN_USER_NAME, ""));
                ((MinePresenter) mPresenter).getUser();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
