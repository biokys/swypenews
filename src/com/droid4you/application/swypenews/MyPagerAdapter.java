package com.droid4you.application.swypenews;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ProgressBar;

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

    private ViewPager mViewPager;

    private SparseArray<WebView> mWebViewContainer;

    private int mCurrentPosition;


    public MyPagerAdapter(Context context, ViewPager viewPager, List<ServerObject> serverList) {

        this.mContext = context;
        this.serverList = serverList;
        this.mViewPager = viewPager;
        mWebViewContainer = new SparseArray<WebView>();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int lPosition;
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {

                lPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                if (i == ViewPager.SCROLL_STATE_IDLE) {

                    mCurrentPosition = lPosition;

                }
            }
        });
    }

    public int getCurrentPosition() {

        return mCurrentPosition;
    }

    public WebView getCurrentWebView() {

        return mWebViewContainer.get(mCurrentPosition);
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

        mWebViewContainer.delete(position);
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getWebView(position);
        mWebViewContainer.put(position, (WebView)view);
        container.addView(view);

        return view;
    }

    private View getWebView(int position) {

        //final ProgressBar pb = new ProgressBar(mContext);
        //pb.setIndeterminate(true);
        //pb.setVisibility(View.VISIBLE);
        WebView wv = new WebView(mContext);
        //wv.setVisibility(View.GONE);

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                //pb.setVisibility(View.GONE);
                //wv.setVisibility(View.VISIBLE);

                super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        wv.loadUrl(serverList.get(position % serverList.size()).getUrl());
        return wv;
    }


}
