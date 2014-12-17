package com.example.root.oauthgithub;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.Profile;

/**
 * Created by root on 16/12/14.
 */
public class SimplePagerAdapter extends PagerAdapter {
    Fragment f;
    private int[] imageResId={R.drawable.ic_action_important,R.drawable.ic_action_person,R.drawable.ic_action_storage};
    String token;
    WSManager manager;
    public SimplePagerAdapter(Fragment f,String token){
        this.f=f;
        this.token=token;
        manager=new WSManager();
    }
        /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return imageResId.length;
    }

    /**
     * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
     * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = f.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth()*3/2, image.getIntrinsicHeight()*3/2);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
    // END_INCLUDE (pageradapter_getpagetitle)

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view=null;
        if(position==1) {
            Profile profile;
            view = f.getActivity().getLayoutInflater().inflate(R.layout.profile_item,container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);
            RequestParams params = new RequestParams();
            params.put("access_token", token);
            manager.setContext(f);
            manager.loadProfile(params);

        }else{
            view = f.getActivity().getLayoutInflater().inflate(R.layout.repos_item,
                    container, false);
            container.addView(view);
            // Add the newly created View to the ViewPager
        }

        // Return the View
        return view;
    }

    /**
     * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
     * {@link android.view.View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}