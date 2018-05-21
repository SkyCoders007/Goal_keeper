package com.mxi.goalkeeper.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.model.country;

import java.util.ArrayList;

/**
 * Created by android on 23/3/17.
 */

public class ListViewAdapter extends ArrayAdapter<country> {

    private Activity activity;
    private ArrayList<country> countries;
    private final String TAG = ListViewAdapter.class.getSimpleName();

    public ListViewAdapter(Activity activity, int resource, ArrayList<country> countries) {
        super(activity, resource, countries);
        this.activity = activity;
        this.countries = countries;
        Log.i(TAG, "init adapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.row_string_list, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        country product = countries.get(position);

        // set product data to views

        holder.name.setText(product.getC_name());


        // the view must be returned to our activity
        return convertView;
    }

    private class ViewHolder {

        private TextView name;


        public ViewHolder(View v) {

            name = (TextView) v.findViewById(R.id.tv_string_list);

        }
    }

}