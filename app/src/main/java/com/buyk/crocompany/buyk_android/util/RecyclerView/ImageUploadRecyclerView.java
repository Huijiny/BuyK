package com.buyk.crocompany.buyk_android.util.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.R;

public class ImageUploadRecyclerView extends RecyclerView.Adapter<ViewHolder> {
    public Context context;

    public ImageUploadRecyclerView(Context mcontext){
        context=mcontext;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler,parent,false);

        return new ViewHolder(v,this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mlistName;
    Context cnxt;

    public ViewHolder(View itemView, Context context1) {
        super(itemView);
        cnxt = context1;
        itemView.setOnClickListener(this);
        mlistName = (TextView) itemView.findViewById(R.id.recycleViewListName1);
    }

    @Override
    public void onClick(View v) {

    }
}