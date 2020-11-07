package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME,"In onCreate()");
    }

    public void clickHandler(View view){
        Intent i = new Intent(this, ListItemsActivity.class);
        startActivityForResult(i, 10);
    }

    public void chatClickHandler(View view){
        Log.i(ACTIVITY_NAME,"User clicked Start Chat");

        Intent i = new Intent(this, ChatWindow.class);
        startActivityForResult(i, 10);

    }

    public void toolClickHandler(View view) {
        //toolbar test
        Intent i = new Intent(this, TestToolbar.class);
        startActivityForResult(i, 10);
        Log.i("main", "toolClickHandler: ");
    }
    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode==10 &&  responseCode== Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityResult");

            //Read extra data from response
            String messagePassed = data.getStringExtra("Response");

            //diplay on toast
            int duration = Toast.LENGTH_LONG;
            String displayText = "ListItemsActivity passed: "+messagePassed;
            Toast toast = Toast.makeText(this , displayText, duration);
            toast.show(); //display your message box
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
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }


}