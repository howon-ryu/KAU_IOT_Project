package com.example.esp32_android;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDataActivity extends AppCompatActivity {
    private static String TAG = "awsexample";

    private TextView mTextViewResult;
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
        setContentView(R.layout.activity_main);

        lineChart = (LineChart) findViewById(R.id.chart);

        chartData = new LineData();
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEditTextSearchKeyword = (EditText) findViewById(R.id.editText_main_searchKeyword);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());



        mArrayList = new ArrayList<>();
        dTempList = new ArrayList<>();
        dDustList = new ArrayList<>();
        dHumidList = new ArrayList<>();
        mAdapter = new UsersAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        Button button_search = (Button) findViewById(R.id.button_main_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                mAdapter.notifyDataSetChanged();


                String Keyword =  mEditTextSearchKeyword.getText().toString();
                mEditTextSearchKeyword.setText("");

                GetData task = new GetData();
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

            progressDialog = ProgressDialog.show(PostDataActivity.this,
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

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            String str = jsonObject.getString("body");
            JSONArray jsonArray1 = new JSONArray(str);

            for(int i=0; i < jsonArray1.length(); i++){
                JSONObject jsonObjectBody = jsonArray1.getJSONObject(i);
                String ID = jsonObjectBody.getString(TAG_ID);
                String DATE = jsonObjectBody.getString(TAG_DATE);

                String HUMID = jsonObjectBody.getString(TAG_HUMID);
                String TEMP = jsonObjectBody.getString(TAG_TEMP);
                PersonalData personalData = new PersonalData();

                dTempList.add(new Entry(i, Float.parseFloat(TEMP)));
                dHumidList.add(new Entry(i, Float.parseFloat((HUMID))));


                personalData.setMember_ID(ID);
                personalData.setMember_DATE(DATE);

                personalData.setMember_HUMID(HUMID);
                personalData.setMember_TEMP(TEMP);
                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }
            LineDataSet lineDataSet1 = new LineDataSet(dTempList, "LineTemp"); // 데이터가 담긴 Arraylist 를 LineDataSet 으로 변환한다.
            LineDataSet lineDataSet2 = new LineDataSet(dHumidList, "LineHumid");

            lineDataSet1.setColor(Color.RED); // 해당 LineDataSet의 색 설정 :: 각 Line 과 관련된 세팅은 여기서 설정한다.
            lineDataSet2.setColor(Color.BLACK);


            chartData.addDataSet(lineDataSet1); // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
            chartData.addDataSet(lineDataSet2);


            lineChart.setData(chartData); // 차트에 위의 DataSet을 넣는다.

            lineChart.invalidate(); // 차트 업데이트
            lineChart.setTouchEnabled(false); // 차트 터치 disable


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ",e);
        }

    }
}
