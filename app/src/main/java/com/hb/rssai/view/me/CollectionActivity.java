package com.hb.rssai.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.adapter.CollectionAdapter;
import com.hb.rssai.adapter.DialogAdapter;
import com.hb.rssai.base.BaseActivity;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.CollectionPresenter;
import com.hb.rssai.util.Base64Util;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.view.common.QrCodeActivity;
import com.hb.rssai.view.iView.ICollectionView;
import com.hb.rssai.view.widget.FullListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.drakeet.materialdialog.MaterialDialog;

public class CollectionActivity extends BaseActivity implements CollectionAdapter.onItemLongClickedListener, ICollectionView {

    @BindView(R.id.sys_tv_title)
    TextView mSysTvTitle;
    @BindView(R.id.sys_toolbar)
    Toolbar mSysToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.coll_tv_empty)
    TextView mCollTvEmpty;
    @BindView(R.id.coll_ll)
    LinearLayout mCollLl;
    @BindView(R.id.coll_recycler_view)
    RecyclerView mCollRecyclerView;
    @BindView(R.id.coll_swipe_layout)
    SwipeRefreshLayout mCollSwipeLayout;
    private LinearLayoutManager mLayoutManager;
    //    CollectionAdapter adapter;
    private String collectionId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CollectionPresenter) mPresenter).getList();
    }


    @Override
    protected void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mCollRecyclerView.setLayoutManager(mLayoutManager);
        mCollSwipeLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mCollSwipeLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

    }

    @Override
    protected int providerContentViewId() {
        return R.layout.activity_collection;
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
        mSysTvTitle.setText(getResources().getString(R.string.str_coll_title));
    }

    @Override
    protected BasePresenter createPresenter() {
        return new CollectionPresenter(this, this);
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


    private ResCollection.RetObjBean.RowsBean newRowsBean;
    @Override
    public void onItemLongClicked(ResCollection.RetObjBean.RowsBean rowsBean) {
        newRowsBean=rowsBean;
        sureCollection(rowsBean);
    }

    /**
     * 构造对话框数据
     *
     * @return
     */
    private List<HashMap<String, Object>> initDialogData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "分享");
        map.put("collectionId", 1);
        map.put("url", R.mipmap.ic_share);
        list.add(map);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "删除");
        map2.put("collectionId", 2);
        map2.put("url", R.mipmap.ic_delete);
        list.add(map2);
        return list;
    }

    /**
     * 取消对话框
     *
     * @return
     */
    DialogAdapter dialogAdapter;
    MaterialDialog materialDialog;

    private void sureCollection(ResCollection.RetObjBean.RowsBean rowsBean) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.view_dialog, null);
            FullListView listView = (FullListView) view.findViewById(R.id.dialog_listView);

            List<HashMap<String, Object>> list = initDialogData();
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                if (list.get(position).get("collectionId").equals(1)) {
                    //TODO 分享
                    materialDialog.dismiss();
                    Intent intent = new Intent(CollectionActivity.this, QrCodeActivity.class);
                    intent.putExtra(QrCodeActivity.KEY_FROM, QrCodeActivity.FROM_VALUES[1]);
                    intent.putExtra(QrCodeActivity.KEY_TITLE, newRowsBean.getTitle());
                    intent.putExtra(QrCodeActivity.KEY_CONTENT, Base64Util.getEncodeStr(Constant.FLAG_COLLECTION_SOURCE + newRowsBean.getLink()));
                    startActivity(intent);
                } else if (list.get(position).get("collectionId").equals(2)) {
                    materialDialog.dismiss();
                    LiteOrmDBUtil.deleteWhere(UserCollection.class, "collectionId", new String[]{"" + newRowsBean.getId()});
                    //adapter.notifyDataSetChanged();
                    collectionId = newRowsBean.getId();
                    ((CollectionPresenter) mPresenter).del();
                    //T.ShowToast(CollectionActivity.this, "删除成功！");
                }
            });
            if (dialogAdapter == null) {
                dialogAdapter = new DialogAdapter(this, list);
                listView.setAdapter(dialogAdapter);
            }
            dialogAdapter.notifyDataSetChanged();
            materialDialog.setContentView(view).setTitle(Constant.TIPS_SYSTEM).setNegativeButton("关闭", v -> {
                materialDialog.dismiss();
            }).show();
        } else {
            materialDialog.show();
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mCollRecyclerView;
    }

    @Override
    public SwipeRefreshLayout getSwipeLayout() {
        return mCollSwipeLayout;
    }

    @Override
    public LinearLayoutManager getManager() {
        return mLayoutManager;
    }

    public String getCollectionId() {
        return collectionId;
    }
}
