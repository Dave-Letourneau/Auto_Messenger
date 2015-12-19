package com.example.david.automessanger;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    TextView msgDisplay;
    final Context context = this;
    // private SharedPreferences data;
    String msg = "userMsg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        SharedPreferences ck = getSharedPreferences("check", Context.MODE_PRIVATE);

        boolean checked = ck.getBoolean("check", false);
        checkBox.setChecked(checked);

        // need references
        msgDisplay = (TextView) findViewById(R.id.msgDisplay);
        SharedPreferences data = getSharedPreferences(msg, Context.MODE_PRIVATE);

        if (data == null || data.getString(msg, "").equals("")) {
            msgDisplay.setText("This is a default message. To change this message, click the button below. Must not exceed 160 characters.");
        } else {
            msgDisplay.setText(data.getString(msg, ""));
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // button to change users data.
    public void changeMsgClick(View view) {
        // get prompts
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompt to be the layout when button is clicked by setting it to the view
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
        // set up the dialogue window for the user
        // get reference to the count text field for use later.
        final TextView count = (TextView) promptView.findViewById(R.id.countView);

        // create a textwatcher in order to count the number of typed characters
        final TextWatcher watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // count.setVisibility(View.VISIBLE);
            }

            public void afterTextChanged(Editable s) {
                // everytime the user enters a character, the counter updates the string count in the prompt XML
                count.setText("Character Count: " + input.getText().toString().length());

            }
        };
        // add the textWatcher object to the user's input dialogue box.
        input.addTextChangedListener(watcher);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                // get user's message and set it to the display
                // check if the message is greater than 160 characters.
                // if it is, apply the negative and grant a toast saying use less
                if (input.getText().length() > 160) {
                    d.cancel();
                    Toast.makeText(context, "Message exceeds 160 characters.", Toast.LENGTH_LONG).show();
                } else {
                    msgDisplay.setText(input.getText());

                    SharedPreferences data = getSharedPreferences("userMsg", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = data.edit();
                    // put the current msg string into preferences.
                    edit.putString("userMsg", msgDisplay.getText().toString());
                    edit.apply();

                    Toast.makeText(context, "Message saved.", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                d.cancel();
            }
        });

        // create alert dialog - not certain if second is needed
        AlertDialog alrt = alertDialogBuilder.create();
        alrt.show();

    }

    public void onSessionClear(View view) {
        // when the session clear button is clicked, the user manually clears the current
        // preference list.
        SharedPreferences adrList = getSharedPreferences("list", Context.MODE_PRIVATE);

        adrList.edit().clear().commit();
        String t = "Session cleared";
        Toast.makeText(context, t, Toast.LENGTH_LONG).show();
    }



    // When the check is clicked, a boolean isClicked is saved in true.
    // when not clicked, isClicked is saved in preferences as false.
    // in SmsReceiver, let's have a check. The app should only fire if it's checked true.
    public void itemClicked(View view) {
        //code to check if this checkbox is checked!

        SharedPreferences ck = getSharedPreferences("check", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = ck.edit();
        //CheckBox checkBox = (CheckBox)view;
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if(checkBox.isChecked()){
            edit.putBoolean("check", true).commit();
            Toast.makeText(context, "Messenger on", Toast.LENGTH_LONG).show();
        } else {
            edit.putBoolean("check", false).commit();
            Toast.makeText(context, "Messenger off", Toast.LENGTH_LONG).show();
        }
    }
}
