package com.example.root.oauthgithub;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import Models.Commit;
import Models.Repo;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListCommitFragment extends Fragment {
    View view;
    public ListCommitFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_list_commit, container, false);
        this.view=view;
        initComponents();
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onDetach(){
        super.onDetach();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    private void initComponents() {
        Bundle arguments=getArguments();

        RequestParams params = new RequestParams();
        WSManager manager=new WSManager(this);
        params.put("access_token", ((MainActivity) getActivity()).getToken());
        manager.loadCommits(params, (String) arguments.get("commits_url"));
        TextView title=(TextView)view.findViewById(R.id.repo_name_label);
        title.setText((String)arguments.get("repo_name")+"/Commits");
    }

}