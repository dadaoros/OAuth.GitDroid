package com.example.root.oauthgithub;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 6/01/15.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    ImageView imgImagen;
    ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub
        Log.i("doInBackground", "Entra en doInBackground");
        String url = params[0];
        Bitmap imagen = descargarImagen(url);
        return imagen;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        imgImagen.setImageBitmap(result);
    }
    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }

    public void setImageView(ImageView imageView) {
        imgImagen=imageView;
    }
}
