package com.example.ninja.app2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ServiceCommunicator extends Service
{
    private SMSreceiver mSMSreceiver;
    private final String TAG = this.getClass().getSimpleName();

    private IntentFilter mIntentFilter;
    private class SMSreceiver extends BroadcastReceiver
    {
        private final String TAG = this.getClass().getSimpleName();

        @Override
        public void onReceive( final Context context, Intent intent)
        {

            Bundle extras = intent.getExtras();

            String strMessage = "";

            if ( extras != null )
            {
                Object[] smsextras = (Object[]) extras.get( "pdus" );

                for ( int i = 0; i < smsextras.length; i++ )
                {
                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                    String strMsgBody = smsmsg.getMessageBody().toString();
                    final String strMsgSrc = smsmsg.getOriginatingAddress();
                    Log.d(TAG,strMsgBody);

                    strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;
                    Toast.makeText(context,""+strMessage,Toast.LENGTH_LONG).show();
                 final  String[] data=strMsgBody.split(" ");
                   // Toast.makeText(context,""+strMessage+data[0]+" "+data[1]+" "+data[2],Toast.LENGTH_LONG).show();
                    if(data[0].equals("OYOH")){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                //URL myUrl = new URL("http://172.17.41.173:8080/get?latlong=" + data[2] + "%20" + data[1] + "%20" + data[3] + "%20" + data[4]+"%20"+strMsgSrc);
                                URL myUrl = new URL("http://oyooff.us-east-2.elasticbeanstalk.com/get?latlong=" + data[2] + "%20" + data[1] + "%20" + data[3] + "%20" + data[4]+"%20"+strMsgSrc);

                                HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setRequestProperty("Content-Type", "application/json");
//                    conn.connect();

                              int statusCode;
    try{      statusCode = conn.getResponseCode();}
    catch (Exception e)
        {
            statusCode=600;
        }
                                if (statusCode != 200) {

                                    SmsManager s = SmsManager.getDefault();
                                    s.sendTextMessage(strMsgSrc, null, "Unable to Fetch Hotel Details Try again later" + "", null, null);


                                }
else{
                                InputStream inputStream = conn.getInputStream();
                                StringBuffer buffer = new StringBuffer();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                                String line;
                                while ((line = reader.readLine()) != null) {
                                    buffer.append(line + "\n");
                                }


                                String response = buffer.toString();
                                String[] hotel = response.split("!");
                                for (int i = 0; i < hotel.length - 1; i++) {

                                    SmsManager s = SmsManager.getDefault();
                                    s.sendTextMessage(strMsgSrc, null, hotel[i] + "", null, null);
                                }

                                //  Toast.makeText(context,""+response,Toast.LENGTH_LONG)
                                ;}

                            } catch (IOException e) {

                                e.printStackTrace();

                            }


                        }
                    });}



                    else if(data[0].equals("OYOID")){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                   // URL myUrl = new URL("http://172.17.41.173:8080/booking?code=" + data[1]+" "+strMsgSrc );
                                    URL myUrl = new URL("http://oyooff.us-east-2.elasticbeanstalk.com/booking?code=" + data[1]+" "+strMsgSrc );

                                    HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.connect();

                                    int statusCode = conn.getResponseCode();
                                    System.out.print("da"+statusCode);
                                    if (statusCode != 200) {

                                        SmsManager s = SmsManager.getDefault();
                                        s.sendTextMessage(strMsgSrc, null, "Sorry,Unable to Book! Try gain later" + "", null, null);


                                    }
                                    else{
                                        InputStream inputStream = conn.getInputStream();
                                        StringBuffer buffer = new StringBuffer();

                                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            buffer.append(line + "\n");
                                        }


                                        String response = buffer.toString();


//                                        Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                                        ;
                                            SmsManager s = SmsManager.getDefault();
                                            ArrayList<String> parts=s.divideMessage(response);
                                            s.sendMultipartTextMessage(strMsgSrc,null,parts,null,null);
                                           //z s.sendTextMessage(strMsgSrc, null, response + "", null, null);


                                        //  Toast.makeText(context,""+response,Toast.LENGTH_LONG)
                                        ;}

                                } catch (IOException e) {

                                    e.printStackTrace();

                                }


                            }
                        });}



















                }



                }

            }



    }
    @Override
    public void onCreate()
    {
        super.onCreate();

        //SMS event receiver
        mSMSreceiver = new SMSreceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
