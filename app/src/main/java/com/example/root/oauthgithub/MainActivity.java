package com.example.root.oauthgithub;


import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.loopj.android.http.RequestParams;



public class MainActivity extends FragmentActivity {
    GitReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString("access_token", getIntent().getExtras().getString("token"));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            fragment.setArguments(arguments);
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onResume(){
        super.onResume();
        receiver=new GitReceiver();
        registerReceiver(receiver,new IntentFilter(GitReceiver.FILTER_NAME));
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(receiver);
    }
}
