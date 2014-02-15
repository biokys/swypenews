package com.droid4you.application.swypenews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
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

    private ProgressBar progressBar;

    private int mCurrentPosition;


    public MyPagerAdapter(Context context, ViewPager viewPager, List<ServerObject> serverList, ProgressBar progressBar) {

        this.mContext = context;
        this.serverList = serverList;
        this.mViewPager = viewPager;
        this.progressBar = progressBar;
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

        //mWebViewContainer.delete(position);
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        WebView webView = mWebViewContainer.get(position);
        if (webView == null) {

            webView = (WebView)getWebView(position);
            mWebViewContainer.put(position, webView);
        }
        container.addView(webView);

        return webView;
    }

    private View getWebView(int position) {

        //final ProgressBar pb = new ProgressBar(mContext);
        //pb.setIndeterminate(true);
        //pb.setVisibility(View.VISIBLE);
        WebView wv = new WebView(mContext);
        //wv.setVisibility(View.GONE);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }});
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress < 100) {
                    ((Activity) mContext).findViewById(R.id.progress1).setVisibility(View.VISIBLE);
                }
                if (newProgress == 100) {
                    ((Activity) mContext).findViewById(R.id.progress1).setVisibility(View.INVISIBLE);
                }

            }

           /* @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ((Activity) mContext).findViewById(R.id.progress1).setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //pb.setVisibility(View.GONE);
                //wv.setVisibility(View.VISIBLE);

                super.onPageFinished(view, url);    //To change body of overridden methods use File | Settings | File Templates.
                ((Activity) mContext).findViewById(R.id.progress1).setVisibility(View.INVISIBLE);
            }*/
        });

        wv.loadUrl(serverList.get(position % serverList.size()).getUrl());
        return wv;
    }


}
