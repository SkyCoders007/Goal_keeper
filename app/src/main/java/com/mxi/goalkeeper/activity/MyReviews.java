package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.game_type;
import com.mxi.goalkeeper.model.get_rating;
import com.mxi.goalkeeper.model.mygames;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyReviews extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    RecyclerView recycler_view;
    boolean pay = false;
    Toolbar toolbar;
    ArrayList<mygames> mygameslist;
    mygames mg;
    TextView tv_team_name, tv_date, tv_winner_name, tv_winner_pro_name, tv_info, tv_pages_text;
    RadioGroup radio_winner, rg_winner_profile_name;
    Button btn_login;
    RadioButton rb_league, rb_right, rb_weak_league, rb_ice_time, rb_late_ice, rb_show_up;
    LinearLayout ll_linear;
    ImageView rb_a, rb_b, rb_c, rb_d, rb_e;
    CommanClass cc;
    ProgressDialog pDialog;

    String winner, profile_name, winner_option;
    ArrayList<game_type> upcominglist;
    ArrayList<get_rating> ratinglist;
    ArrayList<game_type> availablelist;
    String title;
    ImageView iv_no_image;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("reviews");
        cc = new CommanClass(MyReviews.this);
        if (title.equals("reviews")) {
            setContentView(R.layout.activity_my_reviews);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
            recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            iv_no_image = (ImageView) findViewById(R.id.iv_no_image);
            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyReviews.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(true);
                makeJsonGetRating();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }

        } else if (title.equals("surveys")) {

            setContentView(R.layout.activity_post_game_survey);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);

            tv_team_name = (TextView) findViewById(R.id.tv_team_name);
            tv_date = (TextView) findViewById(R.id.tv_date);
            tv_winner_name = (TextView) findViewById(R.id.tv_winner_name);
            tv_winner_pro_name = (TextView) findViewById(R.id.tv_winner_pro_name);
            tv_info = (TextView) findViewById(R.id.tv_info);
            ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
            radio_winner = (RadioGroup) findViewById(R.id.radio_winner);
            rg_winner_profile_name = (RadioGroup) findViewById(R.id.rg_winner_profile_name);
            // radiogroup_winner_option = (RadioGroup) findViewById(R.id.radiogroup_winner_option);
            Typeface font = Typeface.createFromAsset(getAssets(), "font/Questrial-Regular.otf");
            rb_league = (RadioButton) findViewById(R.id.rb_league);

            rb_right = (RadioButton) findViewById(R.id.rb_right);
            rb_weak_league = (RadioButton) findViewById(R.id.rb_weak_league);
            rb_ice_time = (RadioButton) findViewById(R.id.rb_ice_time);
            rb_late_ice = (RadioButton) findViewById(R.id.rb_late_ice);
            rb_show_up = (RadioButton) findViewById(R.id.rb_show_up);
            rb_a = (ImageView) findViewById(R.id.rb_a);
            rb_b = (ImageView) findViewById(R.id.rb_b);
            rb_c = (ImageView) findViewById(R.id.rb_c);
            rb_d = (ImageView) findViewById(R.id.rb_d);
            rb_e = (ImageView) findViewById(R.id.rb_e);

            radio_winner.setOnCheckedChangeListener(this);
            rg_winner_profile_name.setOnCheckedChangeListener(this);
            tv_info.setOnClickListener(this);
            rb_a.setOnClickListener(this);
            rb_b.setOnClickListener(this);
            rb_c.setOnClickListener(this);
            rb_d.setOnClickListener(this);
            rb_e.setOnClickListener(this);

            try {
                tv_info.setText("From 1 being very pleasant to 5 being Unsportsmanlike, how would you rate" + " " + GameSurveys.player_first_name + " " + GameSurveys.player_last_name + "?");
                tv_winner_name.setText("Was" + " " + GameSurveys.player_first_name + " " + GameSurveys.player_last_name.toUpperCase().charAt(0) + "");
                tv_winner_pro_name.setText("Was" + " " + GameSurveys.player_first_name + " " + GameSurveys.player_last_name.toUpperCase().charAt(0) + "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            winner = rb_league.getText().toString().trim();
            profile_name = rb_ice_time.getText().toString().trim();
            winner_option = "1";

            winner = "A";
            profile_name = "A";

            rb_league.setTypeface(font);
            rb_right.setTypeface(font);
            rb_weak_league.setTypeface(font);
            rb_ice_time.setTypeface(font);
            rb_late_ice.setTypeface(font);
            rb_show_up.setTypeface(font);

            btn_login = (Button) findViewById(R.id.btn_login);

            tv_team_name.setText(GameSurveys.team_name);
            tv_date.setText(GameSurveys.date_time);
            btn_login.setOnClickListener(this);


        } else if (title.equals("help")) {
            setContentView(R.layout.activity_help);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);

            ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
            tv_pages_text = (TextView) findViewById(R.id.tv_pages_text);
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyReviews.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(true);
                makeJsonHelp();
            } else {
                cc.showSnackbar(ll_linear, getString(R.string.no_internet));
            }
        } else if (title.equals("Upcoming Games")) {
            setContentView(R.layout.activity_my_reviews);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);
            recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
            recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this));
            iv_no_image = (ImageView) findViewById(R.id.iv_no_image);
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyReviews.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(true);
                makeJsonUpcomingGames();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } else if (title.equals("Past Games")) {
            setContentView(R.layout.activity_my_reviews);
            iv_no_image = (ImageView) findViewById(R.id.iv_no_image);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);

            recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
            recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this));

            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyReviews.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(true);
                makeJsonPastGames();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } else if (title.equals("Available Games")) {

            setContentView(R.layout.activity_my_reviews);
            iv_no_image = (ImageView) findViewById(R.id.iv_no_image);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(this);

            recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
            recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this));

            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyReviews.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                pDialog.setCancelable(true);
                makeJsonAvailableGAmes();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (title.equals("surveys")) {
                    if (cc.isConnectingToInternet()) {
                        pDialog = new ProgressDialog(MyReviews.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.show();
                        pDialog.setCancelable(true);
                        makeJsonPostSurvey();
                    } else {
                        cc.showSnackbar(ll_linear, getString(R.string.no_internet));
                    }

                }
                break;
            case R.id.tv_info:
                RatingDialog();
                break;
            case R.id.rb_a:
                rb_a.setBackgroundResource(R.mipmap.ic_star_red);
                rb_b.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_c.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_d.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_e.setBackgroundResource(R.mipmap.ic_star_gray);
                winner_option = "1";
                break;
            case R.id.rb_b:
                rb_a.setBackgroundResource(R.mipmap.ic_star_red);
                rb_b.setBackgroundResource(R.mipmap.ic_star_red);
                rb_c.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_d.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_e.setBackgroundResource(R.mipmap.ic_star_gray);
                winner_option = "2";
                break;
            case R.id.rb_c:
                rb_a.setBackgroundResource(R.mipmap.ic_star_red);
                rb_b.setBackgroundResource(R.mipmap.ic_star_red);
                rb_c.setBackgroundResource(R.mipmap.ic_star_red);
                rb_d.setBackgroundResource(R.mipmap.ic_star_gray);
                rb_e.setBackgroundResource(R.mipmap.ic_star_gray);
                winner_option = "3";
                break;
            case R.id.rb_d:
                rb_a.setBackgroundResource(R.mipmap.ic_star_red);
                rb_b.setBackgroundResource(R.mipmap.ic_star_red);
                rb_c.setBackgroundResource(R.mipmap.ic_star_red);
                rb_d.setBackgroundResource(R.mipmap.ic_star_red);
                rb_e.setBackgroundResource(R.mipmap.ic_star_gray);
                winner_option = "4";
                break;
            case R.id.rb_e:
                rb_a.setBackgroundResource(R.mipmap.ic_star_red);
                rb_b.setBackgroundResource(R.mipmap.ic_star_red);
                rb_c.setBackgroundResource(R.mipmap.ic_star_red);
                rb_d.setBackgroundResource(R.mipmap.ic_star_red);
                rb_e.setBackgroundResource(R.mipmap.ic_star_red);
                winner_option = "5";
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void RatingDialog() {
        final Dialog dialog = new Dialog(MyReviews.this);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);

        switch (group.getId()) {
            case R.id.radio_winner:

                String winner_text = radioButton.getText().toString();
                if (winner_text.equals(getString(R.string.good_league))) {
                    winner = "A";
                } else if (winner_text.equals(getString(R.string.right_on))) {
                    winner = "B";
                } else if (winner_text.equals(getString(R.string.weak_league))) {
                    winner = "C";
                }

                break;
            case R.id.rg_winner_profile_name:
                profile_name = radioButton.getText().toString();
                if (profile_name.equals(getString(R.string.ice_time))) {
                    profile_name = "A";
                } else if (profile_name.equals(getString(R.string.late_ice))) {
                    profile_name = "B";
                } else if (profile_name.equals("Didn't show up")) {
                    profile_name = "C";
                }
                break;
           /* case R.id.radiogroup_winner_option:
                winner_option = radioButton.getText().toString();

                break;*/
        }
    }


    //========================================HTML PAGES=======================================================================================

    private void makeJsonHelp() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_pages,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Help", response);
                        jsonParseLogin(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page_id", "6");

                Log.i("request Help", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("user_token", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                tv_pages_text.setText(Html.fromHtml(jsonObject.getString("page_data_obj")));

            } else {

                cc.showSnackbar(ll_linear, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error pages", e.toString());
        }
    }

    // ==========================Upcoming Games ===========================================================================================
    private void makeJsonUpcomingGames() {
        //AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_upcoming_games,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Upcoming Games", response);
                        jsonParseUpcoming(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
                // iv_no_image.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.i("request Upcoming games", params.toString());

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseUpcoming(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            upcominglist = new ArrayList<game_type>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("upcoming_game_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    game_type gt = new game_type();
                    ArrayList<String> playerList = new ArrayList<>();
                    gt.setId(jsonObject1.getString("id"));
                    gt.setManager_name(jsonObject1.getString("Team_manager"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setGame_date(jsonObject1.getString("game_date"));
                    gt.setGame_start_time(cc.TimeFormate(jsonObject1.getString("game_start_time")));
                    gt.setDuration(jsonObject1.getString("duration"));
                    gt.setGame_type(jsonObject1.getString("game_type"));
                    gt.setGround_size(jsonObject1.getString("ground_size"));
                    gt.setLevel(jsonObject1.getString("level"));
                    gt.setGame_caliber(jsonObject1.getString("calibre"));
                    //  gt.setP_id(jsonObject1.getString("p_id"));
                    //  gt.setRequest_id(jsonObject1.getString("request_id"));
                    //  gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    //  gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    gt.setFull_name(jsonObject1.getString("Team_manager"));
                    gt.setUser_id(jsonObject1.getString("manager_id"));
                    gt.setGame_gender(jsonObject1.getString("game_gender"));
                    gt.setAddress(jsonObject1.getString("address"));
                    gt.setAddress_lat(jsonObject1.getString("address_lat"));
                    gt.setAddress_long(jsonObject1.getString("address_long"));
                    // gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    //  gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    gt.setIs_status(jsonObject1.getString("status"));

                    JSONArray jsonArray = jsonObject1.getJSONArray("player_name");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        playerList.add(object.getString("name"));
                    }

                    gt.setGame_player_list(playerList);
                    gt.setSet(false);
                    upcominglist.add(gt);
                }
                if (!upcominglist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);
                    recycler_view.setAdapter(new com.mxi.goalkeeper.adapter.Games(MyReviews.this, upcominglist));
                    recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
                //  Log.e("currentlist", upcominglist.size() + "");
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                // cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            // e.printStackTrace();
            iv_no_image.setVisibility(View.VISIBLE);
            Log.e("Error upcoming_games", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // ==========================Past Games ===========================================================================================
    private void makeJsonPastGames() {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_past_game,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Past Games", response);
                        jsonParsePast(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("game_survey", "past_game_survey");

                Log.i("request past games", params.toString());

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParsePast(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            upcominglist = new ArrayList<game_type>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("past_game_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    game_type gt = new game_type();
                    ArrayList<String> playerList = new ArrayList<>();
                    gt.setId(jsonObject1.getString("id"));
                    gt.setManager_name(jsonObject1.getString("Team_manager"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setGame_date(jsonObject1.getString("game_date"));
                    gt.setGame_start_time(cc.TimeFormate(jsonObject1.getString("game_start_time")));
                    gt.setDuration(jsonObject1.getString("duration"));
                    gt.setGame_type(jsonObject1.getString("game_type"));
                    gt.setGround_size(jsonObject1.getString("ground_size"));
                    gt.setLevel(jsonObject1.getString("level"));
                    gt.setGame_caliber(jsonObject1.getString("calibre"));
                    // gt.setP_id(jsonObject1.getString("p_id"));
                    // gt.setRequest_id(jsonObject1.getString("request_id"));
                    // gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    //  gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    gt.setFull_name(jsonObject1.getString("Team_manager"));
                    gt.setUser_id(jsonObject1.getString("manager_id"));
                    gt.setGame_gender(jsonObject1.getString("game_gender"));
                    gt.setAddress(jsonObject1.getString("address"));
                    gt.setAddress_lat(jsonObject1.getString("address_lat"));
                    gt.setAddress_long(jsonObject1.getString("address_long"));
                    //  gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    //   gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    gt.setIs_status(jsonObject1.getString("status"));


                    JSONArray jsonArray = jsonObject1.getJSONArray("player_name");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        playerList.add(object.getString("name"));
                    }

                    gt.setGame_player_list(playerList);
                    gt.setSet(false);
                    upcominglist.add(gt);
                }
                if (!upcominglist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);

                    recycler_view.setAdapter(new com.mxi.goalkeeper.adapter.Games(MyReviews.this, upcominglist));
                    recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
                // Log.e("past Games", upcominglist.size() + "");
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                //  cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            // e.printStackTrace();
            Log.e("Error:past_game", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //======================= Get Rating =====================================================================================================
    private void makeJsonGetRating() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_rating_data,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Get_Rating", response);
                        jsonParseGetRatimg(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
                // iv_no_image.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.i("request:get_rating_data", params.toString());

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseGetRatimg(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            ratinglist = new ArrayList<get_rating>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray rating_data = jsonObject.getJSONArray("rating_data");

                for (int i = 0; i < rating_data.length(); i++) {
                    JSONObject jsonObject1 = rating_data.getJSONObject(i);

                    get_rating gt = new get_rating();
                    gt.setAttitude_rate(jsonObject1.getString("attitude_rate"));
                    // gt.setAttitude_rate("B");
                    gt.setRequest_id(jsonObject1.getString("request_id"));
                    gt.setSkill_rating(jsonObject1.getString("skill_rating"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setPlayer_type(jsonObject1.getString("player_type"));
                    gt.setManagername(jsonObject1.getString("managername"));

                    ratinglist.add(gt);
                }
                if (!ratinglist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);
                    com.mxi.goalkeeper.adapter.MyGames adapter = new com.mxi.goalkeeper.adapter.MyGames(MyReviews.this, ratinglist);
                    recycler_view.setAdapter(adapter);
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
                // Log.e("ratinglist", ratinglist.size() + "");
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                //cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("get_rating_data Error", e.toString());
        }
    }

    //====================================================================Post Survey ==================================================================
    private void makeJsonPostSurvey() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_player_rating,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:player_rating", response);
                        jsonParsePostSurvey(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("player_id", GameSurveys.player_id);
                params.put("request_id", GameSurveys.request_id);
                params.put("league_performance", winner);
                params.put("time_performance", profile_name);
                params.put("player_rating", winner_option);

                Log.i("request:player_rating", params.toString());

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParsePostSurvey(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {
                cc.showToast(jsonObject.getString("message"));
                finish();
            } else {

                cc.showSnackbar(ll_linear, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            //  e.printStackTrace();
            Log.e("player_rating Error", e.toString());
        }
    }

    //====================================Available Games =====================================================================================

    private void makeJsonAvailableGAmes() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_available_games,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:avilable_games", response);
                        jsonParseAvailableGames(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.i("request: avilable_games", params.toString());

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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseAvailableGames(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            availablelist = new ArrayList<game_type>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("available_game_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    game_type gt = new game_type();
                    gt.setId(jsonObject1.getString("id"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setGame_date(cc.dateConvert(jsonObject1.getString("game_date")));
                    gt.setGame_start_time(cc.TimeFormate(jsonObject1.getString("game_start_time")));
                    gt.setDuration(jsonObject1.getString("duration"));
                    gt.setGame_type(jsonObject1.getString("game_type"));
                    gt.setGround_size(jsonObject1.getString("ground_size"));
                    gt.setLevel(jsonObject1.getString("level"));
                    gt.setGame_caliber(jsonObject1.getString("calibre"));
                    //gt.setP_id(jsonObject1.getString("p_id"));
                    //gt.setRequest_id(jsonObject1.getString("request_id"));
                    //gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    //gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    gt.setFull_name(jsonObject1.getString("Team_manager"));
                    gt.setUser_id(jsonObject1.getString("manager_id"));
                    gt.setGame_gender(jsonObject1.getString("game_gender"));
                    gt.setAddress(jsonObject1.getString("address"));
                    gt.setAddress_lat(jsonObject1.getString("address_lat"));
                    gt.setAddress_long(jsonObject1.getString("address_long"));
                    gt.setPlayer_type(jsonObject1.getString("player_type"));
                    gt.setAvailable_time(jsonObject1.getString("remaning_time"));
                    gt.setStatus(jsonObject1.getString("Status"));

                    availablelist.add(gt);
                }
                if (!availablelist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);

                    recycler_view.setAdapter(new AvailableGamesAdapter(MyReviews.this, availablelist));
                    recycler_view.setLayoutManager(new LinearLayoutManager(MyReviews.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                // cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

            if (cc.loadPrefBoolean("fromApplyPlayer")) {
                cc.savePrefBoolean("fromApplyPlayer", false);
                finish();
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error view_request", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public class AvailableGamesAdapter extends RecyclerView.Adapter<AvailableGamesAdapter.CustomViewHolder> {
        private Boolean checked = false;
        private Context context;
        private ArrayList<game_type> arrayList;
        CountDownTimer yourCountDownTimer;

        public AvailableGamesAdapter(Context context, ArrayList<game_type> list) {
            this.context = context;
            arrayList = ((ArrayList) list);

        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.row_available_games, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int i) {

            final game_type list = arrayList.get(i);
            holder.tv_title.setText(list.getTeam_name());
            holder.tv_date.setText(list.getGame_date() + " " + " " + " " + list.getGame_start_time());
            holder.tv_time.setText(list.getDuration() + " " + "MIN");
            holder.tv_surface.setText("Surface" + " : " + list.getGame_type());
            holder.tv_team_size.setText("Team size" + " : " + list.getGround_size().replace("*", "x"));
            holder.tv_gender.setText("Gender" + " : " + list.getGame_gender());
            holder.tv_team_category.setText("Game Caliber" + " : " + list.getGame_caliber());
//            holder.tv_team_category.setText("Game Caliber" + " : " + list.getLevel());
            holder.tv_location.setText(list.getAddress());

            if (list.getStatus().equals("Available")) {
                try {
                    Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+)");
                    Matcher m = p.matcher(list.getAvailable_time());
                    if (m.matches()) {
                        int hrs = Integer.parseInt(m.group(1));
                        int min = Integer.parseInt(m.group(2));
                        int sec = Integer.parseInt(m.group(3));
                        long ms = (long) hrs * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000;
                        System.out.println("hrs=" + hrs + " min=" + min + " sec=" + sec + " ms=" + ms);

                        new CountDownTimer(ms, 1000) {

                            public void onTick(long millisUntilFinished) {
                                holder.tv_time_count.setText("Time Left" + " " + String.format("%d:%02d",
                                        // TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                            }

                            public void onFinish() {
                                holder.tv_time_count.setText("Finish!");
                                list.setStatus("Available");
                            }

                        }.start();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
//                holder.tv_time_count.setText(list.getStatus());

                try {
                    Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+)");
                    Matcher m = p.matcher(list.getAvailable_time());
                    if (m.matches()) {
                        int hrs = Integer.parseInt(m.group(1));
                        int min = Integer.parseInt(m.group(2));
                        int sec = Integer.parseInt(m.group(3));
                        long ms = (long) hrs * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000;
                        System.out.println("hrs=" + hrs + " min=" + min + " sec=" + sec + " ms=" + ms);

                        new CountDownTimer(ms, 1000) {

                            public void onTick(long millisUntilFinished) {
                                holder.tv_time_count.setText("Pending" + " " + String.format("%d:%02d",
                                        // TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                            }

                            public void onFinish() {
                                holder.tv_time_count.setText("Finish!");
                                list.setStatus("Available");
                            }
                        }.start();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            holder.ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.getUser_id().equals(cc.loadPrefString("user_id")) && list.getStatus().equals("Available")) {
                        cc.savePrefBoolean("toApprove",true);
                        startActivity(new Intent(MyReviews.this, AvailableDetails.class).putExtra("requested_id", list.getId()));
                    } else if (!list.getUser_id().equals(cc.loadPrefString("user_id")) && list.getStatus().equals("Available")) {

                        if (list.isChecked == false)
//                            showDialogDetails(list.getId(), i,holder.cv_main);
                            showChooseAmount(list.getId(), i, holder.cv_main);
                    }

                }
            });
            holder.tv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    double latitude = Double.parseDouble(list.getAddress_lat());
                    double longitude = Double.parseDouble(list.getAddress_long());

                    Log.e("latitude - longitude ", latitude + " - " + longitude);
                    try {
                        Intent directioIntent = new Intent(
                                android.content.Intent.ACTION_VIEW, Uri
                                .parse("http://maps.google.com/maps?daddr="
                                        + latitude + "," + longitude));
                        directioIntent.setClassName("com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity");
                        startActivity(directioIntent);
                    } catch (Exception e) {
                        Log.e("MAP Error", e.getMessage());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv_title, tv_date, tv_time, tv_surface, tv_team_size, tv_gender, tv_team_category, tv_location, tv_time_count;
            LinearLayout ll_row;
            CardView cv_main;

            public CustomViewHolder(View convertView) {
                super(convertView);

                tv_time_count = (TextView) convertView.findViewById(R.id.tv_time_count);
                tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                tv_surface = (TextView) convertView.findViewById(R.id.tv_surface);
                tv_team_size = (TextView) convertView.findViewById(R.id.tv_team_size);
                tv_gender = (TextView) convertView.findViewById(R.id.tv_gender);
                tv_team_category = (TextView) convertView.findViewById(R.id.tv_team_category);
                tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);
                cv_main = (CardView) convertView.findViewById(R.id.cv_main);

            }
        }

        private void showDialogDetails(final String requested_id, final int position, final CardView cv_main) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cureent_details_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button btn_reject = (Button) dialog.findViewById(R.id.btn_reject);
            Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
            ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
            final EditText et_price = (EditText) dialog.findViewById(R.id.et_price);
            final EditText et_amount = (EditText) dialog.findViewById(R.id.et_amount);
            final CheckBox cb_free = (CheckBox) dialog.findViewById(R.id.cb_free);

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            cb_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb_free.setChecked(isChecked);
                    checked = isChecked;
                }
            });
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String price = et_price.getText().toString().trim();
                    String amount = et_amount.getText().toString().trim();
                    cv_main.setVisibility(View.GONE);

                    availablelist.get(position).isChecked = true;
                    if (!cc.isConnectingToInternet()) {
                        cc.showToast(context.getString(R.string.no_internet));
                    } else if (price.equals("") && !checked && amount.equals("")) {
                        cc.showToast("Please enter amount for play/Play for free/Pay to play");
                    } else if (!price.equals("") || checked || !amount.equals("")) {

                        pDialog.show();
                        pDialog.setCancelable(false);
                        makeJsonApplyPlayer(requested_id, " Free", "Free");

                        dialog.dismiss();
                    } else if (!checked) {

                        if (!price.equals("")) {
                            pDialog.show();
                            pDialog.setCancelable(false);
                            makeJsonApplyPlayer(requested_id, "Take_Amount", price);

                            dialog.dismiss();
                        } else {
                            pDialog.show();
                            pDialog.setCancelable(false);
                            makeJsonApplyPlayer(requested_id, "Give_Amount", amount);

                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.show();
        }

    }


    private void showChooseAmount(final String requested_id, final int position, final CardView cv_main) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.current_detail_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btn_reject = (Button) dialog.findViewById(R.id.btn_reject);
        SeekBar seekbar_amount = (SeekBar) dialog.findViewById(R.id.seekbar_amount);
        final TextView tv_amount = (TextView) dialog.findViewById(R.id.tv_amount);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);


        seekbar_amount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int amount = 0;
                if (progress == 50) {
                    pay = false;
                    amount = 0;
                } else if (progress < 50) {
                    amount = (100 - (50 + progress));
                    pay = false;
                } else if (progress > 50) {
                    amount = (progress - 50);
                    pay = true;
                }
                tv_amount.setText(String.valueOf(amount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "";

                if (!pay && tv_amount.getText().equals("0")) {
                    a = "Free";
                } else if (!pay) {
                    a = "Take_Amount";
                } else if (pay) {
                    a = "Give_Amount";
                }
                Log.e("@@@amount", tv_amount.getText() + "");
                Log.e("@@@Role", a);
                dialog.dismiss();
                cv_main.setVisibility(View.GONE);
                makeJsonApplyPlayer(requested_id, a, tv_amount.getText().toString());

            }
        });
        dialog.show();
    }

    //========================================Apply Players=======================================================================================

    private void makeJsonApplyPlayer(final String requested_id, final String type, final String fee) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_apply_player,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:apply_player", response);
                        jsonParseDetails(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("request_id", requested_id);
                params.put("playing_type", type);
                params.put("playing_amount", fee);

                Log.i("request apply_player", params.toString());

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

        AppController.getInstance().

                addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showSnackbar(recycler_view, jsonObject.getString("message"));
                if (cc.isConnectingToInternet()) {
                    pDialog = new ProgressDialog(MyReviews.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();
                    pDialog.setCancelable(true);
                    cc.savePrefBoolean("fromApplyPlayer", true);
                    makeJsonAvailableGAmes();
                } else {
                    cc.showSnackbar(recycler_view, getString(R.string.no_internet));
                }
            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error apply_player", e.toString());
        }
    }
}
