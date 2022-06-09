package com.example.esp32_android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RealTimeActivity extends AppCompatActivity {
    private static String TAG = "realtime";
    private EditText mEditTextName;
    private TextView mTextViewResult;
    private TextView mTextViewData;
    private String name;

    @Override
    protected void onStop(){
        super.onStop();

        RealTimeActivity.InsertData task = new RealTimeActivity.InsertData();
        task.execute("https://ycvtuyh484.execute-api.ap-northeast-2.amazonaws.com/default/Andorid_AWS_Communication", "###");

        mEditTextName.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        mTextViewResult = (TextView)findViewById(R.id.textView_get_result);
        mTextViewData = (TextView)findViewById(R.id.textView_get_result2);
        mEditTextName = (EditText)findViewById(R.id.editText_main_name);

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mEditTextName.getText().toString();
                

                RealTimeActivity.InsertData task = new RealTimeActivity.InsertData();
                task.execute("https://ycvtuyh484.execute-api.ap-northeast-2.amazonaws.com/default/Andorid_AWS_Communication", name);

                mEditTextName.setText("");

            }
        });
    }




    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RealTimeActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(isFinishing() == false) {
                progressDialog.dismiss();
            }
            mTextViewResult.setText(result);
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

}
