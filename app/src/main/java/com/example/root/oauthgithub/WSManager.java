package com.example.root.oauthgithub;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Profile;
import Models.Repo;


public class WSManager {
    private ProgressDialog pDialog;
    private AsyncHttpClient client;
    private Fragment f;
    private ReposListAdapter reposAdapter;
    RequestHandle requestHandle;
    public WSManager(){
        client = new AsyncHttpClient();
    }

    public void loadProfile(RequestParams params){
        if(pDialog==null)pDialog= new ProgressDialog(f.getActivity());
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        requestHandle = client.get("https://api.github.com/user", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                TextView name, login_user, url, fwng, fwrs;
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    Profile p = new Profile(obj.getString("name"), obj.getString("login"), obj.getString("avatar_url"),
                            obj.getString("url"), obj.getString("followers"), obj.getString("following"));

                    name = (TextView) f.getActivity().findViewById(R.id.view_name);
                    login_user = (TextView) f.getActivity().findViewById(R.id.view_username);
                    url = (TextView) f.getActivity().findViewById(R.id.view_url);
                    fwng = (TextView) f.getActivity().findViewById(R.id.view_following);
                    fwrs = (TextView) f.getActivity().findViewById(R.id.view_followers);
                    name.setText(p.getName());
                    login_user.setText(p.getLogin());
                    url.setText(p.getUrl());
                    fwng.setText(p.getFollowing());
                    fwrs.setText(p.getFollowers());
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Log.w("excepcion: ", e.toString());
                    e.printStackTrace();

                }
                pDialog.hide();
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                Log.w("Error - statuscode: ", " " + statusCode);

                pDialog.hide();
            }

        });
    }
    public void loadRepos(RequestParams params, final ListReposFragment reposFragment){
        // Show Progress Dialog
        if(pDialog==null) pDialog= new ProgressDialog(f.getActivity());
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        client.get("https://api.github.com/user/repos",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                ListView reposList=(ListView)f.getActivity().findViewById(R.id.fragment_list);
                try {
                    // JSON Object
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Repo> repos=crearArrayRepos(jsonArray);
                    reposAdapter = new ReposListAdapter(repos,f.getActivity());
                    //reposFragment.setListAdapter(reposAdapter);
                    reposAdapter.notifyDataSetChanged();
                    reposAdapter.setNotifyOnChange(true);
                    reposList.setAdapter(reposAdapter);
                    reposAdapter.notifyDataSetChanged();
                    reposAdapter.setNotifyOnChange(true);
                    Log.w("count repos: ", " " + reposAdapter.getCount());


                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Log.w("excepcion: ",e.toString());
                    e.printStackTrace();

                }
                pDialog.hide();
            }

            private ArrayList<Repo> crearArrayRepos(JSONArray jsonArray) {
                ArrayList<Repo> repos=new ArrayList<Repo>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jRepo=null;
                    Repo repo=null;
                    String descripcion=" ",name=" ",url=" ",commits=" ";
                    try {
                        jRepo=(JSONObject)jsonArray.get(i);
                        name=jRepo.optString("name");
                        url=jRepo.getString("url");
                        commits=jRepo.getString("commits_url");
                        descripcion=jRepo.optString("descripcion");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    repo=new Repo(name,descripcion,url,commits);
                    try {
                        repos.add(repo);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
                return repos;
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
