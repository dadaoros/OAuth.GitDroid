package com.example.root.oauthgithub;


import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.oauth.*;


public class LoginActivity extends Activity implements OAuthCallback { // implement the OAuthCallback interface to get the right information


	private Context context;
    private Intent intent;
    private Button conectar;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        initComponents();
    }
    public void initComponents(){


        final OAuth o = new OAuth(this);

        o.initialize("VrV3MM4lkUQPt6iqk5OwBrnVgE8"); // Initialize the oauth key

        conectar = (Button) findViewById(R.id.submit);
        conectar.setOnClickListener(new View.OnClickListener() { // Listen the on click event
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
            intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("token", data.token);
            startActivity(intent);
        }
    }
    private void desplegarInicio(){

    }

    /*
    **	Get the information
    **
    */

}
