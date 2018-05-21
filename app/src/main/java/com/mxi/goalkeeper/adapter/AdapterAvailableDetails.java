/*
package com.mxi.goalkeeper.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.availabledetails;
import com.mxi.goalkeeper.mudule.DependencyHandler;
import com.stripe.android.view.CardInputWidget;

import java.util.ArrayList;

*/
/**
 * Created by sonali on 20/4/17.
 *//*


public class AdapterAvailableDetails extends RecyclerView.Adapter<AdapterAvailableDetails.CustomViewHolder> {

    private Context context;
    private ArrayList<availabledetails> arrayList;
    Boolean isChecked = false;
    final ArrayList<String> select = new ArrayList<String>();

    public AdapterAvailableDetails(Context context, ArrayList<availabledetails> list) {
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

        final availabledetails list = arrayList.get(i);
        holder.tv_name.setText(list.getPlayer_name());

        holder.tv_caliber.setText("Caliber" + " : " + " " + list.getCaliber());
        holder.tv_self_rating.setText("Self Ratting" + " : " + " " + list.getSelf_ratting());
        holder.tv_playing_for.setText("Playing for" + " : " + list.getPlaying_for());
        // int j = i;
        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("i", i + "");
                // Log.e("boolean", arrayList.get(i).getChecked() + "");

                if (!list.isChecked) {
                    if (select.size() <= 1) {
                        holder.ll_row.setBackgroundResource(R.drawable.ic_shadow);
                        list.isChecked = true;
                        select.add(true + "");
                        Log.e("addsize", select.size() + "");
                    }
                } else {
                    holder.ll_row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    list.isChecked = false;
                    select.remove(false + "");
                    Log.e("remove size", select.size() + "");

                }
                notifyDataSetChanged();
                if (select.size() == 2) {
                    PaymentDialog();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_name, tv_caliber, tv_self_rating, tv_playing_for;
        protected LinearLayout ll_row;

        public CustomViewHolder(View convertView) {
            super(convertView);

            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_caliber = (TextView) convertView.findViewById(R.id.tv_caliber);
            tv_self_rating = (TextView) convertView.findViewById(R.id.tv_self_rating);
            tv_playing_for = (TextView) convertView.findViewById(R.id.tv_playing_for);
            ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);

        }
    }
    //================================================= PAYMENT DIALOG =======================================================================

    private void PaymentDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DependencyHandler mDependencyHandler;

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
       // CardInputWidget card_input_widget = (CardInputWidget) dialog.findViewById(R.id.card_input_widget);
        TextView tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        Button saveButton = (Button) dialog.findViewById(R.id.save);
        mDependencyHandler = new DependencyHandler(context, (CardInputWidget) dialog.findViewById(R.id.card_input_widget));
        tv_price.setText("PRICE" + " " + " - " + " " + "25 $");
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

}
*/
