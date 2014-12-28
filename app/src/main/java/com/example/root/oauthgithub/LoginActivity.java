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
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Udinic account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Udinic account";



    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

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
        //TODO: variabe quemada
        //TODO: variable quemada

        showAccountPicker(AUTHTOKEN_TYPE_FULL_ACCESS,false);


        final OAuth o = new OAuth(this);

        o.initialize("VrV3MM4lkUQPt6iqk5OwBrnVgE8"); // Initialize the oauth key

        conectarBtn = (Button) findViewById(R.id.submit);
        conectarBtn.setOnClickListener(new View.OnClickListener() { // Listen the on click event
            @Override
            public void onClick(View v) {
                o.popup("github", LoginActivity.this); // Launch the pop up with the right provider & callback
            }
        });

    }

    public void onFinished(OAuthData data) {

        if (!data.status.equals("success")) {
            Toast.makeText(context, "error" + data.error, Toast.LENGTH_LONG).show();
        } else {
            //addNewAccount(ACCOUNT_TYPE, AUTHTOKEN_TYPE_FULL_ACCESS);
            final Account account = new Account("dadaoros", ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, "555555",null);
            mAccountManager.setAuthToken(account, AUTHTOKEN_TYPE_FULL_ACCESS, data.token);

            intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("token", data.token);

            startActivity(intent);

        }
    }
    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    Log.d("msg","Account was created");
                    Log.d("udinic", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("msg",e.getMessage());
                }
            }
        }, null);
    }

    private void showAccountPicker(final String authTokenType, final boolean invalidate) {
        mInvalidate = invalidate;

        final Account availableAccounts[] = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        if (availableAccounts.length == 0) {
            //TODO: retornar nulo para seguir el proceso de logueo
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
        } else {
            String name;
            name= availableAccounts[0].name;
            Log.d("Account_token", availableAccounts[0].toString());


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
                    Log.d("mensaje ",authtoken);
                    //TODO: rpobar si la token es valida y logueatse


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("error ",e.getMessage());
                }
            }
        }).start();
    }

}
