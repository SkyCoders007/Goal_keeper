package com.mxi.goalkeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.get_rating;

import java.util.ArrayList;

/**
 * Created by sonali on 10/3/17.
 */
public class MyGames extends RecyclerView.Adapter<MyGames.CustomViewHolder> {

    private Context context;
    private ArrayList<get_rating> arrayList;

    public MyGames(Context context, ArrayList<get_rating> list) {
        this.context = context;
        arrayList = ((ArrayList) list);

    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_my_games, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final get_rating list = arrayList.get(i);

        holder.tv_title.setText(list.getManagername() + " - " + list.getTeam_name());
        holder.tv_skill_rating.setText("Skill Rating : " + list.getSkill_rating());
        holder.tv_game_position.setText("Position played in that game : " + list.getPlayer_type());
        if (list.getAttitude_rate().equals("A")) {
            holder.iv_1.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_2.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_3.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_4.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_5.setBackgroundResource(R.mipmap.heart_active);

        } else if (list.getAttitude_rate().equals("B")) {
            holder.iv_1.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_2.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_3.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_4.setBackgroundResource(R.mipmap.heart_active);
        } else if (list.getAttitude_rate().equals("C")) {
            holder.iv_1.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_2.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_3.setBackgroundResource(R.mipmap.heart_active);

        } else if (list.getAttitude_rate().equals("D")) {
            holder.iv_1.setBackgroundResource(R.mipmap.heart_active);
            holder.iv_2.setBackgroundResource(R.mipmap.heart_active);

        } else if (list.getAttitude_rate().equals("E")) {
            holder.iv_1.setBackgroundResource(R.mipmap.heart_active);
        }


    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_title, tv_skill_rating, tv_attitude_rating, tv_game_position;
        // LinearLayout ratingBar1;
        ImageView iv_1, iv_2, iv_3, iv_4, iv_5;

        public CustomViewHolder(View convertView) {
            super(convertView);

            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_skill_rating = (TextView) convertView.findViewById(R.id.tv_skill_rating);
            tv_attitude_rating = (TextView) convertView.findViewById(R.id.tv_attitude_rating);
            tv_game_position = (TextView) convertView.findViewById(R.id.tv_game_position);
            iv_1 = (ImageView) convertView.findViewById(R.id.iv_1);
            iv_2 = (ImageView) convertView.findViewById(R.id.iv_2);
            iv_3 = (ImageView) convertView.findViewById(R.id.iv_3);
            iv_4 = (ImageView) convertView.findViewById(R.id.iv_4);
            iv_5 = (ImageView) convertView.findViewById(R.id.iv_5);

        }
    }

}
