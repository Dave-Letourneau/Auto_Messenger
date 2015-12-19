package com.example.david.automessanger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by David on 6/24/2015.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_EXTRA_NAME = "pdus";
    // shared preferences and String containing msg info;
    // private SharedPreferences data;
    String msg = "userMsg";


    // final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // We need to access the sharedPreferences from the checkBox.
        // if checked, go ahead and do all of this. Else, we'll do nothing.
        SharedPreferences ck = context.getSharedPreferences("check", Context.MODE_PRIVATE);
        Boolean checked = ck.getBoolean("check", false);
        Boolean tester = false;
        if (checked) {

            Bundle extras = intent.getExtras();
            String messages = "";
            // String adrString = "";

            if (extras != null) {
                // Get received SMS array
                Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

                // Get ContentResolver object for pushing encrypted SMS to the incoming folder
                ContentResolver contentResolver = context.getContentResolver();

                for (int i = 0; i < smsExtra.length; ++i) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

                    String body = sms.getMessageBody().toString();
                    String address = sms.getOriginatingAddress();

                    messages += "SMS from " + address + " :\n";
                    messages += body + "\n";

                    /// Code using SMS information goes here
                    // Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();



                    SharedPreferences adrList = context.getSharedPreferences("list", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = adrList.edit();

                    if (adrList.getString("list", "").contains(address)) {

                    } else {

                        // adrList.getString("list", "");
                        // instead of a hardcoded string, let's
                        // getString off preferences += address that way we avoid clearing without
                        // clearing prefs.

                        e.putString("list", adrList.getString("list","") + " " + address);
                        e.apply();



                         SharedPreferences data = context.getSharedPreferences(msg, Context.MODE_PRIVATE);
                        String message = data.getString("userMsg", "");
                        if (message == null) {
                            message = "This is a default message. To change this message, click the button below. Must not exceed 160 characters.";
                        }
                        sendSms(address, message);
                    }

                }

            }

        } else {

        }



    }

    private void sendSms(String address, String message) {
        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(address, null, message, null, null);
    }

}
