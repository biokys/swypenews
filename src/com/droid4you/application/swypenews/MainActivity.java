package com.droid4you.application.swypenews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private boolean mSelected[];
  private String mNames[];
  private MyPagerAdapter mAdapter;
  private ProgressBar mProgressBar;
  private ViewPager mPager;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    List<CountryObject> countryList = Helper.getCountries(this);
    List<CountryObject> countries = Helper.readSettings(this);

    mNames = new String[countryList.size()];
    mSelected = new boolean[countryList.size()];

    for (int i = 0; i < mNames.length; i++) {

      mNames[i] = countryList.get(i).getCountryName();
    }

    if (countries == null || countries.size() == 0) {

      String countryCode = Helper.getCountryCodeBasedOnCurrentCountry(this);
      if (countryCode == null || countryCode.length() == 0 || !countryList.contains(
          countryCode)) { //Fixed:ebotta

        showSettingsDialog();
      } else {

        List<CountryObject> countryObjectsForSave = new ArrayList<CountryObject>();
        countryObjectsForSave.add(Helper.getCountryObjectBasedOnCurrentCountry(this));
        Helper.saveSettings(this, countryObjectsForSave);
        showPages();
      }
    } else {

      showPages();
    }

  }

  private void showPages() {

    List<CountryObject> countries = Helper.readSettings(this);
    List<ServerObject> list = Helper.getServersForCountries(this, countries);

    for (int i = 0; i < mNames.length; i++) {

      for (int j = 0; j < countries.size(); j++) {

        if (mNames[i].equals(countries.get(j).getCountryName())) {

          mSelected[i] = true;
        }
      }
    }

    mPager = (ViewPager) findViewById(R.id.mypager);

    mProgressBar = (ProgressBar) findViewById(R.id.progress); //Send progressbar context to adapter

    mAdapter = new MyPagerAdapter(this, mPager, list, mProgressBar);
    mPager.setOffscreenPageLimit(3);
    mPager.setAdapter(mAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.menu_config:

        showSettingsDialog();
        return true;
      case R.id.menu_share:

        showShareDialog();
        return true;
      case R.id.menu_refresh:

        refresh();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showSettingsDialog() {

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.select_countries);

    builder.setMultiChoiceItems(mNames, mSelected,
        new DialogInterface.OnMultiChoiceClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i, boolean b) {
          }
        });

    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        boolean canSave = false;

        for (int loop = 0; loop < mSelected.length; loop++) {

          if (mSelected[loop]) {

            canSave = true;
          }
        }

        if (canSave) {

          saveSettings();
          dialogInterface.dismiss();
        } else {

          Toast.makeText(MainActivity.this, getString(R.string.choose_at_least_one),
              Toast.LENGTH_SHORT).show();
          AlertDialog alert = builder.create();
          alert.show();
        }
      }
    });

    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
      }
    });

    builder.setCancelable(
        true); //ebotta-fix:Resi problem s odskakovanim na prvni noviny pri potvrzeni stejneho
        // nastaveni ktere jiz bylo ulozeno drive

    AlertDialog alert = builder.create();
    alert.show();
  }

  private void saveSettings() {

    List<CountryObject> countryObjectsForSave = new ArrayList<CountryObject>();
    List<CountryObject> countryObjects = Helper.getCountries(this);

    for (int i = 0; i < mSelected.length; i++) {

      if (mSelected[i]) {

        for (CountryObject countryObject : countryObjects) {

          if (countryObject.getCountryName().equals(mNames[i])) {

            countryObjectsForSave.add(countryObject);
          }
        }
      }
    }
    Helper.saveSettings(this, countryObjectsForSave);
    showPages();
  }

  @Override
  public void onBackPressed() {

    if (mAdapter == null || mAdapter.getCurrentWebView() == null) {

      super.onBackPressed();
    }

    WebView webView = mAdapter.getCurrentWebView();
    if (webView.canGoBack()) {

      webView.goBack();
    } else {

      super.onBackPressed();
    }
  }

  private String getCurrentURL() {
    return mAdapter.getCurrentWebView().getUrl();
  }

  private void showShareDialog() {

    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    String url = this.getCurrentURL();
    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    intent.putExtra(Intent.EXTRA_TEXT, url);
    //send real url on tab; ebotta
    startActivity(Intent.createChooser(intent, getString(R.string.menu_share_with)));
  }

  private void refresh() {

    int currentItem = mPager.getCurrentItem();
    showPages();
    mPager.setCurrentItem(currentItem, false);
  }
}
