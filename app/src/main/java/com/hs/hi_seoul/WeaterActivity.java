package com.hs.hi_seoul;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class WeaterActivity extends AppCompatActivity {
    TextView tv_date = null;
    TextView tv_day = null;
    TextView tv_temperature = null;

    String api_date = null;
    String api_time = null;

    String sky;
    double temperature;
    String precipitationForm;
    Double precipitation;

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
        sdf = new SimpleDateFormat("yyyyMMdd");
        api_date = sdf.format(date);
        sdf = new SimpleDateFormat("hh", Locale.KOREA);
        api_time = sdf.format(date);
        Log.e("superdroid", api_date + " / " + api_time);

        getJSON task = new getJSON();
        task.execute();

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
                Log.e("superdroid", url + key + date + time + nx + ny + num_of_rows + type);

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

                    eventListJSONParser(receiveMsg);
                }
                else {
                    Log.e("superdroid", con.getResponseCode() + "ERROR!");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(String.valueOf(result));

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
                    }
                    else if(skyValue == 2) {
                        sky = "구름조금";
                    }
                    else if(skyValue == 3) {
                        sky = "구름많음";
                    }
                    else if(skyValue == 4) {
                        sky = "흐림";
                    }
                }

                //기온
                else if(category.equals("T1H")) {
                    temperature = Double.parseDouble(fcstValue);
                }

                //강수형태
                else if(category.equals("PTY")) {
                    int precipitationValue = Integer.parseInt(fcstValue);
                    if(precipitationValue == 0) {
                        precipitationForm = "비/눈 없음";
                        }
                    else if(precipitationValue == 1) {
                        precipitationForm = "비";
                    }
                    else if(precipitationValue == 2) {
                        precipitationForm = "비/눈";
                    }
                    else if(precipitationValue == 3) {
                        precipitationForm = "눈";
                    }
                }

                //강수량
                else if(category.equals("RN1")) {
                    precipitation = Double.parseDouble(fcstValue.toString());
                }
            }

            Log.e("superdroid", "sky : " + sky + " / 기온 : " + temperature + "도 / 강수형태 : " + precipitationForm + " / 강수량 : " + precipitation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
