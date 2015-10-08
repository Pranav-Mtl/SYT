package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.Age;
import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.constant.Constant;
import com.syt.util.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SellerQuestionOne extends ActionBarActivity implements AdapterView.OnItemClickListener {

    Calendar dateandtime;
    EditText txtFirstName,txtLastName;
    EditText phone;
    AutoCompleteTextView zip;
    Button btnNext;
    Button txtDate;
    private TextView datePickerShowDialogButton;
    Spinner spinnerGender;
    RadioGroup groupIam,groupGender;
    RadioButton iAM,Gender;
    ImageButton back;

    SellerQuestionBE objSellerQuestionBE;

    private static final String LOG_TAG = "SellYourTime";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_question_one);
        txtDate= (Button) findViewById(R.id.seller_date);
        btnNext= (Button) findViewById(R.id.seller_next);
        groupIam= (RadioGroup) findViewById(R.id.radio_iam);
        groupGender= (RadioGroup) findViewById(R.id.radio_gender);
        back= (ImageButton) findViewById(R.id.seller_question_one);
        //spinnerGender= (Spinner) findViewById(R.id.spinnerSeller);
        txtFirstName= (EditText) findViewById(R.id.seller_firstname);
        txtLastName= (EditText) findViewById(R.id.seller_lastname);
        phone= (EditText) findViewById(R.id.seller_phone);
        zip= (AutoCompleteTextView) findViewById(R.id.seller_zip);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent2=getIntent();
        objSellerQuestionBE=(SellerQuestionBE)intent2.getSerializableExtra("SellerQuestionBE");

        LinearLayout obj_ll_et2=(LinearLayout)findViewById(R.id.seller1HeaderMain);  //keyboard hiding
        obj_ll_et2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });

        if(Constant.LoginMedium=="linkedin" || Constant.LoginMedium=="facebook") {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            String fname = bundle.get("First Name").toString();
            String lname = bundle.get("Last Name").toString();
            txtFirstName.setText(fname);
            txtLastName.setText(lname);
        }
        else
        {

        }
        List categories = new ArrayList();
        categories.add("Gender");
        categories.add("Male");
        categories.add("Female");

        /*ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_spinner_item,categories);
        spinnerGender.setAdapter(adapter);*/

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {

                int selected = groupIam.getCheckedRadioButtonId();
                int selectedGender=groupGender.getCheckedRadioButtonId();
                iAM = (RadioButton) findViewById(selected);
                Gender= (RadioButton) findViewById(selectedGender);
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String date = txtDate.getText().toString();
                String mPhone=phone.getText().toString();
                String mAddress=zip.getText().toString();



                int years=0;

                try {
                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format2.parse(date);
                    System.out.println("new date"+format1.format(date1));
                    String newDate=format1.format(date1);

                    Date birthDate = format1.parse(newDate); //Yeh !! It's my date of birth :-)


                    Age age = AgeCalculater.calculateAge(birthDate);
                    //My age is

                    System.out.println("AGGGEGEGGEGGEGE"+age);
                    years=age.getYears();
                    System.out.println("YEArs" + String.valueOf(age.getYears()));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                // String formarDate=new SimpleDateFormat("yyyy-MM-dd").format(date);
               // String gender = spinnerGender.getSelectedItem().toString();

                if (firstName.trim().length() == 0) {
                    txtFirstName.setError("required");

                } else if (lastName.trim().length() == 0) {
                    txtLastName.setError("required");
                } else if (date.trim().length() == 0) {

                    txtDate.setError("required");
                }
                else if(mPhone.trim().length()==0)
                {
                    phone.setError("required");
                }
                else if(mPhone.trim().length()!=10)
                {
                    phone.setError("Invalid number");
                }
                else if(mAddress.trim().length()==0)
                {
                    zip.setError("required");
                }
                else if(years<18)
                {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            SellerQuestionOne.this);
// Setting Dialog Title


// Setting Dialog Message
                    alertDialog2.setMessage("You must be atleast 18 years old to register");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                    alertDialog2.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();
                                }
                            });
// Setting Negative "NO" Btn

// Showing Alert Dialog
                    alertDialog2.show();

                       }
                else {

                    String firstNameCaps = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
                    String lastNameCaps = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);

                    objSellerQuestionBE.setGender(Gender.getText().toString());
                    objSellerQuestionBE.setServicesWhom(iAM.getText().toString());
                    objSellerQuestionBE.setFirstName(firstNameCaps.trim());
                    objSellerQuestionBE.setLastName(lastNameCaps.trim());
                    //objSellerQuestionBE.setGender(gender.trim());
                    objSellerQuestionBE.setDob(date.trim());
                    objSellerQuestionBE.setPhone(mPhone);
                    objSellerQuestionBE.setZip(mAddress);

                    if (Configuration.isInternetConnection(SellerQuestionOne.this)) {

                        Intent intent = new Intent(getApplicationContext(), SellerQuestionExpandable.class);
                        intent.putExtra("SellerQuestionBE", objSellerQuestionBE);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SellerQuestionOne.this);
// Setting Dialog Title
                        alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
                        alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog

                        alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
                        alertDialog2.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    }
                                });
// Setting Negative "NO" Btn
                        alertDialog2.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        Toast.makeText(getApplicationContext(),
                                                "You clicked on NO", Toast.LENGTH_SHORT)
                                                .show();
                                        dialog.cancel();
                                        finish();
                                    }
                                });

// Showing Alert Dialog
                        alertDialog2.show();
                    }

                }

            }
        });

        zip.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.gender_spinner_item));

        //tvLocation.showDropDown();
        zip.setOnItemClickListener(this);

    }

    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.custom_datepicker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("1930-12-12"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        dialogBuilder.setIcon(R.drawable.logo_watch);
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = formatter.parse(
                    datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons
        dialogBuilder.setNegativeButton(
                "Go to today",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDate.setText(
                                formatter.format(now.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );

        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        txtDate.setText(
                                dateViewFormatter.format(choosen.getTime())
                        );
                        dialog.dismiss();
                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(
                                dateViewFormatter.format(choosenDate.getTime())
                        );
                        if (choosenDate.get(Calendar.DAY_OF_WEEK) ==
                                Calendar.SUNDAY ||
                                now.compareTo(choosenDate) < 0) {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        } else {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        }
                    }
                }
        );
        // Finish
        dialog.show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_question_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        System.out.println(resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&language=en");
            sb.append("components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "mmmmmmmm", e);
            return resultList;
        }

        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

}
