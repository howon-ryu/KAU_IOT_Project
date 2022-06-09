package com.example.esp32_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindChangeActivity extends AppCompatActivity {
    private static String TAG = "awsexample";

    private TextView mTextViewResult;
    private TextView mTextViewStand;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mEditTextSearchKeyword;
    private String mJsonString;

    private ArrayList<Entry> dTempList;
    private ArrayList<Entry> dDustList;
    private ArrayList<Entry> dHumidList;

    private LineChart lineChart;
    private LineData chartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);

        lineChart = (LineChart) findViewById(R.id.chart);

        chartData = new LineData();
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);


        mEditTextSearchKeyword = (EditText) findViewById(R.id.editText_main_searchKeyword);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());



        mArrayList = new ArrayList<>();
        dTempList = new ArrayList<>();
        dDustList = new ArrayList<>();
        dHumidList = new ArrayList<>();
        mAdapter = new UsersAdapter(this, mArrayList);

        mTextViewStand = (TextView)findViewById(R.id.textView_stand);
        mTextViewStand.setVisibility(View.VISIBLE);
        mTextViewStand.setText("미세먼지 적정농도 좋음: 80 이하\n미세먼지 적정농도 나쁨: 81~150 이하\n미세먼지 적정농도 매우나쁨: 151 이상");

        Button button_search = (Button) findViewById(R.id.button_main_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                mAdapter.notifyDataSetChanged();


                String Keyword =  mEditTextSearchKeyword.getText().toString();
                mEditTextSearchKeyword.setText("");

                FindChangeActivity.GetData task = new FindChangeActivity.GetData();
                task.execute( "https://vn23hf5ug1.execute-api.ap-northeast-2.amazonaws.com/default/Andorid_AWS_GetData\n", Keyword);
            }
        });
    }


//    class InsertData extends AsyncTask<String, Void, String>{
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "Please Wait", null, true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//            mTextViewResult.setText(result);
//            Log.d(TAG, "POST response  - " + result);
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String name = params[1];
//            String country = params[2];
//
//            String serverURL = params[0];
//
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("ID", name);
//                jsonObject.put("NAME", country);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            OkHttpClient client = new OkHttpClient();
//            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//
//            Request request = new Request.Builder()
//                    .url(serverURL)
//                    .post(body)
//                    .build();
//
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//                String resStr = response.body().string();
//
//                return resStr;
//            } catch (IOException e) {
//                e.printStackTrace();
//
//                return e.toString();
//            }
//
//        }
//    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(FindChangeActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String name = params[1];
            String serverURL = params[0];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String resStr = response.body().string();

                return resStr;
            } catch (IOException e) {
                e.printStackTrace();

                return e.toString();
            }
        }
    }

    private void showResult(){

        String TAG_JSON="webnautes";
        //String TAG_ID = "id";
        String TAG_ID = "ID";
        String TAG_DATE ="DATE";
        String TAG_DUST ="DUST";
        String TAG_HUMID ="HUMID";
        String TAG_TEMP ="TEMP";
        ArrayList<Float> Dust_data_List = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            String str = jsonObject.getString("body");
            JSONArray jsonArray1 = new JSONArray(str);
            for(int i=0; i < jsonArray1.length(); i++){
                JSONObject jsonObjectBody = jsonArray1.getJSONObject(i);
                String ID = jsonObjectBody.getString(TAG_ID);
                String DATE = jsonObjectBody.getString(TAG_DATE);
                String DUST = jsonObjectBody.getString(TAG_DUST);

                PersonalData personalData = new PersonalData();



                if(!DUST.equals("null")) {
                    Dust_data_List.add(Float.parseFloat(DUST));
                    personalData.setMember_DATE(DATE);
                    personalData.setMember_DUST(DUST);
                    mArrayList.add(personalData);
                    mAdapter.notifyDataSetChanged();
                }
            }
            for(int i=0,j=0,c=0; i<Dust_data_List.size(); i++){
                j += Dust_data_List.get(i);
                Log.d("view_dust", String.format("%d",j));


                if(i % 24 == 0 && i !=0){
                    dDustList.add(new Entry(c++,j / i));
                    Log.d("dust", Integer.toString(j));
                    j = 0;
                }else if(i == Dust_data_List.size() -1){
                    dDustList.add(new Entry(c++,j / i));
                    Log.d("dust", Integer.toString(i));
                }
            }

            LineDataSet lineDataSet3 = new LineDataSet(dDustList, "LineDust");
            lineDataSet3.setColor(Color.RED);


            chartData.addDataSet(lineDataSet3);

            lineChart.setData(chartData); // 차트에 위의 DataSet을 넣는다.

            lineChart.invalidate(); // 차트 업데이트
            lineChart.setTouchEnabled(false); // 차트 터치 disable




        } catch (JSONException e) {

            Log.d(TAG, "showResult : ",e);
        }

    }
}
