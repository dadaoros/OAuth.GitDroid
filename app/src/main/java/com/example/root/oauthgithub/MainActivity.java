package com.example.root.oauthgithub;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ProgressDialog;
import java.net.URLConnection;

import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.oauth.*;


public class MainActivity extends Activity implements OAuthCallback { // implement the OAuthCallback interface to get the right information

    ProgressDialog prgDialog;
	Button conectar;
    private Context context;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        final OAuth o = new OAuth(this);
        o.initialize("VrV3MM4lkUQPt6iqk5OwBrnVgE8"); // Initialize the oauth key

        conectar = (Button) findViewById(R.id.submit);

        conectar.setOnClickListener(new View.OnClickListener() { // Listen the on click event
            @Override
            public void onClick(View v) 
            {
				o.popup("github", MainActivity.this); // Launch the pop up with the right provider & callback
            }
        });
        prgDialog = new ProgressDialog(this);

        
    }
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.github.com/user",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object

                    JSONObject obj = new JSONObject(response);
                    Log.w("estado",obj.getString("url"));

                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        Log.w("estado","estalisto");
                    }
                    // Else display error message
                    else{
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Log.w("excepcion: ",e.toString());
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{

                    Log.w("codigo: "," "+statusCode+" "+content);
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*
    **	Get the information
    **
    */
	public void onFinished(OAuthData data) {

		if ( ! data.status.equals("success")) {
            Toast.makeText(context, "error"+data.error,Toast.LENGTH_LONG).show();
		}else{
            Toast.makeText(context, "Ya estas logueado "+data.status,Toast.LENGTH_LONG).show();
            Log.w("data.request: ",data.request.toString());
            Log.w("data.token: ",data.token);
            RequestParams params = new RequestParams();
            params.put("access_token", data.token);
            invokeWS(params);
        }
		
		// You can access the tokens through data.token and data.secret

		// Let's skip the NetworkOnMainThreadException for the purpose of this sample.
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

		// To make an authenticated request, you can implement OAuthRequest with your prefered way.
		// Here, we use an URLConnection (HttpURLConnection) but you can use any library.
		data.http(data.provider.equals("github") ? "/user" : "/1.1/account/verify_credentials.json", new OAuthRequest() {
			private URL url;
			private URLConnection con;
			
			@Override
			public void onSetURL(String _url) {
				try {
					url = new URL(_url);
					con = url.openConnection();
				} catch (Exception e) { e.printStackTrace(); }
			}
			
			@Override
			public void onSetHeader(String header, String value) {
				con.addRequestProperty(header, value);
			}
			
			@Override
			public void onReady() {
				try {
					BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder total = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
					    total.append(line);
					}
					JSONObject result = new JSONObject(total.toString());
                    Toast.makeText(context, "error: "+result.getString("name"),Toast.LENGTH_LONG).show();

				} catch (Exception e) { e.printStackTrace(); }
			}
			
			@Override
			public void onError(String message) {
                Toast.makeText(context, "error: "+message,Toast.LENGTH_LONG).show();
			}
		});
    }
}
