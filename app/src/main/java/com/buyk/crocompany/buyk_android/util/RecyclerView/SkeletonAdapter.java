package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.buyk.crocompany.buyk_android.R;


/**
 * Created by ethanhua on 2017/7/27.
 */

public class SkeletonAdapter extends RecyclerView.Adapter<SkeletonViewHolder> {


    @Override
    public SkeletonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkeletonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.skeleton_offering_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SkeletonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
