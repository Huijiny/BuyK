package com.buyk.crocompany.buyk_android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyk.crocompany.buyk_android.OfferingList;
import com.buyk.crocompany.buyk_android.R;


public class SearchCategory extends Fragment {

    int img1[] = {R.drawable.ic_bike_replica_4_x,R.drawable.ic_bike_naked_4_x,R.drawable.ic_bike_american_4_x,R.drawable.ic_bike_tour_4_x,R.drawable.ic_bike_scooter_4_x,R.drawable.ic_bike_atv_4_x,R.drawable.ic_bike_offroad_4_x
    ,R.drawable.ic_bike_industrial_4_x,R.drawable.ic_bike_elec_4_x};
    String categroyItemText[] = {"레플리카","네이키드","아메리칸","투어링","스쿠터","ATV","오프로드","상업용","전기"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_search, container, false);

        // 커스텀 아답타 생성
        MyAdapter adapter = new MyAdapter (getActivity().getApplicationContext(), R.layout.category_item, img1);

        GridView gv = view.findViewById(R.id.categoryGridView);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용

        Log.e("그리드뷰 들어가지나","ok");
        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.e("gridview onclick", String.valueOf(position));
                Intent gotoOfferingListActivity = new Intent(getActivity().getApplicationContext(), OfferingList.class);
                gotoOfferingListActivity.putExtra("model__types__id",String.valueOf(position+1));
                gotoOfferingListActivity.putExtra("modelName",categroyItemText[position]);
                startActivity(gotoOfferingListActivity);
            }
        });
        return view;
    }


class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    int img[];
    LayoutInflater inf;

    public MyAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.image_category_item);
        TextView tv = (TextView)convertView.findViewById(R.id.text_category_item);
        iv.setImageResource(img[position]);
        tv.setText(categroyItemText[position]);

        return convertView;
    }
}



}
