package com.example.root.oauthgithub;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import java.lang.ref.WeakReference;

import Models.Profile;


public class MainActivity extends ActionBarActivity {
    GitReceiver receiver;
    private AccountManager mAccountManager;
    private String token;
    private ProgressDialog pDialog;
    Bundle savedInstanceState;
    private ListReposFragment reposFragment;
    private SlidingTabsBasicFragment fragment;
    private Profile profile;
    private static WeakReference<MainActivity> wrActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrActivity = new WeakReference<MainActivity>(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.savedInstanceState=savedInstanceState;
        mAccountManager = AccountManager.get(this);

        getAccount(GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS,false);

    }
    public void initComponents(){
        if (savedInstanceState == null) {
            FragmentTransaction transaction = wrActivity.get().getSupportFragmentManager().beginTransaction();
            fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
        if(pDialog!=null)pDialog.dismiss();
        unregisterReceiver(receiver);
    }
    private void addNewAccount(String accountType, String authTokenType) {

        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    Log.d("msg","Account was created");
                    getAccount(GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS,false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("msg",e.toString());
                }
            }
        }, null);
    }
    public void getAccount(final String authTokenType, final boolean invalidate) {
        final Account availableAccounts[] = mAccountManager.getAccountsByType(LoginActivity.ACCOUNT_TYPE);

        if (availableAccounts.length == 0) {
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
            addNewAccount(LoginActivity.ACCOUNT_TYPE, GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS);
        } else {
            String name;
            name= availableAccounts[0].name;
            if(invalidate) {
                invalidateAuthToken(availableAccounts[0], authTokenType);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }else
                getExistingAccountAuthToken(availableAccounts[0], authTokenType);
        }
    }
    private void invalidateAuthToken(final Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null,null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    mAccountManager.removeAccount(account,null,null);
                    Log.d(account.name , " invalidated");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error",e.getMessage());
                }
            }
        }).start();
    }

    private void getExistingAccountAuthToken(Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();

                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    token=authtoken;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error ",e.getMessage());
                }
                WSManager wsManager=new WSManager(getActivity());
                RequestParams params = new RequestParams();
                params.put("access_token", token);
                if(token!=null)initComponents();
            }
        }).start();
    }
    private MainActivity getActivity(){
        return this;
    }
    public ListReposFragment getReposFragment() {
        if(reposFragment==null)
            reposFragment=new ListReposFragment();
        return reposFragment;
    }
    public ProgressDialog getPDialog(){
        if(pDialog==null)pDialog=new ProgressDialog(this);
        return pDialog;
    }
    public String getToken(){
        return token;
    }

    public Profile getProfile() {
        return profile;
    }
    public void setTokenNull(){token=null;}
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
