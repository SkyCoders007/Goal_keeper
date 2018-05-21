package com.mxi.goalkeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.country;

import java.util.ArrayList;

/**
 * Created by android on 23/3/17.
 */

public class StateCountryAdapter extends RecyclerView.Adapter<StateCountryAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<country> arrayList;

    public StateCountryAdapter(Context context, ArrayList<country> list) {
        this.context = context;
        arrayList = ((ArrayList) list);

    }

    @Override
    public StateCountryAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_string_list, viewGroup, false);

        StateCountryAdapter.CustomViewHolder viewHolder = new StateCountryAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StateCountryAdapter.CustomViewHolder holder, final int i) {
        country list = arrayList.get(i);
        holder.tv_string_list.setText(list.getC_name());
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_string_list;

        public CustomViewHolder(View convertView) {
            super(convertView);

            tv_string_list = (TextView) convertView.findViewById(R.id.tv_string_list);

        }
    }

}