package com.example.androidassignments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME,"In onCreate()");

        //switch listener
        final Switch listSwitch = (Switch) findViewById(R.id.listSwitch);
        listSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             switchToast(isChecked);
            }
        });

        //switch listener
        final CheckBox listCheckBox = (CheckBox) findViewById(R.id.listCB);
        listCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkDialog();
            }
        });
    }

    public void checkDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml

                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent resultIntent = new Intent(  );
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    public void switchToast(boolean isChecked){
        //displays toast whenever switch is changed
        // true if the switch is in the On position
        CharSequence text = getResources().getString(R.string.switchOff); //"Switch is Off";
        int duration =  Toast.LENGTH_LONG;
        if(isChecked){
            //change text and duration if switch is on
            text = getResources().getString(R.string.switchOn);;// "Switch is On"
            duration = Toast.LENGTH_SHORT;
        }
        Toast toast = Toast.makeText(this , text, duration);
        toast.show(); //display your message box

    }
    public void imageClick(View view){
            //request camera

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
                Log.e(ACTIVITY_NAME,"Error on image button start camera");
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //get image button and set image
            final ImageButton imageButton =(ImageButton) findViewById(R.id.imgBt);
            imageButton.setImageBitmap(imageBitmap);
        }
    }
    public void  onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");

    }

    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");

    }

    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");

    }

    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}