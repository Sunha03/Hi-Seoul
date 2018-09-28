package com.hs.hi_seoul;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    int line_num;
    Context mContext;
    LayoutInflater inflater = null;
    private ArrayList<TouristSpot> mSpots = null;
    private int nListCnt = 0;

    ImageButton ib_spot;
    TextView tv_spot_name;

    public ListAdapter(Context context, int lineNum,  ArrayList<TouristSpot> events)
    {
        mSpots = events;
        nListCnt = mSpots.size();
        line_num = lineNum;
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
                SpotDialog dialog = new SpotDialog(mContext, line_num, mSpots.get(position).spot_name);
                dialog.show();
            }
        };

        //Stamp 설정
        ib_spot = (ImageButton)convertView.findViewById(R.id.ib_spot);
        tv_spot_name = (TextView)convertView.findViewById(R.id.tv_spot_name);

        switch(line_num) {
            case 1:
                ib_spot.setBackgroundResource(R.drawable.line1);
                break;
            case 2:
                ib_spot.setBackgroundResource(R.drawable.line2);
                break;
            case 3:
                ib_spot.setBackgroundResource(R.drawable.line3);
                break;
            case 4:
                ib_spot.setBackgroundResource(R.drawable.line4);
                break;
            case 5:
                ib_spot.setBackgroundResource(R.drawable.line5);
                break;
            case 6:
                ib_spot.setBackgroundResource(R.drawable.line6);
                break;
            case 7:
                ib_spot.setBackgroundResource(R.drawable.line7);
                break;
            case 8:
                ib_spot.setBackgroundResource(R.drawable.line8);
                break;
        }

        tv_spot_name.setText(mSpots.get(position).spot_name);
        ib_spot.setOnClickListener(clickListener);

        return convertView;
    }
}
