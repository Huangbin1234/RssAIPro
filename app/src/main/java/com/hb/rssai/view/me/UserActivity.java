package com.hb.rssai.view.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.hb.rssai.R;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.UserPresenter;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.iView.IUserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;

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
    @BindView(R.id.ama_ll_sex)
    LinearLayout mAmaLlSex;
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
    @BindView(R.id.ama_ll_nick_name)
    LinearLayout mAmaLlNickName;
    @BindView(R.id.ama_ll_birth)
    LinearLayout mAmaLlBirth;
    @BindView(R.id.ama_ll_signature)
    LinearLayout mAmaLlSignature;

    private OptionsPickerView mGenderPicker;
    private TimePickerView mBirthTimePickerView;

    private String editType = "";//"1" sex "2" birth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserPresenter) mPresenter).getUserInfo();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(UserEvent event) {
        if (event.getMessage() == 0) {
            ((UserPresenter) mPresenter).getUserInfo();
        }
    }

    @Override
    protected void initView() {

        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("男");
        genderList.add("女");

        mGenderPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                editType = "1";
                mAmaTvSex.setText(genderList.get(options1));
                ((UserPresenter) mPresenter).updateUserInfo();
            }
        }).setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(getResources().getColor(R.color.color_general_white))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .setCyclic(false, false, false)
                .build();
        mGenderPicker.setPicker(genderList);

        //时间选择器
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1920, 8, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(3000, 11, 1);
        mBirthTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                editType = "2";
                mAmaTvBirth.setText(DateUtil.format(date, Constant.DATE_SHORT_PATTERN));
                ((UserPresenter) mPresenter).updateUserInfo();
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setDate(selectedDate).setRangDate(startDate, endDate).build();
        mAmaLlBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBirthTimePickerView.show();
            }
        });
        mAmaLlSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenderPicker.show();
            }
        });
        mAmaLlNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
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
        mAmaLlSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, EditSignatureActivity.class);
                intent.putExtra(EditSignatureActivity.KEY_SIGNATURE, mAmaTvSignature.getText().toString());
                startActivity(intent);
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

    @Override
    public String getEditType() {
        return editType;
    }

    @Override
    public String getEtContent() {
        return etContent;
    }

    MaterialDialog materialDialog;

    private String etContent = "";
    EditText et;

    private void openDialog() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_et_view, null);
            et = (EditText) view.findViewById(R.id.dev_et);
            ResUser.RetObjBean retObjBean = ((UserPresenter) mPresenter).getResUser().getRetObj();
            if (retObjBean != null && retObjBean.getNickName() != null) {
                et.setText(retObjBean.getNickName());
            }
            materialDialog.setContentView(view).setTitle(Constant.TIPS_NICK_NAME)
                    .setNegativeButton("关闭", v -> {
                        materialDialog.dismiss();
                    }).setPositiveButton("确定", v -> {
                editType = "3";
                etContent = et.getText().toString().trim();
                ((UserPresenter) mPresenter).updateUserInfo();
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }
}
