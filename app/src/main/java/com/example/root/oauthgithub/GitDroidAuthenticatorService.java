package com.example.root.oauthgithub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by root on 27/12/14.
 */
public class GitDroidAuthenticatorService extends Service{
    @Override
    public IBinder onBind(Intent intent) {

        GitAuthenticator authenticator = new GitAuthenticator(this);
        return authenticator.getIBinder();
    }
}
