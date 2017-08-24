package com.xuhong.smarthome.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.adapter.mRecyclerViewMyScenceAdapter;
import com.xuhong.smarthome.bean.ScencesListBean;

import java.util.ArrayList;
import java.util.List;


public class ScenceFragment extends BaseFragment {


    private mRecyclerViewMyScenceAdapter mScenceAdapter;

    private List<ScencesListBean> beanList;

    private Toolbar toolbarl;

    private boolean isGridMode = false;
    private RecyclerView rVMyScences;

    private DividerItemDecoration dividerItemDecoration;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), toolbarl);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_scence;
    }

    @Override
    protected void initView(View view) {
        toolbarl = (Toolbar) view.findViewById(R.id.toolbar);
        toolbarl.inflateMenu(R.menu.menu_scence_add);
        toolbarl.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_id_list_mode:
                        rV_Mode(false);
                        break;
                    case R.id.menu_id_grid_mode:
                        rV_Mode(true);
                        mScenceAdapter.notifyDataSetChanged();
                        break;

                }

                return false;
            }
        });
        toolbarl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        rVMyScences = (RecyclerView) view.findViewById(R.id.rVMyScences);

        beanList = new ArrayList<>();
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());
        beanList.add(new ScencesListBean());

        dividerItemDecoration = new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL);
        rV_Mode(false);


    }

    private void rV_Mode(boolean isGridMode) {

        if (isGridMode) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            mScenceAdapter = new mRecyclerViewMyScenceAdapter(beanList, true, getActivity());
            rVMyScences.setLayoutManager(gridLayoutManager);
            rVMyScences.removeItemDecoration(dividerItemDecoration);
            rVMyScences.setAdapter(mScenceAdapter);

        } else {
            rVMyScences.removeItemDecoration(dividerItemDecoration);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mScenceAdapter = new mRecyclerViewMyScenceAdapter(beanList, false, getActivity());
            rVMyScences.setLayoutManager(linearLayoutManager);
            rVMyScences.addItemDecoration(dividerItemDecoration);
            rVMyScences.setAdapter(mScenceAdapter);

        }


    }


}
