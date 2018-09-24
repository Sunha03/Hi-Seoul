package com.hs.hi_seoul;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("superdroid", "============ID : " + idByANDROID_ID);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_start) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
    }
}
