package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.network.CommanClass;

/**
 * Created by sonali on 29/3/17.
 */
public class Setting extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    CommanClass cc;
    ProgressDialog pDialog;
    Button btn_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        btn_change_password = (Button) findViewById(R.id.btn_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password:
                startActivity(new Intent(Setting.this, EditProfile.class).putExtra("reviews", "Setting"));
                break;
        }
    }
}