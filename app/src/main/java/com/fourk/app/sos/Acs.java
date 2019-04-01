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

import com.fourk.app.sos.util.SystemUiHider;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Acs extends Activity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK = true;
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_acs);
        final EditText _phone = (EditText) findViewById(R.id.textPhone);
        final EditText _name = (EditText) findViewById(R.id.textName);
        final EditText _email = (EditText) findViewById(R.id.textEmail);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final ContactRepo repo = new ContactRepo(getApplicationContext());
                    Contact contact = new Contact();
                    contact.contact_phone= _phone.getText().toString();
                    contact.contact_name= _name.getText().toString();
                    Email em = new Email();
                    em.email_name = _name.getText().toString();
                    em.email_id = _email.getText().toString();
                try{
                    String notify ="-";
                    if(_name.getText().toString().trim().length() == 0){
                        notify = notify + "No Name";
                    } else{
                        if(_email.getText().toString().trim().length() == 0){
                            notify = notify + "No Email";
                        }
                        else{
                            repo.insertEmail(em);
                        }
                        if(_phone.getText().toString().trim().length() == 0){
                            notify = notify + "No Phone";
                        } else {
                            repo.insert(contact);
                        }
                        Toast.makeText(getApplicationContext(), notify, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex){
                    try {
                        repo.update(contact);
                        Toast.makeText(getApplicationContext(), "Contact updated", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception ex1){
                        Toast.makeText(getApplicationContext(), ex1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }}
                catch (Exception x){
                    Toast.makeText(getApplicationContext(), x.getMessage(),Toast.LENGTH_LONG).show();
                }
                _phone.setText("");
                _name.setText("");
                _email.setText("");
                RefreshView();
            }
        });

        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ContactRepo repo = new ContactRepo(getApplicationContext());
        ArrayList<HashMap<String, String>> contactList =  repo.getContactList();
        for (int a =0; a<contactList.size();a++)
        {
            HashMap<String, String> tmpData = (HashMap<String, String>) contactList.get(a);
            Set<String> key = tmpData.keySet();
            Iterator it = key.iterator();
            String _nameText ="";
            String phone="";
            while (it.hasNext()) {
                String hmKey = (String)it.next();
                String hmData = tmpData.get(hmKey);
                if(hmKey.equals("name")){
                    _nameText = hmData;
                }
                if(hmKey.equals("phone")){
                    phone = hmData;
                    if(phone.length()==10){
                        phone = "0" + phone;
                    }
                }
                it.remove();
            }
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView name = new TextView(this);
            name.setText(_nameText);
            name.setWidth(200);
            ll.addView(name);
            TextView no = new TextView(this);
            no.setText(phone);
            no.setWidth(200);
            ll.addView(no);
            final Button btn = new Button(this);
            btn.setTag(_nameText);
            btn.setText("");
            btn.setWidth(24);
            btn.setHeight(24);
            //btn.setBackgroundResource(R.drawable.cancel24);
            btn.setLayoutParams(params);
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactRepo repo = new ContactRepo(getApplicationContext());
                    repo.delete(btn.getTag().toString());
                    RefreshView();
                }
            });

            //Add button to LinearLayout
            ll.addView(btn);
            //Add button to LinearLayout defined in XML
            lm.addView(ll);
        }
    }

    public void RefreshView(){
        try{
        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearList);
        lm.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ContactRepo repo = new ContactRepo(getApplicationContext());
        ArrayList<HashMap<String, String>> contactList =  repo.getContactList();
        for (int a =0; a<contactList.size();a++)
        {
            HashMap<String, String> tmpData = (HashMap<String, String>) contactList.get(a);
            Set<String> key = tmpData.keySet();
            Iterator it = key.iterator();
            String _nameText ="";
            String phone="";
            while (it.hasNext()) {
                String hmKey = (String)it.next();
                String hmData = tmpData.get(hmKey);
                if(hmKey.equals("name")){
                    _nameText = hmData;
                }
                if(hmKey.equals("phone")){
                    phone = hmData;
                    if(phone.length()==10){
                        phone = "0" + phone;
                    }
                }
                it.remove();
            }
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView name = new TextView(this);
            name.setText(_nameText);
            name.setWidth(200);
            ll.addView(name);
            TextView no = new TextView(this);
            no.setText(phone);
            no.setWidth(200);
            ll.addView(no);
            final Button btn = new Button(this);
            btn.setTag(_nameText);
            btn.setText("");
            btn.setWidth(24);
            btn.setHeight(24);
            //btn.setBackgroundResource(R.drawable.cancel24);
            btn.setLayoutParams(params);
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ContactRepo repo = new ContactRepo(getApplicationContext());
                    repo.delete(btn.getTag().toString());
                    RefreshView();
                }
            });

            //Add button to LinearLayout
            ll.addView(btn);
            //Add button to LinearLayout defined in XML
            lm.addView(ll);
        }
        } catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
