package com.example.root.oauthgithub;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import Models.Commit;
import Models.Repo;

/**
 * Created by root on 6/10/14.
 */
public class CommitListAdapter extends ArrayAdapter {
    List<Commit> commits;
    Activity ctxt;
    public CommitListAdapter(List<Commit> commits, Activity ctxt) {
        super(ctxt,R.layout.listview_commititem,commits);
        this.ctxt=ctxt;
        this.commits=commits;
    }

    @Override
    public int getCount() {
        return commits.size();
    }

    @Override
    public Object getItem(int position) {
        return commits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Commit> getReposList(){
        return commits;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = ctxt.getLayoutInflater().inflate(R.layout.listview_commititem, null);
        }
        TextView commitDate= (TextView) view.findViewById(R.id.commit_date);
        TextView commitAuthor= (TextView) view.findViewById(R.id.commit_author);
        TextView commitMessage= (TextView) view.findViewById(R.id.commit_message);
        TextView commitUrl=(TextView)view.findViewById(R.id.commit_url_view);
        Commit commit=commits.get(position);
        commitAuthor.setText(commit.getAuthor());
        commitMessage.setText(commit.getMessage());
        commitUrl.setText(commit.getUrl());
        commitDate.setText(commit.getDate());

        return view;
    }
}
