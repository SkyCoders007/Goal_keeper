package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.game_type;
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

public class GameSurveys extends AppCompatActivity {
    RecyclerView recycler_view;
    CommanClass cc;
    private Toolbar toolbar;
    ProgressDialog pDialog;
    ArrayList<game_type> survaylist;
    public static String request_id = "", player_id = "", team_name = "", date_time = "", player_first_name = "", player_last_name = "";
    ImageView iv_no_image;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_surveys);
        cc = new CommanClass(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(GameSurveys.this));
        iv_no_image = (ImageView) findViewById(R.id.iv_no_image);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (cc.isConnectingToInternet()) {
            pDialog = new ProgressDialog(GameSurveys.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            makeJsonSurvey();
        } else {
            cc.showSnackbar(recycler_view, getString(R.string.no_internet));
        }

    }

    private void makeJsonSurvey() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_past_game,
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
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("game_survey", "post_game_survey");

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
            survaylist = new ArrayList<game_type>();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray player_data = jsonObject.getJSONArray("past_game_data");

                for (int i = 0; i < player_data.length(); i++) {
                    JSONObject jsonObject1 = player_data.getJSONObject(i);

                    game_type gt = new game_type();
                    ArrayList<String> playerList = new ArrayList<>();
                    gt.setId(jsonObject1.getString("id"));
                    gt.setManager_name(jsonObject1.getString("Team_manager"));
                    gt.setTeam_name(jsonObject1.getString("team_name"));
                    gt.setGame_date(cc.dateConvert(jsonObject1.getString("game_date")));
                    gt.setGame_start_time(cc.TimeFormate(jsonObject1.getString("game_start_time")));
                    gt.setDuration(jsonObject1.getString("duration"));
                    gt.setGame_type(jsonObject1.getString("game_type"));
                    gt.setGround_size(jsonObject1.getString("ground_size"));
                    gt.setLevel(jsonObject1.getString("level"));
                    gt.setGame_caliber(jsonObject1.getString("calibre"));
                    gt.setP_id(jsonObject1.getString("p_id"));
                    gt.setRequest_id(jsonObject1.getString("request_id"));
                    gt.setApplicant_status(jsonObject1.getString("applicant_status"));
                    gt.setRequestor_status(jsonObject1.getString("requestor_status"));
                    gt.setFull_name(jsonObject1.getString("Team_manager"));
                    gt.setUser_id(jsonObject1.getString("manager_id"));
                    gt.setGame_gender(jsonObject1.getString("game_gender"));
                    gt.setAddress(jsonObject1.getString("address"));
                    gt.setAddress_lat(jsonObject1.getString("address_lat"));
                    gt.setAddress_long(jsonObject1.getString("address_long"));
                    // gt.setIs_status(jsonObject1.getString("is_survey"));
//                    gt.setPlayer_first_name(jsonObject1.getString("player_first_name"));
//                    gt.setPlayer_last_name(jsonObject1.getString("player_last_name"));

                    JSONArray jsonArray = jsonObject1.getJSONArray("player_name");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        playerList.add(object.getString("name"));
                    }

                    gt.setGame_player_list(playerList);
                    gt.setSet(false);
                    survaylist.add(gt);
                }
                if (!survaylist.isEmpty()) {
                    iv_no_image.setVisibility(View.GONE);
                    recycler_view.setLayoutManager(new LinearLayoutManager(GameSurveys.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recycler_view.setAdapter(new GamesSurveyAdapter(GameSurveys.this, survaylist));
                } else {
                    iv_no_image.setVisibility(View.VISIBLE);
                }
                //Log.e("currentlist", survaylist.size() + "");
            } else {
                iv_no_image.setVisibility(View.VISIBLE);
                // cc.showSnackbar(recycler_view, jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            pDialog.dismiss();
            Log.e("Error view_request", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public class GamesSurveyAdapter extends RecyclerView.Adapter<GamesSurveyAdapter.CustomViewHolder> {

        private Context context;
        private ArrayList<game_type> arrayList;

        public GamesSurveyAdapter(Context context, ArrayList<game_type> list) {
            this.context = context;
            arrayList = ((ArrayList) list);

        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.row_games, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int i) {

            final game_type list = arrayList.get(i);

            /*ArrayList<String> player_name=list.getGame_player_list();

            Log.e("TotalPlayer",player_name.size()+"");
            for (int j = 0; j < player_name.size(); j++) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setText(player_name.get(j));
                textView.setTextColor(context.getResources().getColor(R.color.Color_black_text));
                textView.setPadding(10, 10, 10, 10);// in pixels (left, top, right, bottom)
                holder.ll_player_list.addView(textView);
            }

            if(!list.getUser_id().equals(cc.loadPrefString("user_id"))){
                holder.tv_player_title.setText("Team Manger");
                Log.e("TeamManagerId",list.getUser_id());
            }*/

            if (!list.isSet()) {
                if (list.getUser_id().equals(cc.loadPrefString("user_id"))) {
                    holder.tv_player_title.setText("Team Manger");
                    Log.e("TeamManagerId", list.getUser_id());

                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText(list.getManager_name());
                    textView.setTextColor(context.getResources().getColor(R.color.Color_black_text));
                    textView.setPadding(10, 10, 10, 10);// in pixels (left, top, right, bottom)
                    holder.ll_player_list.addView(textView);

                } else {
                    ArrayList<String> player_name = list.getGame_player_list();

                    Log.e("TotalPlayer", player_name.size() + "");
//                    for (int j = 0; j < player_name.size(); j++) {
//                        TextView textView = new TextView(context);
//                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT));
//                        textView.setText(player_name.get(j));
//                        textView.setTextColor(context.getResources().getColor(R.color.Color_black_text));
//                        textView.setPadding(10, 10, 10, 10);// in pixels (left, top, right, bottom)
//                        holder.ll_player_list.addView(textView);
//                    }
                    StringBuilder builder = new StringBuilder();
                    for(String s : player_name) {
                        if(player_name.indexOf(s)==0){
                            builder.append(s);
                        }else{
                            builder.append(","+s);
                        }
                    }
                    String str = builder.toString();
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText(str);
                    textView.setTextColor(context.getResources().getColor(R.color.Color_black_text));
                    textView.setPadding(10, 10, 10, 10);// in pixels (left, top, right, bottom)
                    holder.ll_player_list.addView(textView);
                }

                holder.tv_title.setText(list.getTeam_name());
                holder.tv_date.setText(list.getGame_date() + " " + " " + " " + list.getGame_start_time());
                holder.tv_time.setText(list.getDuration() + " " + "MIN");
                holder.tv_surface.setText("Surface" + " : " + list.getGame_type());
                holder.tv_team_size.setText("Team size" + " : " + list.getGround_size().replace("*", "x"));
                holder.tv_gender.setText("Gender" + " : " + list.getGame_gender());
//                holder.tv_team_category.setText("Game Caliber" + " : " + list.getLevel());
                holder.tv_location.setText(list.getAddress());
                holder.tv_team_category.setText("Game Caliber" + " : " + list.getGame_caliber());
                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request_id = survaylist.get(i).getRequest_id();
                        player_id = survaylist.get(i).getP_id();
                        team_name = survaylist.get(i).getTeam_name();
                        player_first_name = survaylist.get(i).getPlayer_first_name();
                        player_last_name = survaylist.get(i).getPlayer_last_name();
                        date_time = survaylist.get(i).getGame_date() + " " + " " + survaylist.get(i).getGame_start_time();

                        startActivity(new Intent(GameSurveys.this, MyReviews.class).putExtra("reviews", "surveys"));

                    }
                });
                holder.tv_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        double latitude = Double.parseDouble(list.getAddress_lat());
                        double longitude = Double.parseDouble(list.getAddress_long());
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

                list.setSet(true);
            }

        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv_title, tv_date, tv_time, tv_surface, tv_team_size, tv_gender, tv_team_category, tv_location;
            LinearLayout ll_row;
            LinearLayout ll_player_list;
            TextView tv_player_title;

            public CustomViewHolder(View convertView) {
                super(convertView);

                ll_player_list = (LinearLayout) convertView.findViewById(R.id.ll_player_list);
                tv_player_title = (TextView) convertView.findViewById(R.id.tv_player_title);

                tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                tv_surface = (TextView) convertView.findViewById(R.id.tv_surface);
                tv_team_size = (TextView) convertView.findViewById(R.id.tv_team_size);
                tv_gender = (TextView) convertView.findViewById(R.id.tv_gender);
                tv_team_category = (TextView) convertView.findViewById(R.id.tv_team_category);
                tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);

            }
        }
    }
}
