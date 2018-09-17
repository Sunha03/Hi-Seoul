package com.hs.hi_seoul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeaterActivity extends AppCompatActivity {
    TextView tv_date = null;
    TextView tv_day = null;
    TextView tv_temperature = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weater);

        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_temperature = (TextView)findViewById(R.id.tv_temperature);

        //현재 날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);

        tv_date.setText(strDate);
        tv_day.setText("DAY 1");
        tv_temperature.setText("27'C");
    }

    public void onClick(View v) {
        if(v.getId() == R.id.ib_item1) {
            Toast.makeText(getApplicationContext(), "Click Item1", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.ib_item2) {
            Toast.makeText(getApplicationContext(), "Click Item2", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.ib_item3) {
            Toast.makeText(getApplicationContext(), "Click Item3", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.ib_item4) {
            Toast.makeText(getApplicationContext(), "Click Item4", Toast.LENGTH_SHORT).show();
        }
    }
}
