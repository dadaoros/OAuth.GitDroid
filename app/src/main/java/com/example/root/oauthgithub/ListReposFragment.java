package com.example.root.oauthgithub;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class ListReposFragment extends Fragment {

    private ArrayAdapter<Repo> listAdapter;
    private ListView listView;
    public ListReposFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_list_repos, container, false);
        initComponents(view);
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
    public void setListAdapter(ArrayAdapter<Repo> listAdapter) {
        this.listAdapter = listAdapter;
    }
    private void initComponents(View v) {
        RequestParams params = new RequestParams();
        WSManager manager=new WSManager(this);
        params.put("access_token",((MainActivity)getActivity()).getToken());
        manager.loadRepos(params);


    }

}