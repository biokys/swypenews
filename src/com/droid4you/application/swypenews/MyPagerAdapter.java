package com.droid4you.application.swypenews;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: muller10
 * Date: 8/3/12
 * Time: 4:57 PM
 */
public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<ServerObject> serverList;

    public static class BaseLayoutObject {

        View header;

        View content;

        View footer;
    }



    public MyPagerAdapter(Context context, List<ServerObject> serverList) {

        this.mContext = context;
        this.serverList = serverList;
    }

    @Override
    public int getCount() {
        return serverList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return serverList.get(position).getName();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getWebView(position);
        container.addView(view);

        return view;
    }

    View getWebView(int position) {

        WebView wv = new WebView(mContext);

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {


                super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        wv.loadUrl(serverList.get(position % serverList.size()).getUrl());
        return wv;
    }


}
