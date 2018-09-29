package com.hs.hi_seoul;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    String android_id;

    TextView tv_date = null;
    TextView tv_day = null;
    TextView tv_temperature = null;
    ImageView iv_sunny, iv_cloud,iv_many, iv_rain, iv_snow;

    String api_date = null;
    String api_time = null;

    String sky;
    int temperature;
    String precipitationForm;
    double precipitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_temperature = (TextView)findViewById(R.id.tv_temperature);
        iv_sunny = (ImageView)findViewById(R.id.iv_sunny);
        iv_cloud = (ImageView)findViewById(R.id.iv_cloud);
        iv_many = (ImageView)findViewById(R.id.iv_many);
        iv_rain = (ImageView)findViewById(R.id.iv_rain);
        iv_snow = (ImageView)findViewById(R.id.iv_snow);

        //현재 날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);
        sdf = new SimpleDateFormat("yyyyMMdd");
        api_date = sdf.format(date);
        sdf = new SimpleDateFormat("HH");
        api_time = sdf.format(date);
        Log.e("superdroid", api_date + " / " + api_time);

        getJSON task = new getJSON();
        task.execute();

        tv_date.setText(strDate);
        tv_day.setText("DAY 1");

    }

    public void onClick(View v) {
        Intent intent = new Intent(this, LineActivity.class);

        switch(v.getId()) {
            case R.id.ib_item1:
            case R.id.ib_item2:
            case R.id.ib_item3:
            case R.id.ib_item4:
                startActivity(intent);
                break;
            case R.id.ib_item5:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seoul.go.kr/main/index.html"));
                intent.setData(Uri.parse("http://www.seoul.go.kr/main/index.html"));
                startActivity(intent);
                break;
            default:
                    break;
        }
    }

    //JSON 가져오기
    class getJSON extends AsyncTask<String, Integer, String > {
        private String str, receiveMsg;
        private final String myKey = "bvR%2BDEDUQQhgCmHXv4wDPzycMyADP9Zj7yURE8H%2Bp%2F0UYoDhpsbnJk%2F6Kvsn7WBSKZ%2FKJZgud%2FmuKh%2Bmg8K%2BLg%3D%3D";            //API KEY

        @Override
        protected String doInBackground(String... strings) {
            URL api_url = null;
            try {
                String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastTimeData?";
                String key = "serviceKey=" + myKey;
                String date = "&base_date=" + api_date;
                String time = "&base_time=" + api_time + "00";
                String nx = "&nx=" + "60";
                String ny = "&ny=" + "127";
                String num_of_rows = "&numOfRows=" + "100";
                String type = "&_type=" + "json";

                api_url = new URL(url + key + date + time + nx + ny + num_of_rows + type);
                Log.e("WeatherActivity", "JSON URL : " + url + key + date + time + nx + ny + num_of_rows + type);

                HttpURLConnection   con = (HttpURLConnection) api_url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if(con.getResponseCode() == con.HTTP_OK) {
                    InputStreamReader inputStream = new InputStreamReader(con.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                    StringBuffer strBuffer = new StringBuffer();
                    while((str = bufferedReader.readLine()) != null) {
                        strBuffer.append(str);
                    }

                    bufferedReader.close();
                    con.disconnect();

                    receiveMsg = strBuffer.toString();
                }
                else {
                    Log.e("WeatherActivity", con.getResponseCode() + "ERROR!");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(String.valueOf(result));
            eventListJSONParser(receiveMsg);
        }
    }

    //필요한 데이터 가져오기(3시간 뒤 기상예보)
    public void eventListJSONParser(String jsonString) {
        String category;
        String fcstValue;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject SearchWeather = jsonObject.getJSONObject("response");
            JSONObject SearchWeather2 = SearchWeather.getJSONObject("body");
            JSONObject SearchWeather3 = SearchWeather2.getJSONObject("items");
            JSONArray jsonArray = SearchWeather3.getJSONArray("item");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jObject = jsonArray.getJSONObject(i);

                category = jObject.optString("category");
                fcstValue = jObject.optString("fcstValue");

                //하늘 상태
                if(category.equals("SKY")) {
                    int skyValue = Integer.parseInt(fcstValue);
                    if(skyValue == 1) {
                        sky = "맑음";
                        iv_sunny.setVisibility(View.VISIBLE);
                    }
                    else if(skyValue == 2) {
                        sky = "구름조금";
                        iv_cloud.setVisibility(View.VISIBLE);
                    }
                    else if(skyValue == 3) {
                        sky = "구름많음";
                        iv_many.setVisibility(View.VISIBLE);
                    }
                    else if(skyValue == 4) {
                        sky = "흐림";
                        iv_many.setVisibility(View.VISIBLE);
                    }
                }

                //기온
                else if(category.equals("T1H")) {
                    Double douTemperature = Double.parseDouble(fcstValue);
                    temperature = douTemperature.intValue();
                    tv_temperature.setText(temperature + "'C");
                }

                //강수형태
                else if(category.equals("PTY")) {
                    int precipitationValue = Integer.parseInt(fcstValue);
                    if(precipitationValue == 0) {
                        precipitationForm = "비/눈 없음";
                        iv_sunny.setVisibility(View.VISIBLE);
                    }
                    else if(precipitationValue == 1) {
                        precipitationForm = "비";
                        iv_rain.setVisibility(View.VISIBLE);

                    }
                    else if(precipitationValue == 2) {
                        precipitationForm = "비/눈";
                        iv_snow.setVisibility(View.VISIBLE);

                    }
                    else if(precipitationValue == 3) {
                        precipitationForm = "눈";
                        iv_snow.setVisibility(View.VISIBLE);

                    }
                }

                //강수량
                else if(category.equals("RN1")) {
                    precipitation = Double.parseDouble(fcstValue);
                }
            }

            Log.e("superdroid", "sky : " + sky + " / 기온 : " + temperature + "도 / 강수형태 : " + precipitationForm + " / 강수량 : " + precipitation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
