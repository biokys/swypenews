package com.droid4you.application.swypenews;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: muller10
 * Date: 8/5/12
 * Time: 9:04 PM
 */
final public class Helper {

    public static final String    SEPARATOR   = "\\|";

    private Helper() {}

    public static String getCountryCodeBasedOnCurrentCountry(Context context) {

        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getSimCountryIso();
    }

    public static CountryObject getCountryObjectBasedOnCurrentCountry(Context context) {

        for (CountryObject countryObject : getCountries(context)) {

            if (countryObject.getCountryCode().equalsIgnoreCase(getCountryCodeBasedOnCurrentCountry(context))) {

                return countryObject;
            }
        }

        throw  new IllegalStateException("no actual country object found");
    }

    public static List<ServerObject> getServers(Context context) {

        List<ServerObject> list = new ArrayList<ServerObject>();
        CharSequence[] array = context.getResources().getTextArray(R.array.servers);

        String[] splitted;
        ServerObject serverObject;
        int index = 0;

        for (CharSequence server : array) {

            splitted = server.toString().split(SEPARATOR);

            if (splitted.length != 3) {

                throw new IllegalStateException("wrong server configuration at index " + index);
            }

            serverObject = new ServerObject(splitted[0], splitted[1], splitted[2]);
            list.add(serverObject);
            index++;

        }

        return list;
    }

    public static List<ServerObject> getServersForActualCountry(Context context) {

        List<ServerObject> listForCountry = new ArrayList<ServerObject>();
        List<ServerObject> list = getServers(context);
        String countryCode = getCountryCodeBasedOnCurrentCountry(context);

        for (ServerObject serverObject : list) {

            if (serverObject.getCountryCode().equalsIgnoreCase(countryCode)) {

                listForCountry.add(serverObject);
            }
        }

        return listForCountry;
    }

    public static List<ServerObject> getServersForCountries(Context context, List<CountryObject> countries) {

        List<ServerObject> listForCountry = new ArrayList<ServerObject>();
        List<ServerObject> list = getServers(context);

        for (ServerObject serverObject : list) {

            for (CountryObject countryObject : countries) {

                if (serverObject.getCountryCode().equalsIgnoreCase(countryObject.getCountryCode())) {

                    listForCountry.add(serverObject);
                }

            }
        }

        return listForCountry;
    }

    public static List<CountryObject> getCountries(Context context) {

        List<CountryObject> list = new ArrayList<CountryObject>();
        CharSequence[] array = context.getResources().getTextArray(R.array.countries);

        String[] splitted;
        CountryObject countryObject;
        int index = 0;

        for (CharSequence country : array) {

            splitted = country.toString().split(SEPARATOR);

            if (splitted.length != 2) {

                throw new IllegalStateException("wrong country configuration at index " + index);
            }

            countryObject = new CountryObject(splitted[0], splitted[1]);
            list.add(countryObject);
            index++;

        }

        return list;

    }

    private static CountryObject getCountryObjectByCode(Context context, String code) {

        for (CountryObject countryObject : getCountries(context)) {

            if (countryObject.getCountryCode().equalsIgnoreCase(code)) {

                return countryObject;
            }
        }

        throw new IllegalStateException("code " + code + " wasnt found");
    }

    public static void saveSettings(Context context, List<CountryObject> countries) {

        SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.clear();

        for (CountryObject countryObject : countries) {

            prefsEditor.putBoolean(countryObject.getCountryCode(), true);
        }
        prefsEditor.commit();
    }

    public static List<CountryObject> readSettings(Context context) {

        SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Map<String, ?> map = myPrefs.getAll();

        List<CountryObject> list = new ArrayList<CountryObject>();

        Iterator<String> it = map.keySet().iterator();

        while (it.hasNext()) {

            String key = it.next();

            list.add(getCountryObjectByCode(context, key));

        }

        return list;
    }
}
