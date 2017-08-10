package com.xuhong.smarthome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuhong.smarthome.R;
import com.xuhong.smarthome.bean.ScencesListBean;

import java.util.List;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.view
 * 文件名字： mRecyclerViewMyScenceAdapter
 * 创建时间：2017/8/10 17:40
 * 项目名： Xuhong
 * 描述： 情景组的适配器
 */
public class mRecyclerViewMyScenceAdapter extends RecyclerView.Adapter<mRecyclerViewMyScenceAdapter.ViewHolder> {


    private List<ScencesListBean> beanList;

    private Context mContext;

    private LayoutInflater inflater;

    public mRecyclerViewMyScenceAdapter(List<ScencesListBean> beanList, Context mContext) {
        this.beanList = beanList;
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_scence, null);
        return new mRecyclerViewMyScenceAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);

        }
    }


}
