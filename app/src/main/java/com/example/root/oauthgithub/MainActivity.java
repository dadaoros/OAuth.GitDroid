package com.example.root.oauthgithub;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import Models.Profile;


public class MainActivity extends FragmentActivity {
    GitReceiver receiver;
    private AccountManager mAccountManager;
    private boolean mInvalidate;
    private String token;
    private ProgressDialog pDialog;
    Bundle savedInstanceState;
    private ListReposFragment reposFragment;
    private SlidingTabsBasicFragment fragment;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState=savedInstanceState;
        mAccountManager = AccountManager.get(this);

        getAccount(GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS,false);

    }
    public void initComponents(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(GitStatic.TOKEN, token);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new SlidingTabsBasicFragment();
            fragment.setArguments(arguments);
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
        pDialog.dismiss();
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
    private void getAccount(final String authTokenType, final boolean invalidate) {
        mInvalidate = invalidate;

        final Account availableAccounts[] = mAccountManager.getAccountsByType(LoginActivity.ACCOUNT_TYPE);

        if (availableAccounts.length == 0) {
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
            addNewAccount(LoginActivity.ACCOUNT_TYPE, GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS);
        } else {
            String name;
            name= availableAccounts[0].name;
            getExistingAccountAuthToken(availableAccounts[0], authTokenType);
        }
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
                    initComponents(savedInstanceState);
                    //TODO: probar si la token es valida
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("error ",e.getMessage());
                }
            }
        }).start();
    }

    public ListReposFragment getReposFragment() {
        if(reposFragment==null)
            reposFragment=new ListReposFragment();
        return reposFragment;
    }
    public SlidingTabsBasicFragment getSlidintabsBasicFragment(){
        return fragment;
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

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
