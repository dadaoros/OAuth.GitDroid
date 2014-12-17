package com.example.root.oauthgithub;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Models.Profile;

/**
 * Created by root on 15/12/14.
 */
public class WSManager {
    private ProgressDialog pDialog;
    private AsyncHttpClient client;
    private Fragment f;
    public WSManager(){
        client = new AsyncHttpClient();
    }

    public void loadProfile(RequestParams params){
        // Show Progress Dialog
        pDialog= new ProgressDialog(f.getActivity());
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        client.get("https://api.github.com/user",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                TextView login,name,login_user,url,fwng,fwrs;
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    Profile p =new Profile(obj.getString("name"),obj.getString("login"), obj.getString("avatar_url"),
                                            obj.getString("url"),obj.getString("followers"),obj.getString("following"));
                    login = (TextView) f.getActivity().findViewById(R.id.login_name);
                    name = (TextView) f.getActivity().findViewById(R.id.view_name);
                    login_user = (TextView) f.getActivity().findViewById(R.id.view_username);
                    url = (TextView) f.getActivity().findViewById(R.id.view_url);
                    fwng = (TextView) f.getActivity().findViewById(R.id.view_following);
                    fwrs = (TextView) f.getActivity().findViewById(R.id.view_followers);
                    login.setText(p.getLogin());
                    name.setText(p.getName());
                    login_user.setText(p.getLogin());
                    url.setText(p.getUrl());
                    fwng.setText(p.getFollowing());
                    fwrs.setText(p.getFollowers());
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Log.w("excepcion: ",e.toString());
                    e.printStackTrace();

                }
                pDialog.hide();
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                Log.w("Error - statuscode: "," "+statusCode);
                /*
                if(statusCode == 404)
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                else
                if(statusCode == 500)
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                else   // When Http response code other than 404, 500
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                */
                pDialog.hide();
            }

        });
    }
    public void loadRepos(RequestParams params){
        // Show Progress Dialog
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        client.get("https://api.github.com/user/repos",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {

                try {
                    // JSON Object
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject repo=(JSONObject)jsonArray.get(1);

                    Log.w("name", repo.getString("name"));
                    Log.w("Descr", repo.getString("description"));
                    Log.w("url", repo.getString("url"));
                    Log.w("url", repo.getString("git_commits_url"));


                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Log.w("excepcion: ",e.toString());
                    e.printStackTrace();

                }
                pDialog.hide();
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                Log.w("Error - statuscode: "," "+statusCode);

                pDialog.hide();
            }

        });
    }
    public void setpDialog(ProgressDialog pDialog){
        this.pDialog=pDialog;
    }
    public void setContext(Fragment f){
        this.f=f;
    }
}
