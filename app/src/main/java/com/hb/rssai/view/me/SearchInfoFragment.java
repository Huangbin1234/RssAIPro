package com.hb.rssai.view.me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hb.rssai.R;
import com.hb.rssai.base.BaseFragment;
import com.hb.rssai.presenter.BasePresenter;
import com.hb.rssai.presenter.SearchInfoPresenter;
import com.hb.rssai.view.iView.ISearchInfoView;
import com.hb.rssai.view.iView.SearchKeyWordListener;
import com.hb.rssai.view.widget.MyDecoration;

import butterknife.BindView;



public class SearchInfoFragment extends BaseFragment implements ISearchInfoView, SearchKeyWordListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.search_info_recycler_view)
    RecyclerView mSearchInfoRecyclerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager infoManager;
    private String keyWord = "";

    public SearchInfoFragment() {
        // Required empty public constructor
    }

    public static SearchInfoFragment newInstance(String param1, String param2) {
        SearchInfoFragment fragment = new SearchInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected void setAppTitle() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return new SearchInfoPresenter(getContext(), this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_search_info;
    }

    @Override
    protected void initView(View rootView) {
        infoManager = new LinearLayoutManager(getContext());
        mSearchInfoRecyclerView.setLayoutManager(infoManager);
        mSearchInfoRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    protected void lazyLoad() {

    }

    @Override
    public String getKeyWords() {
        return keyWord;
    }

    @Override
    public RecyclerView getInfoRecyclerView() {
        return mSearchInfoRecyclerView;
    }

    @Override
    public LinearLayoutManager getInfoManager() {
        return infoManager;
    }

    @Override
    public void setKeyWords(String val) {
        keyWord = val;
        ((SearchInfoPresenter) mPresenter).refreshInfoList(val);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
