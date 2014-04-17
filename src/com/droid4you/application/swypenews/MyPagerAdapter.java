package com.droid4you.application.swypenews;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: muller10
 * Date: 8/3/12
 * Time: 4:57 PM
 */
public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<ServerObject> mServerList;

    private SparseArray<WebView> mWebViewContainer;

    private ProgressBar mProgressBar;

    private int mCurrentPosition;


    public MyPagerAdapter(Context context, ViewPager viewPager, List<ServerObject> serverList, ProgressBar progressBar) {

        this.mContext = context;
        this.mServerList = serverList;
        this.mProgressBar = progressBar;
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
        return mServerList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mServerList.get(position).getName();
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

        WebView webView = mWebViewContainer.get(position);
        if (webView == null) {

            webView = (WebView)getWebView(position);
            mWebViewContainer.put(position, webView);
        }
        container.addView(webView);

        return webView;
    }

    private View getWebView(int position) {

        WebView wv = new WebView(mContext);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }});
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress < 100) {
                    ((Activity) mContext).findViewById(R.id.progress).setVisibility(View.VISIBLE);
                }
                if (newProgress == 100) {
                    ((Activity) mContext).findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                }

            }
        });

        wv.loadUrl(mServerList.get(position % mServerList.size()).getUrl());
        return wv;
    }


}
