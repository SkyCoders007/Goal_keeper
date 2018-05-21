package com.mxi.goalkeeper.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.activity.AvailableDetails;
import com.mxi.goalkeeper.activity.EditProfile;
import com.mxi.goalkeeper.activity.GameSurveys;
import com.mxi.goalkeeper.activity.MyReviews;
import com.mxi.goalkeeper.activity.Setting;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyGames extends Fragment implements View.OnClickListener {

    Button btn_upcoming, btn_post_games, btn_my_reviews, btn_game_surveys, btn_available_game;

    TextView tvCount_available, tvCount,tvCount_upcoming;
    String games;

    public MyGames(String s) {
        games = s;
    }

    View rootView;
    CommanClass cc;

    @SuppressLint("ValidFragment")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (games.equals("my games")) {
            rootView = inflater.inflate(R.layout.fragment_my_games, container, false);
            cc = new CommanClass(getActivity());

            btn_upcoming = (Button) rootView.findViewById(R.id.btn_upcoming);
            btn_post_games = (Button) rootView.findViewById(R.id.btn_post_games);
            btn_my_reviews = (Button) rootView.findViewById(R.id.btn_my_reviews);
            btn_game_surveys = (Button) rootView.findViewById(R.id.btn_game_surveys);
            btn_available_game = (Button) rootView.findViewById(R.id.btn_available_game);
            tvCount = (TextView) rootView.findViewById(R.id.tvCount);
            tvCount_available = (TextView) rootView.findViewById(R.id.tvCount_available);
            tvCount_upcoming = (TextView) rootView.findViewById(R.id.tvCount_upcoming);

            // btn_available_game.setVisibility(View.VISIBLE);
            tvCount_available.setVisibility(View.GONE);

            /*if (Integer.parseInt(cc.loadPrefString("user_type_count")) >= 1 && !cc.loadPrefString("isManager").equals("Team_Manager")) {
                btn_game_surveys.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);
            } else {
                btn_game_surveys.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.GONE);
            }*/

            btn_upcoming.setOnClickListener(this);
            btn_post_games.setOnClickListener(this);
            btn_my_reviews.setOnClickListener(this);
            btn_game_surveys.setOnClickListener(this);
            btn_available_game.setOnClickListener(this);
            //BudgetCount();

        } else if (games.equals("more")) {
            rootView = inflater.inflate(R.layout.activity_more, container, false);

            btn_upcoming = (Button) rootView.findViewById(R.id.btn_upcoming);
            btn_post_games = (Button) rootView.findViewById(R.id.btn_post_games);
            btn_my_reviews = (Button) rootView.findViewById(R.id.btn_my_reviews);
            btn_game_surveys = (Button) rootView.findViewById(R.id.btn_game_surveys);
            btn_game_surveys.setVisibility(View.VISIBLE);

            btn_upcoming.setOnClickListener(this);
            btn_post_games.setOnClickListener(this);
            btn_my_reviews.setOnClickListener(this);
            btn_game_surveys.setOnClickListener(this);
        }

        return rootView;

    }

    public void onResume() {
        super.onResume();
        try {
            BudgetCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upcoming:
                if (games.equals("my games")) {
                    startActivity(new Intent(getActivity(), MyReviews.class).putExtra("reviews", "Upcoming Games"));
                } else if (games.equals("more")) {
                    startActivity(new Intent(getActivity(), EditProfile.class).putExtra("reviews", "My Profile"));
                }
                break;
            case R.id.btn_post_games:
                if (games.equals("my games")) {
                    startActivity(new Intent(getActivity(), MyReviews.class).putExtra("reviews", "Past Games"));
                } else if (games.equals("more")) {
                    /*Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store"));
                    startActivity(i);*/
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Goalkeeper");
                    i.putExtra(android.content.Intent.EXTRA_TEXT, "Please download this app : - https://play.google.com/store");
                    startActivity(Intent.createChooser(i, "Share via"));
                }
                break;
            case R.id.btn_my_reviews:
                if (games.equals("my games")) {
                    startActivity(new Intent(getActivity(), MyReviews.class).putExtra("reviews", "reviews"));
                } else if (games.equals("more")) {
                    startActivity(new Intent(getActivity(), Setting.class));
                }
                break;
            case R.id.btn_game_surveys:
                if (games.equals("my games")) {
                    startActivity(new Intent(getActivity(), GameSurveys.class));
                } else if (games.equals("more")) {
                    startActivity(new Intent(getActivity(), MyReviews.class).putExtra("reviews", "help"));
                }
                break;
            case R.id.btn_available_game:

                startActivity(new Intent(getActivity(), MyReviews.class).putExtra("reviews", "Available Games"));
                // startActivity(new Intent(getActivity(), AvailableDetails.class));
                break;
        }
    }

    //================================================Budget Count ============================================

    private void BudgetCount() {
        // pDialog = new ProgressDialog(getActivity());
        //  pDialog.setMessage("Please Wait");
        // pDialog.show();
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_buzzer,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:Url_buzzer", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                JSONObject Buzzer = jsonObject.getJSONObject("Buzzer");
//                                String upcoming_game_count = Buzzer.getString("upcoming_game_count");
                                String manager_available_count = Buzzer.getString("manager_available_count");
                                String player_available_count = Buzzer.getString("player_available_count");
                                String survey_count = Buzzer.getString("survey_count");
                                String upcoming_game_count = Buzzer.getString("upcoming_game_count");

                                tvCount.setText(survey_count);
                                tvCount_available.setText((Integer.parseInt(manager_available_count) + Integer.parseInt(player_available_count)) + "");
                                tvCount_upcoming.setText(upcoming_game_count);

                                if (tvCount.getText().toString().trim().equals("0") || tvCount.getText().toString().trim().equals("")) {
                                    tvCount.setVisibility(View.GONE);
                                } else {
                                    tvCount.setVisibility(View.VISIBLE);
                                }

                                if (tvCount_available.getText().toString().trim().equals("0") || tvCount_available.getText().toString().trim().equals("")) {
                                    tvCount_available.setVisibility(View.GONE);
                                } else {
                                    tvCount_available.setVisibility(View.VISIBLE);
                                }

                                if (tvCount_upcoming.getText().toString().trim().equals("0") || tvCount_upcoming.getText().toString().trim().equals("")) {
                                    tvCount_upcoming.setVisibility(View.GONE);
                                } else {
                                    tvCount_upcoming.setVisibility(View.VISIBLE);
                                }

                            } else {
                                //  pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.dismiss();
                // cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.e("request buzzer", params.toString());
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

}
