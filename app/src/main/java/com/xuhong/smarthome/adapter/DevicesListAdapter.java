package com.xuhong.smarthome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.xuhong.smarthome.R;
import com.xuhong.smarthome.utils.L;
import com.xuhong.smarthome.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名： SmartHome
 * 包名： com.xuhong.smarthome.adapter
 * 文件名字： DevicesListAdapter
 * 创建时间：2017/8/26 10:02
 * 项目名： Xuhong
 * 描述： 设备列表
 */

public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.ViewHolder> {


    private DevicesListAdapter.OnItemClickListener onItemClickListener;
    private DevicesListAdapter.OnItemLongClickListener onItemLongClickListener;

    private Context mContext;

    private LayoutInflater inflater;

    private List<GizWifiDevice> mList;


    public DevicesListAdapter(Context mContext, List<GizWifiDevice> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(DevicesListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(DevicesListAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public DevicesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_devices_list, null);
        return new DevicesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DevicesListAdapter.ViewHolder holder, final int position) {
        final GizWifiDevice mGizWifiDevice = mList.get(position);
        //设置图标
        switch (mGizWifiDevice.getProductKey()) {
            //智能插座
            case "71b4ebd7f42d4985992734a9d82acda8":
                PicassoUtils.loadImageViewFromLocal(mContext, R.drawable.device_list_socket, holder.ivDevicesIcon);
                break;
            //智能灯泡
            case "6e506b1e21284c85a403bfb7b5c54510":
                PicassoUtils.loadImageViewFromLocal(mContext, R.drawable.device_list_light, holder.ivDevicesIcon);
                break;
        }
        PicassoUtils.loadImageViewFromLocal(mContext, R.drawable.device_list_socket, holder.ivDevicesIcon);
        //设置名字
        if (mGizWifiDevice.getAlias() == null|| mGizWifiDevice.getAlias().isEmpty()) {
            //L.e("Alias:" + mGizWifiDevice.getProductName() + mGizWifiDevice.getMacAddress().substring(0, 3));
            holder.tvDevicesName.setText(mGizWifiDevice.getProductName() + mGizWifiDevice.getMacAddress().substring(0, 3));

        } else {
            //L.e("Alias:" + mGizWifiDevice.getAlias());
            holder.tvDevicesName.setText(mGizWifiDevice.getAlias());
        }

        if (mGizWifiDevice.isLAN()) {
            holder.tvDevicesStutas.setText("局域网在线");
        } else {
            holder.tvDevicesStutas.setText("远程在线");
        }

        if (mGizWifiDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOffline) {
            holder.ivNext.setVisibility(View.INVISIBLE);
            holder.tvDevicesStutas.setText("离线");
            holder.tvDevicesStutas.setTextColor(mContext.getResources().getColor(R.color.side_bar));
            holder.tvDevicesName.setTextColor(mContext.getResources().getColor(R.color.side_bar));
        } else {
            holder.ivNext.setVisibility(View.VISIBLE);
            holder.tvDevicesStutas.setTextColor(mContext.getResources().getColor(R.color.black0));
            holder.tvDevicesName.setTextColor(mContext.getResources().getColor(R.color.black0));
        }

        holder.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(mGizWifiDevice);
                }

            }
        });

        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onClick(mGizWifiDevice);
                }
                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDevicesName, tvDevicesStutas;
        ImageView ivDevicesIcon, ivNext;
        RelativeLayout all;

        ViewHolder(View view) {
            super(view);
            all = (RelativeLayout) view.findViewById(R.id.all);
            tvDevicesStutas = (TextView) view.findViewById(R.id.tvDevicesStutas);
            tvDevicesName = (TextView) view.findViewById(R.id.tvDevicesName);
            ivDevicesIcon = (ImageView) view.findViewById(R.id.ivDevicesIcon);
            ivNext = (ImageView) view.findViewById(R.id.ivNext);
        }
    }

    public interface OnItemClickListener {
        void onClick(GizWifiDevice mGizWifiDevice);
    }

    public interface OnItemLongClickListener {
        void onClick(GizWifiDevice mGizWifiDevice);
    }

}

