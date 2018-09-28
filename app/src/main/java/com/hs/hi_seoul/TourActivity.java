package com.hs.hi_seoul;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TourActivity extends AppCompatActivity {
    ArrayList<TouristSpot> spotList;
    ListView listView;
    TextView tv_line;
    ListAdapter listAdapter;

    Intent getIntent;
    int line_num;
    String strLine = "Line";

    TouristSpot spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        listView = (ListView)findViewById(R.id.listView);
        tv_line = (TextView)findViewById(R.id.tv_line);

        spotList = new ArrayList<>();

        getIntent = getIntent();
        line_num = getIntent.getIntExtra("LINE", 0);
        strLine = strLine.concat(String.valueOf(line_num));
        tv_line.setText("LINE " + String.valueOf(line_num));

        //Get Data from firebase
        getData();

    }


    //Get Data from firebase
    public void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Tourist attraction");
        reference.child(strLine).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    spot = new TouristSpot();
                    spot.spot_name = snapshot.getKey();

                    for(DataSnapshot snap : snapshot.getChildren()) {
                        String key = snap.getKey();
                        String value = snap.getValue().toString();

                        switch(key) {
                            case "Phone Number":
                                spot.phone_number = value;
                                break;
                            case "Image":
                                spot.image_url = value;
                                break;
                            case "Contents":
                                spot.contents = value;
                                break;
                            case "Type":
                                spot.type = value;
                                break;
                            case "Subway Station":
                                spot.station = value;
                                break;
                            case "Price":
                                spot.price = value;
                                break;
                            case "Hours":
                                spot.hours = value;
                                break;
                            case "Site":
                                spot.site = value;
                                break;
                        }
                    }

                    spotList.add(spot);

                }

                listAdapter = new ListAdapter(TourActivity.this, line_num, spotList);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("superdroid", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
