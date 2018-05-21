package com.mxi.goalkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.transaction_data;
import com.mxi.goalkeeper.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by android on 18/5/17.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.CustomViewHolder> {

    private Context context;
    CommanClass cc;
    private ArrayList<transaction_data> arrayList;

    public TransactionAdapter(Context context, ArrayList<transaction_data> list) {
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
                inflate(R.layout.raw_transaction_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final transaction_data list = arrayList.get(i);

        holder.tv_credit.setText(list.getTotal_credit());
        holder.tv_transaction_date.setText(list.getTrancation_date());
        holder.tv_transaction_time.setText(list.getTrancation_time());
//        holder.tv_transaction_id.setText(list.getTrancation_id());
        holder.tv_transaction_id.setText(list.getTrancation_plan());

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_credit, tv_transaction_id, tv_transaction_date, tv_transaction_time;

        public CustomViewHolder(View convertView) {
            super(convertView);
            tv_credit = (TextView) convertView.findViewById(R.id.tv_credit);
            tv_transaction_id = (TextView) convertView.findViewById(R.id.tv_transaction_id);
            tv_transaction_date = (TextView) convertView.findViewById(R.id.tv_transaction_date);
            tv_transaction_time = (TextView) convertView.findViewById(R.id.tv_transaction_time);


        }
    }

}