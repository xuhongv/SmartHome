package com.xuhong.smarthome.view;



import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xuhong.smarthome.R;

import java.util.List;

/**
 * 项目名： animotionpopupwindow_master
 * 包名： com.xuhong.animotionpopupwindow_master
 * 文件名字： AnimotionPopupWindow
 * 创建时间：2017/8/16 14:45
 * 作者： Xuhong
 * 描述： 带动画效果的PopupWindow (高仿ios)
 */

public class AnimotionPopupWindow extends PopupWindow {

    private List<String> itemList;

    private Activity mActivity;

    private LayoutAnimationController mLac;

    private View view;

    private AnimotionPopupWindowOnClickListener listener;
    private ListView listView;


    public AnimotionPopupWindow(Activity mContext, List<String> itemList) {
        super(mContext);
        this.itemList = itemList;
        this.mActivity = mContext;
        init();
    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_anim, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置动画
        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.lv).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /** LayoutAnimation */
        // 从自已3倍的位置下面移到自己原来的位置
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF,3f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(400);
        animation.setStartOffset(150);

        mLac = new LayoutAnimationController(animation, 0.12f);
        mLac.setInterpolator(new DecelerateInterpolator());

        /** 初始化控件*/
        Button button = (Button) view.findViewById(R.id.btCancle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        listView = (ListView) view.findViewById(R.id.lv);
        Adapter adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setLayoutAnimation(mLac);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null) {
                    listener.onPopWindowClickListener(position);
                    dismiss();
                }
            }
        });


    }

    public void show() {
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = mActivity.getWindow().getDecorView().getHeight();
        this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);

    }

    //设置监听事件
    public void setAnimotionPopupWindowOnClickListener(AnimotionPopupWindowOnClickListener listener) {
        this.listener = listener;
    }


    public interface AnimotionPopupWindowOnClickListener {
        void onPopWindowClickListener(int position);
    }

    private class Adapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public Adapter() {
            layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHoler viewHoler = null;
            if (view == null) {
                viewHoler = new ViewHoler();
                view = layoutInflater.inflate(R.layout.item_listview, null);
                viewHoler.textView = (TextView) view.findViewById(R.id.textView);
                view.setTag(viewHoler);

            } else {
                viewHoler = (ViewHoler) view.getTag();
            }
            viewHoler.textView.setText(itemList.get(position));
            return view;
        }

        private class ViewHoler {
            TextView textView;
        }
    }
}