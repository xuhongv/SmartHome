package com.xuhong.smarthome.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.adapter.mRecyclerViewMyScenceAdapter;
import com.xuhong.smarthome.bean.ScencesListBean;

import java.util.ArrayList;
import java.util.List;


public class DevicesFragment extends BaseFragment {


    private Toolbar toolbarl;

    private RecyclerView rlMyScence, rVMyDevices;

    private mRecyclerViewMyScenceAdapter mScenceAdapter;

    private List<ScencesListBean> beanList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), toolbarl);
        toolbarl.inflateMenu(R.menu.menu_devices_add);
        toolbarl.setOverflowIcon(getActivity().getResources().getDrawable(R.drawable.ic_toolbar_devices_add));
        toolbarl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_devices;
    }

    @Override
    protected void initView(View view) {
        toolbarl = (Toolbar) view.findViewById(R.id.toolbar);
        rlMyScence = (RecyclerView) view.findViewById(R.id.rVMyDevices);
        rVMyDevices = (RecyclerView) view.findViewById(R.id.rVMyDevices);
    }

    @Override
    protected void initData() {



    }
}


