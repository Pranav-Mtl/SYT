package com.syt.balram.syt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.RequirementBE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RequirementTwo extends ActionBarActivity {

    RequirementTwoAdapter objRequirementTwoAdapter;

    RequirementBE objRequirementBE;
    Button btnStart,btnEnd,btnNext;
    private TextView datePickerShowDialogButton,error;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_two);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
        back= (ImageButton) findViewById(R.id.requirement_two_back);
        objRequirementTwoAdapter=new RequirementTwoAdapter(getApplicationContext());
        ListView listview = (ListView)findViewById(R.id.requirement_two_listview);
        btnStart= (Button) findViewById(R.id.requirement_two_start_date);
        btnEnd= (Button) findViewById(R.id.requirement_two_end_date);
        btnNext= (Button) findViewById(R.id.requirement_two_next);
        error= (TextView) findViewById(R.id.requirement_two_error);
        listview.setAdapter(objRequirementTwoAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent1=getIntent();
        objRequirementBE= (RequirementBE) intent1.getSerializableExtra("RequirementBE");


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                objRequirementBE.setServiceDays(RequirementTwoAdapter.txtItemList[position]);
                Intent intent=new Intent(getApplicationContext(),RequirementThree.class);
                intent.putExtra("RequirementBE",objRequirementBE);
                startActivity(intent);

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerEnd();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error.setVisibility(View.GONE);
                String txtStart = btnStart.getText().toString();
                String txtEnd = btnEnd.getText().toString();
                final Calendar now = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
                Date date1, date2,currentDate;
                try {

                if(txtStart.length()==0)
                {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please enter start date");
                }
                else if(txtEnd.length()==0)
                {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please enter end date");
                }
                else
                {
                    date1 = formatter.parse(txtStart);
                    date2 = formatter.parse(txtEnd);
                    //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    System.out.println(formatter.format(date));
                    String current=formatter.format(date);
                    currentDate=formatter.parse(current);
                    System.out.println("current date text"+current);
                    System.out.println("start date text"+txtStart);
                    System.out.println("end date text"+txtEnd);
                    System.out.println("current date"+currentDate);
                    System.out.println("start date"+date1);
                    System.out.println("end date"+date2);
                    if ((date1.compareTo(date2)>0)) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("End date must be greater or equal to start date");
                        System.out.println(" end must be greater or equals to start date");
                     }
                    else if((date1.compareTo(currentDate)<0))
                    {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Start date must be greater or equal to current date");
                        System.out.println("Start date must be greater or equals to current date");
                    }
                    else if((date2.compareTo(currentDate)<0))
                    {
                        error.setVisibility(View.VISIBLE);
                        error.setText("End date must be greater or equal to current date");
                        System.out.println("End date must be greater or equals to current date");
                    }
                    else {
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd",Locale.UK);
                        txtStart=formatter2.format(date1);
                        txtEnd=formatter2.format(date2);
                        objRequirementBE.setServiceDays(txtStart + " - " + txtEnd);
                        Intent intent = new Intent(getApplicationContext(), RequirementThree.class);
                        intent.putExtra("RequirementBE", objRequirementBE);
                        startActivity(intent);
                    }
                }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requirement_two, menu);
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


    public static class RequirementTwoAdapter extends BaseAdapter {

        Context mContext;
        TextView txtItem;
        public static String[] txtItemList;

        public RequirementTwoAdapter(Context context)
        {
            mContext=context;

            txtItemList=new String[4];
            txtItemList[0]="Monday - Friday";
            txtItemList[1]="Monday - Saturday";
            txtItemList[2]="Weekends";
            txtItemList[3]="All 7 days";


        }
        @Override
        public int getCount() {
            return txtItemList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(mContext);
                gridView= infalInflater.inflate(R.layout.requirement_raw_list, null);

            }
            txtItem = (TextView) gridView .findViewById(R.id.requirement_list_text);

            txtItem.setText(txtItemList[position]);

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,80));
            return gridView;
        }
    }

    public void showDatePickerEnd() {
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
                new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("11-12-1950"));
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
                        btnEnd.setText(
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
                        btnEnd.setText(
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
                new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("11-12-1950"));
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
                        btnStart.setText(
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
                        btnStart.setText(
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
}
