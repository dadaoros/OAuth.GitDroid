package com.example.root.oauthgithub;


import android.accounts.AccountAuthenticatorActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import com.example.root.oauthgithub.GitStatic.*;
import io.oauth.*;


public class LoginActivity extends AccountAuthenticatorActivity implements OAuthCallback { // implement the OAuthCallback interface to get the right information
    public static final String ACCOUNT_TYPE = "com.example.root.oauthgithub";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "GitDroid";

    /**
     * Auth token types
     */


    private AccountManager mAccountManager;
	private Context context;
    private Intent intent;
    private Button conectarBtn;
    private boolean mInvalidate;
    private AlertDialog mAlertDialog;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
    }
    public void initComponents(){
        context=this;
        mAccountManager = AccountManager.get(this);

        final OAuth o = new OAuth(this);

        o.initialize(GitStatic.CLIENT_KEY); // Initialize the oauth key

        conectarBtn = (Button) findViewById(R.id.submit);
        conectarBtn.setOnClickListener(new View.OnClickListener() { // Listen the on click event
            @Override
            public void onClick(View v) {
                o.popup(GitStatic.AUTH_POPUP_PROVIDER, LoginActivity.this); // Launch the pop up with the right provider & callback
            }
        });

    }

    public void onFinished(OAuthData data) {

        if (!data.status.equals("success")) {
            Toast.makeText(context, "error" + data.error, Toast.LENGTH_LONG).show();
        } else {
            final Account account = new Account(GitStatic.ACCOUNT_USERNAME, ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, GitStatic.DEFAULT_USER_PASS,null);
            mAccountManager.setAuthToken(account, GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS, data.token);


            final Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_AUTHTOKEN, data.token);
            Intent intent = new Intent();
            intent.putExtras(bundle);

            setAccountAuthenticatorResult(bundle);
            setResult(RESULT_OK, intent);
            finish();

        }
    }





}
