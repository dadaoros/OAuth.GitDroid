package com.example.root.oauthgithub;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Models.Repo;

/**
 * Created by root on 6/10/14.
 */
public class ReposListAdapter extends ArrayAdapter {
    ArrayList<Repo> repos;
    Activity ctxt;
    public ReposListAdapter(ArrayList<Repo> repos, Activity ctxt) {
        super(ctxt,R.layout.listview_item,repos);
        this.ctxt=ctxt;
        this.repos=repos;
    }

    @Override
    public int getCount() {
        return repos.size();
    }

    @Override
    public Object getItem(int position) {
        return repos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Repo> getReposList(){
        return repos;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = ctxt.getLayoutInflater().inflate(R.layout.listview_item, null);
        }
        TextView nombreLoteria= (TextView) view.findViewById(R.id.title);
        TextView numeroLoteria= (TextView) view.findViewById(R.id.l_number);
        TextView numeroSerie= (TextView) view.findViewById(R.id.l_series);

        Repo repo=repos.get(position);
        nombreLoteria.setText(repo.getName());
        numeroLoteria.setText("numero");
        numeroSerie.setText("serie");

        return view;
    }
}
