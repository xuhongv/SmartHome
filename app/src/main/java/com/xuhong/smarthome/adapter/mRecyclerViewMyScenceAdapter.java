package com.xuhong.smarthome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private OnItemLongClickListener longClickListener;

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

            holder.allGridMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null) {
                        itemOnClickListener.onClick(position);
                    }
                }
            });

            holder.allGridMode.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        longClickListener.onClick(position);
                    }
                    return false;
                }
            });

            holder.ivGridModeLaunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(2000);
                    holder.ivGridModeLaunch.startAnimation(rotateAnimation);
                    if (launchOnClickListener != null) {
                        launchOnClickListener.onClick(position);
                    }
                }
            });


        } else {

            holder.allListMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClickListener != null) {
                        itemOnClickListener.onClick(position);
                    }
                }
            });

            holder.allListModeLaunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(2000);
                    rotateAnimation.setFillAfter(true);
                    holder.ivListModeLaunch.startAnimation(rotateAnimation);
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

        LinearLayout allListModeLaunch;

        RelativeLayout allListMode, allGridMode;

        ImageView ivListModeLaunch, ivGridModeLaunch;


        ViewHolder(View view) {
            super(view);
            if (isGrilMode) {
                ivGridModeLaunch = (ImageView) view.findViewById(R.id.ivScence);
                allGridMode = (RelativeLayout) view.findViewById(R.id.allGridMode);
            } else {
                allListMode = (RelativeLayout) view.findViewById(R.id.allListMode);
                allListModeLaunch = (LinearLayout) view.findViewById(R.id.allLaunch);
                ivListModeLaunch = (ImageView) view.findViewById(R.id.ivLaunch);
            }

        }
    }


    public void setOnItemClickListener(mRecyclerViewMyScenceAdapter.OnItemClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }


    public void setOnLaunchClickListener(mRecyclerViewMyScenceAdapter.OnLaunchClickListener itemOnClickListener) {
        this.launchOnClickListener = itemOnClickListener;
    }

    public void setOnLaunchClickListener(mRecyclerViewMyScenceAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


    public interface OnLaunchClickListener {
        void onClick(int position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }


}
