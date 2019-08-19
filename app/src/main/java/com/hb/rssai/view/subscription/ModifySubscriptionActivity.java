package com.hb.rssai.view.subscription;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.DataTypeAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.bean.SubParams;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.contract.ModifySubscriptionContract;
import com.hb.rssai.presenter.ModifySubscriptionPresenter;
import com.hb.rssai.util.GsonUtil;
import com.hb.rssai.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class ModifySubscriptionActivity extends BaseActivity<ModifySubscriptionContract.View, ModifySubscriptionPresenter> implements ModifySubscriptionContract.View, View.OnClickListener {
    public static final String KEY_NAME = "key_name";
    public static final String KEY_ID = "key_id";
    public static final String KEY_IMG = "key_img";
    public static final String KEY_ABS_CONTENT = "key_abs_content";
    public static final String KEY_LINK = "key_link";
    public static final String KEY_SORT = "key_sort";
    public static final String KEY_IS_RECOMMEND = "key_is_recommend";
    public static final String KEY_IS_TAG = "key_is_tag";
    public static final String KEY_DATA_TYPE = "key_data_type";
    ModifySubscriptionContract.Presenter mPresenter;
    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.msa_btn_sure)
    Button mMsaBtnSure;
    @BindView(R.id.llRootView)
    LinearLayout mLlRootView;
    @BindView(R.id.msa_et_name)
    EditText mMsaEtName;
    @BindView(R.id.msa_sp_data_type)
    Spinner mMsaSpDataType;
    @BindView(R.id.msa_et_url)
    EditText mMsaEtUrl;
    @BindView(R.id.msa_et_img_url)
    EditText mMsaEtImgUrl;
    @BindView(R.id.msa_rb_is_recommend_yes)
    AppCompatRadioButton mMsaRbIsRecommendYes;
    @BindView(R.id.msa_rb_is_recommend_no)
    AppCompatRadioButton mMsaRbIsRecommendNo;
    @BindView(R.id.msa_rg_is_recommend)
    RadioGroup mMsaRgIsRecommend;
    @BindView(R.id.msa_et_abstract)
    EditText mMsaEtAbstract;
    @BindView(R.id.msa_et_sort)
    EditText mMsaEtSort;
    List<ResDataGroup.RetObjBean.RowsBean> dataTypeList = new ArrayList<>();

    String str_isRecommend = "否";

    DataTypeAdapter dataTypeAdapter;

    int dataType = 7;
    String v_id;
    String v_name;
    String v_img;
    String v_abs_content;
    String v_link;
    long v_sort;
    boolean v_is_recommend;
    boolean v_is_tag;
    private int v_data_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        super.initIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            v_id = bundle.getString(KEY_ID);
            v_name = bundle.getString(KEY_NAME);
            v_img = bundle.getString(KEY_IMG);
            v_abs_content = bundle.getString(KEY_ABS_CONTENT);
            v_link = bundle.getString(KEY_LINK);
            v_sort = bundle.getLong(KEY_SORT);
            v_is_recommend = bundle.getBoolean(KEY_IS_RECOMMEND);
            v_is_tag = bundle.getBoolean(KEY_IS_TAG);
            v_data_type = bundle.getInt(KEY_DATA_TYPE);

            dataType = v_data_type;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void initView() {
        mMsaSpDataType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataType = dataTypeList.get(position).getVal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMsaRgIsRecommend.setOnCheckedChangeListener((radioGroup, i) -> selectRecommendRadioBtn());

        //初始化信息
        mMsaEtName.setText(v_name);
        mMsaEtImgUrl.setText(v_img);
        mMsaEtAbstract.setText(v_abs_content);
        mMsaEtUrl.setText(v_link);
        mMsaEtSort.setText("" + v_sort);

        if (v_is_recommend) {
            mMsaRbIsRecommendYes.setChecked(true);
            mMsaRbIsRecommendNo.setChecked(false);
        } else {
            mMsaRbIsRecommendYes.setChecked(false);
            mMsaRbIsRecommendNo.setChecked(true);
        }
    }

    private void selectRecommendRadioBtn() {
        RadioButton rb = this.findViewById(mMsaRgIsRecommend.getCheckedRadioButtonId());
        str_isRecommend = rb.getText().toString();
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_modify_subscription;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_modify_subscribe_title));
    }

    @Override
    protected ModifySubscriptionPresenter createPresenter() {
        return new ModifySubscriptionPresenter(this);
    }


    @Override
    public void showModifyResult(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public void setDataGroupResult(ResDataGroup resDataGroup) {
        if (resDataGroup != null && resDataGroup.getRetCode() == 0 && resDataGroup.getRetObj().getRows().size() > 0) {
            //设置
            if (dataTypeList.size() > 0) {
                dataTypeList.clear();
            }
            dataTypeList.addAll(resDataGroup.getRetObj().getRows());
            if (null == dataTypeAdapter) {
                dataTypeAdapter = new DataTypeAdapter(this, dataTypeList);
                mMsaSpDataType.setAdapter(dataTypeAdapter);
            }
            dataTypeAdapter.notifyDataSetChanged();
            //  设置选中
            selectSp(v_data_type);
        }
    }

    private void selectSp(int dt) {
        try {
            int k = mMsaSpDataType.getAdapter().getCount();
            for (int i = 0; i < k; i++) {
                ResDataGroup.RetObjBean.RowsBean rb = (ResDataGroup.RetObjBean.RowsBean) mMsaSpDataType.getItemAtPosition(i);
                if (dt == (rb.getVal())) {
                    mMsaSpDataType.setSelection(i, true);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setPresenter(ModifySubscriptionContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick({R.id.msa_btn_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msa_btn_sure:
                //组装参数
                createParams();
                break;
        }
    }

    private void createParams() {
        String name = mMsaEtName.getText().toString().trim();
        String abstractContent = mMsaEtAbstract.getText().toString().trim();
        String link = mMsaEtUrl.getText().toString().trim();
        String imgUrl = mMsaEtImgUrl.getText().toString().trim();

        String sort = mMsaEtSort.getText().toString().trim();
        boolean isRecommend = "是".equals(str_isRecommend);
        if (TextUtils.isEmpty(name)) {
            T.ShowToast(this, "请输入RSS源名称");
            return;
        }
        if (TextUtils.isEmpty(link) || !link.startsWith("http")) {
            T.ShowToast(this, "请输入正确的源链接地址");
            return;
        }
        if (TextUtils.isEmpty(imgUrl) || !imgUrl.startsWith("http")) {
            T.ShowToast(this, "请输入正确的图片地址");
            return;
        }
        if (TextUtils.isEmpty(sort) || Long.valueOf(sort) > 2147483647) {
            T.ShowToast(this, "请输入正确的排序值");
            return;
        }
        if (TextUtils.isEmpty(abstractContent)) {
            T.ShowToast(this, "请输入摘要");
            return;
        }
        SubParams subParams = new SubParams();
        subParams.setId(v_id);
        subParams.setName(name);
        subParams.setAbstractContent(abstractContent);
        subParams.setLink(link);
        subParams.setImg(imgUrl);
        String dt = "" + dataType;
        subParams.setDataType(dt);
        subParams.setTag(v_is_tag);
        subParams.setRecommend(isRecommend);

        subParams.setSort(sort);

        Map<String, Object> map = new HashMap<>();
        String jsonParams = GsonUtil.toJson(subParams);
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);

        mPresenter.modifySubscription(map);
    }
}
