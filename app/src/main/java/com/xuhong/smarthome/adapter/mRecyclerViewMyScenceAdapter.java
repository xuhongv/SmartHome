package com.xuhong.smarthome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

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

    private LayoutInflater inflater;

    //true 为宫格模式  false为列表模式
    private boolean isGrilMode;

    private OnLaunchClickListener launchOnClickListener;

    private OnItemClickListener itemOnClickListener;

    public mRecyclerViewMyScenceAdapter(List<ScencesListBean> beanList, boolean isGrilMode, Context mContext) {
        this.isGrilMode = isGrilMode;
        this.beanList = beanList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (isGrilMode) {
            view = inflater.inflate(R.layout.layout_item_grid_mode_scence, null);
        } else {
            view = inflater.inflate(R.layout.layout_item_list_mode_scence, null);
        }

        return new mRecyclerViewMyScenceAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (isGrilMode) {


        } else {
            holder.rlLaunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(2000);
                    //rotateAnimation.setInterpolator(new BounceInterpolator());
                    rotateAnimation.setFillAfter(true);
                    holder.rlLaunch.startAnimation(rotateAnimation);
                    if (launchOnClickListener != null) {
                        launchOnClickListener.onClick(position);
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlLaunch;

        ViewHolder(View view) {
            super(view);
            if (isGrilMode) {

            } else {
                rlLaunch = (RelativeLayout) view.findViewById(R.id.rlLaunch);
            }

        }
    }


    public void setOnItemClickListener(mRecyclerViewMyScenceAdapter.OnItemClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }


    public void setOnLaunchClickListener(mRecyclerViewMyScenceAdapter.OnLaunchClickListener itemOnClickListener) {
        this.launchOnClickListener = itemOnClickListener;
    }


    public interface OnLaunchClickListener {
        void onClick(int position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
