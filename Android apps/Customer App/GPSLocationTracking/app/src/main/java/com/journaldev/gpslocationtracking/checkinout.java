package com.journaldev.gpslocationtracking;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class checkinout extends Activity implements View.OnClickListener {


    String datein=null;
    Date indate;
    Date outdate;
    String dateout=null;
    EditText echckin;
    EditText echckout;
    FloatingActionButton button;

    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dates);

        echckin=(EditText)findViewById(R.id.etext1);
        echckout=(EditText)findViewById(R.id.etext2);
        button=(FloatingActionButton)findViewById(R.id.datesfab1);
        echckin.setOnClickListener(this);
        echckout.setOnClickListener(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(outdate==null||indate==null)
                    Toast.makeText(getApplicationContext(),"Checkout or Checkin Dates could not be empty",Toast.LENGTH_LONG).show();


               else if(indate.compareTo(Calendar.getInstance().getTime())<0)
                {Toast.makeText(getApplicationContext(),"Checkin date must be greater than Current date",Toast.LENGTH_LONG).show();


                }
                else if(outdate.compareTo(indate)<=0){
                    Toast.makeText(getApplicationContext(),"Checkout date must be greater than Checkin",Toast.LENGTH_LONG).show();
                }
                else{
                    //SmsManager s=SmsManager.getDefault();
                    // Toast.makeText(getApplicationContext(),indate.toString()+" "+outdate.toString(),Toast.LENGTH_LONG).show();
                    //s.sendTextMessage(Variables.serverno,null,"OYOH " + Double.toString(longitude) + " " + Double.toString(latitude)+" "+datein+" "+dateout,null,null);
                    Intent i= new Intent(getApplicationContext(),Guest.class);
                    i.putExtra("longitude",getIntent().getExtras().getDouble("longitude"));
                    i.putExtra("latitude",getIntent().getExtras().getDouble("latitude"));
                    i.putExtra("checkin",datein);
                    i.putExtra("checkout",dateout);
                    startActivity(i);}




            }
        });

    }




    @Override
    public void onClick(View view) {
        if(view==echckin)
        {
            getdate(1);
        }
        else if(view==echckout)
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
                            echckout.setText(s);
                            outdate=new Date(year,monthOfYear,dayOfMonth);}

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}



