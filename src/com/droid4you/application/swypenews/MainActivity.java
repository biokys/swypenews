package com.droid4you.application.swypenews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private boolean selected[];

    private String names[];

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        List<CountryObject> countryList = Helper.getCountries(this);

        List<CountryObject> countries = Helper.readSettings(this);

        if (countries == null || countries.size() == 0) {

            List<CountryObject> countryObjectsForSave = new ArrayList<CountryObject>();
            countryObjectsForSave.add(Helper.getCountryObjectBasedOnCurrentCountry(this));
            Helper.saveSettings(this, countryObjectsForSave);
        }


        names = new String[countryList.size()];
        selected = new boolean[countryList.size()];

        for (int i = 0; i < names.length; i++) {

            names[i] = countryList.get(i).getCountryName();
        }

        showPages();
    }

    private void showPages() {

        List<CountryObject> countries = Helper.readSettings(this);
        List<ServerObject> list = Helper.getServersForCountries(this, countries);

        for (int i = 0; i < names.length; i++) {

            for (int j = 0; j < countries.size(); j++) {

                if (names[i].equals(countries.get(j).getCountryName())) {

                    selected[i] = true;
                }
            }
        }

        MyPagerAdapter adapter = new MyPagerAdapter(this, list);
        ViewPager myPager = (ViewPager) findViewById(R.id.mypager);

        myPager.setOffscreenPageLimit(3);
        myPager.setAdapter(adapter);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_countries);



        builder.setMultiChoiceItems(names, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                boolean canSave = false;

                for (int loop = 0; loop < selected.length; loop++) {

                    if (selected[loop]) {

                        canSave = true;
                    }
                }

                if (canSave) {

                    saveSettings();
                    dialogInterface.dismiss();
                } else {

                    Toast.makeText(MainActivity.this, getString(R.string.choose_at_least_one), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveSettings() {

        List<CountryObject> countryObjectsForSave = new ArrayList<CountryObject>();
        List<CountryObject> countryObjects = Helper.getCountries(this);

        for (int i = 0; i < selected.length; i ++) {

            if (selected[i]) {

                for (CountryObject countryObject : countryObjects) {

                    if (countryObject.getCountryName().equals(names[i])) {

                        countryObjectsForSave.add(countryObject);
                    }
                }
            }
        }
        Helper.saveSettings(this, countryObjectsForSave);
        showPages();
    }

}
