package com.journaldev.gpslocationtracking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
        Bundle extras = getIntent().getExtras();
        String msg=null;
        if (extras != null) {
           msg = extras.getString("book_conf");}

           String[] temp=msg.split("#");

        bookid.setText("BOOKING ID : "+temp[1]);
        price.setText("₹ "+temp[3]);
        hotelname.setText(temp[2]);
        addr.setText(temp[4].trim());

    }
}
