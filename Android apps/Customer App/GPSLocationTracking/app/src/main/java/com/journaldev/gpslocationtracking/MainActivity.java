package com.journaldev.gpslocationtracking;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
Button checkin;
Button checkout;
String datein=null;
Date indate;
Date outdate;
String dateout=null;
EditText echckin;
EditText echckout;

    String s;
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        echckin=(EditText)findViewById(R.id.echeckin);
        echckout=(EditText)findViewById(R.id.echeckout);
        checkin=(Button)findViewById(R.id.checkin);
        checkout=(Button)findViewById(R.id.checkout);
        checkin.setOnClickListener(this);
        checkout.setOnClickListener(this);
        echckin.setOnClickListener(this);
        echckout.setOnClickListener(this);
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


        Button btn = (Button) findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTrack(MainActivity.this);


                if(outdate==null||indate==null)
                    Toast.makeText(getApplicationContext(),"Checkout or Checkin Dates could not be empty",Toast.LENGTH_LONG).show();


             else  if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

                    if(longitude!=0.00&&latitude!=0.000) {
                        if(indate.compareTo(Calendar.getInstance().getTime())<0)
                        {Toast.makeText(getApplicationContext(),"Checkin date must be greater than Current date",Toast.LENGTH_LONG).show();


                        }
                       else if(outdate.compareTo(indate)<=0){
                            Toast.makeText(getApplicationContext(),"Checkout date must be greater than Checkin",Toast.LENGTH_LONG).show();
                        }
                            else{
                        SmsManager s=SmsManager.getDefault();
                       // Toast.makeText(getApplicationContext(),indate.toString()+" "+outdate.toString(),Toast.LENGTH_LONG).show();
                        s.sendTextMessage(Variables.serverno,null,"OYOH " + Double.toString(longitude) + " " + Double.toString(latitude)+" "+datein+" "+dateout,null,null);
                        Intent i= new Intent(getApplicationContext(),SmsActivity.class);
                        locationTrack.stopListener();

                         startActivity(i);}

                    }
                    else
                    {Toast.makeText(getApplicationContext(),"Unable to fetch the location.Please move to some other near place and try again",Toast.LENGTH_LONG).show();

                    }


                } else {

                    locationTrack.showSettingsAlert();
                }

            }
        });

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
        if(view==checkin||view==echckin)
        {
            getdate(1);
           }
        else if(view==checkout||view==echckout)
        {
            getdate(2);
           }
    }


    void getdate(final int  type)
    { final Calendar c = Calendar.getInstance();
      int  mYear = c.get(Calendar.YEAR);
      int mMonth = c.get(Calendar.MONTH);
      int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                      s=  ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

                      //s=new StringBuilder().append(monthOfYear).append("/").append(dayOfMonth).append("/").append(year);

                     if(type==1)
                     {
                         echckin.setText(s);
                     datein=s;
                     indate=new Date(year,monthOfYear,dayOfMonth);
                     }
                     else
                     { dateout=s;
                     echckout.setText(s); outdate=new Date(year,monthOfYear,dayOfMonth);}

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}

