package com.bhavaniprasad.reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class EventsAdapter extends ArrayAdapter<EventsModel> {
    Context mCtx;
    int layoutRes;
    List<EventsModel> employeeList;

    //the databasemanager object
    DatabaseManager mDatabase;
    public EventsAdapter(Context context, int list_layout_events, List<EventsModel> eventsmodellist, DatabaseManager mDatabase) {
        super(context,list_layout_events,eventsmodellist);
        this.mCtx=context;
        this.layoutRes=list_layout_events;
        this.employeeList=eventsmodellist;
        this.mDatabase=mDatabase;
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes, null);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDept = view.findViewById(R.id.textViewdescription);
        TextView textViewJoinDate = view.findViewById(R.id.textViewJoiningDate);

        final EventsModel events = employeeList.get(position);

        textViewName.setText(events.getName());
        textViewDept.setText(events.getDescription());
        String date_from_millisec = convertDate(events.getDate(),"dd/MM/yyyy hh:mm:ss");

        textViewJoinDate.setText(date_from_millisec);

        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee(events);
            }
        });

        return view;
    }

    private void deleteEmployee(final EventsModel employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //calling the delete method from the database manager instance
                if (mDatabase.deleteEmployee(employee.getId()))
                    loadEmployeesFromDatabaseAgain();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadEmployeesFromDatabaseAgain() {
        //calling the read method from database instance
        Cursor cursor = mDatabase.getAllEmployees();

        employeeList.clear();
        if (cursor.moveToFirst()) {
            do {
                employeeList.add(new EventsModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }
}
