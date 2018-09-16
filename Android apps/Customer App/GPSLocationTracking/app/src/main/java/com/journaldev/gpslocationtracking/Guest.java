package com.journaldev.gpslocationtracking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Guest extends Activity implements View.OnClickListener {
    private boolean bo1=false;
    private boolean bo2=false;
    private boolean bo3=false;
    Button b1,b2,b3;
    FloatingActionButton guestfab;
    String guestno=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.guestsno);
        b1=(Button)findViewById(R.id.gb1);
        b2=(Button)findViewById(R.id.gb2);
        b3=(Button)findViewById(R.id.gb3);
        guestfab=(FloatingActionButton)findViewById(R.id.guestfab);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        guestfab.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        if(view!=guestfab)
        {
            View button=view;
            if(view==b1)
            {
                bo1=!bo1;
                bo2=false;
                bo3=false;
            }
            else if(view==b2)
            {

                bo2=!bo2;
                bo1=false;
                bo3=false;

            }

            else if(view==b3)
            {
                bo3=!bo3;
                bo2=false;
                bo1=false;

            }
             updaterView(bo1,b1);
            updaterView(bo2,b2);
            updaterView(bo3,b3);



        }
        else if(view==guestfab)
        {

            if(!(bo1||bo2||bo3))
            {
                Toast.makeText(getApplicationContext(),"Please select the number of guests",Toast.LENGTH_LONG).show();}

           else {
                Intent i = new Intent(getApplicationContext(), SmsActivity.class);
                i.putExtra("guestno", guestno);
                Double longitude=getIntent().getExtras().getDouble("longitude");
                Double latitude=getIntent().getExtras().getDouble("latitude");
                String datein=getIntent().getExtras().getString("checkin");
                String dateout=getIntent().getExtras().getString("checkout");

                SmsManager s= SmsManager.getDefault();
                // Toast.makeText(getApplicationContext(),indate.toString()+" "+outdate.toString(),Toast.LENGTH_LONG).show();
                s.sendTextMessage(Variables.serverno,null,"OYOH " + Double.toString(longitude) + " " + Double.toString(latitude)+" "+datein+" "+dateout,null,null);


                startActivity(i);
            }
        }

    }

    public void updaterView(boolean bool,Button bg) {

        if(bool)
        {  guestno=bg.getText().toString();
            bg.setTextColor(Color.parseColor("#ef4123"));
            bg.setBackgroundResource(R.drawable.selectdraw);
        }
        else
        {
            bg.setTextColor(Color.parseColor("#ffffff"));
            bg.setBackgroundResource(R.drawable.draw);
        }

    }
}
