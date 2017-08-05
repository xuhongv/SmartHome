package com.xuhong.smarthome.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuhong.smarthome.R;

import java.util.List;


public class mRecyclerViewCardAdapter extends RecyclerView.Adapter<mRecyclerViewCardAdapter.ViewHolder> {


    private OnItemClickListener onItemClickListener;

    private Context mContext;

    private LayoutInflater inflater;

    private List<String> mList;



    public mRecyclerViewCardAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_index_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String showInf = mList.get(position);
        holder.tvShowNewsIndex.setText(showInf);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvShowNewsIndex;
        public CardView mCardView;

        public ViewHolder(View view) {
            super(view);
            tvShowNewsIndex = (TextView) view.findViewById(R.id.tvShowNewsIndex);
            mCardView = (CardView) view.findViewById(R.id.mCardView);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

}
