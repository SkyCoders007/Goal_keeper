package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.database.SQLiteTD;
import com.mxi.goalkeeper.model.country;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mxi.goalkeeper.activity.Splash.countryList;

public class SignUpNext extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    int PLACE_PICKER_REQUEST = 1;
    ProgressDialog pDialog;
    public static ArrayList<country> stateList;
    public static String countryId = "";
    public static String stateId = "";
    public static boolean b_goalie = false, b_striker = false, b_defense = false, b_referee = false;
    private PopupWindow popupWindow;
    TextView tv_sign_in, tv_rating_info, et_country, et_province, tv_year, tv_month, tv_day,tv_address;
    RoundedImageView iv_profile;
    EditText et_first_name, et_last_name, et_civic_no, et_email, et_confirm_email, et_phone_number, et_password, et_confirm_password;
    //    EditText et_apartment, et_street, et_city, et_postal_code;
    RadioGroup radio_choose_player, radio_goalie_rating, radio_defence_rating, radio_forward_rating, radio_referee_rating, radio_choose_gender;
    RadioButton radio_player, radio_team_manager, radioA, radioB, radioC, radioD, radioE,
            radio_defenceA, radio_defenceB, radio_defenceC, radio_defenceD, radio_defenceE,
            radio_forwardA, radio_forwardB, radio_forwardC, radio_forwardD, radio_forwardE,
            radio_refereeA, radio_refereeB, radio_refereeC, radio_refereeD, radio_refereeE;
    CheckBox checkbox_goalie, checkbox_defence, checkbox_forward, checkbox_referee, radio_male, radio_female;
    Button btn_next;
    LinearLayout ll_team_manager;
    ScrollView scrollView;
    CommanClass cc;
    SQLiteTD dbcom;
    String DOB = "", gender = "", youare = "", goalie = "", goalie_rating = "", defence = "", defence_rating = "", forward = "", forward_rating = "", referee = "",
            referee_rating = "";
    private static final int SELECT_PICTURE = 2;
    private String selectedImagePath = "";
    int to_year, to_month, to_day;
    Calendar cal;
    long totalSize = 0;
    // GoogleCloudMessaging gcmObj;
    double longitude;
    double latitude;
    String gcmId, address;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cc = new CommanClass(SignUpNext.this);
        dbcom = new SQLiteTD(SignUpNext.this);
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        cc.savePrefString("device_id", android_id);
        //  new GetGcmId().execute();
        gcmId = FirebaseInstanceId.getInstance().getToken();

/*        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Address");*/

        Log.e("gcmId ", gcmId);
        initUI();


        googleApiClient  = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


/*        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    private void initUI() {

        tv_address= (TextView) findViewById(R.id.tv_address);
        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        tv_rating_info = (TextView) findViewById(R.id.tv_rating_info);
        iv_profile = (RoundedImageView) findViewById(R.id.iv_profile);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_civic_no = (EditText) findViewById(R.id.et_civic_no);
//        et_apartment = (EditText) findViewById(R.id.et_apartment);
//        et_street = (EditText) findViewById(R.id.et_street);
//        et_city = (EditText) findViewById(R.id.et_city);
        et_country = (TextView) findViewById(R.id.et_country);
        et_province = (TextView) findViewById(R.id.et_province);
//        et_postal_code = (EditText) findViewById(R.id.et_postal_code);
        et_email = (EditText) findViewById(R.id.et_email);
        et_confirm_email = (EditText) findViewById(R.id.et_confirm_email);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_password = (EditText) findViewById(R.id.et_password);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ll_team_manager = (LinearLayout) findViewById(R.id.ll_team_manager);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_day = (TextView) findViewById(R.id.tv_day);
        radio_choose_player = (RadioGroup) findViewById(R.id.radio_choose_player);
        radio_goalie_rating = (RadioGroup) findViewById(R.id.radio_goalie_rating);
        radio_defence_rating = (RadioGroup) findViewById(R.id.radio_defence_rating);
        radio_forward_rating = (RadioGroup) findViewById(R.id.radio_forward_rating);
        radio_referee_rating = (RadioGroup) findViewById(R.id.radio_referee_rating);
        radio_choose_gender = (RadioGroup) findViewById(R.id.radio_choose_gender);

        radio_player = (RadioButton) findViewById(R.id.radio_player);
        radio_team_manager = (RadioButton) findViewById(R.id.radio_team_manager);
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

        checkbox_goalie = (CheckBox) findViewById(R.id.checkbox_goalie);
        checkbox_defence = (CheckBox) findViewById(R.id.checkbox_defence);
        checkbox_forward = (CheckBox) findViewById(R.id.checkbox_forward);
        checkbox_referee = (CheckBox) findViewById(R.id.checkbox_referee);

        btn_next = (Button) findViewById(R.id.btn_next);

        Typeface font = Typeface.createFromAsset(getAssets(), "font/Questrial-Regular.otf");
        radioA.setTypeface(font);
        radioB.setTypeface(font);
        radioC.setTypeface(font);
        radioD.setTypeface(font);
        radioE.setTypeface(font);
        radio_defenceA.setTypeface(font);
        radio_defenceB.setTypeface(font);
        radio_defenceC.setTypeface(font);
        radio_defenceD.setTypeface(font);
        radio_defenceE.setTypeface(font);
        radio_forwardA.setTypeface(font);
        radio_forwardB.setTypeface(font);
        radio_forwardC.setTypeface(font);
        radio_forwardD.setTypeface(font);
        radio_forwardE.setTypeface(font);
        radio_refereeA.setTypeface(font);
        radio_refereeB.setTypeface(font);
        radio_refereeC.setTypeface(font);
        radio_refereeD.setTypeface(font);
        radio_refereeE.setTypeface(font);

        checkbox_goalie.setTypeface(font);
        checkbox_defence.setTypeface(font);
        checkbox_forward.setTypeface(font);
        checkbox_referee.setTypeface(font);
        tv_sign_in.setText(Html.fromHtml(getString(R.string.already_account) + " " + "<u>" + getString(R.string.sign_in) + "</u"));

        tv_rating_info.setOnClickListener(this);
        tv_sign_in.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        et_country.setOnClickListener(this);
        et_province.setOnClickListener(this);

        checkbox_goalie.setOnCheckedChangeListener(this);
        checkbox_defence.setOnCheckedChangeListener(this);
        checkbox_forward.setOnCheckedChangeListener(this);
        checkbox_referee.setOnCheckedChangeListener(this);
        tv_year.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_day.setOnClickListener(this);
        tv_address.setOnClickListener(this);

        radio_choose_player.setOnCheckedChangeListener(this);
        radio_goalie_rating.setOnCheckedChangeListener(this);
        radio_defence_rating.setOnCheckedChangeListener(this);
        radio_forward_rating.setOnCheckedChangeListener(this);
        radio_referee_rating.setOnCheckedChangeListener(this);
        radio_choose_gender.setOnCheckedChangeListener(this);

        ll_team_manager.setVisibility(View.GONE);

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

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    makeJsonEmailVerify(et_email.getText().toString().trim());
                }
            }
        });
        /*et_postal_code.addTextChangedListener(new TextWatcher() {
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

                    // et_postal_code.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et_postal_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }

            }
        });
*/
    }

    private void RatingDialog() {
        final Dialog dialog = new Dialog(SignUpNext.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address:
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(SignUpNext.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_rating_info:
                RatingDialog();
                break;
            case R.id.btn_next:
                SignUpValidation();
                break;
            case R.id.iv_profile:
                selectfile();
                break;
            case R.id.tv_sign_in:
                startActivity(new Intent(SignUpNext.this, Login.class));
                finish();
                break;
            case R.id.et_country:
                countryPopup();
                break;
            case R.id.et_province:
                if (countryId.equals("")) {
                    cc.showSnackbar(scrollView, getString(R.string.enter_country));
                } else {
                    proviencePopup();
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
            SimpleDateFormat formate_to1 = null, formate_to2 = null, formate_to3 = null,formate_to4 = null, formate_to5 = null;
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
                formate_to5 = new SimpleDateFormat("MM-dd-yyyy");
                formate_to4 = new SimpleDateFormat("yyyy-MM-dd");
                DOB = formate_to4.format(date);
                Log.e("@@set selected date", DOB);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void proviencePopup() {
        PopupMenu popup = new PopupMenu(SignUpNext.this, et_province);
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
/*                Toast.makeText(
                        SignUpNext.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();*/

//                province=item.getTitle().toString();
                stateId = String.valueOf(item.getItemId());
                et_province.setText(item.getTitle().toString());
                return true;
            }
        });

        popup.show();
    }

    private void countryPopup() {
        PopupMenu popup = new PopupMenu(SignUpNext.this, et_country);
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
        pDialog = new ProgressDialog(SignUpNext.this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_state,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:get_state", response);
                        stateList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("state_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                country country = new country();
                                country.setC_id(object.getString("state_id"));
                                country.setC_name(object.getString("state"));
                                stateList.add(country);
                            }
                            pDialog.dismiss();
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
                Log.i("request get_state", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void selectfile() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }

    private void SignUpValidation() {

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
        String confirm_email = et_confirm_email.getText().toString().trim();
        String phone_number = et_phone_number.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String address = tv_address.getText().toString().trim();
        String confirm_password = et_confirm_password.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            cc.showSnackbar(scrollView, getString(R.string.no_internet));
        } /*else if (selectedImagePath.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_image));
        }*/ else if (first_name.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_first_name));
        } else if ((!isName(first_name))) {
            cc.showSnackbar(scrollView, getString(R.string.numeral_first_name));
        } else if (last_name.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_last_name));
        } else if ((!isName(last_name))) {
            cc.showSnackbar(scrollView, getString(R.string.numeral_last_name));
        } else if (address.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_address));
        }/*else if (civic_no.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_civic_no));
        }*//* else if (apparment.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_apartment));
        }*/
        /*else if (street.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_street));
        } else if (city.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_city));
        }*/
        /*else if (country.equals("") || country.equals(getResources().getString(R.string.country))) {
            cc.showSnackbar(scrollView, getString(R.string.enter_country));
        } else if (province.equals("") || province.equals(getResources().getString(R.string.province))) {
            cc.showSnackbar(scrollView, getString(R.string.enter_province_state));
        }*/
        /*else if (postal_code.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_postal_code));
        }else if (et_country.getText().toString().trim().equals("Canada") && !isPostalCode(postal_code)) {
            cc.showSnackbar(scrollView, "Please enter valid postal code");
        } else if (et_country.getText().toString().trim().equals("United States of America") && !isUs(postal_code)) {
            cc.showSnackbar(scrollView, "Please enter valid postal code");
        }*/
        else if (email.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_email));
        } else if (confirm_email.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_confirm_email));
        } else if (!confirm_email.equals(email)) {
            cc.showSnackbar(scrollView, getString(R.string.enter_match_email));
        } else if (password.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_password));
        } else if (password.length() < 6) {
            cc.showSnackbar(scrollView, getString(R.string.password_length));
        } else if (confirm_password.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.confirm_pass));
        } else if (!password.matches(confirm_password)) {
            cc.showSnackbar(scrollView, getString(R.string.enter_confirm_pass));
        } else if (phone_number.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.enter_phone_number));
        } else if (phone_number.length() != 12) {
            cc.showSnackbar(scrollView, getString(R.string.mobile_validation));
        } else if (gender.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_gender));
        } else if (DOB.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_dob));
        } else if (youare.equals("")) {
            cc.showSnackbar(scrollView, getString(R.string.select_you_are));
        } else if (youare.equals(getResources().getString(R.string.player))) {

            if (goalie.equals("") && defence.equals("") && forward.equals("") && referee.equals("")) {
                cc.showSnackbar(scrollView, getString(R.string.select_player_role));
            } else {
                if (!goalie.equals("") && goalie_rating.equals("")) {
                    cc.showSnackbar(scrollView, getString(R.string.select_player_self_rating));
                } else if (!defence.equals("") && defence_rating.equals("")) {
                    cc.showSnackbar(scrollView, getString(R.string.select_player_self_rating));
                } else if (!forward.equals("") && forward_rating.equals("")) {
                    cc.showSnackbar(scrollView, getString(R.string.select_player_self_rating));
                } else if (!referee.equals("") && referee_rating.equals("")) {
                    cc.showSnackbar(scrollView, getString(R.string.select_player_self_rating));
                } else {

//                    gotoNextActivity(first_name, last_name, civic_no, apparment, street, city, country,
//                            postal_code, email, province, password, confirm_email, confirm_password, phone_number, gcmId);

                    gotoNextActivity(first_name, last_name, civic_no, address, latitude + "", longitude + "", country, province, email, password, confirm_email, confirm_password, phone_number, gcmId);
                }
            }

        } else {
            if (goalie.equals("") && defence.equals("") && forward.equals("") && referee.equals("")) {
//                new UploadFileToServer(first_name, last_name, civic_no, apparment, street, city, country,
//                        postal_code, email, province, password, confirm_email, confirm_password, phone_number).execute();
                new UploadFileToServer(first_name, last_name, civic_no, address, latitude + "", longitude + "", country, province, email, password, confirm_email, confirm_password, phone_number).execute();

            } else {
//                gotoNextActivity(first_name, last_name, civic_no, apparment, street, city, country,
//                        postal_code, email, province, password, confirm_email, confirm_password, phone_number, gcmId);
                gotoNextActivity(first_name, last_name, civic_no, address, latitude + "", longitude + "", country, province, email, password, confirm_email, confirm_password, phone_number, gcmId);

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
/*
    private void gotoNextActivity(String first_name, String last_name, String civic_no, String apparment, String street
            , String city, String country, String postal_code, String email, String province
            , String password, String confirm_email, String confirm_password, String phone_number, String gcmId) {
        startActivity(new Intent(SignUpNext.this, SignUp.class).putExtra("first_name", first_name).
                putExtra("last_name", last_name).putExtra("civic_no", civic_no).putExtra("apparment", apparment).
                putExtra("street", street).putExtra("city", city).putExtra("country", country).putExtra("province", province)
                .putExtra("postal_code", postal_code).putExtra("email", email).putExtra("confirm_email", confirm_email).
                        putExtra("password", password).putExtra("confirm_password", confirm_password).putExtra("youare", youare)
                .putExtra("phone_number", phone_number).putExtra("goalie", goalie).putExtra("goalie_rating", goalie_rating)
                .putExtra("defence", defence).putExtra("defence_rating", defence_rating).putExtra("forward", forward)
                .putExtra("forward_rating", forward_rating).putExtra("referee", referee).putExtra("referee_rating", referee_rating)
                .putExtra("selectedImagePath", selectedImagePath).putExtra("DOB", DOB).putExtra("gender", gender).putExtra("gcmId", gcmId));
        Log.e("Country", country);
        Log.e("Province", province);
        finish();
    }*/


    private void gotoNextActivity(String first_name, String last_name, String civic_no, String address, String lat, String longi, String country, String province, String email,
                                  String password, String confirm_email, String confirm_password, String phone_number, String gcmId) {
        startActivity(new Intent(SignUpNext.this, SignUp.class).putExtra("first_name", first_name).
                putExtra("last_name", last_name).putExtra("civic_no", civic_no).putExtra("address", address).
                putExtra("lat", lat).putExtra("long", longi).putExtra("country", country).putExtra("province", province)
                .putExtra("email", email).putExtra("confirm_email", confirm_email).
                        putExtra("password", password).putExtra("confirm_password", confirm_password).putExtra("youare", youare)
                .putExtra("phone_number", phone_number).putExtra("goalie", goalie).putExtra("goalie_rating", goalie_rating)
                .putExtra("defence", defence).putExtra("defence_rating", defence_rating).putExtra("forward", forward)
                .putExtra("forward_rating", forward_rating).putExtra("referee", referee).putExtra("referee_rating", referee_rating)
                .putExtra("selectedImagePath", selectedImagePath).putExtra("DOB", DOB).putExtra("gender", gender).putExtra("gcmId", gcmId));
        Log.e("Country", country);
//        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);
        switch (group.getId()) {
            case R.id.radio_choose_player:
                youare = choose_answer.getText().toString();
                if (youare.equals("Team Manager")) {
                    youare = "Team_Manager";
                    ll_team_manager.setVisibility(View.GONE);
                    btn_next.setText(getString(R.string.submit));

                } else {
                    youare = "Player";
                    ll_team_manager.setVisibility(View.VISIBLE);
                    btn_next.setText(getString(R.string.next));
                }
                break;
            case R.id.radio_goalie_rating:
                if (checkbox_goalie.isChecked()) {
                    goalie_rating = choose_answer.getText().toString();
                    btn_next.setText(getString(R.string.next));
                } else {
                    if (youare.equals("Team_Manager")) {
                        btn_next.setText(getString(R.string.submit));
                    }
                }

                break;
            case R.id.radio_defence_rating:
                if (checkbox_defence.isChecked()) {
                    defence_rating = choose_answer.getText().toString();
                    btn_next.setText(getString(R.string.next));
                } else {
                    if (youare.equals("Team_Manager")) {
                        btn_next.setText(getString(R.string.submit));
                    }
                }

                break;
            case R.id.radio_forward_rating:
                if (checkbox_forward.isChecked()) {
                    forward_rating = choose_answer.getText().toString();
                    btn_next.setText(getString(R.string.next));
                } else {
                    if (youare.equals("Team_Manager")) {
                        btn_next.setText(getString(R.string.submit));
                    }
                }
                break;
            case R.id.radio_referee_rating:
                if (checkbox_referee.isChecked()) {
                    referee_rating = choose_answer.getText().toString();
                    btn_next.setText(getString(R.string.next));
                } else {
                    if (youare.equals("Team_Manager")) {
                        btn_next.setText(getString(R.string.submit));
                    }
                }
                break;
            case R.id.radio_choose_gender:
                gender = choose_answer.getText().toString();
                // Toast.makeText(SignUp.this, choose_text.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
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
//                    defence = checkbox_defence.getText().toString();//defense
                    defence = getResources().getString(R.string.defence);
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
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SignUpNext.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    selectedImagePath = getPath(selectedImageUri);
                    Log.e("Selected File", selectedImagePath);

                    ExifInterface ei = null;
                    Bitmap mybitmap = null;
                    Bitmap retVal = null;
                    //                        ei = new ExifInterface(selectedImagePath);
                    mybitmap = BitmapFactory.decodeFile(selectedImagePath);

                    File file = new File(selectedImagePath);
                    long fileSizeInBytes = file.length();

                    long fileSizeInKB = fileSizeInBytes / 1024;

                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 10) {
                        selectedImagePath = "";
                        new AlertDialog.Builder(SignUpNext.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Picasso.with(SignUpNext.this)
                                .load(uri)
                                .into(iv_profile);

//                        GenerateImage(retVal);
                        GenerateImage(mybitmap);
                        /*if (orientation != 0) {



                        }*/

                    }


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
                cursor = SignUpNext.this.getContentResolver().query(uri,
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
            File file = new File(path, "MIP.jpg");
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
        File file = new File(path, "MIP.jpg");
        selectedImagePath = file.toString();

    }

    public final static boolean isValidEmail(String target) {
        if (target.length() == 0) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return false;
        } else {
            return false;
        }
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
                email, province, password, confirm_email, confirm_password, phone_number;
        //        String apparment, street, city, country,postal_code;
        String address, lat, longi;


        /*public UploadFileToServer(String first_name, String last_name, String civic_no,
                                  String apparment, String street, String city, String country,
                                  String postal_code, String email, String province, String password,
                                  String confirm_email, String confirm_password, String phone_number) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.civic_no = civic_no;
            this.apparment = apparment;
            this.street = street;
            this.city = city;
            this.country = country;
            this.postal_code = postal_code;
            this.email = email;
            this.province = province;
            this.password = password;
            this.confirm_email = confirm_email;
            this.confirm_password = confirm_password;
            this.phone_number = phone_number;

        }*/

        public UploadFileToServer(String first_name, String last_name, String civic_no, String address, String lat, String longi,
                                  String country, String province, String email, String password,
                                  String confirm_email, String confirm_password, String phone_number) {
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
            this.password = password;
            this.confirm_email = confirm_email;
            this.confirm_password = confirm_password;
            this.phone_number = phone_number;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpNext.this);
            // pDialog.setMessage("Please wait...");
            pDialog.show();
            //progressDialog.show();
            //  progressDialog.setContentView(R.layout.custom_progressdialog);
            // progressBar_tv = (TextView) progressDialog.findViewById(R.id.progressBar_tv);
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
                entity.addPart("address", new StringBody(address));
                entity.addPart("lat", new StringBody(latitude + ""));
                entity.addPart("long", new StringBody(longitude + ""));
//                entity.addPart("apartment", new StringBody(apparment));
//                entity.addPart("street", new StringBody(street));
//                entity.addPart("city", new StringBody(city));
                // entity.addPart("state", new StringBody(province));
                entity.addPart("state_id", new StringBody(SignUpNext.stateId));//check============
                // entity.addPart("country", new StringBody(country));
                entity.addPart("country_id", new StringBody(SignUpNext.countryId));//check============
//                entity.addPart("postal_code", new StringBody(postal_code));
                entity.addPart("email", new StringBody(email));
                entity.addPart("phone_number", new StringBody(phone_number));
                entity.addPart("password", new StringBody(password));
                entity.addPart("login_type", new StringBody("App"));

                entity.addPart("gender", new StringBody(gender));

                entity.addPart("user_type[]", new StringBody(youare));

                /*if (b_goalie) {
                    entity.addPart("user_type[]", new StringBody(goalie));
                    entity.addPart("goalie", new StringBody(goalie_rating));
                    Log.e("goalie_rating", goalie_rating);
                }

                if (b_defense) {
                    entity.addPart("user_type[]", new StringBody(defence));
                    entity.addPart("defence", new StringBody(defence_rating));
                    Log.e("defence_rating", defence_rating);

                }

                if (b_striker) {
                    entity.addPart("user_type[]", new StringBody(forward));
                    entity.addPart("forward", new StringBody(forward_rating));
                    Log.e("forward_rating", forward_rating);
                }

                if (b_referee) {
                    entity.addPart("user_type[]", new StringBody(referee));
                    entity.addPart("referee", new StringBody(referee_rating));
                    Log.e("referee_rating", referee_rating);
                }*/


                Log.e("@@birth_date",DOB);
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
                pDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {
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

                        if (user_list.get(j).equals("Team_Manager")) {

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
                    Intent mIntent = new Intent(SignUpNext.this,
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

    /*public class GetGcmId extends AsyncTask<String, Void, Void> {

        String regId;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // progress_bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                if (gcmObj == null) {
                    gcmObj = GoogleCloudMessaging
                            .getInstance(getApplicationContext());
                }
                regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);

            } catch (IOException ex) {
                Log.e("Error in get GCM", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //progress_bar.setVisibility(View.INVISIBLE);

            if (!TextUtils.isEmpty(regId)) {
                Log.e("gcm_id", regId);

                gcmId = regId;
                //  makeJsonlogin(email, password, regId);

            } else {
                Toast.makeText(SignUpNext.this, "Can't get GCM reg. ID", Toast.LENGTH_LONG);

            }

        }
    }*/

    //======================================================EMAIL VERIFY==================================

    private void makeJsonEmailVerify(final String email) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_email_verify,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:email_verify", response);
                        countryList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));
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
                params.put("email", email);

                Log.i("request email_verify", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                Log.i("request header", headers.toString());
                return headers;
            }


        };


        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
        }

    }
}
