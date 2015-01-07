package com.example.root.oauthgithub;
/**
 * Created by root on 7/10/14.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import Models.Commit;
import Models.Profile;
import Models.Repo;

import static com.example.root.oauthgithub.WSManager.crearListaCommits;
import static com.example.root.oauthgithub.WSManager.crearListaRepos;

/**
 * Created by alejandro on 5/2/14.
 */
public class GitReceiver extends BroadcastReceiver {
    public static final String FILTER_NAME = "git";
    public static final int UPDATE_PROFILE = 1;
    public static final int UPDATE_REPOS = 2;
    public static final int LOAD_COMMITS = 3;
    public static final String OPERATION_CODE="operacion";

    @Override
    public void onReceive(Context context, Intent intent) {

        int operacion = intent.getIntExtra(OPERATION_CODE, -1);
        switch (operacion) {
            case UPDATE_PROFILE:
                updateProfile(context,intent);
                break;
            case UPDATE_REPOS:
                updateRepos(context,intent);
                break;
            case LOAD_COMMITS:
                loadCommits(context, intent);
                break;
        }
    }

    private void loadCommits(Context ctx, Intent intent) {
        ListView commitList=(ListView)((Activity)ctx).findViewById(R.id.fragment_list_c);
        try {
            JSONArray jsonArray = new JSONArray(intent.getStringExtra("response"));
            List<Commit> commits=crearListaCommits(jsonArray);

            CommitListAdapter commitAdapter = new CommitListAdapter(commits,((Activity)ctx));
            commitAdapter.notifyDataSetChanged();
            commitAdapter.setNotifyOnChange(true);
            if(commitList.getAdapter()==null){
                commitList.setAdapter(commitAdapter);
                commitAdapter.setNotifyOnChange(true);
            }else{
                commitAdapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            Log.w("excepcion: ",e.toString());
            e.printStackTrace();

        }
    }

    private void updateProfile(Context ctx,Intent intent) {
        TextView name, login_user, url, fwng, fwrs;
        ImageView imageView;
        String imgUrl=null;
        try {
            JSONObject obj = new JSONObject(intent.getStringExtra("response"));
            Profile p = new Profile(obj.getString("name"), obj.getString("login"), obj.getString("avatar_url"),
                    obj.getString("url"), obj.getString("followers"), obj.getString("following"));

            name = (TextView) ((Activity) ctx).findViewById(R.id.view_name);
            login_user = (TextView) ((Activity) ctx).findViewById(R.id.view_username);
            url = (TextView) ((Activity) ctx).findViewById(R.id.view_url);
            fwng = (TextView) ((Activity) ctx).findViewById(R.id.view_following);
            fwrs = (TextView) ((Activity) ctx).findViewById(R.id.view_followers);
            name.setText(p.getName());
            login_user.setText(p.getLogin());
            url.setText(p.getUrl());
            fwng.setText(p.getFollowing());
            fwrs.setText(p.getFollowers());
            imgUrl=p.getAvatar_url();

        } catch (JSONException e) {
            //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            Log.e("excepcion: ", e.toString());
            e.printStackTrace();

        }
        if (imgUrl!=null) {
            imageView = (ImageView) ((Activity) ctx).findViewById(R.id.profile_image);
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.setImageView(imageView);
            imageLoader.execute(imgUrl);
            //imageView.getDrawable(); -> tomar la imagen descargada para reutilziarla en dado momento

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
                setListener(ctx,reposList);
            }else{
                reposAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            Log.w("excepcion: ",e.toString());
            e.printStackTrace();

        }
    }
    public void setListener(final Context ctx, final ListView reposList){

        reposList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Repo repo = (Repo) reposList.getAdapter().getItem(position);
                Bundle arguments = new Bundle();
                arguments.putString("repo_name",repo.getName());
                arguments.putString("commits_url", repo.getCommits_url());
                loadFragment(ctx, new ListCommitFragment(), arguments);
            }
        });
    }
    private void loadFragment(Context ctx,Fragment fragment,Bundle arguments) {
        fragment.setArguments(arguments);
        FragmentManager fManager = ((MainActivity)ctx).getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
