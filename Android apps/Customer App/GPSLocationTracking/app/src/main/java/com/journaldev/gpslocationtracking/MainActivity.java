package com.journaldev.gpslocationtracking;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;


public class MainActivity extends Activity implements View.OnClickListener {

    private static ArrayList<String> localities=new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    TextView textView;
   AutoCompleteTextView locationtext;
    FloatingActionButton f;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        try {
            initalizedb();
        } catch (IOException e) {
            System.out.print("DB error");
        }
        locationtext = (AutoCompleteTextView) findViewById(R.id.location);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_selectable_list_item, localities);

        locationtext.setThreshold(2);
        locationtext.setDropDownBackgroundResource(R.drawable.rectangle);
        locationtext.setAdapter(adapter);
        f=(FloatingActionButton)findViewById(R.id.locfab);
        textView=(TextView)findViewById(R.id.cur) ;
        f.setOnClickListener(this);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(RECEIVE_SMS);
        permissions.add(READ_SMS);
        permissions.add(SEND_SMS);


        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }




        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                locationTrack = new LocationTrack(MainActivity.this);


                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

                    if (longitude != 0.00 && latitude != 0.000) {

                             Intent i = new Intent(getApplicationContext(), checkinout.class);
                            locationTrack.stopListener();
                        Toast.makeText(getApplicationContext(), longitude+".Please move to some other near place and try again", Toast.LENGTH_LONG).show();

                        i.putExtra("longitude",longitude);
                            i.putExtra("latitude",latitude);
                            startActivity(i);


                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to fetch the location.Please move to some other near place and try again", Toast.LENGTH_LONG).show();

                    }


                } else {

                    locationTrack.showSettingsAlert();
                }

            }
        });

    }

    private void initalizedb() throws IOException {



        SQLiteDatabase db = this.openOrCreateDatabase("LocationDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Location (name varchar, lat varchar, lon varchar);");
        long count = DatabaseUtils.queryNumEntries(db, "Location");
        if (count == 0) {

            InputStream inputStream = getResources().openRawResource(R.raw.location);
            InputStreamReader isr = new InputStreamReader(inputStream);

            BufferedReader buffer = new BufferedReader(isr);

            String line = "";

            db.beginTransaction();

            buffer.readLine();

            while ((line = buffer.readLine()) != null) {

                String[] str = line.split(",", 3);  // defining 3 columns with null or blank field //values acceptance
                String name = str[0].toString();

                String lat =str[1].toString();
                String longe=str[2].toString();
                String sql = "INSERT or replace INTO Location (name,lat,lon) VALUES("+name+" , "+lat+","+longe+")" ;
                db.execSQL(sql);



            }
            count = DatabaseUtils.queryNumEntries(db, "Location");
            db.setTransactionSuccessful();
            db.endTransaction();


        }

        {

           Cursor c= db.rawQuery("Select name from Location",null);

            while (c.moveToNext())
            {
                localities.add(c.getString(0));
            }
            c.close();
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {

        if(locationtext.getText().toString().isEmpty()||locationtext.getText().toString()==null||locationtext.getText().toString().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Please enter the location",Toast.LENGTH_LONG).show();
        }
        else{




            Intent i=new Intent(MainActivity.this,checkinout.class);

            SQLiteDatabase db = this.openOrCreateDatabase("LocationDB", MODE_PRIVATE, null);
           Cursor c= db.rawQuery("Select lat,lon from location where name='"+locationtext.getText().toString()+"'",null);

           if(c.moveToNext())
            {   double longitude=Double.parseDouble(c.getString(1));
                double latitude=Double.parseDouble(c.getString(0));
                i.putExtra("longitude",longitude);
                i.putExtra("latitude",latitude);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Invalid Location, Please select the location",Toast.LENGTH_LONG).show();
            }

        }
    }
}

