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
import com.hb.rssai.presenter.SearchSubscribePresenter;
import com.hb.rssai.view.iView.ISearchSubscribeView;
import com.hb.rssai.view.widget.MyDecoration;

import butterknife.BindView;

public class SearchSubscribeFragment extends BaseFragment implements ISearchSubscribeView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.search_subscribe_recycler_view)
    RecyclerView searchSubscribeRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager subscribeManager;

    public SearchSubscribeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchSubscribeFragment newInstance(String param1, String param2) {
        SearchSubscribeFragment fragment = new SearchSubscribeFragment();
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
        return new SearchSubscribePresenter(getContext(),this);
    }

    @Override
    protected int providerContentViewId() {
        return R.layout.fragment_search_subscibe;
    }

    @Override
    protected void initView(View rootView) {
        subscribeManager = new LinearLayoutManager(getContext());
        searchSubscribeRecyclerView.setLayoutManager(subscribeManager);
        searchSubscribeRecyclerView.addItemDecoration(new MyDecoration(getContext(), LinearLayoutManager.VERTICAL));
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
    public RecyclerView getSubscribeRecyclerView() {
        return searchSubscribeRecyclerView;
    }

    @Override
    public LinearLayoutManager getSubscribeManager() {
        return subscribeManager;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
