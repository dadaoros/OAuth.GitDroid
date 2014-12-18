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
import android.widget.ArrayAdapter;
import Models.Repo;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListReposFragment extends Fragment {

    //TODO: remover implement Onclick
    private ArrayAdapter<Repo> listAdapter;

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
        Log.w("Estado", "Working");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.w("Estado", "Suspendido");
    }
    public void setListAdapter(ArrayAdapter<Repo> listAdapter) {
        this.listAdapter = listAdapter;
    }
    private void initComponents(View v) {


    }

}