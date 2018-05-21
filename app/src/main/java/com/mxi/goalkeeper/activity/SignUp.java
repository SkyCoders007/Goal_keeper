package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.efor18.rangeseekbar.RangeSeekBar;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.adapter.SignUpSunday;
import com.mxi.goalkeeper.adapter.SignupDayTime;
import com.mxi.goalkeeper.adapter.SignupFriday;
import com.mxi.goalkeeper.adapter.SignupSaturday;
import com.mxi.goalkeeper.adapter.SignupThursday;
import com.mxi.goalkeeper.adapter.SignupTuesday;
import com.mxi.goalkeeper.adapter.SignupWednesday;
import com.mxi.goalkeeper.database.SQLiteTD;
import com.mxi.goalkeeper.model.friday;
import com.mxi.goalkeeper.model.saturday;
import com.mxi.goalkeeper.model.signup_time;
import com.mxi.goalkeeper.model.sunday;
import com.mxi.goalkeeper.model.thursday;
import com.mxi.goalkeeper.model.tuesday;
import com.mxi.goalkeeper.model.wednesday;
import com.mxi.goalkeeper.network.AndroidMultiPartEntity;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener, View.OnClickListener, RangeSeekBar.OnRangeSeekBarChangeListener<Long> {

    ImageView iv_back;
    RadioGroup radio_mix_gender;
    TextView tv_no_mon_intervals,tv_no_tue_intervals,tv_no_wed_intervals,tv_no_thurs_intervals,tv_no_fri_intervals,tv_no_sat_intervals,tv_no_sun_intervals;
    TextView tv_initial_diatance, tv_last_distance, tv_travel_distance,
            tv_header_monday, tv_initial_time, tv_last_time, tv_header_tuesday, tv_tues_initial_time, tv_tuesd_last_time,
            tv_wednesday_header_name, tv_wednes_initial_time, tv_wednes_last_time, tv_thr_initial_time, tv_thr_last_time, tv_header_thrusday,
            tv_fri_initial_time, tv_fri_last_time, tv_header_friday, tv_sat_initial_time, tv_sat_last_time, tv_sun_last_time, tv_sun_initial_time;
    ImageView iv_down_monday, iv_up_monday, iv_plus_monday, iv_down_tuesday, iv_up_tuesday, iv_plus_tuesday, iv_down_wesday, iv_up_wesday, iv_down_thrusday,
            iv_plus_wednesday, iv_plus_thrusday, iv_plus_friday, iv_plus_saturday, iv_plus_sunday,
            iv_up_thrusday, iv_down_friday, iv_up_friday, iv_down_saturday, iv_up_saturday, iv_down_sunday, iv_up_sunday;
    ViewGroup seekbar, seekbar_tuesday, seekbar_wednesday, seekbar_thrusday, seekbar_friday, seekbar_saturday, seekbar_sunday;
    CheckBox checkbox_on_ice, checkbox_off_ice, checkbox_3, checkbox_4, checkbox_5, checkbox_A, checkbox_B, checkbox_C, checkbox_D, checkbox_E;
    SeekBar seekbar_distance;
    LinearLayout ll_monday, ll_monday_child, ll_tuesday, ll_tuesday_child, ll_wednesday, ll_wednesday_child, ll_thursday, ll_thrusday_child,
            ll_friday, ll_friday_child, ll_saturday, ll_saturday_child, ll_sunday, ll_sunday_child;
    Boolean monday = false, tuesday = false, wednesday = false, thursday = false, friday = false, saturday = false, sunday = false;
    RangeSeekBar<Long> seekBarMonday;
    RangeSeekBar<Long> seekBarTuesday;
    RangeSeekBar<Long> seekBarWednesday;
    RangeSeekBar<Long> seekBarThursday;
    RangeSeekBar<Long> seekBarFriday;
    RangeSeekBar<Long> seekBarSaturday;
    RangeSeekBar<Long> seekBarSunday;
    ArrayList<String> gameTypeList = new ArrayList<String>();
    ArrayList<String> groundTypeList = new ArrayList<String>();
    ArrayList<String> calibreList = new ArrayList<String>();
    public static ArrayList<signup_time> timelist = new ArrayList<signup_time>();
    public static ArrayList<tuesday> tuesdaylist = new ArrayList<tuesday>();
    public static ArrayList<wednesday> wednesdaylist = new ArrayList<wednesday>();
    public static ArrayList<thursday> thursdaylist = new ArrayList<thursday>();
    public static ArrayList<com.mxi.goalkeeper.model.friday> fridaylist = new ArrayList<friday>();
    public static ArrayList<com.mxi.goalkeeper.model.saturday> saturdaylist = new ArrayList<saturday>();
    public static ArrayList<com.mxi.goalkeeper.model.sunday> sundaylist = new ArrayList<sunday>();
    RecyclerView RecyclerView_monday, RecyclerView_tuesday, RecyclerView_wednesday, RecyclerView_thursday, RecyclerView_friday, RecyclerView_saturday,
            RecyclerView_sunday;

    Button btn_next;
    long totalSize = 0;
    ProgressDialog progressDialog;
    CommanClass cc;
    SQLiteTD dbcom;
    String first_name, last_name, civic_no, country, province,email,
            confirm_email, password, confirm_password, youare, phone_number, goalie, goalie_rating, defence, defence_rating,
            forward, forward_rating, referee, referee_rating, selectedImagePath;
//    String apparment, street, city, postal_code;
    String address,lat,longi;
    String gender = "", mix_gender = "", DOB = "", game_type = "", ground_type = "", calibre = "", distance = "", gcmId = "";
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        cc = new CommanClass(SignUp.this);
        dbcom = new SQLiteTD(SignUp.this);
        timelist.clear();
        tuesdaylist.clear();
        wednesdaylist.clear();
        thursdaylist.clear();
        fridaylist.clear();
        saturdaylist.clear();
        sundaylist.clear();

        try {
            first_name = getIntent().getStringExtra("first_name");
            last_name = getIntent().getStringExtra("last_name");
            civic_no = getIntent().getStringExtra("civic_no");
//            apparment = getIntent().getStringExtra("apparment");
//            street = getIntent().getStringExtra("street");
//            city = getIntent().getStringExtra("city");
            address = getIntent().getStringExtra("address");
            lat = getIntent().getStringExtra("lat");
            longi= getIntent().getStringExtra("long");
            country = getIntent().getStringExtra("country");
            province = getIntent().getStringExtra("province");
//            postal_code = getIntent().getStringExtra("postal_code");
            email = getIntent().getStringExtra("email");
            confirm_email = getIntent().getStringExtra("confirm_email");
            password = getIntent().getStringExtra("password");
            confirm_password = getIntent().getStringExtra("confirm_password");
            youare = getIntent().getStringExtra("youare");
            phone_number = getIntent().getStringExtra("phone_number");
            goalie = getIntent().getStringExtra("goalie");
            goalie_rating = getIntent().getStringExtra("goalie_rating");
            defence = getIntent().getStringExtra("defence");
            defence_rating = getIntent().getStringExtra("defence_rating");
            forward = getIntent().getStringExtra("forward");
            forward_rating = getIntent().getStringExtra("forward_rating");
            referee = getIntent().getStringExtra("referee");
            referee_rating = getIntent().getStringExtra("referee_rating");
            selectedImagePath = getIntent().getStringExtra("selectedImagePath");
            DOB = getIntent().getStringExtra("DOB");
            gender = getIntent().getStringExtra("gender");
            gcmId = getIntent().getStringExtra("gcmId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        radio_mix_gender = (RadioGroup) findViewById(R.id.radio_mix_gender);

        tv_initial_diatance = (TextView) findViewById(R.id.tv_initial_diatance);
        tv_last_distance = (TextView) findViewById(R.id.tv_last_distance);
        tv_travel_distance = (TextView) findViewById(R.id.tv_travel_distance);

        tv_no_mon_intervals= (TextView) findViewById(R.id.tv_no_mon_intervals);
        tv_no_tue_intervals= (TextView) findViewById(R.id.tv_no_tue_intervals);
        tv_no_wed_intervals= (TextView) findViewById(R.id.tv_no_wed_intervals);
        tv_no_thurs_intervals= (TextView) findViewById(R.id.tv_no_thurs_intervals);
        tv_no_fri_intervals= (TextView) findViewById(R.id.tv_no_fri_intervals);
        tv_no_sat_intervals= (TextView) findViewById(R.id.tv_no_sat_intervals);
        tv_no_sun_intervals= (TextView) findViewById(R.id.tv_no_sun_intervals);
        iv_back= (ImageView) findViewById(R.id.iv_back);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        checkbox_on_ice = (CheckBox) findViewById(R.id.checkbox_on_ice);
        checkbox_off_ice = (CheckBox) findViewById(R.id.checkbox_off_ice);
        checkbox_3 = (CheckBox) findViewById(R.id.checkbox_3);
        checkbox_4 = (CheckBox) findViewById(R.id.checkbox_4);
        checkbox_5 = (CheckBox) findViewById(R.id.checkbox_5);
        checkbox_A = (CheckBox) findViewById(R.id.checkbox_A);
        checkbox_B = (CheckBox) findViewById(R.id.checkbox_B);
        checkbox_C = (CheckBox) findViewById(R.id.checkbox_C);
        checkbox_D = (CheckBox) findViewById(R.id.checkbox_D);
        checkbox_E = (CheckBox) findViewById(R.id.checkbox_E);

        seekbar_distance = (SeekBar) findViewById(R.id.seekbar_distance);

        tv_travel_distance.setText(getString(R.string.travel_distance) + " " + 0 + " " + "KM");
        radio_mix_gender.setOnCheckedChangeListener(this);

        checkbox_on_ice.setOnCheckedChangeListener(this);
        checkbox_off_ice.setOnCheckedChangeListener(this);
        checkbox_3.setOnCheckedChangeListener(this);
        checkbox_4.setOnCheckedChangeListener(this);
        checkbox_5.setOnCheckedChangeListener(this);
        checkbox_A.setOnCheckedChangeListener(this);
        checkbox_B.setOnCheckedChangeListener(this);
        checkbox_C.setOnCheckedChangeListener(this);
        checkbox_D.setOnCheckedChangeListener(this);
        checkbox_E.setOnCheckedChangeListener(this);

        ll_monday = (LinearLayout) findViewById(R.id.ll_monday);
        ll_monday_child = (LinearLayout) findViewById(R.id.ll_monday_child);
        ll_tuesday = (LinearLayout) findViewById(R.id.ll_tuesday);
        ll_tuesday_child = (LinearLayout) findViewById(R.id.ll_tuesday_child);
        ll_wednesday = (LinearLayout) findViewById(R.id.ll_wednesday);
        ll_wednesday_child = (LinearLayout) findViewById(R.id.ll_wednesday_child);
        ll_thursday = (LinearLayout) findViewById(R.id.ll_thursday);
        ll_thrusday_child = (LinearLayout) findViewById(R.id.ll_thursday_child);
        ll_friday = (LinearLayout) findViewById(R.id.ll_friday);
        ll_friday_child = (LinearLayout) findViewById(R.id.ll_friday_child);
        ll_saturday = (LinearLayout) findViewById(R.id.ll_saturday);
        ll_saturday_child = (LinearLayout) findViewById(R.id.ll_saturday_child);
        ll_sunday = (LinearLayout) findViewById(R.id.ll_sunday);
        ll_sunday_child = (LinearLayout) findViewById(R.id.ll_sunday_child);

        seekbar = (ViewGroup) findViewById(R.id.seekbar);
        seekbar_tuesday = (ViewGroup) findViewById(R.id.seekbar_tuesday);
        seekbar_wednesday = (ViewGroup) findViewById(R.id.seekbar_wednesday);
        seekbar_thrusday = (ViewGroup) findViewById(R.id.seekbar_thursday);
        seekbar_friday = (ViewGroup) findViewById(R.id.seekbar_friday);
        seekbar_saturday = (ViewGroup) findViewById(R.id.seekbar_saturday);
        seekbar_sunday = (ViewGroup) findViewById(R.id.seekbar_sunday);

        iv_down_monday = (ImageView) findViewById(R.id.iv_down_monday);
        iv_up_monday = (ImageView) findViewById(R.id.iv_up_monday);
        iv_plus_monday = (ImageView) findViewById(R.id.iv_plus_monday);
        iv_down_tuesday = (ImageView) findViewById(R.id.iv_down_tuesday);
        iv_up_tuesday = (ImageView) findViewById(R.id.iv_up_tuesday);
        iv_plus_tuesday = (ImageView) findViewById(R.id.iv_plus_tuesday);
        iv_down_wesday = (ImageView) findViewById(R.id.iv_down_wesday);
        iv_up_wesday = (ImageView) findViewById(R.id.iv_up_wesday);
        iv_plus_wednesday = (ImageView) findViewById(R.id.iv_plus_wednesday);
        iv_down_thrusday = (ImageView) findViewById(R.id.iv_down_thursday);
        iv_up_thrusday = (ImageView) findViewById(R.id.iv_up_thursday);
        iv_plus_thrusday = (ImageView) findViewById(R.id.iv_plus_thursday);
        iv_plus_friday = (ImageView) findViewById(R.id.iv_plus_friday);
        iv_down_friday = (ImageView) findViewById(R.id.iv_down_friday);
        iv_up_friday = (ImageView) findViewById(R.id.iv_up_friday);
        iv_plus_saturday = (ImageView) findViewById(R.id.iv_plus_saturday);
        iv_down_saturday = (ImageView) findViewById(R.id.iv_down_saturday);
        iv_up_saturday = (ImageView) findViewById(R.id.iv_up_saturday);
        iv_plus_sunday = (ImageView) findViewById(R.id.iv_plus_sunday);
        iv_down_sunday = (ImageView) findViewById(R.id.iv_down_sunday);
        iv_up_sunday = (ImageView) findViewById(R.id.iv_up_sunday);

        tv_header_monday = (TextView) findViewById(R.id.tv_header_monday);
        tv_initial_time = (TextView) findViewById(R.id.tv_initial_time);
        tv_last_time = (TextView) findViewById(R.id.tv_last_time);
        tv_header_tuesday = (TextView) findViewById(R.id.tv_header_tuesday);
        tv_tues_initial_time = (TextView) findViewById(R.id.tv_tues_initial_time);
        tv_tuesd_last_time = (TextView) findViewById(R.id.tv_tuesd_last_time);
        tv_wednesday_header_name = (TextView) findViewById(R.id.tv_wednesday_header_name);
        tv_wednes_initial_time = (TextView) findViewById(R.id.tv_wednes_initial_time);
        tv_wednes_last_time = (TextView) findViewById(R.id.tv_wednes_last_time);
        tv_thr_initial_time = (TextView) findViewById(R.id.tv_thr_initial_time);
        tv_thr_last_time = (TextView) findViewById(R.id.tv_thr_last_time);
        tv_header_thrusday = (TextView) findViewById(R.id.tv_header_thrusday);
        tv_fri_initial_time = (TextView) findViewById(R.id.tv_fri_initial_time);
        tv_fri_last_time = (TextView) findViewById(R.id.tv_fri_last_time);
        tv_header_friday = (TextView) findViewById(R.id.tv_header_friday);
        tv_sat_initial_time = (TextView) findViewById(R.id.tv_sat_initial_time);
        tv_sat_last_time = (TextView) findViewById(R.id.tv_sat_last_time);
        tv_sun_last_time = (TextView) findViewById(R.id.tv_sun_last_time);
        tv_sun_initial_time = (TextView) findViewById(R.id.tv_sun_initial_time);

        RecyclerView_monday = (RecyclerView) findViewById(R.id.RecyclerView_monday);
        RecyclerView_tuesday = (RecyclerView) findViewById(R.id.RecyclerView_tuesday);
        RecyclerView_wednesday = (RecyclerView) findViewById(R.id.RecyclerView_wednesday);
        RecyclerView_thursday = (RecyclerView) findViewById(R.id.RecyclerView_thursday);
        RecyclerView_friday = (RecyclerView) findViewById(R.id.RecyclerView_friday);
        RecyclerView_saturday = (RecyclerView) findViewById(R.id.RecyclerView_saturday);
        RecyclerView_sunday = (RecyclerView) findViewById(R.id.RecyclerView_sunday);

        btn_next = (Button) findViewById(R.id.btn_next);

        seekBarMonday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar.addView(seekBarMonday);
        seekBarTuesday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_tuesday.addView(seekBarTuesday);
        seekBarWednesday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_wednesday.addView(seekBarWednesday);
        seekBarThursday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_thrusday.addView(seekBarThursday);
        seekBarFriday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_friday.addView(seekBarFriday);
        seekBarSaturday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_saturday.addView(seekBarSaturday);
        seekBarSunday = new RangeSeekBar<Long>(0000L / 900000,
                86340000L / 900000, SignUp.this);
        seekbar_sunday.addView(seekBarSunday);

        seekBarMonday.setOnRangeSeekBarChangeListener(this);
        seekBarTuesday.setOnRangeSeekBarChangeListener(this);
        seekBarWednesday.setOnRangeSeekBarChangeListener(this);
        seekBarThursday.setOnRangeSeekBarChangeListener(this);
        seekBarFriday.setOnRangeSeekBarChangeListener(this);
        seekBarSaturday.setOnRangeSeekBarChangeListener(this);
        seekBarSunday.setOnRangeSeekBarChangeListener(this);

        seekbar_distance.setOnSeekBarChangeListener(this);

        ll_monday.setOnClickListener(this);
        ll_tuesday.setOnClickListener(this);
        ll_wednesday.setOnClickListener(this);
        ll_thursday.setOnClickListener(this);
        ll_friday.setOnClickListener(this);
        ll_saturday.setOnClickListener(this);
        ll_sunday.setOnClickListener(this);

        iv_plus_monday.setOnClickListener(this);
        iv_plus_tuesday.setOnClickListener(this);
        iv_plus_wednesday.setOnClickListener(this);
        iv_plus_thrusday.setOnClickListener(this);
        iv_plus_friday.setOnClickListener(this);
        iv_plus_saturday.setOnClickListener(this);
        iv_plus_sunday.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        btn_next.setOnClickListener(this);


    }

    private void SubmitSignUp() {
        if (!cc.isConnectingToInternet()) {
            cc.showSnackbar(scrollView, getString(R.string.no_internet));
        } else if (game_type.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.error_game_type));
        } else if (ground_type.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.error_ground_type));
        } else if (calibre.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_calibre));
        } else if (mix_gender.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_mix_gender));
        } else if (distance.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_tavel_distance));
        } else {
            //  if (timelist.size() != 0 || tuesdaylist.size() != 0 || thursdaylist.size() != 0 || fridaylist.size() != 0 || saturdaylist.size() != 0 || sundaylist.size() != 0) {
//                loadGroundAndGameType();
            new UploadFileToServer().execute();
            //    } else {
            //       cc.showSnackbar(scrollView, getString(R.string.select_time_interval));
            //   }
        }
    }

    /* private void loadGroundAndGameType() {

         if (!gameTypeList.isEmpty()) {
             gameTypeList.clear();
         }
         if (!groundTypeList.isEmpty()) {
             groundTypeList.clear();
         }

         if (checkbox_off_ice.isChecked()) {
             gameTypeList.add(checkbox_off_ice.getText().toString());
         }
         if (checkbox_on_ice.isChecked()) {
             gameTypeList.add(checkbox_on_ice.getText().toString());
         }
         if (checkbox_3.isChecked()) {
             groundTypeList.add(checkbox_3.getText().toString());
         }
         if (checkbox_5.isChecked()) {
             groundTypeList.add(checkbox_5.getText().toString());
         }

     }
 */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton choose_text = (RadioButton) group.findViewById(checkedId);
        switch (group.getId()) {
            case R.id.radio_choose_gender:
                gender = choose_text.getText().toString();
                // Toast.makeText(SignUp.this, choose_text.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_mix_gender:
                mix_gender = choose_text.getText().toString();
                // Toast.makeText(SignUp.this, choose_text.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkbox_on_ice:
//                checkbox_off_ice.setChecked(false);
//                checkbox_3.setChecked(false);
//                checkbox_5.setChecked(false);
                checkbox_on_ice.setChecked(isChecked);
                /*checkbox_A.setChecked(false);
                checkbox_B.setChecked(false);
                checkbox_C.setChecked(false);
                checkbox_D.setChecked(false);
                checkbox_E.setChecked(false);*/
                if (checkbox_on_ice.isChecked()) {
                    game_type = checkbox_on_ice.getText().toString();
                    gameTypeList.add(game_type);
                } else {
                    gameTypeList.remove(checkbox_on_ice.getText().toString());
                }

                // Toast.makeText(SignUp.this, checkbox_on_ice.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_off_ice:
//                checkbox_on_ice.setChecked(false);
//                checkbox_3.setChecked(false);
//                checkbox_5.setChecked(false);
                checkbox_off_ice.setChecked(isChecked);
               /* checkbox_A.setChecked(false);
                checkbox_B.setChecked(false);
                checkbox_C.setChecked(false);
                checkbox_D.setChecked(false);
                checkbox_E.setChecked(false);*/
                if (checkbox_off_ice.isChecked()) {
                    game_type = checkbox_off_ice.getText().toString();
                    gameTypeList.add(game_type);
                } else {
                    gameTypeList.remove(checkbox_off_ice.getText().toString());
                }

                break;
            case R.id.checkbox_3:
//                checkbox_on_ice.setChecked(false);
//                checkbox_off_ice.setChecked(false);
//                checkbox_5.setChecked(false);
                checkbox_3.setChecked(isChecked);
                /*checkbox_A.setChecked(false);
                checkbox_B.setChecked(false);
                checkbox_C.setChecked(false);
                checkbox_D.setChecked(false);
                checkbox_E.setChecked(false);*/
                if (checkbox_3.isChecked()) {
                    ground_type = checkbox_3.getText().toString();
                    // ground_type = "3*3";
                    groundTypeList.add(ground_type);
                } else {
                    groundTypeList.remove(checkbox_3.getText().toString());
                }

                break;
            case R.id.checkbox_4:
                if (checkbox_4.isChecked()) {
                    ground_type = checkbox_4.getText().toString();
                    groundTypeList.add(ground_type);
                } else {
                    groundTypeList.remove(checkbox_4.getText().toString());
                }
                break;
            case R.id.checkbox_5:
//                checkbox_on_ice.setChecked(false);
//                checkbox_off_ice.setChecked(false);
//                checkbox_3.setChecked(false);
                checkbox_5.setChecked(isChecked);
               /* checkbox_A.setChecked(false);
                checkbox_B.setChecked(false);
                checkbox_C.setChecked(false);
                checkbox_D.setChecked(false);
                checkbox_E.setChecked(false);*/
                if (checkbox_5.isChecked()) {
                    ground_type = checkbox_5.getText().toString();
                    groundTypeList.add(ground_type);
                } else {
                    groundTypeList.remove(checkbox_5.getText().toString());
                }
                break;
            case R.id.checkbox_A:
               /* checkbox_on_ice.setChecked(false);
                checkbox_off_ice.setChecked(false);
                checkbox_5.setChecked(false);
                checkbox_3.setChecked(false);*/
//                checkbox_B.setChecked(false);
//                checkbox_C.setChecked(false);
//                checkbox_D.setChecked(false);
//                checkbox_E.setChecked(false);
                checkbox_A.setChecked(isChecked);
                if (checkbox_A.isChecked()) {
                    calibre = checkbox_A.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_A.getText().toString());
                }

                break;
            case R.id.checkbox_B:
               /* checkbox_on_ice.setChecked(false);
                checkbox_off_ice.setChecked(false);
                checkbox_5.setChecked(false);
                checkbox_3.setChecked(false);*/
//                checkbox_A.setChecked(false);
//                checkbox_C.setChecked(false);
//                checkbox_D.setChecked(false);
//                checkbox_E.setChecked(false);
                checkbox_B.setChecked(isChecked);
                if (checkbox_B.isChecked()) {
                    calibre = checkbox_B.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_B.getText().toString());
                }
                break;
            case R.id.checkbox_C:
                /*checkbox_on_ice.setChecked(false);
                checkbox_off_ice.setChecked(false);
                checkbox_5.setChecked(false);
                checkbox_3.setChecked(false);*/
//                checkbox_A.setChecked(false);
//                checkbox_B.setChecked(false);
//                checkbox_D.setChecked(false);
//                checkbox_E.setChecked(false);
                checkbox_C.setChecked(isChecked);
                if (checkbox_C.isChecked()) {
                    calibre = checkbox_C.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_C.getText().toString());
                }
                break;
            case R.id.checkbox_D:
               /* checkbox_on_ice.setChecked(false);
                checkbox_off_ice.setChecked(false);
                checkbox_5.setChecked(false);
                checkbox_3.setChecked(false);*/
//                checkbox_A.setChecked(false);
//                checkbox_C.setChecked(false);
//                checkbox_B.setChecked(false);
//                checkbox_E.setChecked(false);
                checkbox_D.setChecked(isChecked);
                if (checkbox_D.isChecked()) {
                    calibre = checkbox_D.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_D.getText().toString());
                }
                break;
            case R.id.checkbox_E:
               /* checkbox_on_ice.setChecked(false);
                checkbox_off_ice.setChecked(false);
                checkbox_5.setChecked(false);
                checkbox_3.setChecked(false);*/
//                checkbox_A.setChecked(false);
//                checkbox_C.setChecked(false);
//                checkbox_D.setChecked(false);
//                checkbox_B.setChecked(false);
                checkbox_E.setChecked(isChecked);
                if (checkbox_E.isChecked()) {
                    calibre = checkbox_E.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_E.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_travel_distance.setText(getString(R.string.travel_distance) + " " + progress + "" + " " + "KM");
        distance = String.valueOf(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        LinearLayoutManager layoutManager = null;
        switch (v.getId()) {
            case R.id.ll_monday:

                if (!monday) {
                    ll_monday_child.setVisibility(View.VISIBLE);
                    iv_up_monday.setVisibility(View.VISIBLE);
                    iv_down_monday.setVisibility(View.GONE);
                    if(timelist.size() > 0){
                        tv_no_mon_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_mon_intervals.setVisibility(View.VISIBLE);
                    }
                    monday = true;
                } else {
                    ll_monday_child.setVisibility(View.GONE);
                    iv_down_monday.setVisibility(View.VISIBLE);
                    iv_up_monday.setVisibility(View.GONE);

                    monday = false;
                }
                break;

            case R.id.ll_tuesday:
                if (!tuesday) {
                    ll_tuesday_child.setVisibility(View.VISIBLE);
                    iv_up_tuesday.setVisibility(View.VISIBLE);
                    iv_down_tuesday.setVisibility(View.GONE);
                    if(tuesdaylist.size() > 0){
                        tv_no_tue_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_tue_intervals.setVisibility(View.VISIBLE);
                    }
                    tuesday = true;
                } else {
                    ll_tuesday_child.setVisibility(View.GONE);
                    iv_up_tuesday.setVisibility(View.GONE);
                    iv_down_tuesday.setVisibility(View.VISIBLE);
                    tuesday = false;
                }
                break;

            case R.id.ll_wednesday:
                if (!wednesday) {
                    ll_wednesday_child.setVisibility(View.VISIBLE);
                    iv_up_wesday.setVisibility(View.VISIBLE);
                    iv_down_wesday.setVisibility(View.GONE);
                    if(wednesdaylist.size() > 0){
                        tv_no_wed_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_wed_intervals.setVisibility(View.VISIBLE);
                    }
                    wednesday = true;
                } else {
                    ll_wednesday_child.setVisibility(View.GONE);
                    iv_up_wesday.setVisibility(View.GONE);
                    iv_down_wesday.setVisibility(View.VISIBLE);
                    wednesday = false;
                }
                break;

            case R.id.ll_thursday:
                if (!thursday) {
                    ll_thrusday_child.setVisibility(View.VISIBLE);
                    iv_up_thrusday.setVisibility(View.VISIBLE);
                    iv_down_thrusday.setVisibility(View.GONE);
                    if(thursdaylist.size() > 0){
                        tv_no_thurs_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_thurs_intervals.setVisibility(View.VISIBLE);
                    }
                    thursday = true;
                } else {
                    ll_thrusday_child.setVisibility(View.GONE);
                    iv_up_thrusday.setVisibility(View.GONE);
                    iv_down_thrusday.setVisibility(View.VISIBLE);
                    thursday = false;
                }
                break;
            case R.id.ll_friday:
                if (!friday) {
                    ll_friday_child.setVisibility(View.VISIBLE);
                    iv_up_friday.setVisibility(View.VISIBLE);
                    iv_down_friday.setVisibility(View.GONE);
                    friday = true;
                    if(fridaylist.size() > 0){
                        tv_no_fri_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_fri_intervals.setVisibility(View.VISIBLE);
                    }
                } else {
                    ll_friday_child.setVisibility(View.GONE);
                    iv_up_friday.setVisibility(View.GONE);
                    iv_down_friday.setVisibility(View.VISIBLE);
                    friday = false;
                }
                break;

            case R.id.ll_saturday:
                if (!saturday) {
                    ll_saturday_child.setVisibility(View.VISIBLE);
                    iv_up_saturday.setVisibility(View.VISIBLE);
                    iv_down_saturday.setVisibility(View.GONE);
                    if(saturdaylist.size() > 0){
                        tv_no_sat_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_sat_intervals.setVisibility(View.VISIBLE);
                    }
                    saturday = true;
                } else {
                    ll_saturday_child.setVisibility(View.GONE);
                    iv_up_saturday.setVisibility(View.GONE);
                    iv_down_saturday.setVisibility(View.VISIBLE);
                    saturday = false;
                }
                break;
            case R.id.ll_sunday:
                if (!sunday) {
                    ll_sunday_child.setVisibility(View.VISIBLE);
                    iv_up_sunday.setVisibility(View.VISIBLE);
                    iv_down_sunday.setVisibility(View.GONE);
                    if(sundaylist.size() > 0){
                        tv_no_sun_intervals.setVisibility(View.GONE);
                    }else{
                        tv_no_sun_intervals.setVisibility(View.VISIBLE);
                    }
                    sunday = true;
                } else {
                    ll_sunday_child.setVisibility(View.GONE);
                    iv_up_sunday.setVisibility(View.GONE);
                    iv_down_sunday.setVisibility(View.VISIBLE);
                    sunday = false;
                }
                break;
            case R.id.iv_plus_monday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);


                if (!timelist.isEmpty()) {
                    Availability();
                    RecyclerView_monday.setLayoutManager(layoutManager);
                    RecyclerView_monday.setAdapter(new SignupDayTime(SignUp.this, timelist));
                } else {
                    signup_time m = new signup_time();
                    m.setMax_time(tv_last_time.getText().toString().trim());
                    m.setMin_time(tv_initial_time.getText().toString().trim());
                    m.setId("monday");
                    timelist.add(m);
                    RecyclerView_monday.setLayoutManager(layoutManager);
                    RecyclerView_monday.setAdapter(new SignupDayTime(SignUp.this, timelist));
                }


                if(timelist.size() > 0){
                    tv_no_mon_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_mon_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_tuesday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);

                if (tuesdaylist.isEmpty()) {
                    tuesday t = new tuesday();
                    t.setMax_time(tv_tuesd_last_time.getText().toString().trim());
                    t.setMin_time(tv_tues_initial_time.getText().toString().trim());
                    tuesdaylist.add(t);
                    Log.e("tue_size", tuesdaylist.size() + "");
                    RecyclerView_tuesday.setAdapter(new SignupTuesday(SignUp.this, tuesdaylist));
                    RecyclerView_tuesday.setLayoutManager(layoutManager);

                } else {
                    /*String max = tv_tuesd_last_time.getText().toString().trim();
                    String min = tv_tues_initial_time.getText().toString().trim();

                    for (int i = 0; i < tuesdaylist.size(); i++) {
                        String mindata = tuesdaylist.get(i).getMin_time();
                        String maxdata = tuesdaylist.get(i).getMax_time();

                        if (maxdata.equals(max) || mindata.equals(min)) {
                            // cc.showToast("These time already is available");
                            max = "";
                            min = "";
                        } else if (maxdata.equals(max) && mindata.equals(min)) {
                            // cc.showToast("These time already is available!");
                            max = "";
                            min = "";
                        } else {
                            if (!max.equals("") || !min.equals("")) {
                                tuesday t = new tuesday();
                                t.setMax_time(max);
                                t.setMin_time(min);
                                tuesdaylist.add(t);
                                Collections.reverse(tuesdaylist);
                                //  Log.e("add", min);
                            }

                        }

                        max = "";
                        min = "";
                    }*/

                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_tuesd_last_time.getText().toString().trim();
                    String s_min = tv_tues_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < tuesdaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(tuesdaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(tuesdaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        tuesday t = new tuesday();
                        t.setMax_time(s_max);
                        t.setMin_time(s_min);
                        tuesdaylist.add(t);
                        Collections.reverse(tuesdaylist);
                    }
                    RecyclerView_tuesday.setAdapter(new SignupTuesday(SignUp.this, tuesdaylist));
                    RecyclerView_tuesday.setLayoutManager(layoutManager);
                }



                if(tuesdaylist.size() > 0){
                    tv_no_tue_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_tue_intervals.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.iv_plus_wednesday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);
                if (wednesdaylist.isEmpty()) {
                    wednesday w = new wednesday();
                    w.setMax_time(tv_wednes_last_time.getText().toString().trim());
                    w.setMin_time(tv_wednes_initial_time.getText().toString().trim());
                    wednesdaylist.add(w);
                    RecyclerView_wednesday.setAdapter(new SignupWednesday(SignUp.this, wednesdaylist));
                    RecyclerView_wednesday.setLayoutManager(layoutManager);
                } else {

                   /* String max = tv_wednes_last_time.getText().toString().trim();
                    String min = tv_wednes_initial_time.getText().toString().trim();

                    for (int i = 0; i < wednesdaylist.size(); i++) {
                        String mindata = wednesdaylist.get(i).getMin_time();
                        String maxdata = wednesdaylist.get(i).getMax_time();

                        if (maxdata.equals(max) || mindata.equals(min)) {
                            // cc.showToast("These time already is available");
                            max = "";
                            min = "";
                        } else if (maxdata.equals(max) && mindata.equals(min)) {
                            // cc.showToast("These time already is available!");
                            max = "";
                            min = "";
                        } else {
                            if (!max.equals("") || !min.equals("")) {
                                wednesday w = new wednesday();
                                w.setMax_time(max);
                                w.setMin_time(min);
                                wednesdaylist.add(w);
                                Collections.reverse(wednesdaylist);
                                //  Log.e("add", min);
                            }

                        }

                        max = "";
                        min = "";
                    }*/


                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_wednes_last_time.getText().toString().trim();
                    String s_min = tv_wednes_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < wednesdaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(wednesdaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(wednesdaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        wednesday w = new wednesday();
                        w.setMax_time(s_max);
                        w.setMin_time(s_min);
                        wednesdaylist.add(w);
                        Collections.reverse(wednesdaylist);
                    }


                    RecyclerView_wednesday.setAdapter(new SignupWednesday(SignUp.this, wednesdaylist));
                    RecyclerView_wednesday.setLayoutManager(layoutManager);
                }

                if(wednesdaylist.size() > 0){
                    tv_no_wed_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_wed_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_thursday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);

                if (thursdaylist.isEmpty()) {
                    thursday th = new thursday();
                    th.setMax_time(tv_thr_last_time.getText().toString().trim());
                    th.setMin_time(tv_thr_initial_time.getText().toString().trim());
                    thursdaylist.add(th);
                    RecyclerView_thursday.setAdapter(new SignupThursday(SignUp.this, thursdaylist));
                    RecyclerView_thursday.setLayoutManager(layoutManager);
                } else {
                   /* String max = tv_thr_last_time.getText().toString().trim();
                    String min = tv_thr_initial_time.getText().toString().trim();

                    for (int i = 0; i < thursdaylist.size(); i++) {
                        String mindata = thursdaylist.get(i).getMin_time();
                        String maxdata = thursdaylist.get(i).getMax_time();

                        if (maxdata.equals(max) || mindata.equals(min)) {
                            // cc.showToast("These time already is available");
                            max = "";
                            min = "";
                        } else if (maxdata.equals(max) && mindata.equals(min)) {
                            // cc.showToast("These time already is available!");
                            max = "";
                            min = "";
                        } else {
                            if (!max.equals("") || !min.equals("")) {
                                thursday th = new thursday();
                                th.setMax_time(max);
                                th.setMin_time(min);
                                thursdaylist.add(th);
                                Collections.reverse(thursdaylist);
                                //  Log.e("add", min);
                            }

                        }

                        max = "";
                        min = "";
                    }*/

                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_thr_last_time.getText().toString().trim();
                    String s_min = tv_thr_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < thursdaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(thursdaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(thursdaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        thursday th = new thursday();
                        th.setMax_time(s_max);
                        th.setMin_time(s_min);
                        thursdaylist.add(th);
                        Collections.reverse(thursdaylist);
                    }


                    RecyclerView_thursday.setAdapter(new SignupThursday(SignUp.this, thursdaylist));
                    RecyclerView_thursday.setLayoutManager(layoutManager);
                }

                if(thursdaylist.size() > 0){
                    tv_no_thurs_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_thurs_intervals.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.iv_plus_friday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);
                if (fridaylist.isEmpty()) {
                    friday f = new friday();
                    f.setMax_time(tv_fri_last_time.getText().toString().trim());
                    f.setMin_time(tv_fri_initial_time.getText().toString().trim());
                    fridaylist.add(f);
                    RecyclerView_friday.setAdapter(new SignupFriday(SignUp.this, fridaylist));
                    RecyclerView_friday.setLayoutManager(layoutManager);
                } else {

                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_fri_last_time.getText().toString().trim();
                    String s_min = tv_fri_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < fridaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(fridaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(fridaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        friday f = new friday();
                        f.setMax_time(s_max);
                        f.setMin_time(s_min);
                        fridaylist.add(f);
                        Collections.reverse(fridaylist);
                    }

                    RecyclerView_friday.setAdapter(new SignupFriday(SignUp.this, fridaylist));
                    RecyclerView_friday.setLayoutManager(layoutManager);
                }

                if(fridaylist.size() > 0){
                    tv_no_fri_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_fri_intervals.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.iv_plus_saturday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);
                if (saturdaylist.isEmpty()) {
                    saturday sat = new saturday();
                    sat.setMax_time(tv_sat_last_time.getText().toString().trim());
                    sat.setMin_time(tv_sat_initial_time.getText().toString().trim());
                    saturdaylist.add(sat);
                    RecyclerView_saturday.setAdapter(new SignupSaturday(SignUp.this, saturdaylist));
                    RecyclerView_saturday.setLayoutManager(layoutManager);
                } else {

                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_sat_last_time.getText().toString().trim();
                    String s_min = tv_sat_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < saturdaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(saturdaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(saturdaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        saturday sat = new saturday();
                        sat.setMax_time(s_max);
                        sat.setMin_time(s_min);
                        saturdaylist.add(sat);
                        Collections.reverse(saturdaylist);
                    }

                    RecyclerView_saturday.setAdapter(new SignupSaturday(SignUp.this, saturdaylist));
                    RecyclerView_saturday.setLayoutManager(layoutManager);
                }

                if(saturdaylist.size() > 0){
                    tv_no_sat_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_sat_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_sunday:

                layoutManager
                        = new LinearLayoutManager(SignUp.this, LinearLayoutManager.HORIZONTAL, false);
                if (sundaylist.isEmpty()) {
                    sunday sun = new sunday();
                    sun.setMax_time(tv_sun_last_time.getText().toString().trim());
                    sun.setMin_time(tv_sun_initial_time.getText().toString().trim());
                    sundaylist.add(sun);
                    RecyclerView_sunday.setAdapter(new SignUpSunday(SignUp.this, sundaylist));
                    RecyclerView_sunday.setLayoutManager(layoutManager);
                } else {

                    boolean stateFinal = true;
                    ArrayList<Boolean> booleanList = new ArrayList<>();
                    String s_max = tv_sun_last_time.getText().toString().trim();
                    String s_min = tv_sun_initial_time.getText().toString().trim();

                    int max = Integer.parseInt(s_min.replace(":", ""));
                    int min = Integer.parseInt(s_max.replace(":", ""));

                    for (int i = 0; i < sundaylist.size(); i++) {
                        boolean state = false;
                        int mindata = Integer.parseInt(sundaylist.get(i).getMin_time().replace(":", ""));
                        int maxdata = Integer.parseInt(sundaylist.get(i).getMax_time().replace(":", ""));

                        if (!(min >= mindata) || !(min <= maxdata)) {
                            if (min < mindata) {
                                if (max < mindata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            } else if (min > maxdata) {
                                if (max > maxdata) {
                                    state = true;
                                } else {
                                    state = false;
                                }
                            }
                        } else {
                            state = false;
                        }
                        booleanList.add(state);
                    }

                    for (int i = 0; i < booleanList.size(); i++) {

                        if (!booleanList.get(i)) {
                            stateFinal = false;
                        }
                    }

                    if (stateFinal) {
                        sunday s = new sunday();
                        s.setMax_time(s_max);
                        s.setMin_time(s_min);
                        sundaylist.add(s);
                        Collections.reverse(sundaylist);
                    }

                    RecyclerView_sunday.setAdapter(new SignUpSunday(SignUp.this, sundaylist));
                    RecyclerView_sunday.setLayoutManager(layoutManager);
                }

                if(sundaylist.size() > 0){
                    tv_no_sun_intervals.setVisibility(View.GONE);
                }else{
                    tv_no_sun_intervals.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.btn_next:
                SubmitSignUp();
                //startActivity(new Intent(SignUp.this, MainActivity.class));
                break;

            case R.id.iv_back:
                onBackPressed();
                //startActivity(new Intent(SignUp.this, MainActivity.class));
                break;


        }

    }

    private void Availability() {

        boolean stateFinal = true;
        ArrayList<Boolean> booleanList = new ArrayList<>();
        String s_max = tv_last_time.getText().toString().trim();
        String s_min = tv_initial_time.getText().toString().trim();

        int max = Integer.parseInt(s_min.replace(":", ""));
        int min = Integer.parseInt(s_max.replace(":", ""));

        for (int i = 0; i < timelist.size(); i++) {
            boolean state = false;
            int mindata = Integer.parseInt(timelist.get(i).getMin_time().replace(":", ""));
            int maxdata = Integer.parseInt(timelist.get(i).getMax_time().replace(":", ""));

            if (!(min >= mindata) || !(min <= maxdata)) {
                if (min < mindata) {
                    if (max < mindata) {
                        state = true;
                    } else {
                        state = false;
                    }
                } else if (min > maxdata) {
                    if (max > maxdata) {
                        state = true;
                    } else {
                        state = false;
                    }
                }
            } else {
                state = false;
            }
            booleanList.add(state);
        }

        for (int i = 0; i < booleanList.size(); i++) {

            if (!booleanList.get(i)) {
                stateFinal = false;
            }
        }

        if (stateFinal) {
            signup_time m = new signup_time();
            m.setMax_time(s_max);
            m.setMin_time(s_min);
            m.setId("monday");
            timelist.add(m);
            Collections.reverse(timelist);
        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long
            maxValue) {
        if (bar == seekBarMonday) {
            // Toast.makeText(getApplicationContext(), "m" + minValue + "", Toast.LENGTH_SHORT).show();
            // if(timelist)
            tv_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarTuesday) {
            // Toast.makeText(getApplicationContext(), "t" + minValue + "", Toast.LENGTH_SHORT).show();
            tv_tues_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_tuesd_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarWednesday) {
            //Toast.makeText(getApplicationContext(), "w" + minValue + "", Toast.LENGTH_SHORT).show();
            tv_wednes_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_wednes_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarThursday) {
            tv_thr_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_thr_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarFriday) {
            tv_fri_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_fri_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarSaturday) {
            tv_sat_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_sat_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarSunday) {
            tv_sun_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_sun_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        }


    }

    private String ConvertSecondsToHMmSs(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
               /* TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))*/);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        HttpClient httpclient;
        HttpPost httppost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SignUp.this);
            // pDialog.setMessage("Please wait...");
            progressDialog.show();
            //progressDialog.show();
            //  progressDialog.setContentView(R.layout.custom_progressdialog);
            // progressBar_tv = (TextView) progressDialog.findViewById(R.id.progressBar_tv);
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setMessage(String.valueOf("Loading..." + progress[0])
                    + " %");

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(URL.Url_signup);

            httppost.addHeader("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (!selectedImagePath.equals("")) {
                    File sourceFile = new File(selectedImagePath);
                    entity.addPart("image_name", new FileBody(sourceFile));
                }
                entity.addPart("first_name", new StringBody(first_name));
                entity.addPart("last_name", new StringBody(last_name));
                entity.addPart("civic_number", new StringBody(civic_no));
//                entity.addPart("apartment", new StringBody(apparment));
//                entity.addPart("street", new StringBody(street));
//                entity.addPart("city", new StringBody(city));
                entity.addPart("address", new StringBody(address));
                entity.addPart("lat", new StringBody(lat));
                entity.addPart("long", new StringBody(longi));
                 entity.addPart("state", new StringBody(province));
                entity.addPart("state_id", new StringBody(SignUpNext.stateId));//check============
                // entity.addPart("country", new StringBody(country));
                entity.addPart("country_id", new StringBody(SignUpNext.countryId));//check============
//                entity.addPart("postal_code", new StringBody(postal_code));
                entity.addPart("email", new StringBody(email));
                entity.addPart("phone_number", new StringBody(phone_number));
                entity.addPart("password", new StringBody(password));
                entity.addPart("login_type", new StringBody("App"));

                // if (youare.equals(getString(R.string.team_manager))) {
//                entity.addPart("calibre", new StringBody(calibre));
                entity.addPart("calibre[]", new StringBody(calibre));//check============
//                entity.addPart("game_type", new StringBody(game_type));
//                entity.addPart("ground_size", new StringBody(ground_type));//check============

                entity.addPart("travel_distance", new StringBody(distance));

                Log.e("groundTypeList.size()", groundTypeList.size() + "");
                Log.e("calibreList.size()", calibreList.size() + "");
                Log.e("gameTypeList.size()", gameTypeList.size() + "");
                ArrayList<String> groundTypeList1 = new ArrayList<String>();

                if (groundTypeList.size() > 0) {
                    for (int i = 0; i < groundTypeList.size(); i++) {
                        if (groundTypeList.get(i).equals("3X3")) {
                            groundTypeList1.add("3*3");
                        } else if (groundTypeList.get(i).equals("4X4")) {
                            groundTypeList1.add("4*4");
                        }
                        if (groundTypeList.get(i).equals("5X5")) {
                            groundTypeList1.add("5*5");
                        }
                        entity.addPart("ground_size[]", new StringBody("" + groundTypeList1.get(i)));
                        Log.e("ground_size[" + i + "]", groundTypeList1.get(i));
                    }
                }

                if (gameTypeList.size() > 0) {
                    for (int i = 0; i < gameTypeList.size(); i++) {
                        entity.addPart("game_type[]", new StringBody("" + gameTypeList.get(i)));
                        Log.e("game_type[" + i + "]", gameTypeList.get(i));
                    }
                }

                if (calibreList.size() > 0) {
                    for (int i = 0; i < calibreList.size(); i++) {
                        entity.addPart("calibre[]", new StringBody("" + calibreList.get(i)));
                        Log.e("calibre[" + i + "]", calibreList.get(i));
                    }
                }

                if (timelist.size() > 0) {
                    for (int i = 0; i < timelist.size(); i++) {
                        entity.addPart("monday[]", new StringBody("" + timelist.get(i).getMin_time() + "-" + timelist.get(i).getMax_time()));
                    }
                }

                if (tuesdaylist.size() > 0) {
                    for (int i = 0; i < tuesdaylist.size(); i++) {
                        entity.addPart("tuesday[]", new StringBody("" + tuesdaylist.get(i).getMin_time() + "-" + tuesdaylist.get(i).getMax_time()));
                    }
                }

                if (wednesdaylist.size() > 0) {
                    for (int i = 0; i < wednesdaylist.size(); i++) {
                        entity.addPart("wednesday[]", new StringBody("" + wednesdaylist.get(i).getMin_time() + "-" + wednesdaylist.get(i).getMax_time()));
                    }
                }

                if (thursdaylist.size() > 0) {
                    for (int i = 0; i < thursdaylist.size(); i++) {
                        entity.addPart("thursday[]", new StringBody("" + thursdaylist.get(i).getMin_time() + "-" + thursdaylist.get(i).getMax_time()));
                    }
                }

                if (fridaylist.size() > 0) {
                    for (int i = 0; i < fridaylist.size(); i++) {
                        entity.addPart("friday[]", new StringBody("" + fridaylist.get(i).getMin_time() + "-" + fridaylist.get(i).getMax_time()));
                    }
                }

                if (saturdaylist.size() > 0) {
                    for (int i = 0; i < saturdaylist.size(); i++) {
                        entity.addPart("saturday[]", new StringBody("" + saturdaylist.get(i).getMin_time() + "-" + saturdaylist.get(i).getMax_time()));
                    }
                }

                if (sundaylist.size() > 0) {
                    for (int i = 0; i < sundaylist.size(); i++) {
                        entity.addPart("sunday[]", new StringBody("" + sundaylist.get(i).getMin_time() + "-" + sundaylist.get(i).getMax_time()));
                    }
                }

                entity.addPart("mixedgender", new StringBody(mix_gender));

                //  }
                entity.addPart("gender", new StringBody(gender));

                Log.e("youare", youare);
                if (!youare.equals("Player")) {
                    entity.addPart("user_type[]", new StringBody(youare));
                }

                if (SignUpNext.b_goalie) {
                    entity.addPart("user_type[]", new StringBody(goalie));
                    entity.addPart("goalie", new StringBody(goalie_rating));
                    Log.e("goalie_rating", goalie + "" + goalie_rating);
                }

                if (SignUpNext.b_defense) {
                    entity.addPart("user_type[]", new StringBody(defence));
                    entity.addPart("defence", new StringBody(defence_rating));
                    Log.e("defence_rating", defence + "" + defence_rating);

                }

                if (SignUpNext.b_striker) {
                    entity.addPart("user_type[]", new StringBody(forward));
                    entity.addPart("forward", new StringBody(forward_rating));
                    Log.e("forward_rating", forward + "" + forward_rating);
                }

                if (SignUpNext.b_referee) {
                    entity.addPart("user_type[]", new StringBody(referee));
                    entity.addPart("referee", new StringBody(referee_rating));
                    Log.e("referee_rating", referee + "" + referee_rating);
                }

                Log.e("@@BOD",DOB+"");
                entity.addPart("birth_date", new StringBody(DOB));
                entity.addPart("device_id", new StringBody(cc.loadPrefString("device_id")));
                entity.addPart("device_type", new StringBody("Android"));
                entity.addPart("type_id", new StringBody(gcmId));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();


                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    return responseString;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Register: result", "Response from server: " + result);
            try {
                progressDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {
                    cc.showToast(jObject.getString("message"));
                    cc.savePrefString("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                    JSONObject user_data = jObject.getJSONObject("user_data");
                    cc.savePrefString("user_token", user_data.getString("user_token"));
                    cc.savePrefString("user_type_count", user_data.getString("user_type_count"));
                    cc.savePrefString("user_id", user_data.getString("user_id"));
                    cc.savePrefString("you_are", user_data.getString("you are"));
                    cc.savePrefString("first_name", user_data.getString("first_name"));
                    cc.savePrefString("last_name", user_data.getString("last_name"));
                    cc.savePrefString("email", user_data.getString("email"));
                    cc.savePrefString("civic_number", user_data.getString("civic_number"));
//                    cc.savePrefString("apartment", user_data.getString("apartment"));
//                    cc.savePrefString("street", user_data.getString("street"));
//                    cc.savePrefString("city", user_data.getString("city"));
                    cc.savePrefString("address", user_data.getString("address"));
                    cc.savePrefString("lat", user_data.getString("lat"));
                    cc.savePrefString("long", user_data.getString("long"));
                    cc.savePrefString("state_id", user_data.getString("state_id"));
                    cc.savePrefString("country_id", user_data.getString("country_id"));
//                    cc.savePrefString("postal_code", user_data.getString("postal_code"));
                    cc.savePrefString("phone_number", user_data.getString("phone_number"));
                    cc.savePrefString("gender", user_data.getString("gender"));
                    cc.savePrefString("birth_date", user_data.getString("birth_date"));
                    cc.savePrefString("mixedgender", user_data.getString("mixedgender"));
                    cc.savePrefString("login_type", user_data.getString("login_type"));
                    cc.savePrefString("game_type", user_data.getString("game_type"));
                    cc.savePrefString("ground_size", user_data.getString("ground_size"));
                    cc.savePrefString("profile_pic", user_data.getString("Image"));
                    cc.savePrefString("calibre", user_data.getString("calibre"));
                    cc.savePrefString("travel_distance", user_data.getString("travel_distance"));
                    cc.savePrefString("social_login_id", user_data.getString("social_login_id"));
                    cc.savePrefString("verification_code", user_data.getString("verification_code"));
                    cc.savePrefString("status", user_data.getString("status"));
                    cc.savePrefString("created_date", user_data.getString("created_date"));
                    cc.savePrefString("modified_date", user_data.getString("modified_date"));

                    cc.savePrefString("user_type", user_data.getString("user_type"));

                    List<String> user_list = Arrays.asList(user_data.getString("user_type").split(","));
                    for (int j = 0; j < user_list.size(); j++) {

                        if (user_list.get(j).equals("Team_Manager") || user_list.get(j).equals("Team Manager")) {

                            cc.savePrefString("isManager", "Team_Manager");
                        }
                    }
                    cc.savePrefString("self_rate", user_data.getString("self_rate"));

                    JSONArray week_day = user_data.getJSONArray("week_day");
                    for (int i = 0; i < week_day.length(); i++) {
                        JSONObject week = week_day.getJSONObject(i);

                        dbcom.nseartAvailabilities(week.getString("week_day"), week.getString("start_time"),
                                week.getString("end_time"),cc.loadPrefString("user_id"));
                    }
                    cc.savePrefBoolean("islogin", true);
                    cc.showToast(jObject.getString("message"));
                    Intent mIntent = new Intent(SignUp.this,
                            MainActivity.class);
                    startActivity(mIntent);
                    finish();

                } else if (jObject.getString("status").equals("500")) {
                    cc.showToast(getResources().getString(R.string.invalidate_param));
                } else {
                    cc.showSnackbar(scrollView, jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
