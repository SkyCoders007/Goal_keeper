package com.mxi.goalkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.game_type;
import com.mxi.goalkeeper.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by sonali on 10/3/17.
 */
public class Games extends RecyclerView.Adapter<Games.CustomViewHolder> {

    private Context context;
    CommanClass cc;
    private ArrayList<game_type> arrayList;

    public Games(Context context, ArrayList<game_type> list) {
        cc = new CommanClass(context);
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
                inflate(R.layout.row_games, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final game_type list = arrayList.get(i);


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
                /*for (int j = 0; j < player_name.size(); j++) {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText(player_name.get(j));
                    textView.setTextColor(context.getResources().getColor(R.color.Color_black_text));
                    textView.setPadding(10, 10, 10, 10);// in pixels (left, top, right, bottom)
                    holder.ll_player_list.addView(textView);
                }*/
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
//        holder.tv_team_category.setText("Game Caliber" + " : " + list.getLevel());
            holder.tv_team_category.setText("Game Caliber" + " : " + list.getGame_caliber());
            holder.tv_location.setText(list.getAddress());
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
                        context.startActivity(directioIntent);
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
        protected LinearLayout ll_row;
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
