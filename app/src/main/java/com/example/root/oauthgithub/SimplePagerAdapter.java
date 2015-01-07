package com.example.root.oauthgithub;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;

/**
 * Created by root on 16/12/14.
 */
public class SimplePagerAdapter extends PagerAdapter {
    Fragment f;
    private int[] imageResId={R.drawable.ic_action_person,R.drawable.ic_action_storage,R.drawable.ic_action_important};
    String token;
    WSManager manager;
    public SimplePagerAdapter(Fragment f,String token){
        this.f=f;
        this.token=token;
        manager=new WSManager(f);
    }
        /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return imageResId.length-1;
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
        RequestParams params = new RequestParams();
        switch (position) {
            case 0:
                view = f.getActivity().getLayoutInflater().inflate(R.layout.profile_item, container, false);
                // Add the newly created View to the ViewPager
                container.addView(view);

                params.put("access_token", token);
                manager.loadProfile(params);

                break;
            case 1:

                    view = f.getActivity().getLayoutInflater().inflate(R.layout.container,
                            container, false);
                    container.addView(view);

                loadFragment(((MainActivity)f.getActivity()).getReposFragment());
                break;


            // Return the View
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
        f.onDestroy();
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fManager = f.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction;
        try {
            transaction = fManager.beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }catch(IllegalStateException e){
            Log.e("error",e.getMessage());
        }
    }



}
