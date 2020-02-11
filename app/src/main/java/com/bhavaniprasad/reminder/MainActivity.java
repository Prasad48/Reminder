package com.bhavaniprasad.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Calendar c;
    Button future,past;
    ListView listview;
    DatabaseManager mDatabase;
    List<EventsModel> eventsmodellist;
    EditText title,description;
    EventsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase= new DatabaseManager(this);
        future=findViewById(R.id.future);
        past=findViewById(R.id.past);
        listview=findViewById(R.id.listview);
        eventsmodellist =new ArrayList<>();
        future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loadEventsFromDatabase();
//                Date currentTime = Calendar.getInstance().getTime();
//                Calendar c= Calendar.getInstance();
//                c.setTime(currentTime);
//                long time = c.getTimeInMillis();
//                String stime = String.valueOf(time);
//                String newt = convertDate(stime,"dd/MM/yyyy hh:mm:ss");
//                Date c2=currentTime;
                adapter = new EventsAdapter(MainActivity.this, R.layout.list_layout_events, eventsmodellist, mDatabase);
                adapter.clear();
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
                loadfutureEventsFromDatabase();


            }
        });

        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new EventsAdapter(MainActivity.this, R.layout.list_layout_events, eventsmodellist, mDatabase);
                adapter.clear();
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);

                loadpastEventsFromDatabase();
            }
        });
    }

    public void loadfutureEventsFromDatabase(){


        Cursor cursor = mDatabase.getfutureevents();

        if (cursor.moveToFirst()) {
            do {
                eventsmodellist.add(new EventsModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());

            //passing the databasemanager instance this time to the adapter
            adapter = new EventsAdapter(this, R.layout.list_layout_events, eventsmodellist, mDatabase);
            findViewById(R.id.Noitems).setVisibility(View.INVISIBLE);
            listview.setAdapter(adapter);
        }
        else{
            if(eventsmodellist.size()==0)
                findViewById(R.id.Noitems).setVisibility(View.VISIBLE);
        }
    }

    public void loadpastEventsFromDatabase(){


        Cursor cursor = mDatabase.getpastevents();

        if (cursor.moveToFirst()) {
            do {
                eventsmodellist.add(new EventsModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());

            //passing the databasemanager instance this time to the adapter
             adapter = new EventsAdapter(this, R.layout.list_layout_events, eventsmodellist, mDatabase);
            findViewById(R.id.Noitems).setVisibility(View.INVISIBLE);
             listview.setAdapter(adapter);
        }
        else{
            if(eventsmodellist.size()==0)
                findViewById(R.id.Noitems).setVisibility(View.VISIBLE);
        }
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
        final View dialogView = View.inflate(MainActivity.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                description=(EditText) dialogView.findViewById(R.id.description);
                title=(EditText) dialogView.findViewById(R.id.nameedittext);
                Calendar c = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                long time = c.getTimeInMillis();
                String stime = String.valueOf(time);
                String newt = convertDate(stime,"yyyy-MM-dd hh:mm:ss");
                mDatabase.addEmployee(title.getText().toString(),description.getText().toString(),stime);
                alertDialog.dismiss();
            }});
                alertDialog.setView(dialogView);
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
