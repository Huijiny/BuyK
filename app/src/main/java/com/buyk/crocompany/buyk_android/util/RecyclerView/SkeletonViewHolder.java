package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;


public class SkeletonViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views = new SparseArray<>();

    public SkeletonViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int resId) {
        View v = views.get(resId);
        if (null == v) {
            v = itemView.findViewById(resId);
            views.put(resId, v);
        }
        return (V) v;
    }
}