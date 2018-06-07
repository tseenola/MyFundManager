package com.tseenola.jijin.myjijing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/8/10.
 * 描述：
 */
public class CommonViewHolder {
    private final SparseArray<View> mViews = new SparseArray();
    private int mPosition;
    private View mConvertView;

    private CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static CommonViewHolder getCommonViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        return convertView == null ? new CommonViewHolder(context, parent, layoutId, position) : (CommonViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = (View) this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public CommonViewHolder setText(int viewId, String text) {
        TextView view = (TextView) this.getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setTextColor(int viewId, int color) {
        TextView view = (TextView) this.getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 设置view是否可见
     * @param viewId
     * @param pVisibility
     * @return
     */
    public CommonViewHolder setVisible(int viewId,int pVisibility){
        if (pVisibility != View.GONE && pVisibility!= View.VISIBLE && pVisibility != View.INVISIBLE){
            throw new IllegalArgumentException("无效参数 pVisibility");
        }
        View lView = this.getView(viewId);
        lView.setVisibility(pVisibility);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = (ImageView) this.getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public CommonViewHolder setBackground(int viewId, int color) {
        View view = (View) this.getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = (ImageView) this.getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public int getPosition() {
        return this.mPosition;
    }
}
