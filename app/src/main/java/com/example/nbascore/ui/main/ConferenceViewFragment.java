package com.example.nbascore.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nbascore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConferenceViewFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    public GridView listview;

    public JSONObject teamlist;
    public  TeamAdapter teamAdapter ;
    public Context context;


    private void makeTeamList() {
        teamlist=new JSONObject();
        try {
            teamlist.put("1","Atlanta Hawks");
            teamlist.put("2","Boston Celtics");
            teamlist.put("4","Brooklyn Nets");
            teamlist.put("5","Charlotte Hornets");
            teamlist.put("6","Chicago Bulls");
            teamlist.put("7","Cleveland Cavaliers");
            teamlist.put("8","Dallas Mavericks");
            teamlist.put("9","Denver Nuggets");
            teamlist.put("10"," Detroit Pistons");
            teamlist.put("11","Golden State Warriors");
            teamlist.put("14","Houston Rockets");
            teamlist.put("15","Indiana Pacers");
            teamlist.put("16","LA Clippers");
            teamlist.put("17","Los Angeles Lakers");
            teamlist.put("19","Memphis Grizzlies");
            teamlist.put("20","Miami Heat");
            teamlist.put("21","Milwaukee Bucks");
            teamlist.put("22","Minnesota Timberwolves");
            teamlist.put("23","New Orleans Pelicans");
            teamlist.put("24","New York Knicks");
            teamlist.put("25","Oklahoma City Thunder");
            teamlist.put("26","Orlando Magic");
            teamlist.put("27","Philadelphia 76ers");
            teamlist.put("28","Phoenix Suns");
            teamlist.put("29","Trail Blazers");
            teamlist.put("30","Sacramento Kings");
            teamlist.put("31","San Antonio Spurs");
            teamlist.put("38","Toronto Raptors");
            teamlist.put("40","Utah Jazz");
            teamlist.put("41","Washington Wizards");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    int type=1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_object, container, false);
//        listview = view.findViewById(R.id.listview);
        listview = (GridView) view.findViewById(R.id.listview);
        makeTeamList();
        Bundle args = getArguments();
        type = args.getInt(ARG_OBJECT);
      //  runThread();
        GetlistContact(type);
        context = this.getContext();
        teamAdapter = new TeamAdapter(getContext(),new ArrayList<Team>());
        listview.setAdapter(teamAdapter);
        return view;
    }


    public void GetlistContact(int type){

        final ArrayList<Team> teamlistResult = new ArrayList<Team>();
        final ArrayList<JSONObject> teamRankarray = new ArrayList<JSONObject>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-nba-v1.p.rapidapi.com/standings/standard/2019/conference/"+(type==1?"east":"west"))
                .get()
                .addHeader("x-rapidapi-host", "api-nba-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "36ad8130famshab740c067aba105p16fc2cjsn116da49f8e86")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure( Call call,  IOException e) {

            }

            @Override
            public void onResponse( Call call,  Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    try {
                        JSONObject returnData = new JSONObject(responseStr);
                        JSONObject apiData = returnData.getJSONObject("api");
                        JSONArray resultArray = apiData.getJSONArray("standings");
                        for (int i=0;i<resultArray.length();i++)
                        {
                            JSONObject item = resultArray.getJSONObject(i);

                            JSONObject newItem = new JSONObject();
                            newItem.put("teamid",item.getString("teamId"));
                            newItem.put("name", teamlist.getString(item.getString("teamId")));
                            JSONObject confData = item.getJSONObject("conference");
                            newItem.put("rank",confData.getInt("rank"));
                            newItem.put("win",item.getInt("win"));
                            newItem.put("loss",item.getInt("loss"));
                            teamRankarray.add(newItem);
                        }

                        Collections.sort(teamRankarray, new Comparator<JSONObject>() {
                            @Override
                            public int compare(JSONObject lhs, JSONObject rhs) {
                                try {

                                    if (lhs.getInt("rank") > rhs.getInt("rank")) {
                                        return 1;
                                    }
                                    else if (lhs.getInt("rank") < rhs.getInt("rank")) {
                                        return -1;
                                    }
                                    else {
                                        return 0;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        for (int i = 0; i<teamRankarray.size();i++) {
                            JSONObject obj = teamRankarray.get(i);
                            try {
                                Team team = new Team(obj.getString("teamid"),obj.getString("name"),obj.getString("rank"),obj.getString("win"),obj.getString("loss"));
                                teamlistResult.add(team);
                            }catch (Exception e) {
                                System.err.println(e);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                teamAdapter.setList(teamlistResult);
                                teamAdapter.notifyDataSetChanged();
                            }
                        });
                        System.out.println(teamRankarray);
                    }
                    catch (Exception e)
                    {

                    }



                } else {
                    // Request not successful
                }
            }
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        ((TextView) view.findViewById(android.R.id.text1))
//                .setText(Integer.toString(args.getInt(ARG_OBJECT)));



    }
}