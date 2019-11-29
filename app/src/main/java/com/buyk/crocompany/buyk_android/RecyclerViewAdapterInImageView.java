package com.buyk.crocompany.buyk_android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.buyk.crocompany.buyk_android.Event.RemoveSelectedImageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class RecyclerViewAdapterInImageView extends RecyclerView.Adapter<ViewHolderInImageView>{
    Context context;
   static ArrayList<String> bikeImages= new ArrayList<>();
   static ArrayList<String> selectedImages = new ArrayList<>(0);
    ViewHolderInImageView mholder;
    static ArrayList<Integer> selectedPosition = new ArrayList<>();
    static ArrayList<View>selectedView = new ArrayList<>();
    LinkedHashSet temp = new LinkedHashSet<View>();

    Activity activity;

    public RecyclerViewAdapterInImageView(Context cnxt, Activity activity, ArrayList<String>images, View view, int position) {
        context = cnxt;
        bikeImages = images;
        selectedPosition.add (position);
        for (int i = 0; i <= selectedView.size(); i++) {
            if (!selectedView.contains(view)) {
                selectedView.add(view);
            }
        }
        Log.e("das","sad");

        this.activity = activity;

    }

    public RecyclerViewAdapterInImageView(Context cnxt, Activity activity,ArrayList<String>images){
        context = cnxt;
        bikeImages = images;
        this.activity = activity;
    }

    public RecyclerViewAdapterInImageView(Context cnxt)
    {
        context = cnxt;
    }

    public static ArrayList<String> getSelectedImages() {
        return selectedImages;
    }

    public void add(String Imagepath) {
        selectedImages.add(Imagepath);
    }


    public void delete(int position)
    {
        delete(selectedImages.get(position));
        positionDelete(selectedPosition.get(position));
        viewDelete(position);

        notifyItemRemoved(position);
    }

    public void positionDelete(int position)
    {
        Log.e("which position will be deleted",String.valueOf(position));
        for(Iterator<Integer> it = selectedPosition.iterator();it.hasNext();){
            int pos = it.next();
            if(pos==position)
            {
                it.remove();
            }
        }
    }

    public void viewDelete(int position)
    {
        if(selectedView.size()>0) {
            selectedView.stream().forEach(e->Log.e("selectedview 내부 전",e.toString()));
        }
        selectedView.remove(position);

        if(selectedView.size()>0) {
            selectedView.stream().forEach(e->Log.e("selectedview 내부 후",e.toString()));
        }
        Log.e("dd",String.valueOf(position));
        notifyDataSetChanged();
    }

    public void delete(String ImagePath) {

            for(Iterator<String> it = selectedImages.iterator();it.hasNext();){
                String path = it.next();
                if(path.equals(ImagePath))
                {
                    Log.e("이미지path","같음");
                    it.remove();
                }
            }

    }

    @NonNull
    @Override
    public ViewHolderInImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler,parent,false);
        context = parent.getContext();

        return new ViewHolderInImageView(v,this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInImageView holder, int position) {
        String[] status = {"측면","반대측면","전면","후면","계기판"};

        if (holder.getAdapterPosition() == selectedImages.size()) {

            if(holder.getAdapterPosition()>4) {
                holder.imageStatus.setText("상세사진");
            }else {
                holder.imageStatus.setText(status[selectedImages.size()]);
            }

            holder.removeBtn.setVisibility(View.INVISIBLE);
            holder.rl.setVisibility(View.INVISIBLE);

        } else {
            if (selectedImages.size() != 0) {
                    Glide.with(context)
                            .load(selectedImages.get(position))
                            .apply(new RequestOptions().override(200,200).centerCrop())
                            .into(holder.bikeImageView);
                    Log.e("이미지뷰 바인딩됨", "oo");
                    holder.removeBtn.setVisibility(View.VISIBLE);
                    holder.imageStatus.setVisibility(View.INVISIBLE);
                    holder.plus.setVisibility(View.INVISIBLE);
                    holder.rl.setVisibility(View.VISIBLE);
                    if(holder.getAdapterPosition()>=5){
                        holder.imageStatusWhenSelected.setText("상세사진");
                    }else {
                        holder.imageStatusWhenSelected.setText(status[position]);
                    }

            }

            holder.removeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("on remove button onclick listener","yes");
                    int cPosition = holder.getAdapterPosition();

                    try {
                        RemoveSelectedImageEvent removeSelectedImageEvent = new RemoveSelectedImageEvent("remove",selectedImages.get(holder.getAdapterPosition()),selectedPosition.get(cPosition),selectedView.get(cPosition),cPosition);
                        EventBus.getDefault().post(removeSelectedImageEvent);
                        positionDelete(selectedPosition.get(cPosition));
                        viewDelete(cPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                        viewDelete(cPosition);
                    }

                }
            });

        }
    }
    @Override
    public int getItemCount() {
        int num;
        if(selectedImages.isEmpty()){
            num=1;
        }else{
           num= selectedImages.size()+1;
            Log.e("bikeImageList",String.valueOf(num));
        }
        return num;
    }


}


class ViewHolderInImageView extends RecyclerView.ViewHolder  {
    public ImageView bikeImageView;
    public ImageButton removeBtn;
    public ImageView plus;
    public TextView imageStatus;
    public RelativeLayout rl;
    public TextView imageStatusWhenSelected;

    Context cnxt;
    ArrayList<Bitmap>mDataSet;
    RelativeLayout.LayoutParams layoutParams;

    public ViewHolderInImageView(View itemView, Context context1) {
        super(itemView);
        cnxt = context1;
        removeBtn=(ImageButton)itemView.findViewById(R.id.removeImageBtn);
        bikeImageView=(ImageView) itemView.findViewById(R.id.bikeImageInRecycler);
        imageStatus = (TextView)itemView.findViewById(R.id.status);
        plus = (ImageView)itemView.findViewById(R.id.imageView);
        rl = (RelativeLayout)itemView.findViewById(R.id.whenSelectedAppear);
        imageStatusWhenSelected = (TextView)itemView.findViewById(R.id.statusWhenSelected);
    }



}

