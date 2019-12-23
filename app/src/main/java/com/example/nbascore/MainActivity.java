package com.example.nbascore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view_result);
        Button buttonswitch= findViewById(R.id.button_switch);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://odds.p.rapidapi.com/v1/odds?sport=basketball_nba&region=us&mkt=%7Bmkt%7D")
                .get()
                .addHeader("x-rapidapi-host", "odds.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "36ad8130famshab740c067aba105p16fc2cjsn116da49f8e86")
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }
            public void onResponse(Call call, Response response)throws IOException{
                if (response.isSuccessful()){
                    final String myresponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(myresponse);
                        }
                    });
                }
            }
        });


    }
}
