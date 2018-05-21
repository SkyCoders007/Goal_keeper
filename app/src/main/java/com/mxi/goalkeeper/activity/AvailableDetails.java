package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.appiled_list;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvailableDetails extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    CommanClass cc;
    private Toolbar toolbar;
    ImageView iv_back;
    ArrayList<appiled_list> detailslist;
    Button btn_login;
    public static Dialog dialog;
    ArrayList<String> select = new ArrayList<String>();
    ProgressDialog pDialog;
    String requested_id;
    TextView tv_no_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_details);

        cc = new CommanClass(this);

        requested_id = getIntent().getStringExtra("requested_id");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(AvailableDetails.this));
        if (cc.isConnectingToInternet()) {
            pDialog = new ProgressDialog(AvailableDetails.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(true);
            makeJsonPlayerAvailableGames();
        } else {
            cc.showSnackbar(recycler_view, getString(R.string.no_internet));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                if (cc.isConnectingToInternet()) {
                    pDialog = new ProgressDialog(AvailableDetails.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();
                    pDialog.setCancelable(true);
                    for (int i = 0; i < detailslist.size(); i++) {
                        if (detailslist.get(i).getChecked()) {
                            Log.e("@@SelectedPlayers", detailslist.get(i).getP_id() + ", " + detailslist.get(i).getPlayer_name());
                        }

                    }

                    makeJsonApprovePlayer();
                } else {
                    cc.showSnackbar(recycler_view, getString(R.string.no_internet));
                }

                break;
        }
    }
    //===================================Available Details Games ========================================================================

    private void makeJsonPlayerAvailableGames() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_appiled_list,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:appiled_list", response);
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
                params.put("request_id", requested_id);

                Log.i("request:appiled_list", params.toString());

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

            detailslist = new ArrayList<appiled_list>();

            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("applicant_data");

                for (int i = 0; i < player_data.length(); i++) {
                    String playing_type = "";
                    String playing_amt = "";
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    appiled_list gt = new appiled_list();

                    gt.setId(jsonObject1.getString("id"));
                    gt.setManager_id(jsonObject1.getString("manager_id"));

                    gt.setCalibre(jsonObject1.getString("calibre"));
                    gt.setPlayer_type(jsonObject1.getString("user_type"));
                    gt.setSelf_rate(jsonObject1.getString("self_rate"));
                    gt.setP_id(jsonObject1.getString("p_id"));
                    gt.setPlayer_type(jsonObject1.getString("player_type"));

                    playing_type = jsonObject1.getString("playing_type");
                    playing_amt = jsonObject1.getString("playing_amount");
                    if (playing_type.equals("Free")) {
                        playing_type = "Player will play for free";
                    } else if (playing_type.equals("Take_Amount")) {
                        playing_type = "Player wants to be paid " + playing_amt + "$ to play";
                    } else if (playing_type.equals("Give_Amount")) {
                        playing_type = "Player will pay " + playing_amt + "$ to play";
                    }

//                    gt.setPlaying_type(jsonObject1.getString("playing_type"));
                    gt.setPlaying_type(playing_type);
                    gt.setPlayer_name(jsonObject1.getString("player_name"));
                    gt.setPlaying_amount(jsonObject1.getString("playing_amount"));
                    gt.setChecked(false);

                    detailslist.add(gt);
                }
                if (!detailslist.isEmpty()) {
                    tv_no_record.setVisibility(View.GONE);
                    recycler_view.setAdapter(new AdapterAvailableDetails(AvailableDetails.this, detailslist));
                } else {
                    tv_no_record.setVisibility(View.VISIBLE);
                }
            } else if (jsonObject.getString("status").equals("404")) {
                tv_no_record.setVisibility(View.VISIBLE);
            } else {

                cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            // pDialog.dismiss();
            Log.e("Error view_request", e.toString());
        }
    }


    public class AdapterAvailableDetails extends RecyclerView.Adapter<AdapterAvailableDetails.CustomViewHolder> {

        private Context context;
        private ArrayList<appiled_list> arrayList;

        public AdapterAvailableDetails(Context context, ArrayList<appiled_list> list) {
            this.context = context;
            arrayList = ((ArrayList) list);

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.row_availabledetails_games, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int i) {

            final appiled_list list = arrayList.get(i);

            holder.tv_name.setText(list.getPlayer_name());
            holder.tv_amount.setText(list.getPlaying_amount());
//            holder.tv_caliber.setText("Caliber" + " : " + " " + list.getCalibre());
            holder.tv_self_rating.setText("Self Ratting" + " : " + " " + list.getSelf_rate());
//            holder.tv_playing_for.setText("Playing for" + " : " + list.getPlaying_type());
            holder.tv_playing_for.setText(list.getPlaying_type());

            holder.ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!detailslist.get(i).getChecked()) {
                        if (select.size() <= 2) {
                            detailslist.get(i).setChecked(true);
//                            select.add(detailslist.get(i).getChecked() + "");
                            holder.ll_row.setBackgroundColor(getResources().getColor(R.color.color_rating_bg_1));
                            detailslist.get(i).setRequested_id(detailslist.get(i).getId());
                            detailslist.get(i).setPlayer_id(detailslist.get(i).getP_id());
                        }
                    } else {
                        detailslist.get(i).setChecked(false);
                        holder.ll_row.setBackgroundColor(getResources().getColor(R.color.color_white));
//                        select.remove(detailslist.get(i).getChecked() + "");
//                        detailslist.remove(detailslist.get(i).getId());
//                        detailslist.remove(detailslist.get(i).getP_id());
//                        holder.ll_row.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    }
                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv_name, tv_caliber, tv_self_rating, tv_playing_for, tv_amount;
            protected LinearLayout ll_row;

            public CustomViewHolder(View convertView) {
                super(convertView);

                tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//                tv_caliber = (TextView) convertView.findViewById(R.id.tv_caliber);
                tv_self_rating = (TextView) convertView.findViewById(R.id.tv_self_rating);
                tv_playing_for = (TextView) convertView.findViewById(R.id.tv_playing_for);
                ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);

            }
        }

    }

    //========================================Approve Player=======================================================================================

    private void makeJsonApprovePlayer() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_approve_player,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:approve_player", response);
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
                int k = 0;
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("@@SelectTotal", detailslist.size() + "");
                for (int i = 0; i < detailslist.size(); i++) {
                    if (detailslist.get(i).getChecked()) {
                        Log.e("@@Selected Id", detailslist.get(i).getP_id() + "");
                        params.put("player_id[" + i + "]", detailslist.get(i).getP_id());
                    }
                }
                params.put("request_id", requested_id);
                Log.i("request approve_player", params.toString());

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void jsonParseDetails(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));
                if (cc.loadPrefBoolean("toApprove")) {
                    Intent launchNextActivity;
                    launchNextActivity = new Intent(AvailableDetails.this, MainActivity.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(launchNextActivity);
                    finish();
                } else {
                    finish();
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
