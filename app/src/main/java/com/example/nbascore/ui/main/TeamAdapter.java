package com.example.nbascore.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nbascore.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team> {

    ArrayList<Team> list;
    private Context mContext;
    public TeamAdapter(Context context, ArrayList<Team> list) {
        super(context, -1, list);
        this.list = list; this.mContext =context;
    }

    void setList(ArrayList<Team> list)
    {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    // override other abstract methods here



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Team getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
     //   if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.list_item, container, false);
    //    }
        Team item = getItem(position);
        ((TextView) convertView.findViewById(R.id.team_rank))
                .setText(item.getRank());
        ((TextView) convertView.findViewById(R.id.team_name))
                .setText(item.getName());
        ((TextView) convertView.findViewById(R.id.team_status))
                .setText(item.getWin()+"-"+item.getLoss());
        return convertView;
    }

}
