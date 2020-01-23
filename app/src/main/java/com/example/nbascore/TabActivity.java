package com.example.nbascore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.nbascore.ui.main.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nbascore.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TabActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView textView;

    double team2Odds = 0;
    double team1Odds= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        fetchBetItem();// Onluy left set data to
//        textView = viewPager.findViewById(R.id.section_label);

//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                tab.getCustomView()
////                View v = sectionsPagerAdapter.getItem(0);
////                System.out.println("Changes in tab selsction "+txt.getText());
//
//                textView = viewPager.findViewById(R.id.section_label);
//                textView.setText("test1111");
//                textView.setText(Double.toString(bet));
//                System.out.println("Changes in tab selsction "+textView.getText());// First  show the resutl on fragment
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    public void setInit(){
        final TabAdapter sectionsPagerAdapter = new TabAdapter(getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        final TabLayout tabs = findViewById(R.id.tabs);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void fetchBetItem() {
        Button buttonswitch = findViewById(R.id.button_switch);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://odds.p.rapidapi.com/v1/odds?sport=basketball_nba&region=us&mkt=%7Bmkt%7D")
                .get()
                .addHeader("x-rapidapi-host", "odds.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "36ad8130famshab740c067aba105p16fc2cjsn116da49f8e86")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    try {
                        final ArrayList<BetItem> items = ParseFunction(myresponse);
                        //String responseparsed = ParseFunction(myresponse);

                        TabActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String data = ""; // I will chagne into array. ok?

                                for (BetItem bi : items) {
                                    data += String.format(
                                            "team one: %s\nteam two: %s\nodds one: %.2f\nodds two: %.2f\ndate: %s\n\n",
                                            bi.teamOne, bi.teamTwo,
                                            bi.teamOneOdds, bi.teamTwoOdds,
                                            bi.commenceTime.toString()
                                    );
                                }
                                Common.getInstance().setScoreData(data);
                                setInit();
                                //textView.setText(data);// To show the data on fragement of tabs , we should create the adapter file
//TextView mTextView = viewPager.findViewById(R.id.section_label);
//                                textView.setText(data);

                            }
                        });
                    } catch (JSONException jsEx) {
//                        Log.e(TAG, jsEx.getLocalizedMessage());
                        jsEx.printStackTrace();
                    }
                }
            }
        });


    }

    private ArrayList<BetItem> ParseFunction(String mResponse) throws JSONException {
        JSONObject jobject = new JSONObject(mResponse);
        JSONArray jDatas = jobject.getJSONArray("data");
        ArrayList<BetItem> betItems = new ArrayList<>();
        for (int i = 0; i < jDatas.length(); i++) {
            JSONObject data = jDatas.getJSONObject(i);
            long time = data.getLong("commence_time");
            Date commenceTime = new Date(time * 1000L);
            JSONArray teams = data.getJSONArray("teams");
            String team1 = teams.getString(0);
            JSONArray sites = data.getJSONArray("sites");
            Log.d("team", team1);
            String team2 = teams.getString(1);//I want to know about the result json string from api.

            if (data.getJSONArray("sites").length() > 0) {
                JSONArray odds = sites.getJSONObject(0).getJSONObject("odds").getJSONArray("h2h");
                team1Odds = odds.getDouble(0);
                team2Odds = odds.getDouble(1);
            }

            BetItem bi = new BetItem(commenceTime, team1, team2, team1Odds, team2Odds);
//            Log.d(TAG, "added bet_item: " + bi.toString());
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
            return String.format(
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