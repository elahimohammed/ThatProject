
/*
 * Copyright (c) 2015 github.com/4-k
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.fourk.app.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

public class Home extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_home);
            findViewById(R.id.cnt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(), Acs.class);
                    startActivity(i);
                }
            });
            findViewById(R.id.googlePlus).setOnClickListener(new View.OnClickListener(){
                @Override
                public  void onClick(View v){
                    try {
                        Intent j = new Intent(Home.this, SignIn.class);
                        startActivity(j);
                    } catch (Exception err){
                        Toast.makeText(getApplicationContext(),err.getMessage(),Toast.LENGTH_LONG);
                    }
                }
            });
            findViewById(R.id.loc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            findViewById(R.id.scream).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                    // create class object
                    GPSTracker gps = new GPSTracker(Home.this);
                    // check if GPS enabled
                    if(gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        Geocoder geocoder;
                        geocoder = new Geocoder(Home.this, Locale.getDefault());
                        //http://maps.google.com/maps?q=lat,long
                        String url = "http://maps.google.com/maps?q="
                                + String.valueOf(latitude)+ "," + String.valueOf(longitude);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String address = "";
                            for (int i = 0; i < addresses.toArray().length; i++) {
                                address = address + addresses.get(i).getAddressLine(0) + "," +
                                        addresses.get(i).getAddressLine(1) + "," +
                                        addresses.get(i).getAddressLine(2);
                            }
                            SendTextMessage(address, url);
                            SendEmail(address, url);
                        } catch (Exception exe) {
                            Toast.makeText(Home.this, exe.getMessage(), Toast.LENGTH_SHORT).show();
                            SendTextMessage("Latitude:" + String.valueOf(latitude) + ", Longitude:" + String.valueOf(longitude), url);
                            SendEmail("Latitude:" + String.valueOf(latitude) + ", Longitude:" + String.valueOf(longitude), url);
                        }
                    }else{
                        gps.showSettingsAlert();
                    }
                    }
                    catch (Exception ex){
                        Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void SendEmail(String address, String uRL){
        try{
            ContactRepo repo = new ContactRepo(getApplicationContext());
            String _uid = repo.getCredentialE();
            final String pass = repo.getCredentialP();
            final String uid = _uid;
            List contacts = repo.getEmContact();
            Iterator iter = contacts.iterator();
            while (iter.hasNext()){
                String elem = (String) iter.next();
                String cid = elem;
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                return new javax.mail.PasswordAuthentication(uid,pass);
                            }
                        });
                try {
                    String _message = "Hi ! I am in danger. Please find me at " + address +" |" + uRL;
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(_uid));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cid));
                    message.setSubject("Please Help");
                    message.setText(_message);
                    Transport.send(message);

                } catch (javax.mail.MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (Exception err){
            Toast.makeText(Home.this, getResources().getString(R.string.gmailResponse) , Toast.LENGTH_LONG).show();
        }
    }
    private void SendTextMessage(String address, String uRL) {
        try{
        ContactRepo repo = new ContactRepo(getApplicationContext());
        ArrayList<HashMap<String, String>> contactList =  repo.getContactList();
            if(contactList.size()!=0){
                String list="";
                boolean err = false;
                String ErrorNo="Cannot Send to ";
                for (int a =0; a<contactList.size();a++)
                {
                    HashMap<String, String> tmpData = (HashMap<String, String>) contactList.get(a);
                    Set<String> key = tmpData.keySet();
                    Iterator it = key.iterator();
                    String name ="";
                    String phone="";
                    while (it.hasNext()) {
                        String hmKey = (String)it.next();
                        String hmData = tmpData.get(hmKey);
                        if(hmKey.equals("name")){
                            name = hmData;
                        }
                        if(hmKey.equals("phone")){
                            phone = hmData;
                            if(phone.length()==10){
                                phone = "0" + phone;
                            }
                        }
                        it.remove();
                    }
                    String message = "Hi "+ name + " ! I am in danger. Please find me at " + address +" |" + uRL;

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> msgStringArray = smsManager.divideMessage(message);
                        if((phone.startsWith("0")) && (phone.length()==11)){
                            try {
                                smsManager.sendMultipartTextMessage(phone, null, msgStringArray, null, null);
                            } catch (Exception e) {
                                smsManager.sendTextMessage(phone, null, message, null, null);
                            }
                        }
                        else if((phone.startsWith("+91"))&&(phone.length()==13)) {
                            try {
                                smsManager.sendMultipartTextMessage(phone, null, msgStringArray, null, null);
                            } catch (Exception e) {
                                smsManager.sendTextMessage(phone, null, message, null, null);
                            }
                        }
                        else{
                            err = true;
                            ErrorNo = ErrorNo + phone +",";
                        }
                        Toast.makeText(Home.this, "SMS sent to "+phone,
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(Home.this,
                                "SMS failed, please try again. " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    if(err){
                        Toast.makeText(Home.this,
                                ErrorNo,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            else{
                Toast.makeText(Home.this,
                        "SMS failed, No contact found ",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception ee){
            Toast.makeText(Home.this, ee.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
