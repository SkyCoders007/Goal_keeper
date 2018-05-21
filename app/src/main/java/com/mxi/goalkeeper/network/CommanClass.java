package com.mxi.goalkeeper.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin1 on 21/3/16.
 */
public class CommanClass {

    private Context _context;
    SharedPreferences pref;

    public CommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("Goalkeeper",
                _context.MODE_PRIVATE);
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String text) {
        // TODO Auto-generated method stub
        Toast.makeText(_context, text, Toast.LENGTH_LONG).show();
    }

    public void showSnackbar(View coordinatorLayout, String text) {

        try {
            Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try {
            Snackbar snack = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            view.setLayoutParams(params);
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public String MyText(String text) {
        String s = "";
        try {
            s = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;

    }

    @SuppressLint("SimpleDateFormat")
    public String dateConvert(String timestmp)
            throws ParseException {
        String str_date_to;
        Date date = null;
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = formate.parse(timestmp);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formate_to = new SimpleDateFormat("dd MMM yyyy");
        str_date_to = formate_to.format(date);

        return str_date_to;
    }

    @SuppressLint("SimpleDateFormat")
    public String dConvert(String timestmp)
            throws ParseException {
        String str_date_to;
        Date date = null;
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-M-d");
        try {
            date = formate.parse(timestmp);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formate_to = new SimpleDateFormat("MM-dd-yyyy");
        str_date_to = formate_to.format(date);

        return str_date_to;
    }

    public String TimeFormate(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Date date1 = null;
        try {
            date1 = dateFormat.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formate_to = new SimpleDateFormat("HH:mm");
        String ctime = formate_to.format(date1);

        return ctime;
    }

}
