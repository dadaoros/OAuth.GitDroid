package com.example.root.oauthgithub;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Models.Repo;

/**
 * Created by root on 6/10/14.
 */
public class ReposListAdapter extends ArrayAdapter {
    List<Repo> repos;
    Activity ctxt;
    public ReposListAdapter(List<Repo> repos, Activity ctxt) {
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

    public List<Repo> getReposList(){
        return repos;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = ctxt.getLayoutInflater().inflate(R.layout.listview_item, null);
        }
        TextView repoName= (TextView) view.findViewById(R.id.title);
        TextView repoDesc= (TextView) view.findViewById(R.id.repo_description);
        TextView repoUrl= (TextView) view.findViewById(R.id.repo_url_view);

        Repo repo=repos.get(position);
        repoName.setText(repo.getName());
        repoDesc.setText(repo.getDescripcion());
        repoUrl.setText(repo.getUrl());

        return view;
    }
}
