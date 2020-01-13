package com.example.nbascore;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
                if (response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    try {
                        final ArrayList<BetItem> items = ParseFunction(myresponse);
                        //String responseparsed = ParseFunction(myresponse);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String data = "";
                                for(BetItem bi: items) {
                                    data += String.format(
                                            "team one: %s\nteam two: %s\nodds one: %.2f\nodds two: %.2f\ndate: %s\n\n",
                                            bi.teamOne, bi.teamTwo,
                                            bi.teamOneOdds, bi.teamTwoOdds,
                                            bi.commenceTime.toString()
                                        );
                                }
                                mTextView.setText(data);
                            }
                        });
                    } catch(JSONException jsEx) {
                        Log.e(TAG, jsEx.getLocalizedMessage());
                        jsEx.printStackTrace();
                    }
                }
            }
        });


    }

    private ArrayList<BetItem> ParseFunction(String mResponse) throws JSONException {
        JSONObject jobject  = new JSONObject(mResponse);
        JSONArray jDatas = jobject.getJSONArray("data");
        ArrayList<BetItem> betItems = new ArrayList<>();
        for (int i = 0; i <  jDatas.length(); i++)  {
            JSONObject data = jDatas.getJSONObject(i);
            long time = data.getLong("commence_time");
            Date commenceTime = new Date(time * 1000L);
            JSONArray teams = data.getJSONArray("teams");
            String team1 = teams.getString(0);
            String team2 = teams.getString(1);
            JSONArray sites = data.getJSONArray("sites");
            JSONArray odds = sites.getJSONObject(0).getJSONObject("odds").getJSONArray("h2h");
            double team1Odds = odds.getDouble(0);
            double team2Odds = odds.getDouble(1);
            BetItem bi = new BetItem(commenceTime, team1, team2, team1Odds, team2Odds);
            Log.d(TAG, "added bet_item: " + bi.toString());
            betItems.add(bi);
        }
        return betItems;
    }

     class BetItem {
        public final Date commenceTime;
        public final String teamOne, teamTwo;
        public final double teamOneOdds, teamTwoOdds;

        public BetItem(Date cTime, String t1, String t2, double t1Odds, double t2Odds) {
            super();
            this.commenceTime = cTime;
            this.teamOne = t1;
            this.teamTwo = t2;
            this.teamOneOdds = t1Odds;
            this.teamTwoOdds = t2Odds;
        }

        @SuppressLint("DefaultLocale")
        @Override
         public String toString() {
            return  String.format(
                    "commenceTime: %s, team_one: %s, team_two: %s, odd_one: %f, odd_two: %f",
                    commenceTime.toString(),
                    teamOne,
                    teamTwo,
                    teamOneOdds,
                    teamTwoOdds
            );
        }
    }
}
