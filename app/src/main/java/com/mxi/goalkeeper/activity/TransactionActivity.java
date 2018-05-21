package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.adapter.TransactionAdapter;
import com.mxi.goalkeeper.model.game_type;
import com.mxi.goalkeeper.model.transaction_data;
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

public class TransactionActivity extends AppCompatActivity {

    CommanClass cc;
    ImageView iv_back,iv_no_image;
    Toolbar toolbar;
    ProgressDialog pDialog;
    RecyclerView rv_history;
    ArrayList<transaction_data> transactionlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        cc=new CommanClass(this);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_no_image=(ImageView)findViewById(R.id.iv_no_image);
        rv_history=(RecyclerView) findViewById(R.id.rv_history);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        makeJsonCallForHistory();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void makeJsonCallForHistory() {
        pDialog=new ProgressDialog(TransactionActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_transaction_history,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:past_game", response);
                        jsonParseLogin(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.i("request: past_game", params.toString());

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

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            transactionlist = new ArrayList<transaction_data>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("Transaction_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    transaction_data td = new transaction_data();

                    td.setId(jsonObject1.getString("id"));
                    td.setTrancation_id(jsonObject1.getString("trancation_id"));
                    String s=jsonObject1.getString("trancation_date");
                    String[] parts = s.split(" "); // escape .
                    String part1 = parts[0];
                    String part2 = parts[1];
                    td.setTrancation_date(part1);
                    td.setTrancation_time(part2);
                    td.setTotal_credit(jsonObject1.getString("total_credit"));
                    if(td.getTotal_credit().equals("1")){
                        td.setTrancation_plan("Basic");
                    } else if (td.getTotal_credit().equals("5")) {
                        td.setTrancation_plan("Standard");
                    }
                    transactionlist.add(td);
                }
                if (!transactionlist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);
                    TransactionAdapter transactionAdapter =new TransactionAdapter(TransactionActivity.this, transactionlist);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(TransactionActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_history.setAdapter(transactionAdapter);
                    rv_history.setLayoutManager(linearLayoutManager);
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error view_request", e.toString());
        }
    }
}
