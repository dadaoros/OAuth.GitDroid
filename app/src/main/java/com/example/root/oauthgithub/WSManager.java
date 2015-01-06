package com.example.root.oauthgithub;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Models.Commit;
import Models.Profile;
import Models.Repo;


public class WSManager {
    private ProgressDialog pDialog;
    private AsyncHttpClient client;
    private Fragment f;

    RequestHandle requestHandle;
    public WSManager(Fragment f){
        client = new AsyncHttpClient();
        this.f=f;
    }

    public void loadProfile(RequestParams params){
        if(pDialog==null)pDialog= ((MainActivity)f.getActivity()).getPDialog();
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        requestHandle = client.get("https://api.github.com/user", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                JSONObject obj = null;
                Profile p;
                try {
                    obj = new JSONObject(response);
                    p = new Profile(obj.getString("name"), obj.getString("login"), obj.getString("avatar_url"),
                        obj.getString("url"), obj.getString("followers"), obj.getString("following"));
                    ((MainActivity)f.getActivity()).setProfile(p);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent =new Intent(GitReceiver.FILTER_NAME);
                intent.putExtra(GitReceiver.OPERATION_CODE,GitReceiver.UPDATE_PROFILE);
                intent.putExtra("response",response);
                f.getActivity().sendBroadcast(intent);
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
    public void loadRepos(RequestParams params){
        // Show Progress Dialog
        if(pDialog==null)pDialog= ((MainActivity)f.getActivity()).getPDialog();
        pDialog.show();
        client.get("https://api.github.com/user/repos",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                Intent intent =new Intent(GitReceiver.FILTER_NAME);
                intent.putExtra(GitReceiver.OPERATION_CODE,GitReceiver.UPDATE_REPOS);
                intent.putExtra("response",response);
                f.getActivity().sendBroadcast(intent);
                pDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                Log.w("Error - statuscode: "," "+statusCode);

                pDialog.hide();
            }

        });
    }
    public void loadCommits(RequestParams params, String commits_url){
        // Show Progress Dialog
        if(pDialog==null)pDialog= ((MainActivity)f.getActivity()).getPDialog();
        pDialog.show();
        client.get(commits_url,params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                Intent intent =new Intent(GitReceiver.FILTER_NAME);
                intent.putExtra(GitReceiver.OPERATION_CODE,GitReceiver.LOAD_COMMITS);
                intent.putExtra("response",response);
                f.getActivity().sendBroadcast(intent);

                pDialog.hide();
            }

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


    static List<Repo> crearListaRepos(JSONArray jsonArray) {
        List<Repo> repos=new ArrayList<Repo>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jRepo=null;
            Repo repo=null;
            String descripcion=" ",name=" ",url=" ",commits=" ";

            JSONObject arayPrueba;

            try {
                jRepo=(JSONObject)jsonArray.get(i);
                name=jRepo.optString("name");
                url=jRepo.getString("url");
                commits=jRepo.getString("commits_url");
                commits=commits.replace("{/sha}","");
                descripcion=jRepo.optString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(descripcion.isEmpty())descripcion="There's not description";
            repo=new Repo(name,descripcion,url,commits);
            try {
                repos.add(repo);
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
        return repos;
    }
    static List<Commit> crearListaCommits(JSONArray jsonArray) {
        List<Commit> commits=new ArrayList<Commit>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jCommit=null;
            Commit commit=null;
            String message=" ",author=" ",url=" ",date=" ";

            try {
                jCommit=(JSONObject)jsonArray.get(i);
                author=jCommit.optJSONObject("commit").optJSONObject("committer").optString("name");
                url=jCommit.getString("url");
                message=jCommit.optJSONObject("commit").optString("message");
                date=jCommit.optJSONObject("commit").optJSONObject("committer").optString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(message.isEmpty())message="There's no message";
            commit=new Commit(author,date,message,url);
            try {
                commits.add(commit);
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
        return commits;
    }
}
