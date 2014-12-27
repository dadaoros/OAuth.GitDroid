package com.example.root.oauthgithub;
/**
 * Created by root on 7/10/14.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Models.Profile;
import Models.Repo;

import static com.example.root.oauthgithub.WSManager.crearListaRepos;

/**
 * Created by alejandro on 5/2/14.
 */
public class GitReceiver extends BroadcastReceiver {
    public static final String FILTER_NAME = "git";
    public static final int UPDATE_PROFILE = 1;
    public static final int UPDATE_REPOS = 2;
    public static final int CONTACTO_ACTUALIZADO = 3;

    @Override
    public void onReceive(Context context, Intent intent) {

        int operacion = intent.getIntExtra("operacion", -1);
        switch (operacion) {
            case UPDATE_PROFILE:
                updateProfile(context,intent);
                break;
            case UPDATE_REPOS:
                updateRepos(context,intent);
                break;
            case CONTACTO_ACTUALIZADO:
            //TODO: Actualizar Contacto
                break;
        }
    }
    private void updateProfile(Context ctx,Intent intent){
        TextView name, login_user, url, fwng, fwrs;

        try {
            JSONObject obj = new JSONObject(intent.getStringExtra("response"));
            Profile p = new Profile(obj.getString("name"), obj.getString("login"), obj.getString("avatar_url"),
                    obj.getString("url"), obj.getString("followers"), obj.getString("following"));

            name = (TextView) ((Activity)ctx).findViewById(R.id.view_name);
            login_user = (TextView) ((Activity)ctx).findViewById(R.id.view_username);
            url = (TextView) ((Activity)ctx).findViewById(R.id.view_url);
            fwng = (TextView) ((Activity)ctx).findViewById(R.id.view_following);
            fwrs = (TextView) ((Activity)ctx).findViewById(R.id.view_followers);
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
    }

    private void updateRepos(Context ctx, Intent intent){
        ListView reposList=(ListView)((Activity)ctx).findViewById(R.id.fragment_list);
        try {
            JSONArray jsonArray = new JSONArray(intent.getStringExtra("response"));
            List<Repo> repos=crearListaRepos(jsonArray);
            ReposListAdapter reposAdapter = new ReposListAdapter(repos,((Activity)ctx));
            reposAdapter.notifyDataSetChanged();
            reposAdapter.setNotifyOnChange(true);
            if(reposList.getAdapter()==null){
                reposList.setAdapter(reposAdapter);
                reposAdapter.setNotifyOnChange(true);
            }else{
                reposAdapter.notifyDataSetChanged();
            }
            Log.d("test_element",reposList.getItemAtPosition(0).toString());

            Log.w("count repos: ", " " + reposList.getAdapter().getCount());


        } catch (JSONException e) {
            Log.w("excepcion: ",e.toString());
            e.printStackTrace();

        }
    }
}
