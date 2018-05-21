package com.mxi.goalkeeper.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.activity.MyCredit;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.URL;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Logic needed to create tokens using the {@link android.os.AsyncTask} methods included in the
 * sdk: {@link Stripe#createToken(Card, String, TokenCallback)}.
 */
public class AsyncTaskTokenController {

    private CardInputWidget mCardInputWidget;
    private Context mContext;
    private ErrorDialogHandler mErrorDialogHandler;
    private ProgressDialogController mProgressDialogController;
    private String mPublishableKey;

    public AsyncTaskTokenController(
            @NonNull Button button,
            @NonNull CardInputWidget cardInputWidget,
            @NonNull Context context,
            @NonNull ErrorDialogHandler errorDialogHandler,
            @NonNull ProgressDialogController progressDialogController,
            @NonNull String publishableKey) {
        mCardInputWidget = cardInputWidget;
        mContext = context;
        mErrorDialogHandler = errorDialogHandler;
        mPublishableKey = publishableKey;
        mProgressDialogController = progressDialogController;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });
    }

    public void detach() {
        mCardInputWidget = null;
    }

    private void saveCard() {
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            mErrorDialogHandler.showError("Invalid Card Data");
            return;
        }
        mProgressDialogController.startProgress();
        new Stripe(mContext).createToken(
                cardToSave,
                mPublishableKey,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        Log.e("Tockenid", token.getId() + "");

                        makeJsonlogin(token.getId());

                    }

                    public void onError(Exception error) {
                        mErrorDialogHandler.showError(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });


    }


    private void makeJsonlogin(final String token) {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/charges",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:payment", response);
                        jsonParseLogin(response);

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialogController.finishProgress();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount", "50");
                params.put("currency", "USD");
                params.put("source", token);

                Log.i("request payment", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer sk_test_OmGxcevWbmZsJCld7vz9nfcZ");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
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
            mProgressDialogController.finishProgress();

            String transaction_id = jsonObject.getString("id");
            // String amount = jsonObject.getString("amount");
            // Log.e("Payment id", transaction_id);
            // Log.e("Payment id", amount);
            Toast.makeText(mContext, "Payment Successfull", Toast.LENGTH_LONG).show();
            makeJsonPurches_plan(transaction_id);
            MyCredit.dialog.dismiss();
        } catch (JSONException e) {
            Log.e("Error : Payment", e.toString());
            mProgressDialogController.finishProgress();
        }
    }


    //============================ Membership Planes ============================================================

    private void makeJsonPurches_plan(final String transaction_id) {
        AppController.getInstance().getRequestQueue().getCache().clear();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_purches_plan,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:purches_plan", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            mProgressDialogController.finishProgress();

                            if (jsonObject.getString("status").equals("200")) {
                                Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.e("Error view_player_games", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialogController.finishProgress();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MyCredit.user_id);
                params.put("plan_id", MyCredit.plan_id);
                params.put("trancation_id", transaction_id);
                params.put("credit", MyCredit.credits);

                Log.i("request player_games", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", MyCredit.UserAuth);
                headers.put("Authorization", MyCredit.Authorization);
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


}
