package com.hb.rssai.view.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hb.rssai.R;
import com.hb.rssai.adapter.AdviceAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResAdviceList;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.AdvicePresenter;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAdviceView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AdviceActivity extends BaseActivity implements IAdviceView, View.OnClickListener {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.advice_et_content)
    EditText mAdviceEtContent;
    @BindView(R.id.advice_btn_save)
    Button mAdviceBtnSave;
    @BindView(R.id.aa_rv)
    RecyclerView mAaRv;
    @BindView(R.id.aa_srl)
    SwipeRefreshLayout mAaSrl;
    @BindView(R.id.aa_ll_input)
    LinearLayout mAaLlInput;
    @BindView(R.id.aa_fab)
    FloatingActionButton mAaFab;
    @BindView(R.id.aa_sp)
    Spinner mAaSp;


    private int pageNum = 1;
    private boolean isEnd = false, isLoad = false;
    private LinearLayoutManager mLayoutManager;
    List<ResAdviceList.RetObjBean.RowsBean> list = new ArrayList<>();
    private AdviceAdapter adapter;

    private String typeName;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AdvicePresenter) mPresenter).list();
    }

    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mAaRv.setLayoutManager(mLayoutManager);
        mAaSrl.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mAaSrl.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //设置上下拉刷新
        mAaSrl.setOnRefreshListener(() -> onRefresh());
        mAaRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter == null) {
                    isLoad = false;
                    mAaSrl.setRefreshing(false);
                    return;
                }
                // 在最后两条的时候就自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= adapter.getItemCount()) {
                    // 加载更多
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        mAaFab.setOnClickListener(view -> {
            if (mAaLlInput.getVisibility() == View.VISIBLE) {
                mAaLlInput.setVisibility(View.GONE);
            } else {
                mAaLlInput.setVisibility(View.VISIBLE);
            }
        });
        List<String> ls = new ArrayList<>();
//      0未知1新增2调整3报错4兼容5优化6其他
        ls.add("选择类型");
        ls.add("新增");
        ls.add("调整");
        ls.add("报错");
        ls.add("兼容");
        ls.add("优化");
        ls.add("其他");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ls);
        mAaSp.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        mAaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeName = ls.get(i);
                type = i + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_advice;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_advice_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new AdvicePresenter(this);
    }

    @Override
    public void setAddResult(ResBase resBase) {
        T.ShowToast(this, resBase.getRetMsg());
        finish();
    }

    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    @Override
    public String getEtContent() {
        return mAdviceEtContent.getText().toString().trim();
    }

    @Override
    public void setCheckResult(String error) {
        T.ShowToast(this, error);
    }


    @Override
    public void showList(ResAdviceList resAdviceList) {
        //TODO 填充数据
        if (resAdviceList.getRetCode() == 0) {
            if (resAdviceList.getRetObj().getRows() != null && resAdviceList.getRetObj().getRows().size() > 0) {
                this.list.addAll(resAdviceList.getRetObj().getRows());
                if (adapter == null) {
                    adapter = new AdviceAdapter(this, list);
                    mAaRv.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (this.list.size() == resAdviceList.getRetObj().getTotal()) {
                isEnd = true;
            }
        } else {
            T.ShowToast(this, resAdviceList.getRetMsg());
        }
        isLoad = false;
        mAaSrl.setRefreshing(false);
    }

    /**
     * 解决gson默认将int转换为double
     *
     * @return
     */
    public static Gson getIntGson() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue());
                        return new JsonPrimitive(src);
                    }
                }).create();
        return gson;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }


    @Override
    public String getEtTypeName() {
        return typeName;
    }

    @Override
    public String getEtType() {
        return type;
    }

    /**
     * 下拉刷新
     */
    @Override
    protected void onRefresh() {
        pageNum = 1;
        isLoad = true;
        isEnd = false;
        if (list != null) {
            list.clear();
        }
        mAaSrl.setRefreshing(true);
        ((AdvicePresenter) mPresenter).list();
    }

    /**
     * 上拉加载更多
     */
    @Override
    protected void loadMore() {
        if (!isEnd && !isLoad) {
            mAaSrl.setRefreshing(true);
            pageNum++;
            ((AdvicePresenter) mPresenter).list();
        }
    }

    @OnClick({R.id.advice_btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advice_btn_save:
                if ("选择类型".equals(typeName)) {
                    T.ShowToast(this, "请先选择类型");
                    return;
                }
                ((AdvicePresenter) mPresenter).add();
                break;
        }
    }
}
