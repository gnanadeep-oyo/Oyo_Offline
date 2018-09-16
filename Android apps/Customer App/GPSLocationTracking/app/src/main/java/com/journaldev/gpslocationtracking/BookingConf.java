package com.journaldev.gpslocationtracking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingConf extends Activity {
TextView bookid,price,hotelname,addr,dates,guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_conf);
     bookid=(TextView)findViewById(R.id.bookid);
     price=(TextView)findViewById(R.id.price);
     hotelname=(TextView)findViewById(R.id.hotel);
     addr=(TextView)findViewById(R.id.addr);
     dates=(TextView)findViewById(R.id.dates);
     guest=(TextView)findViewById(R.id.guest);
     String din=null;
     String dout=null;
     String gno=null;
        Bundle extras = getIntent().getExtras();
        String msg=null;
        if (extras != null) {
           msg = extras.getString("book_conf");
           din=extras.getString("checkout");
           dout=extras.getString("checkin");
           gno=extras.getString("guestno");


        }

           String[] temp=msg.split("#");
        String[] temp1=din.toString().split("/",3);
        Date d01=new Date(Integer.parseInt(temp1[2])-1900,Integer.parseInt(temp1[1])-1,Integer.parseInt(temp1[0])+0);

        Format formatter = new SimpleDateFormat("MMM");


        String[] temp2=dout.toString().split("/",3);
        Date d02=new Date(Integer.parseInt(temp2[2])-1900,Integer.parseInt(temp2[1])-1,Integer.parseInt(temp2[0])+0);

        String sdin = formatter.format(d01);
        String sdout=formatter.format(d02);

        bookid.setText("BOOKING ID : "+temp[1]);
        price.setText("₹ "+temp[3]);
        hotelname.setText(temp[2]);
        guest.setText(gno);
        dates.setText(temp1[0]+" "+sdin+" - "+temp2[0]+" "+sdout);
        addr.setText(temp[4].trim());

    }

}
