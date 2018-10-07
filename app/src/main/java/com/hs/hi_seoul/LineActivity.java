package com.hs.hi_seoul;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LineActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        intent = new Intent(this, TourActivity.class);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_line1) {
            intent.putExtra("LINE", 1);
        }
        else if(v.getId() == R.id.btn_line2) {
            intent.putExtra("LINE", 2);
        }
        else if(v.getId() == R.id.btn_line3) {
            intent.putExtra("LINE", 3);
        }
        else if(v.getId() == R.id.btn_line4) {
            intent.putExtra("LINE", 4);
        }
        else if(v.getId() == R.id.btn_line5) {
            intent.putExtra("LINE", 5);
        }
        else if(v.getId() == R.id.btn_line6) {
            intent.putExtra("LINE", 6);
        }
        else if(v.getId() == R.id.btn_line7) {
            intent.putExtra("LINE", 7);
        }
        else if(v.getId() == R.id.btn_line8) {
            intent.putExtra("LINE", 8);
        }

        startActivity(intent);
    }
}
