package com.hs.hi_seoul;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StampActivity extends AppCompatActivity {
    public GridView gridView;

    ListAdapter listAdapter;
    ArrayList<ItemStamp> stampList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp);

        stampList = new ArrayList<>();

        getJSON task = new getJSON();
        try {
            String result = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        gridView = (GridView)findViewById(R.id.gridView);
        listAdapter = new ListAdapter(this, stampList);
        gridView.setAdapter(listAdapter);
    }

    //JSON 가져오기
    class getJSON extends AsyncTask<String, Integer, String > {
        int num = 0;
        ItemStamp is=null;

        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<50;i++) {
                num += 1;
                is = new ItemStamp();
                is.name = String.valueOf(num);
                is.stamp = " ";

                stampList.add(is);
            }

            return "";
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(String.valueOf(result));
            ListAdapter adapter = new ListAdapter(StampActivity.this, stampList);

            gridView.setAdapter(adapter);
        }
    }
}
