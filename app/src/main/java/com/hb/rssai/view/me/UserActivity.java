package com.hb.rssai.view.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResUser;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.event.MineEvent;
import com.hb.rssai.event.UserEvent;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.UserPresenter;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.DisplayUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.ImageUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IUserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.llRootView)
    LinearLayout mLlRootView;

    private OptionsPickerView mGenderPicker;
    private TimePickerView mBirthTimePickerView;

    private MaterialDialog materialDialog;
    private String etNickName = "";
    private String editType = "";//"1" sex "2" birth
    private EditText et;
    private ResUser resUser;

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
        mGenderPicker = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            editType = "1";
            mAmaTvSex.setText(genderList.get(options1));
            ((UserPresenter) mPresenter).updateUserInfo();
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
        mBirthTimePickerView = new TimePickerView.Builder(this, (date, v) -> {//选中事件回调
            editType = "2";
            mAmaTvBirth.setText(DateUtil.format(date, Constant.DATE_SHORT_PATTERN));
            ((UserPresenter) mPresenter).updateUserInfo();
        }).setType(new boolean[]{true, true, true, false, false, false}).setDate(selectedDate).setRangDate(startDate, endDate).build();
        mAmaLlBirth.setOnClickListener(v -> mBirthTimePickerView.show());
        mAmaLlSex.setOnClickListener(v -> mGenderPicker.show());
        mAmaLlNickName.setOnClickListener(v -> openDialog());
        mAmaBtnLogin.setOnClickListener(v -> {
            SharedPreferencesUtil.setString(UserActivity.this, Constant.SP_LOGIN_USER_NAME, "");
            SharedPreferencesUtil.setString(UserActivity.this, Constant.SP_LOGIN_PSD, "");
            SharedPreferencesUtil.setString(UserActivity.this, Constant.TOKEN, "");
            SharedPreferencesUtil.setString(UserActivity.this, Constant.USER_ID, "");

            ((UserPresenter) mPresenter).getUserInfo();
            EventBus.getDefault().post(new MineEvent(0));
        });
        mAmaLlSignature.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, EditSignatureActivity.class);
            intent.putExtra(EditSignatureActivity.KEY_SIGNATURE, mAmaTvSignature.getText().toString());
            startActivity(intent);
        });
        mAmaIvUserPhoto.setOnClickListener(v -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            } else {
                mPop.setAnimationStyle(R.style.PopupAnimation);
                if (Build.VERSION.SDK_INT < 24) {
                    mPop.showAtLocation(mLlRootView, Gravity.CENTER, 0, 0);
                } else {
                    mPop.showAtLocation(mLlRootView, Gravity.CENTER, (DisplayUtil.getMobileWidth(this) - (DisplayUtil.getMobileWidth(this) * 8 / 10)) / 2, DisplayUtil.dip2px(this, 90));
                }
                mPop.update();
            }
            backgroundAlpha(0.5f);
            mPop.setOnDismissListener(new PopOnDismissListener());
        });
        //TODO 头像上传
        selectAvatar();
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
        return new UserPresenter(this);
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setUserInfoResult(ResUser resUser) {
        if (resUser.getRetCode() == 0) {
            this.resUser = resUser;
            mAmaTvNickName.setText(resUser.getRetObj().getNickName());
            mAmaTvSignature.setText(resUser.getRetObj().getDescription());
            if (resUser.getRetObj().getSex() == -1) {
                mAmaTvSex.setText("单击设置性别");
            } else {
                mAmaTvSex.setText(resUser.getRetObj().getSex() == 1 ? "男" : "女");
            }
            mAmaTvBirth.setText(resUser.getRetObj().getBirth());
            HttpLoadImg.loadCircleImg(this, ApiRetrofit.BASE_IMG_URL + resUser.getRetObj().getAvatar(), mAmaIvUserPhoto);
        } else {
            T.ShowToast(this, resUser.getRetMsg());
        }
    }

    @Override
    public void setUpdateResult(ResUser resBase) {
        if (resBase.getRetCode() == 0) {
            ((UserPresenter) mPresenter).getUserInfo();
            EventBus.getDefault().post(new MineEvent(0));
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void setAvatarResult(ResBase resBase) {
        if (resBase.getRetCode() == 0) {
            ((UserPresenter) mPresenter).getUserInfo();
            EventBus.getDefault().post(new MineEvent(0));
        }
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
        if (TextUtils.isEmpty(SharedPreferencesUtil.getString(this, Constant.TOKEN, ""))) {
            mAmaTvNickName.setText("点击设置昵称");
            mAmaTvSignature.setText("点击设置个性签名");
            mAmaTvSex.setText("点击设置性别");
            mAmaTvBirth.setText("点击设置生日");
            HttpLoadImg.loadCircleImg(this, R.mipmap.icon_default_avar, mAmaIvUserPhoto);
        }
    }

    @Override
    public String getUserId() {
        return SharedPreferencesUtil.getString(this, Constant.USER_ID, "");
    }

    @Override
    public String getSex() {
        return mAmaTvSex.getText().toString().trim();
    }

    @Override
    public String getBirth() {
        return mAmaTvBirth.getText().toString().trim();
    }

    @Override
    public String getNickName() {
        return etNickName;
    }

    @Override
    public String getEtType() {
        return editType;
    }

    private void openDialog() {
        if (materialDialog == null) {
            if (resUser == null) {
                return;
            }
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_et_view, null);
            et = (EditText) view.findViewById(R.id.dev_et);
            ResUser.RetObjBean retObjBean = resUser.getRetObj();
            if (retObjBean != null && retObjBean.getNickName() != null) {
                et.setText(retObjBean.getNickName());
            }
            materialDialog.setContentView(view).setTitle(Constant.TIPS_NICK_NAME)
                    .setNegativeButton("关闭", v -> {
                        materialDialog.dismiss();
                    }).setPositiveButton("确定", v -> {
                editType = "3";
                etNickName = et.getText().toString().trim();
                ((UserPresenter) mPresenter).updateUserInfo();
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    //TODO ###############################################头像上传####################################################
    //TODO 头像上传相关
    private String filePath = "";//上传图片地址
    private String imageFilePath;// 指定照相后图片保存的地址
    private Uri imageFileUri;

    private View popupView;
    private PopupWindow mPop;// 初始化弹出层

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class PopOnDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void selectAvatar() {
        if (mPop == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            popupView = inflater.inflate(R.layout.pop_image_up, null);
            mPop = new PopupWindow(popupView, DisplayUtil.getMobileWidth(this) * 8 / 10, ViewGroup.LayoutParams.WRAP_CONTENT);

            mPop.setFocusable(true);
            ColorDrawable cd = new ColorDrawable(Color.TRANSPARENT);
            mPop.setBackgroundDrawable(cd);
            mPop.update();
            mPop.setOutsideTouchable(true);
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/myImage");
            if (!file.exists()) {
                if (file.mkdirs() == false) {
                    T.ShowToast(this, Environment.getExternalStorageDirectory().getCanonicalPath() + "/myImage" + "创建失败");
                    return;
                }
            }
            imageFilePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/myImage" + "/shiyan.jpg";
            System.out.println(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置图片的保存路径
        File imageFile = new File(imageFilePath);// 通过路径创建保存文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageFileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);// 获取文件的Uri
        } else {
            imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
        }
        // 手机照相
        popupView.findViewById(R.id.zx_btn).setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(intent, 1);
        });
        // 手机相册
        popupView.findViewById(R.id.bd_btn).setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*"); // 设置文件类型
            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentFromGallery, 0);
        });
        // 取消
        popupView.findViewById(R.id.cancel_btn).setOnClickListener(arg0 -> {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
        });
    }

    // 拍照或选择照片后的返回处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                T.ShowToast(this, "SD卡错误");
                return;
            }
            if (requestCode == 1) {// 照相后返回
                @SuppressWarnings("static-access")
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bitmap bp;
                // 尝试
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageFilePath, options);
                // Calculate inSampleSize
                options.inSampleSize = ImageUtil.calculateInSampleSize(options,
                        400, 320);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                bp = BitmapFactory.decodeFile(imageFilePath, options);
                int degree = ImageUtil.readPictureDegree(imageFilePath);// 得到的图片选择角度
                bp = ImageUtil.rotaingImageView(degree, bp);
                FileOutputStream fos = null;
                File file = new File(ImageUtil.getSdPath(this) + "/myImage");
                if (!file.exists()) {
                    if (file.mkdirs() == false) {
                        T.ShowToast(this, ImageUtil.getSdPath(this) + "/myImage" + "创建失败");
                        return;
                    }
                }
                String fileName = ImageUtil.getSdPath(this) + "/myImage/" + name;
                fos = new FileOutputStream(fileName);
                bp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                filePath = fileName;
                mAmaIvUserPhoto.setImageBitmap(bp);
                fos.flush();
                fos.close();
                ((UserPresenter) mPresenter).uploadAvatar();
            } else if (requestCode == 0) {// 本地相册返回
                Uri uriB = data.getData();
                String selectedImagePath = ImageUtil.getPath(uriB, this);
                new DateFormat();
                String nameB = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bitmap bp;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                // Calculate inSampleSize
                options.inSampleSize = ImageUtil.calculateInSampleSize(options,
                        400, 320);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                bp = BitmapFactory.decodeFile(selectedImagePath, options);
                int degree = ImageUtil.readPictureDegree(selectedImagePath);// 得到的图片选择角度
                bp = ImageUtil.rotaingImageView(degree, bp);
                FileOutputStream fos = null;
                File file = new File(ImageUtil.getSdPath(this) + "/myImage");
                if (!file.exists()) {
                    if (file.mkdirs() == false) {
                        T.ShowToast(this, ImageUtil.getSdPath(this) + "/myImage" + "创建失败");
                        return;
                    }
                }
                String fileName = ImageUtil.getSdPath(this) + "/myImage/" + nameB;
                fos = new FileOutputStream(fileName);
                bp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                filePath = fileName;
                mAmaIvUserPhoto.setImageBitmap(bp);
                fos.flush();
                fos.close();
                ((UserPresenter) mPresenter).uploadAvatar();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO ###############################################头像上传####################################################
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册
        EventBus.getDefault().unregister(this);
    }
}
