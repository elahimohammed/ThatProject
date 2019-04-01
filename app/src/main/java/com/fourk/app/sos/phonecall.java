package com.fourk.app.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class phonecall extends BroadcastReceiver {
    public phonecall() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try{
            if(bundle != null){
                final Object[] pdusObj =(Object[]) bundle.get("pdus");
                for(int i=0; i< pdusObj.length; i++){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0000000000"));
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = "tel";
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Toast.makeText(context, senderNum + "/" + message, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e){

        }
    }
}
