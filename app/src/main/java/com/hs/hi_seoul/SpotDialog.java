package com.hs.hi_seoul;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SpotDialog extends Dialog {
    TouristSpot spot;
    int line_num;
    String spot_name;

    ImageView iv_image;
    Bitmap bmp;
    TextView tv_spot_name;
    TextView tv_station;
    TextView tv_hours;
    TextView tv_phone_number;
    TextView tv_price;
    TextView tv_contents;
    TextView tv_site;

    public SpotDialog(@NonNull Context context, int line_num, String spot_name) {
        super(context);
        this.line_num = line_num;
        this.spot_name = spot_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spot);

        iv_image = (ImageView)findViewById(R.id.iv_image);
        tv_spot_name = (TextView)findViewById(R.id.tv_spot_name);
        tv_station = (TextView)findViewById(R.id.tv_station);
        tv_hours = (TextView)findViewById(R.id.tv_hours);
        tv_phone_number = (TextView)findViewById(R.id.tv_phone_number);
        tv_price = (TextView)findViewById(R.id.tv_price);
        tv_contents = (TextView)findViewById(R.id.tv_contents);
        tv_site = (TextView)findViewById(R.id.tv_site);
        Button btn_back = (Button)findViewById(R.id.btn_back);

        spot = new TouristSpot();

        /*Intent getIntent = getIntent();
        line_num = getIntent.getIntExtra("LINE", 0);
        spot_name = getIntent.getStringExtra("SPOT_NAME");*/

        getData();

        //Back 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpotDialog.this.dismiss();
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {
        //바깥 클릭 안됨
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //Get Data from firebase
    public void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Tourist attraction");
        reference.child("Line".concat(String.valueOf(line_num))).child(spot_name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spot.spot_name = spot_name;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "Phone Number":
                            spot.phone_number = snapshot.getValue().toString();
                            break;
                        case "Image":
                            spot.image_url = snapshot.getValue().toString();
                            break;
                        case "Contents":
                            spot.contents = snapshot.getValue().toString();
                            break;
                        case "Type":
                            spot.type = snapshot.getValue().toString();
                            break;
                        case "Subway Station":
                            spot.station = snapshot.getValue().toString();
                            break;
                        case "Price":
                            spot.price = snapshot.getValue().toString();
                            break;
                        case "Hours":
                            spot.hours = snapshot.getValue().toString();
                            break;
                        case "Site":
                            spot.site = snapshot.getValue().toString();
                            break;
                    }

                }

                setTextView(spot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("superdroid", "Failed to read value.", databaseError.toException());
            }
        });
    }

    //TextView 내용 입력
    public void setTextView(TouristSpot ts) {
        tv_spot_name.setText(ts.spot_name);
        tv_station.setText(ts.station);
        tv_hours.setText(ts.hours);
        tv_phone_number.setText(ts.phone_number);
        tv_price.setText(ts.price);
        tv_contents.setText(ts.contents);
        tv_site.setText(ts.site);

        getImageTask task = new getImageTask();
        task.execute(spot.image_url);
        //iv_image.setImageBitmap(bmp);
    }

    //URL image 가져오기
    class getImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap b;
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bmp = bitmap;
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            iv_image.setImageBitmap(result);
        }
    }
}
