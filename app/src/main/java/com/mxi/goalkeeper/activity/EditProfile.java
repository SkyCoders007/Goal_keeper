package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.efor18.rangeseekbar.RangeSeekBar;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.adapter.SignUpSunday;
import com.mxi.goalkeeper.adapter.SignupDayTime;
import com.mxi.goalkeeper.adapter.SignupFriday;
import com.mxi.goalkeeper.adapter.SignupSaturday;
import com.mxi.goalkeeper.adapter.SignupThursday;
import com.mxi.goalkeeper.adapter.SignupTuesday;
import com.mxi.goalkeeper.adapter.SignupWednesday;
import com.mxi.goalkeeper.database.SQLiteTD;
import com.mxi.goalkeeper.model.availabilites_week;
import com.mxi.goalkeeper.model.country;
import com.mxi.goalkeeper.model.friday;
import com.mxi.goalkeeper.model.saturday;
import com.mxi.goalkeeper.model.signup_time;
import com.mxi.goalkeeper.model.sunday;
import com.mxi.goalkeeper.model.thursday;
import com.mxi.goalkeeper.model.tuesday;
import com.mxi.goalkeeper.model.wednesday;
import com.mxi.goalkeeper.network.AndroidMultiPartEntity;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;
import com.squareup.picasso.Picasso;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mxi.goalkeeper.activity.Splash.countryList;

public class EditProfile extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
        RangeSeekBar.OnRangeSeekBarChangeListener<Long>, CompoundButton.OnCheckedChangeListener {


    Calendar cal;
    int to_year, to_month, to_day;

    public boolean b_goalie = false, b_striker = false, b_defense = false, b_referee = false;
    int PLACE_PICKER_REQUEST = 1;
    TextView tv_no_mon_intervals, tv_no_tue_intervals, tv_no_wed_intervals, tv_no_thurs_intervals, tv_no_fri_intervals, tv_no_sat_intervals, tv_no_sun_intervals;
    Toolbar toolbar;
    EditText et_first_name, et_last_name, et_civic_no, et_email, et_phone_number, et_confirm_password;
    //    EditText et_apartment, et_street, et_city, et_postal_code;
    TextView tv_address, tv_year, tv_month, tv_day;
    EditText et_current_password, et_old_password;
    TextView tv_initial_diatance, tv_last_distance, tv_travel_distance, tv_rating_info,
            tv_header_monday, tv_initial_time, tv_last_time, tv_header_tuesday, tv_tues_initial_time, tv_tuesd_last_time,
            tv_wednesday_header_name, tv_wednes_initial_time, tv_wednes_last_time, tv_thr_initial_time, tv_thr_last_time, tv_header_thrusday,
            tv_fri_initial_time, tv_fri_last_time, tv_header_friday, tv_sat_initial_time, tv_sat_last_time, tv_sun_last_time, tv_sun_initial_time;
    ImageView iv_down_monday, iv_up_monday, iv_plus_monday, iv_down_tuesday, iv_up_tuesday, iv_plus_tuesday, iv_down_wesday, iv_up_wesday, iv_down_thrusday,
            iv_plus_wednesday, iv_plus_thrusday, iv_plus_friday, iv_plus_saturday, iv_plus_sunday,
            iv_up_thrusday, iv_down_friday, iv_up_friday, iv_down_saturday, iv_up_saturday, iv_down_sunday, iv_up_sunday;
    ViewGroup seekbar, seekbar_tuesday, seekbar_wednesday, seekbar_thrusday, seekbar_friday, seekbar_saturday, seekbar_sunday;
    CheckBox checkbox_ice_hockey, checkbox_ball_hockey, checkbox_3, checkbox_4, checkbox_5, checkbox_A, checkbox_B, checkbox_C, checkbox_D, checkbox_E;
    CheckBox checkbox_goalie, checkbox_defence, checkbox_forward, checkbox_referee;
    LinearLayout ll_monday, ll_monday_child, ll_tuesday, ll_tuesday_child, ll_wednesday, ll_wednesday_child, ll_thursday, ll_thrusday_child,
            ll_friday, ll_friday_child, ll_saturday, ll_saturday_child, ll_sunday, ll_sunday_child;
    RadioGroup radio_mix_gender, radio_goalie_rating, radio_defence_rating, radio_forward_rating, radio_referee_rating, radio_choose_player, radio_choose_gender;
    SeekBar seekbar_distance;
    String title;
    TextView et_province, et_country;
    String countryId = "";
    String stateId = "";
    ArrayList<country> stateList = new ArrayList<>();
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
    public static ArrayList<com.mxi.goalkeeper.model.tuesday> tuesdaylist = new ArrayList<tuesday>();
    public static ArrayList<com.mxi.goalkeeper.model.wednesday> wednesdaylist = new ArrayList<wednesday>();
    public static ArrayList<com.mxi.goalkeeper.model.thursday> thursdaylist = new ArrayList<thursday>();
    public static ArrayList<com.mxi.goalkeeper.model.friday> fridaylist = new ArrayList<com.mxi.goalkeeper.model.friday>();
    public static ArrayList<com.mxi.goalkeeper.model.saturday> saturdaylist = new ArrayList<com.mxi.goalkeeper.model.saturday>();
    public static ArrayList<com.mxi.goalkeeper.model.sunday> sundaylist = new ArrayList<com.mxi.goalkeeper.model.sunday>();
    RecyclerView RecyclerView_monday, RecyclerView_tuesday, RecyclerView_wednesday, RecyclerView_thursday, RecyclerView_friday, RecyclerView_saturday,
            RecyclerView_sunday;
    CommanClass cc;
    ProgressDialog pDialog;
    LinearLayout ll_linear, ll_caliber, ll_team_size, ll_game_type, ll_team_manager, ll_goalie,
            ll_defence, ll_forward, ll_referee, ll_availabilites, ll_mixed_gender, ll_travel_distance;
    RoundedImageView iv_profile;
    String mix_gender = "", game_type = "", ground_type = "", calibre = "", distance = "";
    RadioButton radio_yes, radio_no, radioA, radioB, radioC, radioD, radioE, radio_defenceA, radio_defenceB, radio_defenceC, radio_defenceD, radio_defenceE,
            radio_forwardA, radio_forwardB, radio_forwardC, radio_forwardD, radio_forwardE, radio_refereeA, radio_refereeB, radio_refereeC,
            radio_refereeD, radio_refereeE, radio_player, radio_team_manager;
    String DOB = "", gender = "", goalie = "", goalie_rating = "", defence = "", defence_rating = "", forward = "", forward_rating = "", referee = "",
            referee_rating = "";
    long totalSize = 0;
    SQLiteTD dbcom;
    RadioButton radio_male, radio_female;
    ArrayList<availabilites_week> weeklist;
    private static final int SELECT_PICTURE = 2;
    private String selectedImagePath = "", youare = "";
    double longitude;
    double latitude;
    String gcmId, address = "";
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("reviews");
        cc = new CommanClass(EditProfile.this);
        dbcom = new SQLiteTD(EditProfile.this);


        address = cc.loadPrefString("address");
        if(!cc.loadPrefString("lat").equals("")){
            latitude = Double.parseDouble(cc.loadPrefString("lat"));
        }else{
            latitude=0.0;
        }

        if(!cc.loadPrefString("long").equals("")){
            longitude = Double.parseDouble(cc.loadPrefString("long"));
        }else{
            longitude=0.0;
        }



        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (title.equals("My Profile")) {
            setContentView(R.layout.activity_edit_profile);
            Typeface font = Typeface.createFromAsset(getAssets(), "font/Questrial-Regular.otf");
/*            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            autocompleteFragment.setHint("Address");*/


            /*googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();*/

            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();


/*            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.e("Sign Up", "Place: " + place.getName());
                    Log.e("Sign Up", "All_Place: " + place + "");

                    LatLng queriedLocation = place.getLatLng();
                    address = place.getName() + ", " + place.getAddress() + "";

                    Log.e("Sign Up", "address: " + address + "");

                    try {
                        longitude = Double.parseDouble(queriedLocation.longitude + "");
                        latitude = Double.parseDouble(queriedLocation.latitude + "");
                    } catch (NumberFormatException e) {
                        Log.e("error", e.getMessage() + "");
                    }
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("Post Games", "An error occurred: " + status);
                }
            });*/


            timelist.clear();
            tuesdaylist.clear();
            wednesdaylist.clear();
            thursdaylist.clear();
            fridaylist.clear();
            saturdaylist.clear();
            sundaylist.clear();

            tv_no_mon_intervals = (TextView) findViewById(R.id.tv_no_mon_intervals);
            tv_no_tue_intervals = (TextView) findViewById(R.id.tv_no_tue_intervals);
            tv_no_wed_intervals = (TextView) findViewById(R.id.tv_no_wed_intervals);
            tv_no_thurs_intervals = (TextView) findViewById(R.id.tv_no_thurs_intervals);
            tv_no_fri_intervals = (TextView) findViewById(R.id.tv_no_fri_intervals);
            tv_no_sat_intervals = (TextView) findViewById(R.id.tv_no_sat_intervals);
            tv_no_sun_intervals = (TextView) findViewById(R.id.tv_no_sun_intervals);

            et_first_name = (EditText) findViewById(R.id.et_first_name);
            et_phone_number = (EditText) findViewById(R.id.et_phone_number);
            et_last_name = (EditText) findViewById(R.id.et_last_name);
            et_civic_no = (EditText) findViewById(R.id.et_civic_no);
//            et_apartment = (EditText) findViewById(R.id.et_apartment);
//            et_street = (EditText) findViewById(R.id.et_street);
//            et_city = (EditText) findViewById(R.id.et_city);
            et_country = (TextView) findViewById(R.id.et_country);
            et_province = (TextView) findViewById(R.id.et_province);

            tv_rating_info = (TextView) findViewById(R.id.tv_rating_info);
            tv_year = (TextView) findViewById(R.id.tv_year);
            tv_month = (TextView) findViewById(R.id.tv_month);
            tv_day = (TextView) findViewById(R.id.tv_day);
//            et_postal_code = (EditText) findViewById(R.id.et_postal_code);
            et_email = (EditText) findViewById(R.id.et_email);

            ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
            ll_caliber = (LinearLayout) findViewById(R.id.ll_caliber);
            ll_team_size = (LinearLayout) findViewById(R.id.ll_team_size);
            ll_game_type = (LinearLayout) findViewById(R.id.ll_game_type);
            ll_availabilites = (LinearLayout) findViewById(R.id.ll_availabilites);
            ll_mixed_gender = (LinearLayout) findViewById(R.id.ll_mixed_gender);
            ll_travel_distance = (LinearLayout) findViewById(R.id.ll_travel_distance);

            iv_profile = (RoundedImageView) findViewById(R.id.iv_profile);

            radio_yes = (RadioButton) findViewById(R.id.radio_yes);
            radio_no = (RadioButton) findViewById(R.id.radio_no);
            radio_female = (RadioButton) findViewById(R.id.radio_female);
            radio_male = (RadioButton) findViewById(R.id.radio_male);

            radio_mix_gender = (RadioGroup) findViewById(R.id.radio_mix_gender);
            radio_goalie_rating = (RadioGroup) findViewById(R.id.radio_goalie_rating);
            radio_defence_rating = (RadioGroup) findViewById(R.id.radio_defence_rating);
            radio_forward_rating = (RadioGroup) findViewById(R.id.radio_forward_rating);
            radio_referee_rating = (RadioGroup) findViewById(R.id.radio_referee_rating);
            radio_choose_player = (RadioGroup) findViewById(R.id.radio_choose_player);
            radio_choose_gender = (RadioGroup) findViewById(R.id.radio_choose_gender);

            tv_initial_diatance = (TextView) findViewById(R.id.tv_initial_diatance);
            tv_last_distance = (TextView) findViewById(R.id.tv_last_distance);
            tv_travel_distance = (TextView) findViewById(R.id.tv_travel_distance);

            tv_address = (TextView) findViewById(R.id.tv_address);

            checkbox_ice_hockey = (CheckBox) findViewById(R.id.checkbox_ice_hockey);
            checkbox_ball_hockey = (CheckBox) findViewById(R.id.checkbox_ball_hockey);
            checkbox_3 = (CheckBox) findViewById(R.id.checkbox_3);
            checkbox_4 = (CheckBox) findViewById(R.id.checkbox_4);
            checkbox_5 = (CheckBox) findViewById(R.id.checkbox_5);
            checkbox_A = (CheckBox) findViewById(R.id.checkbox_A);
            checkbox_B = (CheckBox) findViewById(R.id.checkbox_B);
            checkbox_C = (CheckBox) findViewById(R.id.checkbox_C);
            checkbox_D = (CheckBox) findViewById(R.id.checkbox_D);
            checkbox_E = (CheckBox) findViewById(R.id.checkbox_E);

            checkbox_goalie = (CheckBox) findViewById(R.id.checkbox_goalie);
            checkbox_defence = (CheckBox) findViewById(R.id.checkbox_defence);
            checkbox_forward = (CheckBox) findViewById(R.id.checkbox_forward);
            checkbox_referee = (CheckBox) findViewById(R.id.checkbox_referee);


            checkbox_goalie.setTypeface(font);
            checkbox_defence.setTypeface(font);
            checkbox_forward.setTypeface(font);
            checkbox_referee.setTypeface(font);

            seekbar_distance = (SeekBar) findViewById(R.id.seekbar_distance);

            tv_travel_distance.setText(getString(R.string.travel_distance) + " " + 0 + " " + "KM");

            radioA = (RadioButton) findViewById(R.id.radioA);
            radioB = (RadioButton) findViewById(R.id.radioB);
            radioC = (RadioButton) findViewById(R.id.radioC);
            radioD = (RadioButton) findViewById(R.id.radioD);
            radioE = (RadioButton) findViewById(R.id.radioE);
            radio_defenceA = (RadioButton) findViewById(R.id.radio_defenceA);
            radio_defenceB = (RadioButton) findViewById(R.id.radio_defenceB);
            radio_defenceC = (RadioButton) findViewById(R.id.radio_defenceC);
            radio_defenceD = (RadioButton) findViewById(R.id.radio_defenceD);
            radio_defenceE = (RadioButton) findViewById(R.id.radio_defenceE);
            radio_forwardA = (RadioButton) findViewById(R.id.radio_forwardA);
            radio_forwardB = (RadioButton) findViewById(R.id.radio_forwardB);
            radio_forwardC = (RadioButton) findViewById(R.id.radio_forwardC);
            radio_forwardD = (RadioButton) findViewById(R.id.radio_forwardD);
            radio_forwardE = (RadioButton) findViewById(R.id.radio_forwardE);
            radio_refereeA = (RadioButton) findViewById(R.id.radio_refereeA);
            radio_refereeB = (RadioButton) findViewById(R.id.radio_refereeB);
            radio_refereeC = (RadioButton) findViewById(R.id.radio_refereeC);
            radio_refereeD = (RadioButton) findViewById(R.id.radio_refereeD);
            radio_refereeE = (RadioButton) findViewById(R.id.radio_refereeE);
            radio_team_manager = (RadioButton) findViewById(R.id.radio_team_manager);
            radio_player = (RadioButton) findViewById(R.id.radio_player);
            ll_team_manager = (LinearLayout) findViewById(R.id.ll_team_manager);
            // ll_caliber, ll_team_size, ll_game_type, ll_team_manager, ll_goalie,
            // ll_defence, ll_forward, ll_referee,ll_availabilites
            if (cc.loadPrefString("you_are").equals("Player")) {
                radio_player.setChecked(true);
                youare = "Player";
                ll_team_manager.setVisibility(View.VISIBLE);
                ll_availabilites.setVisibility(View.VISIBLE);
                ll_caliber.setVisibility(View.VISIBLE);
                ll_team_size.setVisibility(View.VISIBLE);
                ll_game_type.setVisibility(View.VISIBLE);
                ll_mixed_gender.setVisibility(View.VISIBLE);
                ll_travel_distance.setVisibility(View.VISIBLE);
            } else if (cc.loadPrefString("you_are").equals("Team Manager")) {
                radio_team_manager.setChecked(true);
                youare = "Team_Manager";
                ll_team_manager.setVisibility(View.GONE);
                ll_availabilites.setVisibility(View.GONE);
                ll_caliber.setVisibility(View.GONE);
                ll_team_size.setVisibility(View.GONE);
                ll_game_type.setVisibility(View.GONE);
                ll_mixed_gender.setVisibility(View.GONE);
                ll_travel_distance.setVisibility(View.GONE);
            }
            //youare;

            ll_team_manager = (LinearLayout) findViewById(R.id.ll_team_manager);
            ll_goalie = (LinearLayout) findViewById(R.id.ll_goalie);
            ll_defence = (LinearLayout) findViewById(R.id.ll_defence);
            ll_forward = (LinearLayout) findViewById(R.id.ll_forward);
            ll_referee = (LinearLayout) findViewById(R.id.ll_referee);

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
           /* try {
                if (Integer.parseInt(cc.loadPrefString("user_type_count")) == 1 && cc.loadPrefString("isManager").equals("Team_Manager")) {

                    ll_caliber.setVisibility(View.GONE);
                    ll_team_size.setVisibility(View.GONE);
                    ll_game_type.setVisibility(View.GONE);
                    ll_team_manager.setVisibility(View.GONE);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
*/
            seekBarMonday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar.addView(seekBarMonday);
            seekBarTuesday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_tuesday.addView(seekBarTuesday);
            seekBarWednesday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_wednesday.addView(seekBarWednesday);
            seekBarThursday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_thrusday.addView(seekBarThursday);
            seekBarFriday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_friday.addView(seekBarFriday);
            seekBarSaturday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_saturday.addView(seekBarSaturday);
            seekBarSunday = new RangeSeekBar<Long>(0000L / 900000,
                    86340000L / 900000, EditProfile.this);
            seekbar_sunday.addView(seekBarSunday);

            seekBarMonday.setOnRangeSeekBarChangeListener(this);
            seekBarTuesday.setOnRangeSeekBarChangeListener(this);
            seekBarWednesday.setOnRangeSeekBarChangeListener(this);
            seekBarThursday.setOnRangeSeekBarChangeListener(this);
            seekBarFriday.setOnRangeSeekBarChangeListener(this);
            seekBarSaturday.setOnRangeSeekBarChangeListener(this);
            seekBarSunday.setOnRangeSeekBarChangeListener(this);

            seekbar_distance.setOnSeekBarChangeListener(this);

            iv_profile.setOnClickListener(this);
            ll_monday.setOnClickListener(this);
            ll_tuesday.setOnClickListener(this);
            ll_wednesday.setOnClickListener(this);
            ll_thursday.setOnClickListener(this);
            ll_friday.setOnClickListener(this);
            ll_saturday.setOnClickListener(this);
            ll_sunday.setOnClickListener(this);
            et_country.setOnClickListener(this);
            et_province.setOnClickListener(this);

            iv_plus_monday.setOnClickListener(this);
            iv_plus_tuesday.setOnClickListener(this);
            iv_plus_wednesday.setOnClickListener(this);
            iv_plus_thrusday.setOnClickListener(this);
            iv_plus_friday.setOnClickListener(this);
            iv_plus_saturday.setOnClickListener(this);
            iv_plus_sunday.setOnClickListener(this);
            tv_rating_info.setOnClickListener(this);
            tv_address.setOnClickListener(this);

            tv_year.setOnClickListener(this);
            tv_month.setOnClickListener(this);
            tv_day.setOnClickListener(this);

            radio_goalie_rating.setOnCheckedChangeListener(this);
            radio_defence_rating.setOnCheckedChangeListener(this);
            radio_forward_rating.setOnCheckedChangeListener(this);
            radio_referee_rating.setOnCheckedChangeListener(this);
            radio_mix_gender.setOnCheckedChangeListener(this);
            radio_choose_player.setOnCheckedChangeListener(this);

            checkbox_ice_hockey.setOnCheckedChangeListener(this);
            checkbox_ball_hockey.setOnCheckedChangeListener(this);
            checkbox_3.setOnCheckedChangeListener(this);
            checkbox_4.setOnCheckedChangeListener(this);
            checkbox_5.setOnCheckedChangeListener(this);
            checkbox_A.setOnCheckedChangeListener(this);
            checkbox_B.setOnCheckedChangeListener(this);
            checkbox_C.setOnCheckedChangeListener(this);
            checkbox_D.setOnCheckedChangeListener(this);
            checkbox_E.setOnCheckedChangeListener(this);

            checkbox_goalie.setOnCheckedChangeListener(this);
            checkbox_defence.setOnCheckedChangeListener(this);
            checkbox_forward.setOnCheckedChangeListener(this);
            checkbox_referee.setOnCheckedChangeListener(this);

            ll_goalie.setVisibility(View.VISIBLE);
            ll_defence.setVisibility(View.VISIBLE);
            ll_forward.setVisibility(View.VISIBLE);
            ll_referee.setVisibility(View.VISIBLE);
            radio_goalie_rating.setVisibility(View.VISIBLE);
            radio_defence_rating.setVisibility(View.VISIBLE);
            radio_forward_rating.setVisibility(View.VISIBLE);
            radio_referee_rating.setVisibility(View.VISIBLE);
            radio_choose_gender.setOnCheckedChangeListener(this);

            et_phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
                private boolean backspacingFlag = false;
                private boolean editedFlag = false;
                private int cursorComplement;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    cursorComplement = s.length() - et_phone_number.getSelectionStart();
                    if (count > after) {
                        backspacingFlag = true;
                    } else {
                        backspacingFlag = false;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // nothing to do here =D
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string = s.toString();
                    String phone = string.replaceAll("[^\\d]", "");
                    if (!editedFlag) {
                        //we start verifying the worst case, many characters mask need to be added
                        //example: 999999999 <- 6+ digits already typed
                        // masked: (999) 999-999
                        if (phone.length() >= 6 && !backspacingFlag) {
                            //we will edit. next call on this textWatcher will be ignored
                            editedFlag = true;
                            //here is the core. we substring the raw digits and add the mask as convenient
                            String ans = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
                            et_phone_number.setText(ans);
                            //we deliver the cursor to its original position relative to the end of the string
                            et_phone_number.setSelection(et_phone_number.getText().length() - cursorComplement);
                            //we end at the most simple case, when just one character mask is needed
                            //example: 99999 <- 3+ digits already typed
                            // masked: (999) 99
                        } else if (phone.length() >= 3 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = phone.substring(0, 3) + "-" + phone.substring(3);
                            et_phone_number.setText(ans);
                            et_phone_number.setSelection(et_phone_number.getText().length() - cursorComplement);
                        }
                        // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                    } else {
                        editedFlag = false;
                    }
                }
            });


/*            et_postal_code.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (et_country.getText().toString().trim().equals("Canada")) {
                        et_postal_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    } else if (et_country.getText().toString().trim().equals("United States of America")) {
                        et_postal_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                    }
                }
            });*/
            try {
                et_first_name.setText(cc.loadPrefString("first_name"));
                et_last_name.setText(cc.loadPrefString("last_name"));
                et_civic_no.setText(cc.loadPrefString("civic_number"));

//                et_apartment.setText(cc.loadPrefString("apartment"));
//                et_street.setText(cc.loadPrefString("street"));
//                et_city.setText(cc.loadPrefString("city"));
//                et_postal_code.setText(cc.loadPrefString("postal_code"));
                et_email.setText(cc.loadPrefString("email"));
                et_phone_number.setText(cc.loadPrefString("phone_number"));
                Log.e("****address", cc.loadPrefString("address"));
//                autocompleteFragment.setText(address);
                tv_address.setText(address);

                setupDate(cc.loadPrefString("birth_date"));
                Log.e("@@@B-Date", cc.loadPrefString("birth_date"));
                Log.e("@@@Gender", cc.loadPrefString("gender"));

                if (cc.loadPrefString("gender").equals("Male")) {
                    radio_male.setChecked(true);
                } else if (cc.loadPrefString("gender").equals("Female")) {
                    radio_female.setChecked(true);
                }
//                et_postal_code.setText(cc.loadPrefString("postal_code"));
                seekbar_distance.setProgress(Integer.parseInt(cc.loadPrefString("travel_distance")));
                distance = cc.loadPrefString("travel_distance");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                Cursor c = dbcom.getAvailabilities(cc.loadPrefString("user_id"));
                weeklist = new ArrayList<availabilites_week>();
                if (c.getCount() != 0 && c != null) {
                    c.moveToFirst();
                    do {
                        availabilites_week aw = new availabilites_week();
                        aw.setWeek_day(c.getString(1));
                        aw.setStart_time(c.getString(2).substring(0, 5));
                        aw.setEnd_time(c.getString(3).substring(0, 5));
                        Log.e("@@@WeakData", c.getString(1));
                        Log.e("@@@WeakData", c.getString(2).substring(0, 5));
                        Log.e("@@@WeakData", c.getString(3).substring(0, 5));
                        weeklist.add(aw);
                    } while
                            (c.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int k = 0; k < weeklist.size(); k++) {
                    if (weeklist.get(k).getWeek_day().equals("Monday")) {
                        Monday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Tuesday")) {
                        Tuesday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Wednesday")) {
                        Wednesday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Thursday")) {
                        Thursday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Friday")) {
                        Friday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Saturday")) {
                        Saturday(false);
                    } else if (weeklist.get(k).getWeek_day().equals("Sunday")) {
                        Sunday(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!cc.loadPrefString("profile_pic").equals("")) {
                Picasso.with(EditProfile.this).
                        load(cc.loadPrefString("profile_pic")).
                        placeholder(R.mipmap.ic_profile).
                        into(iv_profile);
                //selectedImagePath = cc.loadPrefString("profile_pic");
            }
            for (int i = 0; i < countryList.size(); i++) {
                if (cc.loadPrefString("country_id").equals(countryList.get(i).getC_id())) {
                    et_country.setText(countryList.get(i).getC_name());
                    countryId = countryList.get(i).getC_id();
                }
            }
            if (!cc.loadPrefString("country_id").equals("")) {
                callWsForState(cc.loadPrefString("country_id"));
            }

            List<String> ground_size = Arrays.asList(cc.loadPrefString("ground_size").split(","));
            for (int j = 0; j < ground_size.size(); j++) {
                if (ground_size.get(j).equals("3*3")) {
                    checkbox_3.setChecked(true);
                } else if (ground_size.get(j).equals("4*4")) {
                    checkbox_4.setChecked(true);
                } else if (ground_size.get(j).equals("5*5")) {
                    checkbox_5.setChecked(true);
                }
            }
            List<String> game_type = Arrays.asList(cc.loadPrefString("game_type").split(","));
            for (int i = 0; i < game_type.size(); i++) {
                if (game_type.get(i).equals(getString(R.string.ice_hockey))) {
                    checkbox_ice_hockey.setChecked(true);
                } else if (game_type.get(i).equals(getString(R.string.off_ice))) {
                    checkbox_ball_hockey.setChecked(true);
                }
            }
            List<String> calibre = Arrays.asList(cc.loadPrefString("calibre").split(","));
            for (int i = 0; i < calibre.size(); i++) {
                if (calibre.get(i).equals("A")) {
                    checkbox_A.setChecked(true);
                } else if (calibre.get(i).equals("B")) {
                    checkbox_B.setChecked(true);
                } else if (calibre.get(i).equals("C")) {
                    checkbox_C.setChecked(true);
                } else if (calibre.get(i).equals("D")) {
                    checkbox_D.setChecked(true);
                } else if (calibre.get(i).equals("E")) {
                    checkbox_E.setChecked(true);
                }
            }

            try {
                List<String> user_list = Arrays.asList(cc.loadPrefString("user_type").split(","));
                for (int j = 0; j < user_list.size(); j++) {
                    if (user_list.get(j).equals("Goalie")) {
                        ll_goalie.setVisibility(View.VISIBLE);
                        radio_goalie_rating.setVisibility(View.VISIBLE);
                    } else {
                        radio_goalie_rating.setVisibility(View.GONE);
                    }
                    if (user_list.get(j).equals("Defence")) {
                        ll_defence.setVisibility(View.VISIBLE);
                        radio_defence_rating.setVisibility(View.VISIBLE);
                    } else {
                        radio_defence_rating.setVisibility(View.GONE);
                    }
                    if (user_list.get(j).equals("Forward")) {
                        ll_forward.setVisibility(View.VISIBLE);
                        radio_forward_rating.setVisibility(View.VISIBLE);
                    } else {
                        radio_forward_rating.setVisibility(View.GONE);
                    }
                    if (user_list.get(j).equals("Referee")) {
                        ll_referee.setVisibility(View.VISIBLE);
                        radio_referee_rating.setVisibility(View.VISIBLE);
                    } else {
                        radio_referee_rating.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cc.loadPrefString("mixedgender").equals("Yes")) {
                radio_yes.setChecked(true);
            } else if (cc.loadPrefString("mixedgender").equals("No")) {
                radio_no.setChecked(true);
            }
            List<String> user_type = Arrays.asList(cc.loadPrefString("user_type").split(","));
            List<String> self_rate = Arrays.asList(cc.loadPrefString("self_rate").split(","));
            for (int i = 0; i < user_type.size(); i++) {
                if (user_type.get(i).equals("Goalie")) {
                    checkbox_goalie.setChecked(true);
                    b_goalie = true;
                    goalie = checkbox_goalie.getText().toString();
                    if (self_rate.get(i).equals("A")) {
                        radioA.setChecked(true);
                        goalie_rating = "A";
                    } else if (self_rate.get(i).equals("B")) {
                        radioB.setChecked(true);
                        goalie_rating = "B";
                    } else if (self_rate.get(i).equals("C")) {
                        radioC.setChecked(true);
                        goalie_rating = "C";
                    } else if (self_rate.get(i).equals("D")) {
                        radioD.setChecked(true);
                        goalie_rating = "D";
                    } else if (self_rate.get(i).equals("E")) {
                        radioE.setChecked(true);
                        goalie_rating = "E";
                    }
                } else if (user_type.get(i).equals("Defence")) {
//                    defence = checkbox_defence.getText().toString();//defense
                    defence = getResources().getString(R.string.defence);
                    checkbox_defence.setChecked(true);
                    b_defense = true;
                    if (self_rate.get(i).equals("A")) {
                        radio_defenceA.setChecked(true);
                        defence_rating = "A";
                    } else if (self_rate.get(i).equals("B")) {
                        radio_defenceB.setChecked(true);
                        defence_rating = "B";
                    } else if (self_rate.get(i).equals("C")) {
                        radio_defenceC.setChecked(true);
                        defence_rating = "C";
                    } else if (self_rate.get(i).equals("D")) {
                        radio_defenceD.setChecked(true);
                        defence_rating = "D";
                    } else if (self_rate.get(i).equals("E")) {
                        radio_defenceE.setChecked(true);
                        defence_rating = "E";
                    }
                } else if (user_type.get(i).equals("Forward")) {
                    checkbox_forward.setChecked(true);
                    forward = checkbox_forward.getText().toString();
                    b_striker = true;
                    if (self_rate.get(i).equals("A")) {
                        radio_forwardA.setChecked(true);
                        forward_rating = "A";
                    } else if (self_rate.get(i).equals("B")) {
                        radio_forwardB.setChecked(true);
                        forward_rating = "B";
                    } else if (self_rate.get(i).equals("C")) {
                        radio_forwardC.setChecked(true);
                        forward_rating = "C";
                    } else if (self_rate.get(i).equals("D")) {
                        radio_forwardD.setChecked(true);
                        forward_rating = "D";
                    } else if (self_rate.get(i).equals("E")) {
                        radio_forwardE.setChecked(true);
                        forward_rating = "E";
                    }
                } else if (user_type.get(i).equals("Referee")) {
                    checkbox_referee.setChecked(true);
                    referee = checkbox_referee.getText().toString();
                    b_referee = true;
                    if (self_rate.get(i).equals("A")) {
                        radio_refereeA.setChecked(true);
                        referee_rating = "A";
                    } else if (self_rate.get(i).equals("B")) {
                        radio_refereeB.setChecked(true);
                        referee_rating = "B";
                    } else if (self_rate.get(i).equals("C")) {
                        radio_refereeC.setChecked(true);
                        referee_rating = "C";
                    } else if (self_rate.get(i).equals("D")) {
                        radio_refereeD.setChecked(true);
                        referee_rating = "D";
                    } else if (self_rate.get(i).equals("E")) {
                        radio_refereeE.setChecked(true);
                        referee_rating = "E";
                    }
                }
            }


//==================Setting UI Call========================================================================
        } else if (title.equals("Setting")) {
            setContentView(R.layout.change_password);
            et_current_password = (EditText) findViewById(R.id.et_current_password);
            et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
            et_old_password = (EditText) findViewById(R.id.et_old_password);
            ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()

                                             {
                                                 @Override
                                                 public void onClick(View v) {
                                                     onBackPressed();
                                                 }
                                             }

        );
    }

    private void setupDate(String s) {
        Date date = null;
        SimpleDateFormat formate_to1 = null, formate_to2 = null, formate_to3 = null, formate_to5 = null;

        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyy-M-d");
            date = formate.parse(s);

            formate_to1 = new SimpleDateFormat("yyyy");
            formate_to2 = new SimpleDateFormat("MMM");
            formate_to3 = new SimpleDateFormat("dd");
            tv_year.setText(formate_to1.format(date));
            tv_month.setText(formate_to2.format(date));
            tv_day.setText(formate_to3.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            //===========Change Password Ws Call ======================================================================
            case R.id.action_settings:
                if (title.equals("My Profile")) {

                    address = tv_address.getText().toString();
                    UpdateValidation();

                } else if (title.equals("Setting")) {
                    String confirm_pass = et_confirm_password.getText().toString().trim();
                    String old_pass = et_old_password.getText().toString().trim();
                    String current_pass = et_current_password.getText().toString().trim();
                    if (!cc.isConnectingToInternet()) {
                        cc.showSnackbar(ll_linear, getString(R.string.no_internet));
                    } else if (old_pass.equals("")) {
                        cc.showSnackbar(ll_linear, "Please enter current password");
                    } else if (current_pass.equals("")) {
                        cc.showSnackbar(ll_linear, "Please enter new password");
                    } else if (confirm_pass.equals("")) {
                        cc.showSnackbar(ll_linear, "Please enter Confirm new password");
                    } else if (!confirm_pass.matches(current_pass)) {
                        cc.showSnackbar(ll_linear, "New password and Confirm new password must be same");
                    } else {
                        ChangePassword(old_pass, current_pass);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //=======================Change Password==============================================
    private void ChangePassword(final String old_pass, final String current_pass) {
        pDialog = new ProgressDialog(EditProfile.this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_change_password,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                finish();

                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("old_password", old_pass);
                params.put("new_password", current_pass);
                Log.e("request:change_password", params.toString());
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

    //==============ON CLICK LISTENER USE =================================================================
    @Override
    public void onClick(View v) {
        // LinearLayoutManager layoutManager = null;
        switch (v.getId()) {
            case R.id.ll_monday:

                if (!monday) {
                    if (timelist.size() > 0) {
                        tv_no_mon_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_mon_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_monday_child.setVisibility(View.VISIBLE);
                    iv_up_monday.setVisibility(View.VISIBLE);
                    iv_down_monday.setVisibility(View.GONE);
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
                    if (tuesdaylist.size() > 0) {
                        tv_no_tue_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_tue_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_tuesday_child.setVisibility(View.VISIBLE);
                    iv_up_tuesday.setVisibility(View.VISIBLE);
                    iv_down_tuesday.setVisibility(View.GONE);
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
                    if (wednesdaylist.size() > 0) {
                        tv_no_wed_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_wed_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_wednesday_child.setVisibility(View.VISIBLE);
                    iv_up_wesday.setVisibility(View.VISIBLE);
                    iv_down_wesday.setVisibility(View.GONE);
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
                    if (thursdaylist.size() > 0) {
                        tv_no_thurs_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_thurs_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_thrusday_child.setVisibility(View.VISIBLE);
                    iv_up_thrusday.setVisibility(View.VISIBLE);
                    iv_down_thrusday.setVisibility(View.GONE);
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
                    if (fridaylist.size() > 0) {
                        tv_no_fri_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_fri_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_friday_child.setVisibility(View.VISIBLE);
                    iv_up_friday.setVisibility(View.VISIBLE);
                    iv_down_friday.setVisibility(View.GONE);
                    friday = true;
                } else {
                    ll_friday_child.setVisibility(View.GONE);
                    iv_up_friday.setVisibility(View.GONE);
                    iv_down_friday.setVisibility(View.VISIBLE);
                    friday = false;
                }
                break;

            case R.id.ll_saturday:
                if (!saturday) {
                    if (saturdaylist.size() > 0) {
                        tv_no_sat_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_sat_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_saturday_child.setVisibility(View.VISIBLE);
                    iv_up_saturday.setVisibility(View.VISIBLE);
                    iv_down_saturday.setVisibility(View.GONE);
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
                    if (sundaylist.size() > 0) {
                        tv_no_sun_intervals.setVisibility(View.GONE);
                    } else {
                        tv_no_sun_intervals.setVisibility(View.VISIBLE);
                    }
                    ll_sunday_child.setVisibility(View.VISIBLE);
                    iv_up_sunday.setVisibility(View.VISIBLE);
                    iv_down_sunday.setVisibility(View.GONE);
                    sunday = true;
                } else {
                    ll_sunday_child.setVisibility(View.GONE);
                    iv_up_sunday.setVisibility(View.GONE);
                    iv_down_sunday.setVisibility(View.VISIBLE);
                    sunday = false;
                }
                break;
            case R.id.et_country:
                countryPopup(cc.loadPrefString("country_id"));
                break;
            case R.id.et_province:
                if (countryId.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.enter_country));
                } else {
                    proviencePopup();
                }
                break;
            case R.id.iv_plus_monday:

                Monday(true);
                if (timelist.size() > 0) {
                    tv_no_mon_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_mon_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_tuesday:

                Tuesday(true);
                if (tuesdaylist.size() > 0) {
                    tv_no_tue_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_tue_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_wednesday:

                Wednesday(true);
                if (wednesdaylist.size() > 0) {
                    tv_no_wed_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_wed_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_thursday:

                Thursday(true);
                if (thursdaylist.size() > 0) {
                    tv_no_thurs_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_thurs_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_friday:

                Friday(true);
                if (fridaylist.size() > 0) {
                    tv_no_fri_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_fri_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_saturday:

                Saturday(true);
                if (saturdaylist.size() > 0) {
                    tv_no_sat_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_sat_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_plus_sunday:

                Sunday(true);
                if (sundaylist.size() > 0) {
                    tv_no_sun_intervals.setVisibility(View.GONE);
                } else {
                    tv_no_sun_intervals.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_profile:

                selectfile();
                break;
            case R.id.tv_rating_info:
                RatingDialog();
                break;
            case R.id.tv_address:
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(EditProfile.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_year:
                cal = Calendar.getInstance();
                to_year = cal.get(Calendar.YEAR) - 37;
                to_month = cal.get(Calendar.MONTH);
                to_day = cal.get(Calendar.DAY_OF_MONTH);

                showDialog(0);
                break;
            case R.id.tv_month:
                cal = Calendar.getInstance();
                to_year = cal.get(Calendar.YEAR) - 37;
                to_month = cal.get(Calendar.MONTH);
                to_day = cal.get(Calendar.DAY_OF_MONTH);

                showDialog(0);
                break;
            case R.id.tv_day:
                cal = Calendar.getInstance();
                to_year = cal.get(Calendar.YEAR) - 37;
                to_month = cal.get(Calendar.MONTH);
                to_day = cal.get(Calendar.DAY_OF_MONTH);

                showDialog(0);
                break;
        }
    }


    private void RatingDialog() {
        final Dialog dialog = new Dialog(EditProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_describe);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_rating_a = (TextView) dialog.findViewById(R.id.tv_rating_a);
        TextView tv_rating_b = (TextView) dialog.findViewById(R.id.tv_rating_b);
        TextView tv_rating_c = (TextView) dialog.findViewById(R.id.tv_rating_c);
        TextView tv_rating_e = (TextView) dialog.findViewById(R.id.tv_rating_e);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        tv_rating_a.setText("Pro,Semi-Pro or Minor Pro, NCAA Division I , Major Junior; Women's AAA,");
        tv_rating_b.setText("University or Senior Hockey, Junior A, Quebec Junior AAA;Women's A, BB or BAA");
        tv_rating_c.setText("Canadian College, Junior B or C, Quebec Junior AA or A; High School; Women's C");
        tv_rating_e.setText("No Experience, started playing hockey as an Adult");
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void selectfile() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);

        } catch (Exception e) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EditProfile.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                try {
                    selectedImagePath = getPath(selectedImageUri);
                    Log.e("Selected File", selectedImagePath);
                    Bitmap mybitmap = null;
                    mybitmap = BitmapFactory.decodeFile(selectedImagePath);

                    Uri uri = Uri.fromFile(new File(selectedImagePath));

                    Picasso.with(EditProfile.this)
                            .load(uri)
                            .into(iv_profile);
                    GenerateImage(mybitmap);

                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                String address = String.format("%s", place.getAddress());
                stBuilder.append(address);

                tv_address.setText(stBuilder.toString());

            }
        }
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = EditProfile.this.getContentResolver().query(uri,
                        projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void GenerateImage(Bitmap bm) {

        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "Goalkeeper.jpg");
            outputFileUri = Uri.fromFile(file);
            fOut = new FileOutputStream(file);
        } catch (Exception e) {

        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "Goalkeeper.jpg");
        selectedImagePath = file.toString();

    }

    private void Monday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);


        if (add) {
            if (!timelist.isEmpty()) {
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
                RecyclerView_monday.setLayoutManager(layoutManager);
                RecyclerView_monday.setAdapter(new SignupDayTime(EditProfile.this, timelist));
            } else {
                signup_time m = new signup_time();
                m.setMax_time(tv_last_time.getText().toString().trim());
                m.setMin_time(tv_initial_time.getText().toString().trim());
                m.setId("monday");
                timelist.add(m);
                RecyclerView_monday.setLayoutManager(layoutManager);
                RecyclerView_monday.setAdapter(new SignupDayTime(EditProfile.this, timelist));
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Monday")) {
                    availabilites_week aw = weeklist.get(i);
                    signup_time m = new signup_time();
                    m.setMax_time(aw.getEnd_time());
                    m.setMin_time(aw.getStart_time());
                    m.setId("monday");
                    timelist.add(m);
                }

            }
            RecyclerView_monday.setLayoutManager(layoutManager);
            RecyclerView_monday.setAdapter(new SignupDayTime(EditProfile.this, timelist));
        }

    }

    private void Tuesday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);

        if (add) {
            if (tuesdaylist.isEmpty()) {
                tuesday t = new tuesday();
                t.setMax_time(tv_tuesd_last_time.getText().toString().trim());
                t.setMin_time(tv_tues_initial_time.getText().toString().trim());
                tuesdaylist.add(t);
                Log.e("tue_size", tuesdaylist.size() + "");
                RecyclerView_tuesday.setAdapter(new SignupTuesday(EditProfile.this, tuesdaylist));
                RecyclerView_tuesday.setLayoutManager(layoutManager);

            } else {

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
                RecyclerView_tuesday.setAdapter(new SignupTuesday(EditProfile.this, tuesdaylist));
                RecyclerView_tuesday.setLayoutManager(layoutManager);
            }

        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Tuesday")) {
                    availabilites_week aw = weeklist.get(i);
                    tuesday t = new tuesday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    tuesdaylist.add(t);
                    Collections.reverse(tuesdaylist);
                }
            }
            RecyclerView_tuesday.setAdapter(new SignupTuesday(EditProfile.this, tuesdaylist));
            RecyclerView_tuesday.setLayoutManager(layoutManager);
        }

    }

    private void Wednesday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);

        if (add) {
            if (wednesdaylist.isEmpty()) {
                wednesday w = new wednesday();
                w.setMax_time(tv_wednes_last_time.getText().toString().trim());
                w.setMin_time(tv_wednes_initial_time.getText().toString().trim());
                wednesdaylist.add(w);
                RecyclerView_wednesday.setAdapter(new SignupWednesday(EditProfile.this, wednesdaylist));
                RecyclerView_wednesday.setLayoutManager(layoutManager);
            } else {
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


                RecyclerView_wednesday.setAdapter(new SignupWednesday(EditProfile.this, wednesdaylist));
                RecyclerView_wednesday.setLayoutManager(layoutManager);
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Wednesday")) {
                    availabilites_week aw = weeklist.get(i);
                    wednesday t = new wednesday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    wednesdaylist.add(t);
                    Collections.reverse(wednesdaylist);
                }
            }

            RecyclerView_wednesday.setAdapter(new SignupWednesday(EditProfile.this, wednesdaylist));
            RecyclerView_wednesday.setLayoutManager(layoutManager);
        }

    }

    private void Thursday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);


        if (add) {
            if (thursdaylist.isEmpty()) {
                thursday th = new thursday();
                th.setMax_time(tv_thr_last_time.getText().toString().trim());
                th.setMin_time(tv_thr_initial_time.getText().toString().trim());
                thursdaylist.add(th);
                RecyclerView_thursday.setAdapter(new SignupThursday(EditProfile.this, thursdaylist));
                RecyclerView_thursday.setLayoutManager(layoutManager);
            } else {

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


                RecyclerView_thursday.setAdapter(new SignupThursday(EditProfile.this, thursdaylist));
                RecyclerView_thursday.setLayoutManager(layoutManager);
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Thursday")) {
                    availabilites_week aw = weeklist.get(i);
                    thursday t = new thursday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    thursdaylist.add(t);
                    Collections.reverse(thursdaylist);
                }
            }

            RecyclerView_wednesday.setAdapter(new SignupThursday(EditProfile.this, thursdaylist));
            RecyclerView_wednesday.setLayoutManager(layoutManager);
        }


    }

    private void Friday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);

        if (add) {
            if (fridaylist.isEmpty()) {
                com.mxi.goalkeeper.model.friday f = new friday();
                f.setMax_time(tv_fri_last_time.getText().toString().trim());
                f.setMin_time(tv_fri_initial_time.getText().toString().trim());
                fridaylist.add(f);
                RecyclerView_friday.setAdapter(new SignupFriday(EditProfile.this, fridaylist));
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

                RecyclerView_friday.setAdapter(new SignupFriday(EditProfile.this, fridaylist));
                RecyclerView_friday.setLayoutManager(layoutManager);
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Friday")) {
                    availabilites_week aw = weeklist.get(i);
                    friday t = new friday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    fridaylist.add(t);
                    Collections.reverse(fridaylist);
                }
            }

            RecyclerView_friday.setAdapter(new SignupFriday(EditProfile.this, fridaylist));
            RecyclerView_friday.setLayoutManager(layoutManager);
        }

    }

    private void Saturday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);
        if (add) {
            if (saturdaylist.isEmpty()) {
                com.mxi.goalkeeper.model.saturday sat = new saturday();
                sat.setMax_time(tv_sat_last_time.getText().toString().trim());
                sat.setMin_time(tv_sat_initial_time.getText().toString().trim());
                saturdaylist.add(sat);
                RecyclerView_saturday.setAdapter(new SignupSaturday(EditProfile.this, saturdaylist));
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

                RecyclerView_saturday.setAdapter(new SignupSaturday(EditProfile.this, saturdaylist));
                RecyclerView_saturday.setLayoutManager(layoutManager);
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Saturday")) {
                    availabilites_week aw = weeklist.get(i);
                    saturday t = new saturday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    saturdaylist.add(t);
                    Collections.reverse(saturdaylist);
                }
            }

            RecyclerView_saturday.setAdapter(new SignupSaturday(EditProfile.this, saturdaylist));
            RecyclerView_saturday.setLayoutManager(layoutManager);
        }

    }

    private void Sunday(boolean add) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditProfile.this, LinearLayoutManager.HORIZONTAL, false);

        if (add) {
            if (sundaylist.isEmpty()) {
                com.mxi.goalkeeper.model.sunday sun = new sunday();
                sun.setMax_time(tv_sun_last_time.getText().toString().trim());
                sun.setMin_time(tv_sun_initial_time.getText().toString().trim());
                sundaylist.add(sun);
                RecyclerView_sunday.setAdapter(new SignUpSunday(EditProfile.this, sundaylist));
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

                RecyclerView_sunday.setAdapter(new SignUpSunday(EditProfile.this, sundaylist));
                RecyclerView_sunday.setLayoutManager(layoutManager);
            }
        } else {
            for (int i = 0; i < weeklist.size(); i++) {

                if (weeklist.get(i).getWeek_day().equals("Sunday")) {
                    availabilites_week aw = weeklist.get(i);
                    sunday t = new sunday();
                    t.setMax_time(aw.getEnd_time());
                    t.setMin_time(aw.getStart_time());
                    sundaylist.add(t);
                    Collections.reverse(sundaylist);
                }
            }
            RecyclerView_sunday.setAdapter(new SignUpSunday(EditProfile.this, sundaylist));
            RecyclerView_sunday.setLayoutManager(layoutManager);
        }

    }

    private void proviencePopup() {
        PopupMenu popup = new PopupMenu(EditProfile.this, et_province);
        //Inflating the Popup using xml file

        popup.getMenuInflater()
                .inflate(R.menu.province_menu, popup.getMenu());

        Menu menu = popup.getMenu();
        for (int i = 0; i < stateList.size(); i++) {
            menu.add(0, Integer.parseInt(stateList.get(i).getC_id()), 0, stateList.get(i).getC_name());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                stateId = String.valueOf(item.getItemId());
                et_province.setText(item.getTitle().toString());
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton choose_text = (RadioButton) group.findViewById(checkedId);
        switch (group.getId()) {
            case R.id.radio_choose_gender:
                gender = choose_text.getText().toString();
                break;
            case R.id.radio_mix_gender:
                mix_gender = choose_text.getText().toString();
                break;
            case R.id.radio_goalie_rating:
                if (checkbox_goalie.isChecked()) {
                    goalie_rating = choose_text.getText().toString();
                } else {
                    goalie_rating = "";
                }

                break;
            case R.id.radio_defence_rating:
                if (checkbox_defence.isChecked()) {
                    defence_rating = choose_text.getText().toString();
                } else {
                    defence_rating = "";
                }

                break;
            case R.id.radio_forward_rating:
                if (checkbox_forward.isChecked()) {
                    forward_rating = choose_text.getText().toString();
                } else {
                    forward_rating = "";
                }
                break;
            case R.id.radio_referee_rating:
                if (checkbox_referee.isChecked()) {
                    referee_rating = choose_text.getText().toString();
                } else {
                    referee_rating = "";
                }
                break;
/*            case R.id.radio_goalie_rating:
                goalie = "Goalie";
                goalie_rating = choose_text.getText().toString();
                break;
            case R.id.radio_defence_rating:
                defence = "Defence";
                defence_rating = choose_text.getText().toString();
                break;
            case R.id.radio_forward_rating:
                forward = "Forward";
                forward_rating = choose_text.getText().toString();
                break;
            case R.id.radio_referee_rating:
                referee = "Referee";
                referee_rating = choose_text.getText().toString();
                break;*/
            case R.id.radio_choose_player:
                youare = choose_text.getText().toString();
                if (youare.equals("Player")) {
                    radio_player.setChecked(true);
                    youare = "Player";
                    ll_team_manager.setVisibility(View.VISIBLE);
                    ll_availabilites.setVisibility(View.VISIBLE);
                    ll_caliber.setVisibility(View.VISIBLE);
                    ll_team_size.setVisibility(View.VISIBLE);
                    ll_game_type.setVisibility(View.VISIBLE);
                    ll_mixed_gender.setVisibility(View.VISIBLE);
                    ll_travel_distance.setVisibility(View.VISIBLE);
                } else if (youare.equals("Team Manager")) {
                    radio_team_manager.setChecked(true);
                    youare = "Team_Manager";
                    ll_team_manager.setVisibility(View.GONE);
                    ll_availabilites.setVisibility(View.GONE);
                    ll_caliber.setVisibility(View.GONE);
                    ll_team_size.setVisibility(View.GONE);
                    ll_game_type.setVisibility(View.GONE);
                    ll_mixed_gender.setVisibility(View.GONE);
                    ll_travel_distance.setVisibility(View.GONE);
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
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long maxValue) {
        if (bar == seekBarMonday) {
            tv_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarTuesday) {
            tv_tues_initial_time.setText(ConvertSecondsToHMmSs(Long.parseLong(minValue * 900000 + "")));
            tv_tuesd_last_time.setText(ConvertSecondsToHMmSs(Long.parseLong(maxValue * 900000 + "")));
        } else if (bar == seekBarWednesday) {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.checkbox_goalie:
                if (checkbox_goalie.isChecked()) {
                    radio_goalie_rating.setVisibility(View.VISIBLE);
                    b_goalie = true;
                    goalie = checkbox_goalie.getText().toString();
                } else {
                    radio_goalie_rating.setVisibility(View.GONE);
                    goalie = "";
                    b_goalie = false;
                    radio_goalie_rating.clearCheck();
                }

                // Toast.makeText(SignUpNext.this, checkbox_goalie.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_defence:
                if (checkbox_defence.isChecked()) {
                    radio_defence_rating.setVisibility(View.VISIBLE);
                    b_defense = true;
                    defence = checkbox_defence.getText().toString();
                } else {
                    radio_defence_rating.setVisibility(View.GONE);
                    defence = "";
                    b_defense = false;
                    radio_defence_rating.clearCheck();
                }

                // Toast.makeText(SignUpNext.this, checkbox_defence.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_forward:
                if (checkbox_forward.isChecked()) {
                    radio_forward_rating.setVisibility(View.VISIBLE);
                    b_striker = true;
                    forward = checkbox_forward.getText().toString();
                } else {
                    radio_forward_rating.setVisibility(View.GONE);
                    forward = "";
                    b_striker = false;
                    radio_forward_rating.clearCheck();
                }

                break;
            case R.id.checkbox_referee:
                if (checkbox_referee.isChecked()) {
                    radio_referee_rating.setVisibility(View.VISIBLE);
                    b_referee = true;
                    referee = checkbox_referee.getText().toString();
                } else {
                    radio_referee_rating.setVisibility(View.GONE);
                    referee = "";
                    b_referee = false;
                    radio_referee_rating.clearCheck();
                }

                break;
            case R.id.checkbox_ice_hockey:
                checkbox_ice_hockey.setChecked(isChecked);
                if (checkbox_ice_hockey.isChecked()) {
                    game_type = checkbox_ice_hockey.getText().toString();
                    gameTypeList.add(game_type);
                    Log.e("@@@gmList", gameTypeList.size() + "");
                } else {
                    gameTypeList.remove(checkbox_ice_hockey.getText().toString());
                    Log.e("@@@gmList", gameTypeList.size() + "");
                }

                break;
            case R.id.checkbox_ball_hockey:
                checkbox_ball_hockey.setChecked(isChecked);
                if (checkbox_ball_hockey.isChecked()) {
                    game_type = checkbox_ball_hockey.getText().toString();
                    gameTypeList.add(game_type);
                    Log.e("@@@gmList", gameTypeList.size() + "");
                } else {
                    gameTypeList.remove(checkbox_ball_hockey.getText().toString());
                    Log.e("@@@gmList", gameTypeList.size() + "");
                }

                break;
            case R.id.checkbox_3:
                checkbox_3.setChecked(isChecked);
                if (checkbox_3.isChecked()) {
                    ground_type = checkbox_3.getText().toString();
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
                checkbox_5.setChecked(isChecked);
                if (checkbox_5.isChecked()) {
                    ground_type = checkbox_5.getText().toString();
                    groundTypeList.add(ground_type);
                } else {
                    groundTypeList.remove(checkbox_5.getText().toString());
                }
                break;
            case R.id.checkbox_A:
                checkbox_A.setChecked(isChecked);
                if (checkbox_A.isChecked()) {
                    calibre = checkbox_A.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_A.getText().toString());
                }

                break;
            case R.id.checkbox_B:

                checkbox_B.setChecked(isChecked);
                if (checkbox_B.isChecked()) {
                    calibre = checkbox_B.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_B.getText().toString());
                }
                break;
            case R.id.checkbox_C:
                checkbox_C.setChecked(isChecked);
                if (checkbox_C.isChecked()) {
                    calibre = checkbox_C.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_C.getText().toString());
                }
                break;
            case R.id.checkbox_D:
                checkbox_D.setChecked(isChecked);
                if (checkbox_D.isChecked()) {
                    calibre = checkbox_D.getText().toString();
                    calibreList.add(calibre);
                } else {
                    calibreList.remove(checkbox_D.getText().toString());
                }
                break;
            case R.id.checkbox_E:
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

    private String ConvertSecondsToHMmSs(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
               /* TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))*/);
    }

    private void countryPopup(String country_id) {
        PopupMenu popup = new PopupMenu(EditProfile.this, et_country);
        //Inflating the Popup using xml file

        popup.getMenuInflater()
                .inflate(R.menu.country_menu, popup.getMenu());

        Menu menu = popup.getMenu();
        for (int i = 0; i < countryList.size(); i++) {
            menu.add(0, Integer.parseInt(countryList.get(i).getC_id()), 0, countryList.get(i).getC_name());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                countryId = String.valueOf(item.getItemId());
                Log.e("countryId", countryId);
                et_country.setText(item.getTitle().toString());
                callWsForState(countryId);

                return true;
            }
        });

        popup.show();
    }

    private void callWsForState(final String countryId) {
        pDialog = new ProgressDialog(EditProfile.this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_state,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        try {
                            pDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("state_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                country country = new country();
                                country.setC_id(object.getString("state_id"));
                                country.setC_name(object.getString("state"));
                                stateList.add(country);
                            }
                            for (int i = 0; i < stateList.size(); i++) {
                                if (cc.loadPrefString("state_id").equals(stateList.get(i).getC_id())) {
                                    et_province.setText(stateList.get(i).getC_name());
                                    stateId = stateList.get(i).getC_id();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id", countryId);
                Log.i("request statelist", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void UpdateValidation() {

        String first_name = et_first_name.getText().toString().trim();
        String last_name = et_last_name.getText().toString().trim();
        String civic_no = et_civic_no.getText().toString().trim();
//        String apparment = et_apartment.getText().toString().trim();
//        String street = et_street.getText().toString().trim();
//        String city = et_city.getText().toString().trim();
        String country = et_country.getText().toString().trim();
        String province = et_province.getText().toString().trim();
//        String postal_code = et_postal_code.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone_number = et_phone_number.getText().toString().trim();

        Log.e("@@@youare", youare);
        Log.e("@@@youare_cc", cc.loadPrefString("you_are"));
        if (!cc.isConnectingToInternet()) {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        } else if (first_name.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_first_name));
        } else if ((!isName(first_name))) {
            cc.showSnackbar(ll_linear, getString(R.string.numeral_first_name));
        } else if (last_name.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_last_name));
        } else if ((!isName(last_name))) {
            cc.showSnackbar(ll_linear, getString(R.string.numeral_last_name));
        }
/*        else if (civic_no.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_civic_no));
        }*/
/*        else if (street.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_street));
        } else if (city.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_city));
        } */
/*        else if (country.equals("") || country.equals(getResources().getString(R.string.country))) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_country));
        } else if (province.equals("") || province.equals(getResources().getString(R.string.province))) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_province_state));
        }*/
/*        else if (postal_code.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_postal_code));
        }*/
/*        else if (et_country.getText().toString().trim().equals("Canada") && !isPostalCode(postal_code)) {
            cc.showSnackbar(ll_linear, "Please enter valid postal code");
        } else if (et_country.getText().toString().trim().equals("United States of America") && !isUs(postal_code)) {
            cc.showSnackbar(ll_linear, "Please enter valid postal code");
        }*/
        else if (email.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_email));
        } else if (address.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_address));
        } else if (phone_number.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_phone_number));
        } else if (phone_number.length() != 12) {
            cc.showSnackbar(ll_linear, getString(R.string.mobile_validation));
        } /*else if (goalie_rating.equals("") && defence_rating.equals("") && forward_rating.equals("") && referee_rating.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.select_player_role));
        }*/ else {
            if (youare.equals("Team_Manager") || youare.equals("Team Manager")) {
//                new UploadFileToServer(first_name, last_name, civic_no, apparment, street, city, country,
//                        postal_code, email, province, phone_number).execute();
                Log.e("TM***address_Tm", address);
                new UploadFileToServer(first_name, last_name, civic_no, address, latitude + "", longitude + "", country, email, province, phone_number).execute();
            } else if (youare.equals("Player")) {
                if (goalie_rating.equals("") && defence_rating.equals("") && forward_rating.equals("") && referee_rating.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_player_role));
                } else {
//                    new UploadFileToServer(first_name, last_name, civic_no, apparment, street, city, country,
//                            postal_code, email, province, phone_number).execute();
                    Log.e("Pl***address_pl", address);
                    new UploadFileToServer(first_name, last_name, civic_no, address, latitude + "", longitude + "", country, email, province, phone_number).execute();
                }
            }
        }
    }

    private boolean isName(String email) {

        String regexStr = "^[a-zA-Z ]+$";

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isUs(String email) {

        String regexStr = "^[0-9 ]+$";

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPostalCode(String code) {
        String POSTAL_REGEX = "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]";

        Pattern pattern = Pattern.compile(POSTAL_REGEX);
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }

/*    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        HttpClient httpclient;
        HttpPost httppost;
        String first_name, last_name, civic_no, country,
                email, province, phone_number;
        //        String apparment, street, city, country,postal_code;
        String address, lat, longi;

        public UploadFileToServer(String first_name, String last_name, String civic_no, String address,
                                  String lat, String longi, String country,
                                  String email, String province, String phone_number) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.civic_no = civic_no;
            this.address = address;
            this.lat = lat;
            this.longi = longi;
            this.country = country;
//            this.postal_code = postal_code;
            this.email = email;
            this.province = province;
            this.phone_number = phone_number;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EditProfile.this);
            pDialog.show();
            pDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage(String.valueOf("Loading..." + progress[0])
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
            httppost = new HttpPost(URL.Url_update_profile);

            httppost.addHeader("Authorization", cc.loadPrefString("Authorization"));
            httppost.addHeader("UserAuth", cc.loadPrefString("user_token"));
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
                entity.addPart("address", new StringBody(address));
                entity.addPart("lat", new StringBody(lat));
                entity.addPart("long", new StringBody(longi));
//                entity.addPart("apartment", new StringBody(apparment));
//                entity.addPart("street", new StringBody(street));
//                entity.addPart("city", new StringBody(city));
                entity.addPart("state_id", new StringBody(stateId));
                entity.addPart("country_id", new StringBody(countryId));
//                entity.addPart("postal_code", new StringBody(postal_code));
                entity.addPart("email", new StringBody(email));
                entity.addPart("phone_number", new StringBody(phone_number));
                entity.addPart("login_type", new StringBody("App"));

                entity.addPart("travel_distance", new StringBody(distance));

                Log.e("@@@all", "f_n->" + first_name + ", l_n->" + last_name + "add->" + address + "lat->" + lat + ", long->" + longi);

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
                        } else if (groundTypeList.get(i).equals("5X5")) {
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
                entity.addPart("gender", new StringBody(cc.loadPrefString("gender")));
                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("birth_date", new StringBody(cc.dConvert(cc.loadPrefString("birth_date"))));
                Log.e("@@DOB",cc.dConvert(cc.loadPrefString("birth_date")));
                Log.e("@@DOB2",cc.loadPrefString("birth_date"));

                /*if (cc.loadPrefString("isManager").equals("Team_Manager")) {
                    entity.addPart("user_type[]", new StringBody("Team_Manager"));
                }*/

                if (!youare.equals("Player")) {
                    entity.addPart("user_type[]", new StringBody("Team_Manager"));
                    cc.savePrefString("you_are", "Team Manager");
                } else {

                    if (b_goalie) {
                        entity.addPart("user_type[]", new StringBody(goalie));
                        entity.addPart("goalie", new StringBody(goalie_rating));
                        Log.e("goalie_rating", goalie + "" + goalie_rating);
                    }

                    if (b_defense) {
                        entity.addPart("user_type[]", new StringBody(defence));
                        entity.addPart("defence", new StringBody(defence_rating));
                        Log.e("defence_rating", defence + "" + defence_rating);

                    }

                    if (b_striker) {
                        entity.addPart("user_type[]", new StringBody(forward));
                        entity.addPart("forward", new StringBody(forward_rating));
                        Log.e("forward_rating", forward + "" + forward_rating);
                    }

                    if (b_referee) {
                        entity.addPart("user_type[]", new StringBody(referee));
                        entity.addPart("referee", new StringBody(referee_rating));
                        Log.e("referee_rating", referee + "" + referee_rating);
                    }
                    cc.savePrefString("you_are", "Player");
                    /*if (!goalie.equals("")) {
                        entity.addPart("user_type[]", new StringBody(goalie));
                        entity.addPart("goalie", new StringBody(goalie_rating));
                        Log.e("goalie_rating", goalie_rating);
                    }

                    if (!defence.equals("")) {
                        entity.addPart("user_type[]", new StringBody(defence));
                        entity.addPart("defence", new StringBody(defence_rating));
                        Log.e("defence_rating", defence_rating);

                    }

                    if (!forward.equals("")) {
                        entity.addPart("user_type[]", new StringBody(forward));
                        entity.addPart("forward", new StringBody(forward_rating));
                        Log.e("forward_rating", forward_rating);
                    }

                    if (!referee.equals("")) {
                        entity.addPart("user_type[]", new StringBody(referee));
                        entity.addPart("referee", new StringBody(referee_rating));
                        Log.e("referee_rating", referee_rating);
                    }*/

                }

                entity.addPart("device_id", new StringBody(cc.loadPrefString("device_id")));
                Log.e("@Device", cc.loadPrefString("device_id") + "");
                entity.addPart("login_type", new StringBody("App"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);


/*
                for (Header header : httppost.getAllHeaders()) {
                        Log.e("@@@header",header.getName()+","+header.getValue()+","+header.getElements());
                }
*/

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
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Register: result", "Response from server: " + result);
            try {
                pDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {
                    cc.showToast(jObject.getString("message"));
                    dbcom.deleteTable();
                    JSONObject user_data = jObject.getJSONObject("user_data");
                    cc.savePrefString("user_type_count", user_data.getString("user_type_count"));
                    cc.savePrefString("user_id", user_data.getString("user_id"));
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
//                    int count = (Integer.parseInt(cc.loadPrefString("user_type_count")));

                    cc.savePrefString("self_rate", user_data.getString("self_rate"));

                    JSONArray week_day = user_data.getJSONArray("week_day");
                    for (int i = 0; i < week_day.length(); i++) {
                        JSONObject week = week_day.getJSONObject(i);

                        dbcom.nseartAvailabilities(week.getString("week_day"), week.getString("start_time"),
                                week.getString("end_time"), cc.loadPrefString("user_id"));
                    }

                    finish();

                } else if (jObject.getString("status").equals("500")) {
                    cc.showToast(getResources().getString(R.string.invalidate_param));
                } else {
                    cc.showSnackbar(ll_linear, jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 0:

                android.app.DatePickerDialog _date2 = new android.app.DatePickerDialog(this, to_dateListener, to_year, to_month, to_day);
                Calendar c1 = Calendar.getInstance();
                c1.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), (cal.get(Calendar.DATE)) + 30);
                _date2.getDatePicker().setMaxDate(c1.getTimeInMillis());

                return _date2;

        }
        return null;
    }

    android.app.DatePickerDialog.OnDateSetListener to_dateListener = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            String selected_date = arg1 + "-" + (arg2 + 1) + "-" + arg3;
            Date date = null;
            SimpleDateFormat formate_to1 = null, formate_to2 = null, formate_to3 = null, formate_to4 = null, formate_to5 = null;
            try {

                SimpleDateFormat formate = new SimpleDateFormat("yyyy-M-d");
                date = formate.parse(selected_date);

            } catch (ParseException e) {

            }

            try {
                formate_to1 = new SimpleDateFormat("yyyy");
                formate_to2 = new SimpleDateFormat("MMM");
                formate_to3 = new SimpleDateFormat("dd");
                tv_year.setText(formate_to1.format(date));
                tv_month.setText(formate_to2.format(date));
                tv_day.setText(formate_to3.format(date));
                formate_to4 = new SimpleDateFormat("yyyy-MM-dd");
                formate_to5 = new SimpleDateFormat("MM-dd-yyyy");
                DOB = formate_to5.format(date);
                cc.savePrefString("birth_date",formate_to4.format(date));
                Log.e("@@Selected Date", DOB);
                Log.e("@@selected date", formate_to4.format(date));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    @Override
    public void onPause() {

        super.onPause();
        if (pDialog != null)
            pDialog.dismiss();
    }
}
