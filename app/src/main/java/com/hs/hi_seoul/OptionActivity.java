package com.hs.hi_seoul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionActivity extends AppCompatActivity {
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_weather) {
            intent = new Intent(this, WeatherActivity.class);
            this.finish();
        }
        else if(v.getId() == R.id.btn_stamp) {
            intent = new Intent(this, StampActivity.class);
        }
        else if(v.getId() == R.id.btn_tour) {
            intent = new Intent(this, TourActivity.class);
        }

        startActivity(intent);
    }
}
