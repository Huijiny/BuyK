package com.buyk.crocompany.buyk_android.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.buyk.crocompany.buyk_android.Event.EnableSoldViewNextButton;
import com.buyk.crocompany.buyk_android.Event.EndIdentifyImages;
import com.buyk.crocompany.buyk_android.Event.RemoveSelectedImageEvent;
import com.buyk.crocompany.buyk_android.Event.SetAnimationEvent;
import com.buyk.crocompany.buyk_android.MainActivity;
import com.buyk.crocompany.buyk_android.R;
import com.buyk.crocompany.buyk_android.RecyclerViewAdapterInImageView;
import com.buyk.crocompany.buyk_android.model.RemoteData.JoinResponse;
import com.buyk.crocompany.buyk_android.util.IdentifyWithMlkit;
import com.buyk.crocompany.buyk_android.util.NoticeDialog;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalleryCategory extends Fragment{
    private ArrayList<String> images;

    int int_position;
    RecyclerViewAdapterInImageView recyclerViewAdapterInImageView;
    RecyclerView recyclerView;
    private HashMap<Integer,Boolean> isSelected;
    FrameLayout overAllLay;
    ArrayList<Boolean> isMotorcycle;
    GridView gv;
    MyAdapter adapter;
    Context context;
    long trueCount = 0;
    int endIdentifyImagesCount = 0;
    NoticeDialog noticeDialog;
    Boolean isDialogShowing = false;

    // ViewHolder viewHolder;
    public GalleryCategory() {
        // Required empty public constructor
    }

    public static GalleryCategory newInstance() {

        Bundle args = new Bundle();
        GalleryCategory fragment = new GalleryCategory();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallary_category, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        gv = view.findViewById(R.id.gallery);
        context = view.getContext();
        overAllLay = new FrameLayout(getContext());
        int_position = getActivity().getIntent().getIntExtra("value", 0);
        adapter = new MyAdapter(getActivity().getApplicationContext());
         noticeDialog = new NoticeDialog(getContext());

        gv.setAdapter(adapter);
        isSelected = new HashMap<Integer, Boolean>(0);
        isMotorcycle = new ArrayList<Boolean>();
        Log.e("그리드뷰 들어가져?", "ㅇㅇ");
       recyclerViewAdapterInImageView = new RecyclerViewAdapterInImageView(getContext(),getActivity(),images);
        init_gvListener();


        return view;
    }

    public void init_gvListener(){

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = images.get(position);
                Log.e("넌어디니","ㄴ");
                checkStatus(position, path,view,false);

            }
        });
    }
    public void removeSelectedView(String path, int position,View view)
    {
        recyclerViewAdapterInImageView.delete(path);
        ((CustomView)view).display(false);
    }

    public void checkStatus(int position, String path, View view, Boolean removeCheck)
    {
        recyclerViewAdapterInImageView = new RecyclerViewAdapterInImageView(getContext(),getActivity(),images,view, position);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.image_recycler);
        recyclerView.setAdapter(recyclerViewAdapterInImageView);

        Log.e("on check status position",String.valueOf(position));
        Log.e("on check status path",String.valueOf(path));
        Log.e("pn check status ciew",String.valueOf(view));

        if(select(position) && !removeCheck)
        {
            Log.e("이미지뷰에 추가하는걸로 들옴","ㅇㅇ");
            adapter.selectedPositions.add(position);
            ((CustomView)view).display(true);
            recyclerViewAdapterInImageView.add(path);
            recyclerViewAdapterInImageView.notifyDataSetChanged();

            String selected = recyclerViewAdapterInImageView.getSelectedImages().get(recyclerViewAdapterInImageView.getSelectedImages().size()-1) ;
            if(recyclerViewAdapterInImageView.getSelectedImages().size()>=5) {
                EventBus.getDefault().post(new SetAnimationEvent(true));
                SetIdentifyImages setIdentifyImages = new SetIdentifyImages();
                setIdentifyImages.execute();
//                for (int i=0;i<recyclerViewAdapterInImageView.getSelectedImages().size();i++) {
                    Log.e("isMotorcycle.size();",String.valueOf(isMotorcycle.size()));
//                }
            }

            Context context = getContext();
            recyclerViewAdapterInImageView.notifyDataSetChanged();




        }else{
            Log.e("이미지뷰에 삭제하는걸로들옴","ㅇㅇ");
            Log.e("position+path+view",String.valueOf(position)+path);
            recyclerViewAdapterInImageView.delete(path);
            ((CustomView)view).display(false);

            positionDelete(position);
            ArrayList<String> selected = new ArrayList<>();


            if(recyclerViewAdapterInImageView.getSelectedImages().size()< 5) {

                Log.e("이미지 4개 이하","ㅇㅇ");
                EventBus.getDefault().post(new EnableSoldViewNextButton(false));

            }
            recyclerViewAdapterInImageView.notifyDataSetChanged();

        }
    }
    private boolean select(int position)
    {
        if(!isSelected.containsKey(position)) {//맨처음 set되는 애면 true로 초기화시켜줌
            isSelected.put(position, true);
            Log.e("position",String.valueOf(position)+"boolean"+String.valueOf(isSelected.get(position)));
            return true;
        }
        else {//맨 처음 set되는 애가 아님. 이미 true.
            isSelected.put(position,!isSelected.get(position));

            Log.e("position",String.valueOf(position)+"boolean"+String.valueOf(isSelected.get(position)));
            return isSelected.get(position);
        }

    }

    public void positionDelete(int position)
    {
        Log.e("which position will be deleted",String.valueOf(position));
        int index= 0;
        for(Iterator<Integer> it = adapter.selectedPositions.iterator(); it.hasNext();){
            int pos = it.next();
            if(pos==position)
            {
                    it.remove();

            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void endIdentifyImages(EndIdentifyImages event)
    {
        Log.e("endIdentifyImages","used");
        isMotorcycle.addAll(event.isMotocycle);
        if(recyclerViewAdapterInImageView.getSelectedImages().size()>=5  && endIdentifyImagesCount<1) {
//            endIdentifyImagesCount++;

            for (Boolean e : isMotorcycle) {
                if (e) {
                    trueCount++;
                }
            }
            if(trueCount<3) {
                noticeDialog.setContent("바이크로 인식되는 사진이 없습니다.\n" +
                        "바이크 측면이 보이는 밝은 사진을 업로드시켜주세요.");
                if(!isDialogShowing) {
                    noticeDialog.show();
                    isDialogShowing = true;
                }
                noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isDialogShowing = false;
                    }
                });
                EventBus.getDefault().post(new EnableSoldViewNextButton(false));
            }
            else {
                EventBus.getDefault().post(new EnableSoldViewNextButton(true));

            }
            trueCount = 0;

        }
        EventBus.getDefault().post(new SetAnimationEvent(false));
        isMotorcycle.clear();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeSelectedCheck(RemoveSelectedImageEvent event)
    {
        Log.e("onEventBus","used");
        if(event.status.equals("remove")){
            Log.e("onEventBusFunction","yes");
            checkStatus(event.position,event.path,event.view,true);
//            Log.e("event.index.",String.valueOf(event.viewIndex));
//            Log.e("event.indexdddd",String.valueOf(isMotorcycle.get(event.viewIndex)));
//            if(isMotorcycle.get(event.viewIndex)) {
//                trueCount--;
//            }
//            isMotorcycle.remove(event.viewIndex);
        }
    }


    class MyAdapter extends BaseAdapter {
        Context context;
        ImageView picturesView;
        FrameLayout fl;
        List<Integer> selectedPositions = new ArrayList<>();
        public MyAdapter(Context context) {

            this.context=context;
            images=getAllShownImagesPath(getActivity());
            Log.e("어댑터 실행","ㅇㅇ");
        }


        @Override
        public int getCount() {return images.size();}

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          //  ViewHolder viewHolder;

            int parentWidth = parent.getWidth();
            int itemWidth = parentWidth/3;
            int itemHeight = itemWidth;
            CustomView customView = new CustomView(context,parent);

            customView.display(images.get(position),selectedPositions.contains(position));
            return customView;
        }

        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            try{
                while (cursor.moveToNext()) {
                    absolutePathOfImage = cursor.getString(column_index_data);

                    listOfAllImages.add(absolutePathOfImage);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                Collections.reverse(listOfAllImages);
            }
            return listOfAllImages;
        }


    }
    class SetIdentifyImages extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected Void doInBackground(Integer... params) {

            IdentifyWithMlkit identifyWithMlkit = new IdentifyWithMlkit();

            isMotorcycle.addAll(identifyWithMlkit.IdentifyWithMlkit(context,recyclerViewAdapterInImageView.getSelectedImages()));

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    class CustomView extends FrameLayout {

        private ImageView picturesView;
        private FrameLayout fl;
        public RelativeLayout rl;
        public RadioButton radioBtn;
        private Context context;
        public CustomView(Context context,ViewGroup parent) {
            super(context);
            this.context = context;
            LayoutInflater.from(context).inflate(R.layout.gallery_grid_view_pictures,this);

            int parentWidth = parent.getWidth();
            int itemWidth = parentWidth/3;
            int itemHeight = itemWidth;

            picturesView = (ImageView)getRootView().findViewById(R.id.pictures);
            fl = (FrameLayout)getRootView().findViewById(R.id.galleryFrame);
            rl = (RelativeLayout)getRootView().findViewById(R.id.whenSelected);
            radioBtn = (RadioButton)getRootView().findViewById(R.id.selectedBtn);

            picturesView.getLayoutParams().height=itemHeight;
            picturesView.getLayoutParams().width=itemWidth;
            picturesView.requestLayout();
            picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            rl.getLayoutParams().height=itemHeight;
            rl.getLayoutParams().width=itemWidth;
            rl.requestLayout();
            rl.setVisibility(View.INVISIBLE);

        }




        public void display(String picture, boolean isSelected) {
            Glide.with(context).
                    load(picture)
                    .apply(new RequestOptions().centerCrop())
                    .into(picturesView);

//            display(isSelected);
        }

        public void display(boolean isSelected){
            if(isSelected) {
                Log.e("갤러리뷰에서 invisible","아니");
                rl.setVisibility(VISIBLE);
                radioBtn.setChecked(true);
            }else{
                Log.e("갤러리뷰에서 invisible","응");
                rl.setVisibility(INVISIBLE);
                radioBtn.setChecked(false);
            }
        }
    }
}

