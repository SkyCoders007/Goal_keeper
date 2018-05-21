package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import com.mxi.goalkeeper.model.member_planes;
import com.mxi.goalkeeper.mudule.DependencyHandler;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyCredit extends AppCompatActivity {

    // CommanClass cc;
    RecyclerView recycler_view;
    CommanClass cc;
    ProgressDialog pDialog;
    ArrayList<member_planes> memberlist;
    DependencyHandler mDependencyHandler;
    public static Dialog dialog;
    public static String plan_id, credits, amount, UserAuth, Authorization, user_id;
    private Toolbar toolbar;
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit);
        cc = new CommanClass(MyCredit.this);

        View view = MyCredit.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) MyCredit.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        iv_back=(ImageView)findViewById(R.id.iv_back);
        setSupportActionBar(toolbar);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(MyCredit.this);

        UserAuth = cc.loadPrefString("user_token");
        Authorization = cc.loadPrefString("Authorization");
        user_id = cc.loadPrefString("user_id");

        if (cc.isConnectingToInternet()) {
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            makeJsonMembershipPlanes();
        } else {
            cc.showSnackbar(recycler_view, getString(R.string.no_internet));
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mDependencyHandler.clearReferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //============================ Membership Planes ============================================================

    private void makeJsonMembershipPlanes() {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_membership_plan,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:member_plan", response);
                        jsonParseLogin(response);
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
               /* params.put("user_id", cc.loadPrefString("user_id"));

                Log.i("request player_games", params.toString());*/

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

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            memberlist = new ArrayList<member_planes>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray Membership_plan_data = jsonObject.getJSONArray("Membership_plan_data");

                for (int i = 0; i < Membership_plan_data.length(); i++) {
                    JSONObject jsonObject1 = Membership_plan_data.getJSONObject(i);

                    member_planes gt = new member_planes();

                    gt.setId(jsonObject1.getString("id"));
                    gt.setPlan_name(jsonObject1.getString("plan_name"));
                    gt.setCredit(jsonObject1.getString("credit"));
                    gt.setAmount(jsonObject1.getString("amount"));
                    gt.setStatus(jsonObject1.getString("status"));
                    gt.setCreated_date(jsonObject1.getString("created_date"));

                    memberlist.add(gt);

                }
                try {
                    if (!memberlist.isEmpty()) {

                        recycler_view.setAdapter(new BuyCreditsAdapter(MyCredit.this, memberlist));
                        recycler_view.setLayoutManager(new LinearLayoutManager(MyCredit.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("currentlist", memberlist.size() + "");
            } else {

                cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error view_player_games", e.toString());
        }
    }

//============================Buy credits Adapter ===============================================================================================

    public class BuyCreditsAdapter extends RecyclerView.Adapter<BuyCreditsAdapter.CustomViewHolder> {

        private Context context;
        private ArrayList<member_planes> arrayList;

        public BuyCreditsAdapter(Context context, ArrayList<member_planes> list) {
            this.context = context;
            arrayList = ((ArrayList) list);

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.row_membership, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int i) {

            final member_planes list = arrayList.get(i);
            holder.tv_amount.setText("Amount" + " " + " : " + " " + list.getAmount() + " " + "$");
            holder.tv_plan_name.setText("Plan Name" + " " + " : " + " " + list.getPlan_name());
            holder.tv_credits.setText("Credits" + " " + " : " + " " + list.getCredit());

            holder.ll_plan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    plan_id = list.getId();
                    credits = list.getCredit();
                    amount = list.getAmount();
                    PaymentDialog();

                }
            });

        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv_plan_name, tv_amount, tv_credits;
            protected LinearLayout ll_plan;

            public CustomViewHolder(View convertView) {
                super(convertView);

                tv_plan_name = (TextView) convertView.findViewById(R.id.tv_plan_name);
                tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                tv_credits = (TextView) convertView.findViewById(R.id.tv_credits);
                ll_plan = (LinearLayout) convertView.findViewById(R.id.ll_plan);
            }
        }
    }
    //================================================= PAYMENT DIALOG =======================================================================

    private void PaymentDialog() {
        dialog = new Dialog(MyCredit.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics metrics = MyCredit.this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
        // CardInputWidget card_input_widget = (CardInputWidget) dialog.findViewById(R.id.card_input_widget);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        final Button saveButton = (Button) dialog.findViewById(R.id.save);
        mDependencyHandler = new DependencyHandler(MyCredit.this, (CardInputWidget) dialog.findViewById(R.id.card_input_widget));
        tv_price.setText(amount + " $ ");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                mDependencyHandler.attachAsyncTaskTokenController(saveButton);
                // dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(cc.loadPrefBoolean("fromPost")){
            cc.savePrefBoolean("fromCredit",true);
            cc.savePrefBoolean("fromPost",false);
        }
        finish();
    }
}

