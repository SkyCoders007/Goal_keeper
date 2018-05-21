package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.arena;
import com.mxi.goalkeeper.network.AndroidMultiPartEntity;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;
import com.stripe.android.view.CardInputWidget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class PostGames extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener, NumberPicker.OnValueChangeListener, CheckBox.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private GoogleMap mMap;
    int PLACE_PICKER_REQUEST=1;

    //To store longitude and latitude from map
    double longitude;
    double latitude;
    //Google ApiClient
    GoogleApiClient googleApiClient;

    ImageView iv_back, iv_five, iv_four, iv_mixed, iv_male, iv_golie, iv_defence, iv_forward;
    long totalSize=0;
    EditText et_team_name;
    TextView tv_ice_hockey, tv_ball_hockey, tv_five, tv_four, tv_three, tv_mixed, tv_male, tv_female,
            tv_goalie, tv_defence, tv_forward, tv_refree, tv_start_time, et_date, tv_rating_info, tv_caliber_info;
    CheckBox cb_a, cb_b, cb_c, cb_d, cb_e;
    ArrayList<arena> arenalist = new ArrayList<arena>();
    String durationlist[] = {"Duration", "60 min", "75 min", "90 min", "120 min"};
    CommanClass cc;
    LinearLayout ll_linear;
    Spinner spinner_duration;
    Button btn_next;
    RadioGroup rg_caliber;
    RadioButton rb_a, rb_b, rb_c, rb_d, rb_e;
    String duration = "", game_type = "", ground_size = "", gender = "", player_type = "", arena_id = "", create_date = "", duration_cur = "";
    int to_year, to_month, to_day;
    Calendar cal;
    ArrayList<String> levellist = new ArrayList<String>();
    ProgressDialog pDialog;
    TextView tv_address;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    String address= "", name= "", caliber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_games);
        cc = new CommanClass(PostGames.this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        SupportMapFragment mapFragment = (SupportMapFragment) PostGames.this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        InitUi();

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("POST GAMES", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("POST GAMES", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                String addre = String.format("%s", place.getAddress());
                name = String.format("%s", place.getName());
                stBuilder.append(addre);

                tv_address.setText(stBuilder.toString());

                address=tv_address.getText().toString();
//                moveMap();
            }
        }
    }

    private void InitUi() {
        btn_next = (Button) findViewById(R.id.btn_next);
        spinner_duration = (Spinner) findViewById(R.id.spinner_duration);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        et_team_name = (EditText) findViewById(R.id.et_team_name);
        tv_rating_info = (TextView) findViewById(R.id.tv_rating_info);
        tv_caliber_info = (TextView) findViewById(R.id.tv_caliber_info);

        tv_address = (TextView) findViewById(R.id.tv_address);

        et_date = (TextView) findViewById(R.id.et_date);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_five = (ImageView) findViewById(R.id.iv_five);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        iv_mixed = (ImageView) findViewById(R.id.iv_mixed);
        iv_male = (ImageView) findViewById(R.id.iv_male);
        iv_golie = (ImageView) findViewById(R.id.iv_golie);
        iv_forward = (ImageView) findViewById(R.id.iv_forward);
        iv_defence = (ImageView) findViewById(R.id.iv_defence);
        tv_ice_hockey = (TextView) findViewById(R.id.tv_ice_hockey);
        tv_ball_hockey = (TextView) findViewById(R.id.tv_ball_hockey);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_five = (TextView) findViewById(R.id.tv_five);
        tv_four = (TextView) findViewById(R.id.tv_four);
        tv_three = (TextView) findViewById(R.id.tv_three);
        tv_mixed = (TextView) findViewById(R.id.tv_mixed);
        tv_male = (TextView) findViewById(R.id.tv_male);
        tv_female = (TextView) findViewById(R.id.tv_female);
        tv_goalie = (TextView) findViewById(R.id.tv_goalie);
        tv_defence = (TextView) findViewById(R.id.tv_defence);
        tv_forward = (TextView) findViewById(R.id.tv_forward);
        tv_refree = (TextView) findViewById(R.id.tv_refree);
        cb_a = (CheckBox) findViewById(R.id.cb_a);
        cb_b = (CheckBox) findViewById(R.id.cb_b);
        cb_c = (CheckBox) findViewById(R.id.cb_c);
        cb_d = (CheckBox) findViewById(R.id.cb_d);
        cb_e = (CheckBox) findViewById(R.id.cb_e);
        rg_caliber = (RadioGroup) findViewById(R.id.rg_caliber);

        rb_a = (RadioButton) findViewById(R.id.rb_a);
        rb_b = (RadioButton) findViewById(R.id.rb_b);
        rb_c = (RadioButton) findViewById(R.id.rb_c);
        rb_d = (RadioButton) findViewById(R.id.rb_d);
        rb_e = (RadioButton) findViewById(R.id.rb_e);

        rb_a.setChecked(true);
        caliber = rb_a.getText().toString().trim();

        Typeface font = Typeface.createFromAsset(getAssets(), "font/Questrial-Regular.otf");
        cb_a.setTypeface(font);
        cb_b.setTypeface(font);
        cb_c.setTypeface(font);
        cb_d.setTypeface(font);
        cb_e.setTypeface(font);

        rb_a.setTypeface(font);
        rb_b.setTypeface(font);
        rb_c.setTypeface(font);
        rb_d.setTypeface(font);
        rb_e.setTypeface(font);
        iv_back.setOnClickListener(this);
        tv_ice_hockey.setOnClickListener(this);
        tv_ball_hockey.setOnClickListener(this);
        tv_five.setOnClickListener(this);
        tv_four.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_mixed.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        tv_female.setOnClickListener(this);
        tv_goalie.setOnClickListener(this);
        tv_forward.setOnClickListener(this);
        tv_defence.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        et_date.setOnClickListener(this);
        tv_rating_info.setOnClickListener(this);
        tv_caliber_info.setOnClickListener(this);
        rg_caliber.setOnCheckedChangeListener(this);

        tv_refree.setOnClickListener(this);
        cb_a.setOnCheckedChangeListener(this);
        cb_b.setOnCheckedChangeListener(this);
        cb_c.setOnCheckedChangeListener(this);
        cb_d.setOnCheckedChangeListener(this);
        cb_e.setOnCheckedChangeListener(this);
        game_type = tv_ice_hockey.getText().toString().trim();
        ground_size = tv_five.getText().toString().trim();
        ground_size = "5*5";
        // gender = tv_mixed.getText().toString().trim();
        gender = "Mixed";
//        player_type = tv_goalie.getText().toString().trim();
        player_type = getResources().getString(R.string.goalie);

        levellist.add(cb_a.getText().toString());
        levellist.add(cb_b.getText().toString());
        levellist.add(cb_c.getText().toString());
        levellist.add(cb_d.getText().toString());
        levellist.add(cb_e.getText().toString());

        Log.e("Caliber:size", levellist.size() + "");

        try {
            if (!cc.loadPrefString("time").equals("")) {
                tv_start_time.setText(cc.loadPrefString("time"));
            } else {
                tv_start_time.setText("12:00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*if (cc.isConnectingToInternet()) {
            makeJsonCallForArena();
        } else {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        }*/
        spinner_duration.setPrompt("Duration");
        if (durationlist.length != 0) {
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PostGames.this, R.layout.spinner_row, R.id.text1, durationlist);
            spinner_duration.setAdapter(adapter1);
        }
        spinner_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                try {
                    if (position != 0) {
                        duration = durationlist[position];
                        duration_cur = duration.substring(0, duration.lastIndexOf(" "));
                        Log.e("whatyouaresearching", duration_cur);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        // fetchAddress = false;
        //  fetchType = Constants.USE_ADDRESS_NAME;
       /* iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v;
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (fetchType == Constants.USE_ADDRESS_NAME) {
                    if (country.getText().length() == 0) {
                        Toast.makeText(PostGames.this, "Please enter an address name", Toast.LENGTH_LONG).show();
                        // return;
                    } else {
                        Intent intent = new Intent(PostGames.this, GeocodeAddressIntentService.class);
                        intent.putExtra(Constants.RECEIVER, mResultReceiver);
                        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
                        intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, country.getText().toString());
                        pDialog = new ProgressDialog(PostGames.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        Log.e("Starting Service", "Starting Service");
                        startService(intent);
                    }

                }

            }
        });
*/

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

   /* //Getting current location
    private void getCurrentLocation() {

        try {
            // mMap.clear();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.e("long", longitude + "");
                Log.e("lat", latitude + "");
                //moving the map to location
                moveMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //Function to move the map
    private void moveMap() {
        LatLng latLng = new LatLng(latitude, longitude);
        try {
            mMap.clear();
            if (!name.equals("")) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Current Location"));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);

        } catch (Exception e) {
            Log.e("Error : AddMarker", e.getMessage());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();
        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_a:
                if (isChecked) {
                    levellist.add(cb_a.getText().toString());
                } else {
                    levellist.remove(cb_a.getText().toString());
                }
                break;
            case R.id.cb_b:
                if (isChecked) {
                    levellist.add(cb_b.getText().toString());
                } else {
                    levellist.remove(cb_b.getText().toString());
                }
                break;
            case R.id.cb_c:
                if (isChecked) {
                    levellist.add(cb_c.getText().toString());
                } else {
                    levellist.remove(cb_c.getText().toString());
                }
                break;
            case R.id.cb_d:
                if (isChecked) {
                    levellist.add(cb_d.getText().toString());
                } else {
                    levellist.remove(cb_d.getText().toString());
                }
                break;
            case R.id.cb_e:
                if (isChecked) {
                    levellist.add(cb_e.getText().toString());
                } else {
                    levellist.remove(cb_e.getText().toString());
                }
                break;
        }
        Log.e("levellist", levellist.size() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                googleApiClient.disconnect();
                onBackPressed();
                // startActivity(new Intent(PostGames.this, MainActivity.class));
                break;
            case R.id.tv_address:
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(PostGames.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // startActivity(new Intent(PostGames.this, MainActivity.class));
                break;
            case R.id.tv_ball_hockey:
                tv_ice_hockey.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_ice_hockey.setTextColor(Color.parseColor("#000000"));
                tv_ball_hockey.setTextColor(Color.parseColor("#FFFFFF"));
                tv_ball_hockey.setBackgroundResource(R.mipmap.right_red);
                game_type = tv_ball_hockey.getText().toString().trim();

                break;
            case R.id.tv_ice_hockey:
                tv_ball_hockey.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_ice_hockey.setBackgroundResource(R.mipmap.left_red);
                tv_ball_hockey.setTextColor(Color.parseColor("#000000"));
                tv_ice_hockey.setTextColor(Color.parseColor("#FFFFFF"));
                game_type = tv_ice_hockey.getText().toString().trim();
                break;
            case R.id.tv_five:
                tv_four.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_four.setTextColor(Color.parseColor("#000000"));
                tv_three.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_three.setTextColor(Color.parseColor("#000000"));
                tv_five.setBackgroundResource(R.mipmap.left_red);
                tv_five.setTextColor(Color.parseColor("#FFFFFF"));
                iv_four.setVisibility(View.VISIBLE);
                iv_five.setVisibility(View.GONE);
                //  ground_size = tv_five.getText().toString().trim();
                ground_size = "5*5";
                break;
            case R.id.tv_four:
                tv_three.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_three.setTextColor(Color.parseColor("#000000"));
                tv_five.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_five.setTextColor(Color.parseColor("#000000"));
                tv_four.setBackgroundResource(R.mipmap.mid_red);
                tv_four.setTextColor(Color.parseColor("#FFFFFF"));
                iv_four.setVisibility(View.GONE);
                iv_five.setVisibility(View.GONE);
                //  ground_size = tv_four.getText().toString().trim();
                ground_size = "4*4";
                break;
            case R.id.tv_three:
                tv_four.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_four.setTextColor(Color.parseColor("#000000"));
                tv_five.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_five.setTextColor(Color.parseColor("#000000"));
                tv_three.setBackgroundResource(R.mipmap.right_red);
                tv_three.setTextColor(Color.parseColor("#FFFFFF"));
                iv_four.setVisibility(View.GONE);
                iv_five.setVisibility(View.VISIBLE);
                // ground_size = tv_three.getText().toString().trim();
                ground_size = "3*3";
                break;
            case R.id.tv_mixed:
                tv_male.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_male.setTextColor(Color.parseColor("#000000"));
                tv_female.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_female.setTextColor(Color.parseColor("#000000"));
                tv_mixed.setBackgroundResource(R.mipmap.left_red);
                tv_mixed.setTextColor(Color.parseColor("#FFFFFF"));
                iv_male.setVisibility(View.VISIBLE);
                iv_mixed.setVisibility(View.GONE);
                // gender = tv_mixed.getText().toString().trim();
                gender = "Mixed";
                break;
            case R.id.tv_male:
                tv_mixed.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_mixed.setTextColor(Color.parseColor("#000000"));
                tv_female.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_female.setTextColor(Color.parseColor("#000000"));
                tv_male.setBackgroundResource(R.mipmap.mid_red);
                tv_male.setTextColor(Color.parseColor("#FFFFFF"));
                iv_male.setVisibility(View.GONE);
                iv_mixed.setVisibility(View.GONE);
                gender = tv_male.getText().toString().trim();
                break;
            case R.id.tv_female:
                tv_mixed.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_mixed.setTextColor(Color.parseColor("#000000"));
                tv_male.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_male.setTextColor(Color.parseColor("#000000"));
                tv_female.setBackgroundResource(R.mipmap.right_red);
                tv_female.setTextColor(Color.parseColor("#FFFFFF"));
                iv_male.setVisibility(View.GONE);
                iv_mixed.setVisibility(View.VISIBLE);
                gender = tv_female.getText().toString().trim();
                break;
            case R.id.tv_goalie:
                tv_defence.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_defence.setTextColor(Color.parseColor("#000000"));
                tv_forward.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_forward.setTextColor(Color.parseColor("#000000"));
                tv_refree.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_refree.setTextColor(Color.parseColor("#000000"));
                tv_goalie.setBackgroundResource(R.mipmap.left_red);
                tv_goalie.setTextColor(Color.parseColor("#FFFFFF"));
                iv_defence.setVisibility(View.VISIBLE);
                iv_forward.setVisibility(View.VISIBLE);
                iv_golie.setVisibility(View.GONE);
//                player_type = tv_goalie.getText().toString().trim();
                player_type = getResources().getString(R.string.goalie);
                break;
            case R.id.tv_defence:
                tv_goalie.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_goalie.setTextColor(Color.parseColor("#000000"));
                tv_forward.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_forward.setTextColor(Color.parseColor("#000000"));
                tv_refree.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_refree.setTextColor(Color.parseColor("#000000"));
                tv_defence.setBackgroundResource(R.mipmap.mid_red);
                tv_defence.setTextColor(Color.parseColor("#FFFFFF"));
                iv_defence.setVisibility(View.GONE);
                iv_forward.setVisibility(View.VISIBLE);
                iv_golie.setVisibility(View.GONE);
//                player_type = tv_defence.getText().toString().trim();
                player_type = getResources().getString(R.string.defence);//defense
                break;

            case R.id.tv_forward:
                tv_defence.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_defence.setTextColor(Color.parseColor("#000000"));
                tv_goalie.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_goalie.setTextColor(Color.parseColor("#000000"));
                tv_refree.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_refree.setTextColor(Color.parseColor("#000000"));
                tv_forward.setBackgroundResource(R.mipmap.mid_red);
                tv_forward.setTextColor(Color.parseColor("#FFFFFF"));
                iv_defence.setVisibility(View.GONE);
                iv_forward.setVisibility(View.GONE);
                iv_golie.setVisibility(View.VISIBLE);
//                player_type = tv_forward.getText().toString().trim();
                player_type = getResources().getString(R.string.forward);
                break;
            case R.id.tv_refree:
                tv_defence.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_defence.setTextColor(Color.parseColor("#000000"));
                tv_forward.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_forward.setTextColor(Color.parseColor("#000000"));
                tv_goalie.setBackgroundColor(Color.parseColor("#D0D3D8"));
                tv_goalie.setTextColor(Color.parseColor("#000000"));
                tv_refree.setBackgroundResource(R.mipmap.right_red);
                tv_refree.setTextColor(Color.parseColor("#FFFFFF"));
                iv_defence.setVisibility(View.VISIBLE);
                iv_forward.setVisibility(View.GONE);
                iv_golie.setVisibility(View.VISIBLE);
//                player_type = tv_refree.getText().toString().trim();
                player_type = getResources().getString(R.string.referee);
                break;
            case R.id.tv_start_time:
                showDialogTime();
                break;
            case R.id.btn_next:

                // PaymentDialog();
                final String team_name = et_team_name.getText().toString().trim();
                final String date = et_date.getText().toString().trim();
                final String start_time = tv_start_time.getText().toString().trim();

                Log.e("start:time", start_time.substring(0, start_time.indexOf(":")));
                Log.e("start:time1", start_time.substring(start_time.indexOf(":") + 1, start_time.length()));
                try {
                    cc.savePrefString("time", start_time);
                    cc.savePrefString("time1", start_time.substring(0, start_time.indexOf(":")));
                    cc.savePrefString("time2", start_time.substring(start_time.indexOf(":") + 1, start_time.length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!cc.isConnectingToInternet()) {
                    cc.showSnackbar(ll_linear, getString(R.string.no_internet));
                } else if (team_name.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.enter_teamname));
                } else if (date.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_date));
                } else if (start_time.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_time));
                } else if (duration.equals("Duration")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_duration));
                } else if (duration.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_duration));
                } else if (game_type.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_game));
                } else if (ground_size.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_ground_size));
                } else if (gender.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_gender));
                } else if (player_type.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.player_type));
                } else if (levellist.isEmpty()) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_player_level));
                } else if (address.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.enter_address));
                } /*else if (arena_id.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_select_arena));
                }*/ else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PostGames.this);
                    builder1.setMessage("Are you sure you want to create new request ?");
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (haveSufficientBalance()) {
                                        dialog.cancel();
//                                        pDialog = new ProgressDialog(PostGames.this);
//                                        pDialog.setMessage("Please wait...");
//                                        pDialog.show();
//                                        pDialog.setCancelable(false);
//                                        makeJsonPostGames(team_name, date, start_time);
                                        Log.e("@@Gender",gender);
                                        new UploadFileToServer(team_name, date, start_time).execute();
                                    } else {
                                        showCreditPopUp();
                                    }

                                    //startActivity(new Intent(PostGames.this, PaymentActivity.class));

                                    //  PaymentDialog();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                break;
            case R.id.et_date:
                cal = Calendar.getInstance();
                to_year = cal.get(Calendar.YEAR);
                to_month = cal.get(Calendar.MONTH);
                to_day = cal.get(Calendar.DAY_OF_MONTH);

                showDialog(0);
                break;
            case R.id.tv_rating_info:
                RatingDialog();
                break;
            case R.id.ll_arena:

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(PostGames.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.tv_caliber_info:
                RatingDialog();
                break;
        }
    }


    public void showCreditPopUp() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostGames.this);
        builder1.setMessage("You don't have sufficient balance to create request.\n\nDo you want to get credits?");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cc.savePrefBoolean("fromPost",true);
                        startActivity(new Intent(PostGames.this, MyCredit.class));
//                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public boolean haveSufficientBalance() {
        boolean val = false;
        int credit = Integer.parseInt(cc.loadPrefString("credit"));
        if (credit >= 1) {
            val = true;
        }
        return val;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);
        switch (group.getId()) {
            case R.id.rg_caliber:
                caliber = choose_answer.getText().toString().trim();
                break;
        }

    }
    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_rating_info:
                RatingDialog();
                break;
        }
        return false;
    }*/

    private void RatingDialog() {
        final Dialog dialog = new Dialog(PostGames.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_describe);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_rating_a = (TextView) dialog.findViewById(R.id.tv_rating_a);
        TextView tv_rating_b = (TextView) dialog.findViewById(R.id.tv_rating_b);
        TextView tv_rating_c = (TextView) dialog.findViewById(R.id.tv_rating_c);
        TextView tv_rating_e = (TextView) dialog.findViewById(R.id.tv_rating_e);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        tv_rating_a.setText("Pro,Semi-Pro or Minor Pro, NCAA Division I , Major Junior; Women's AAA,");
        tv_rating_b.setText("University or Senior Hockey, Junior A, Quebec Junior AAA;Women's A, BB or BAA");
        tv_rating_c.setText("Canadian College, Junior B or C, Quebec Junior AA or A; High School; Women's C");
        tv_rating_e.setText("No Experience, started playing hockey as an Adult");
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    //================================================= PAYMENT DIALOG =======================================================================

    private void PaymentDialog() {
        final Dialog dialog = new Dialog(PostGames.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
        CardInputWidget card_input_widget = (CardInputWidget) dialog.findViewById(R.id.card_input_widget);

        ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        Button saveButton = (Button) dialog.findViewById(R.id.save);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                android.app.DatePickerDialog _date2 = new android.app.DatePickerDialog(this, to_dateListener, to_year, to_month, to_day);

                Calendar c1 = Calendar.getInstance();
                c1.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), (cal.get(Calendar.DATE) + 30));
                _date2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return _date2;

        }
        return null;
    }

    android.app.DatePickerDialog.OnDateSetListener to_dateListener = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            String selected_date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            Date date = null;
            SimpleDateFormat formate_to = null;
            SimpleDateFormat formate_to4 = null, formate_to5 = null;
            try {

                SimpleDateFormat formate = new SimpleDateFormat("yyyy-M-d");
                date = formate.parse(selected_date);

            } catch (ParseException e) {
            }
            try {
                formate_to4 = new SimpleDateFormat("dd MMM yyyy");
                formate_to5 = new SimpleDateFormat("dd-MM-yyyy");
                String DOB = formate_to4.format(date);
                create_date = formate_to5.format(date);
                et_date.setText(DOB);
                Log.e("set selected date", DOB);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + picker);
    }

    private void showDialogTime() {
        final Dialog dialog = new Dialog(PostGames.this);
        dialog.setContentView(R.layout.post_start_time);
        Button b1 = (Button) dialog.findViewById(R.id.button1);
        final Button b2 = (Button) dialog.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        final NumberPicker np1 = (NumberPicker) dialog.findViewById(R.id.numberPicker2);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        cal.add(Calendar.HOUR, 2);
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        String time = localTime.substring(0, localTime.lastIndexOf(":"));
        Log.e("system_time", time);
        np.setMinValue(00);
        np.setMaxValue(23);
        try {
            if (cc.loadPrefString("time1").equals("")) {
                np.setValue(12);
            } else {
                np.setValue(Integer.parseInt(cc.loadPrefString("time1")));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        np1.setMinValue(0);
        np1.setMaxValue(3);
        np1.setDisplayedValues(new String[]{"00", "15", "30", "45"});

        try {
            if (cc.loadPrefString("time2").equals("") || cc.loadPrefString("time2").equals("null")) {
                np1.setValue(00);
            } else {
                try {
                    Log.e("post time2", cc.loadPrefString("time2"));
                    if (cc.loadPrefString("time2").equals("00")) {
                        np1.setValue(0);
                    } else if (cc.loadPrefString("time2").equals("15")) {
                        np1.setValue(1);
                    } else if (cc.loadPrefString("time2").equals("30")) {
                        np1.setValue(2);
                    } else if (cc.loadPrefString("time2").equals("45")) {
                        np1.setValue(3);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (String.valueOf(np1.getValue() * 15).equals("0")) {
                        tv_start_time.setText(String.valueOf(np.getValue()) + ":" + "00");
                    } else {
                        tv_start_time.setText(String.valueOf(np.getValue()) + ":" + String.valueOf(np1.getValue() * 15));
                    }

                    String start_time=tv_start_time.getText().toString();
                    cc.savePrefString("time1", start_time.substring(0, start_time.indexOf(":")));
                    cc.savePrefString("time2", start_time.substring(start_time.indexOf(":") + 1, start_time.length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void makeJsonCallForArena() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, URL.Url_get_arena_list,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:get_arena_list", response);
                        arenalist = new ArrayList<arena>();
                        ArrayList<String> alist = new ArrayList<String>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("arena_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                arena a = new arena();
                                StringBuilder builder =new StringBuilder();
                                builder.append(address);
                                a.setArena_id(object.getString("arena_id"));
                                a.setTitle(object.getString("title"));
                                a.setTitle(object.getString("title"));
                                a.setCity(object.getString("city"));
                                a.setState_name(object.getString("state_name"));
                                a.setCountry_name(object.getString("country_name"));
                                a.setAddress_lat(object.getString("address_lat"));
                                a.setAddress_long(object.getString("address_long"));

                                alist.add(object.getString("title"));
                                arenalist.add(a);
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

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    //========================================PostGame=======================================================================================

    private void makeJsonPostGames(final String team_name, final String date, final String start_time) {
//        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_new_request,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:new_request", response);
                        if(response!=null && response.length() > 0){
                            jsonParseLogin(response);
                        }else{
                            Log.e("response","Null");
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
                try {
                    Log.e("new_request:error", error + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("team_name", team_name);
                params.put("game_date", create_date);
                params.put("game_time", start_time);
                // params.put("arena_id", arena_id);
                params.put("game_type", game_type);
                for (int i = 0; i < levellist.size(); i++) {
                    params.put("level[" + i + "]", levellist.get(i));
                    // Log.i("loop", levellist.get(i).toString());
                }
                params.put("ground_size", ground_size);
                params.put("duration", duration_cur);
                params.put("player_type", player_type);
                params.put("game_gender", gender);
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("amount", "50$");
                params.put("transaction_id", "25255252");
                params.put("title", name);
                StringBuilder stBuilder=new StringBuilder();
                stBuilder.append(address);
                address.replace(","," ");
                params.put("address", address);
                params.put("arena_lat", String.valueOf(latitude));
                params.put("arena_long", String.valueOf(longitude));
                params.put("calibre", caliber);

                Log.i("request new_request", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };
        /*jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showSnackbar(ll_linear, jsonObject.getString("message"));
                finish();
            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error", e.toString());
        }
    }



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        HttpClient httpclient;
        HttpPost httppost;
        String team_name, date, start_time;
//
        public UploadFileToServer(final String team_name, final String date, final String start_time) {
            this.team_name= team_name;
            this.date= date;
            this.start_time= start_time;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PostGames.this);
            pDialog.show();
            pDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage(String.valueOf("Loading..." + progress[0])
                    + " %");

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost( URL.Url_new_request);

            httppost.addHeader("Authorization", cc.loadPrefString("Authorization"));
            httppost.addHeader("UserAuth", cc.loadPrefString("user_token"));
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("team_name", new StringBody(team_name));
                entity.addPart("game_date", new StringBody(create_date));
                entity.addPart("game_time", new StringBody(start_time));
                // params.put("arena_id", arena_id);
                entity.addPart("game_type",new StringBody(game_type));
                for (int i = 0; i < levellist.size(); i++) {
                    entity.addPart("level[" + i + "]", new StringBody(levellist.get(i)));
                    // Log.i("loop", levellist.get(i).toString());
                }
                entity.addPart("ground_size", new StringBody(ground_size));
                entity.addPart("duration", new StringBody(duration_cur));
                entity.addPart("player_type", new StringBody(player_type));
                entity.addPart("game_gender", new StringBody(gender));
                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("amount", new StringBody("50$"));
                entity.addPart("transaction_id", new StringBody("25255252"));
                entity.addPart("title", new StringBody(name));
                entity.addPart("address", new StringBody(address));
                entity.addPart("arena_lat", new StringBody(String.valueOf(latitude)));
                entity.addPart("arena_long", new StringBody(String.valueOf(longitude)));
                entity.addPart("calibre", new StringBody(caliber));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    return responseString;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Register: result", "Response from server: " + result);
            try {
                pDialog.dismiss();
                JSONObject jsonObject= new JSONObject(result);
                if (jsonObject.getString("status").equals("200")) {

                    cc.showSnackbar(ll_linear, jsonObject.getString("message"));
                    finish();
                } else {

                    cc.showToast(jsonObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }

    }

    private void getAvailableCredits() {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_available_credits,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Url_getCredits", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
//                                JSONObject Credit = jsonObject.getJSONObject("Total_credit");

                                JSONArray jsonArray = jsonObject.getJSONArray("Total_credit");

                                JSONObject Credit = (JSONObject) jsonArray.get(0);

                                String val_credit = Credit.getString("credit");
                                cc.savePrefString("credit", val_credit);

                            } else {
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.e("request Credits", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                // Log.i("request header", headers.toString());
                return headers;
            }


        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    @Override
    protected void onResume() {
        Log.e("@@","Inresume");
        if(cc.loadPrefBoolean("fromCredit")){
            cc.savePrefBoolean("fromCredit",false);
            getAvailableCredits();
        }
        super.onResume();
    }
}

