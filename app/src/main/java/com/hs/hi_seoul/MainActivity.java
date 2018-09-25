package com.hs.hi_seoul;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("superdroid", "============ID : " + android_id);

        //사용자 unique id 저장/불러오기
        SaveUser();
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_start) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
    }

    //사용자 unique id 저장/불러오기
    public void SaveUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int yes_no = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ID가 있으면 완료한 스탬프 불러오기
                    if(snapshot.getKey().toString().equals(android_id)) {
                        //snapshot.getValue() : 완료한 스탬프
                        Log.e("MainActivity", "Value : " + snapshot.getValue());
                        yes_no += 1;
                        break;
                    }
                }
                //저장된 ID가 없으면 DB에 저장
                if(yes_no == 0) {
                    FirebaseDatabase.getInstance().getReference("User").child(android_id).setValue("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", "Error : " + databaseError.getMessage());
            }
        });
    }
}
