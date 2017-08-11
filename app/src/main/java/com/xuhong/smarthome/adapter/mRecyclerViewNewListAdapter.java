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
import com.xuhong.smarthome.bean.HomeNewsListItemBean;
import com.xuhong.smarthome.utils.PicassoUtils;

import java.net.URI;
import java.util.List;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.adapter
 * 文件名字： mRecyclerViewNewListAdapter
 * 创建时间：2017/8/8 10:18
 * 项目名： Xuhong
 * 描述： 新闻列表适配器
 */

public class mRecyclerViewNewListAdapter extends RecyclerView.Adapter<mRecyclerViewNewListAdapter.ViewHolder> {


    private mRecyclerViewNewListAdapter.OnItemClickListener onItemClickListener;

    private Context mContext;

    private LayoutInflater inflater;

    private List<HomeNewsListItemBean> mList;


    public mRecyclerViewNewListAdapter(Context mContext, List<HomeNewsListItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(mRecyclerViewNewListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public mRecyclerViewNewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_news_list_item, null);
        return new mRecyclerViewNewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mRecyclerViewNewListAdapter.ViewHolder holder, final int position) {
        HomeNewsListItemBean bean = mList.get(position);
        if (!bean.getPicUrl().isEmpty()) {
            PicassoUtils.loadImageViewFromURl(mContext, bean.getPicUrl(), holder.ivNewsPic);
        }
        holder.tvCreatTime.setText(bean.getCreatTime());
        holder.tvTitle.setText(bean.getTitle());
        holder.tvFrom.setText(bean.getNewsFrom());
        holder.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


     class ViewHolder extends RecyclerView.ViewHolder {

         TextView tvTitle, tvCreatTime, tvFrom;
         ImageView ivNewsPic;
         RelativeLayout all;

         ViewHolder(View view) {
            super(view);
            all = (RelativeLayout) view.findViewById(R.id.all);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvCreatTime = (TextView) view.findViewById(R.id.tvCreatTime);
            tvFrom = (TextView) view.findViewById(R.id.tvFrom);
            ivNewsPic = (ImageView) view.findViewById(R.id.ivNewsPic);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

}

