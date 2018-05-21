package com.mxi.goalkeeper.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.database.SQLiteTD;
import com.mxi.goalkeeper.model.country;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {

    public static ArrayList<country> countryList;
    ProgressBar progressBar;
    CommanClass cc;
    SQLiteTD dbcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cc = new CommanClass(this);

        // cc.savePrefString("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

/*
        if (!cc.loadPrefBoolean("firstTimeDB")) {

            File file = new File(Environment.getExternalStorageDirectory() + "/Goalkeeper.db");

            if (file.exists()) {
                Log.e("@@@aksahy", "In DB Clear");
                deleteRecursive(file);
            }
            cc.savePrefBoolean("firstTimeDB", true);
        }*/

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            // checkReadWritePermission();
            checkGPSPermission();
        } else {
            CountDown();
        }

    }


    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    private void makeJsonCallForState() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_country,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:get_country", response);
                        countryList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("country_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                country country = new country();
                                country.setC_id(object.getString("id"));
                                country.setC_name(object.getString("name"));
                                countryList.add(country);
                            }
                            if (cc.loadPrefBoolean("islogin")) {
                                startActivity(new Intent(Splash.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(Splash.this, Login.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                Log.i("request header", headers.toString());
                return headers;
            }


        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CountDown();
                } else {
                    //  Toast.makeText(NewPrescriptionRequest.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                    showErrorDialog("Please allow the permission for better performance", true);
                }
                break;
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkReadWritePermission();
                } else {
                    showErrorDialog("Please allow the permission for better performance", true);
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkReadWritePermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        } else {
            CountDown();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkGPSPermission() {
        int gpsWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if (gpsWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            checkReadWritePermission();
        }
    }

    public void showErrorDialog(String msg, final boolean isFromPermission) {
        progressBar.setVisibility(View.INVISIBLE);
        AlertDialog.Builder alert = new AlertDialog.Builder(Splash.this);
        alert.setTitle("Goalkeeper");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFromPermission) {
                    checkReadWritePermission();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    private void CountDown() {
        dbcon = new SQLiteTD(this);
        makeJsonCallForState();

    }
}
