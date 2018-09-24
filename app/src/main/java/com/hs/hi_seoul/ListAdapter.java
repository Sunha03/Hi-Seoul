package com.hs.hi_seoul;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context mContext;

    ImageView iv_stamp;

    LayoutInflater inflater = null;
    private ArrayList<ItemStamp> mStamps = null;
    private int nListCnt = 0;

    public ListAdapter(Context context, ArrayList<ItemStamp> events)
    {
        mStamps = events;
        nListCnt = mStamps.size();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return nListCnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            final Context context = parent.getContext();
            if(inflater == null) {
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        convertView.setTag(position);

        //Stamp 클릭 시
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "=====" + mStamps.get(position).name + "=====", Toast.LENGTH_SHORT).show();
            }
        };

        //Stamp 설정
        iv_stamp = (ImageView)convertView.findViewById(R.id.iv_stamp);
        iv_stamp.setImageResource(R.drawable.weather_sunny);
        iv_stamp.setOnClickListener(clickListener);

        return convertView;
    }

}
