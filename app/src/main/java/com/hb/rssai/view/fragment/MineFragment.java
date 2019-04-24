package com.hb.rssai.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.app.ProjectApplication;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.bean.Information;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.bean.ResShareCollection;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.MinePresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.DataCleanManager;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.LoginActivity;
import com.hb.rssai.view.common.RichTextActivity;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;

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
    @BindView(R.id.fm_iv_modify_ava)
    ImageView mFmIvModifyAva;
    Unbinder unbinder;

    private OnFragmentInteractionListener mListener;
    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_LOGIN = 2;

    private boolean isPrepared;
    private String infoId;
    private String subscribeId;

    private String FLAG_USER_ACTIVITY = "UserActivity.class";
    private String FLAG_RECORD_ACTIVITY = "RecordActivity.class";
    private String FLAG_SUBSCRIBE_ALL_ACTIVITY = "SubscribeAllActivity.class";
    private String FLAG_MESSAGE_ACTIVITY = "MessageActivity.class";
    private String FLAG_COLLECTION_ACTIVITY = "CollectionActivity.class";

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
    protected void setAppTitle() {
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
                toLogin(FLAG_COLLECTION_ACTIVITY);
                break;
            case R.id.fm_ll_message:
                if (!TextUtils.isEmpty(mFmTvAccount.getText().toString()) && !"登录体验更多功能".equals(mFmTvAccount.getText().toString())) {
                    getActivity().startActivity(new Intent(getContext(), MessageActivity.class));
                } else {
                    T.ShowToast(getContext(), "请登录成功后查看");
                    toLogin(FLAG_MESSAGE_ACTIVITY);
                }
                break;
            case R.id.fm_ll_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.fm_ll_scan:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE);
                break;
            case R.id.fm_ll_avatar:
                toLogin(FLAG_USER_ACTIVITY);
                break;
            case R.id.sla_iv_to_bg:
                toLogin(FLAG_USER_ACTIVITY);
                break;
            case R.id.fm_ll_search:
                getActivity().startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.mf_ll_offline:
                getActivity().startActivity(new Intent(getContext(), OfflineActivity.class));
                break;
            case R.id.mf_ll_subcribe_count:
                toLogin(FLAG_SUBSCRIBE_ALL_ACTIVITY);
                break;
            case R.id.sys_iv_setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.mf_ll_record:
                toLogin(FLAG_RECORD_ACTIVITY);
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

    private void toLogin(String flag) {
        String token = SharedPreferencesUtil.getString(getContext(), Constant.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            //跳转到登录
            Intent intent = new Intent(ProjectApplication.mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ProjectApplication.mContext.startActivity(intent);
        } else {
            if (flag.equals(FLAG_USER_ACTIVITY)) {
                startActivityForResult(new Intent(getContext(), UserActivity.class), REQUEST_LOGIN);
            } else if (flag.equals(FLAG_RECORD_ACTIVITY)) {
                startActivityForResult(new Intent(getContext(), RecordActivity.class), REQUEST_LOGIN);
            } else if (flag.equals(FLAG_SUBSCRIBE_ALL_ACTIVITY)) {
                startActivityForResult(new Intent(getContext(), SubscribeAllActivity.class), REQUEST_LOGIN);
            } else if (flag.equals(FLAG_MESSAGE_ACTIVITY)) {
                startActivityForResult(new Intent(getContext(), MessageActivity.class), REQUEST_LOGIN);
            } else if (flag.equals(FLAG_COLLECTION_ACTIVITY)) {
                startActivityForResult(new Intent(getContext(), CollectionActivity.class), REQUEST_LOGIN);
            }
        }
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
    public void setResult(ResUser user) {
        if (user.getRetCode() == 0) {
            mMfTvReadCount.setText("" + user.getRetObj().getReadCount());
            mMfTvSubscribeCount.setText("" + user.getRetObj().getSubscribeCount());
            mFmTvAccount.setText(user.getRetObj().getNickName());
            mFmIvModifyAva.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(user.getRetObj().getDescription())) {
                mFmTvSignature.setVisibility(View.VISIBLE);
                mFmTvSignature.setText(user.getRetObj().getDescription());
            } else {
                mFmTvSignature.setVisibility(View.GONE);
            }
            HttpLoadImg.loadCircleWithBorderImg(getContext(), ApiRetrofit.BASE_URL + user.getRetObj().getAvatar(), mFmIvAva);
            //毛玻璃效果 与 状态栏不沉浸
//            Glide.with(getContext()).load(ApiRetrofit.BASE_URL + user.getRetObj().getAvatar()).bitmapTransform(new BlurTransformation(getContext(), 20, 2), new CenterCrop(getContext())).into(mMfIvToBg);
            Glide.with(getContext())
                    .load(ApiRetrofit.BASE_URL + user.getRetObj().getAvatar())
                    .apply(new RequestOptions())
                    .transform(new BlurTransformation(100, 2))
                    .into(mMfIvToBg);

        } else {
            T.ShowToast(getContext(), Constant.MSG_NETWORK_ERROR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                } else if (info.startsWith(Constant.FLAG_PRESS_SUB_RSS_SOURCE)) {
                    RssSource rssSource = new RssSource();
                    rssSource.setName("订阅");
                    rssSource.setLink(info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    LiteOrmDBUtil.insert(rssSource);
                    //TODO 订阅 写入服务器
                    subscribeId = info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE, "").split("@,@")[0];
                    ((MinePresenter) mPresenter).addSubscription();

                    String name = info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE, "").split("@,@")[1];
                    String image = info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE, "").split("@,@")[2];
                    String dec = info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE, "").split("@,@")[3];
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_SUB_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, name);
                    intent.putExtra(SourceCardActivity.KEY_DESC, dec);
                    intent.putExtra(SourceCardActivity.KEY_IMAGE, image);
                    intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, subscribeId);
                    getContext().startActivity(intent);
                } else if (info.startsWith(Constant.FLAG_PRESS_RSS_SOURCE)) {
                    RssSource rssSource = new RssSource();
                    rssSource.setName("订阅");
                    rssSource.setLink(info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    LiteOrmDBUtil.insert(rssSource);

                    subscribeId = info.replace(Constant.FLAG_PRESS_RSS_SOURCE, "").split("@,@")[0];
                    String name = info.replace(Constant.FLAG_PRESS_RSS_SOURCE, "").split("@,@")[1];
                    String image = info.replace(Constant.FLAG_PRESS_RSS_SOURCE, "").split("@,@")[2];
                    String dec = info.replace(Constant.FLAG_PRESS_RSS_SOURCE, "").split("@,@")[3];
                    //打开
                    Intent intent = new Intent(getContext(), SourceCardActivity.class);
                    intent.putExtra(SourceCardActivity.KEY_LINK, info.replace(Constant.FLAG_PRESS_RSS_SOURCE + Constant.FLAG_RSS_SOURCE, ""));
                    intent.putExtra(SourceCardActivity.KEY_TITLE, name);
                    intent.putExtra(SourceCardActivity.KEY_DESC, dec);
                    intent.putExtra(SourceCardActivity.KEY_IMAGE, image);
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
    public void setAddResult(ResShareCollection resBase) {
        if (resBase.getRetCode() == 0) {
            if (resBase.getRetObj() != null) {
                Intent intent = new Intent(getContext(), RichTextActivity.class);
                intent.putExtra("abstractContent", resBase.getRetObj().getAbstractContent());
                intent.putExtra(ContentActivity.KEY_TITLE, resBase.getRetObj().getTitle());
                intent.putExtra("whereFrom", resBase.getRetObj().getWhereFrom());
                intent.putExtra("pubDate", resBase.getRetObj().getPubTime());
                intent.putExtra("url", resBase.getRetObj().getLink());
                intent.putExtra("id", resBase.getRetObj().getId());
                intent.putExtra("clickGood", resBase.getRetObj().getClickGood());
                intent.putExtra("clickNotGood", resBase.getRetObj().getClickNotGood());
                getContext().startActivity(intent);
            } else {
                T.ShowToast(getContext(), "抱歉，文章链接已失效，无法打开！");
            }
        }
        T.ShowToast(getContext(), resBase.getRetMsg());
    }

    @Override
    public void setAddSubscribeResult(ResBase resBase) {
        T.ShowToast(getContext(), resBase.getRetMsg());
    }

    @Override
    public void setMessageListResult(ResMessageList resMessageList) {
        //TODO 填充数据
        if (resMessageList.getRetCode() == 0) {
            if (resMessageList.getRetObj() != null) {
//                1、获取消息总数
//                2、用户每点击一条新消息则设置本地累计次数变量+1
//                3、根据本地累计次数变量与消息总数进行比较如果大于或等于则不再显示消息数
                int t = resMessageList.getRetObj().getTotal();
                long localMsgCount = SharedPreferencesUtil.getLong(getContext(), Constant.KEY_MESSAGE_COUNT, 0);
                //存储上一次的消息总数
                SharedPreferencesUtil.setLong(getContext(), Constant.KEY_MESSAGE_TOTAL_COUNT, t);
                if (t > localMsgCount) {
                    mIrsTvMsgCount.setVisibility(View.VISIBLE);
                    mIrsTvMsgCount.setText("" + (t - localMsgCount));
                } else {
                    mIrsTvMsgCount.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void showLoadUserError() {
        if (TextUtils.isEmpty(SharedPreferencesUtil.getString(getContext(), Constant.TOKEN, ""))) {
            mMfTvReadCount.setText("0");
            mMfTvSubscribeCount.setText("0");
            mFmTvAccount.setText(getContext().getResources().getString(R.string.str_mf_no_login));
            mFmIvModifyAva.setVisibility(View.VISIBLE);
            HttpLoadImg.loadRoundImg(getContext(), R.mipmap.icon_default_avar, mFmIvAva);
        }
    }

    @Override
    public void showNetError() {
        T.ShowToast(getContext(), Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void showNoLogin() {
        T.ShowToast(getContext(), Constant.MSG_NO_LOGIN);
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(getContext(), Constant.USER_ID, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
