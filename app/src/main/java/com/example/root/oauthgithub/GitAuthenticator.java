package com.example.root.oauthgithub;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

/**
 * Created by root on 27/12/14.
 */
public class GitAuthenticator extends AbstractAccountAuthenticator {
    Context mContext;
    public GitAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("GitDroid",  "> addAccount");

        final Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(GitStatic.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(GitStatic.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(GitStatic.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }



    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }else
            return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return GitStatic.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (GitStatic.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return GitStatic.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

}
