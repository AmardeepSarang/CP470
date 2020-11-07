package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatList;
    EditText chatTextBox;
    Button sendBt;
    ArrayList <String> messages;
    ChatAdapter messageAdapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList= (ListView) findViewById(R.id.chatView);
        chatTextBox = (EditText) findViewById(R.id.editChatText);
        sendBt = (Button) findViewById(R.id.chatButton);
        messages =  new ArrayList<String>();

        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter =new ChatAdapter( this );
        chatList.setAdapter (messageAdapter);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();
        readDb(database);
    }

    private void readDb(SQLiteDatabase readableDatabase) {

        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM "+ChatDatabaseHelper.TABLE_NAME,null);
        //print db messages to log and add to message list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int columnIndex =cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
            int id=cursor.getInt(columnIndex);
            String mesg =cursor.getString(1);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "+mesg);
            messages.add(mesg);
            cursor.moveToNext();
        }

        //print column count
        int columnCount=cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Cursor’s  column count = " +columnCount);

        //print column names
        for (int i=0; i<columnCount; i++){
            Log.i(ACTIVITY_NAME,"Column name at index "+i+" is "+cursor.getColumnName(i));
        }

    }

    public void writeDb(SQLiteDatabase writeDb, String msg){
        //write masage to database
        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatDatabaseHelper.KEY_MESSAGE,msg);
        writeDb.insert(ChatDatabaseHelper.TABLE_NAME,"NullPlaceholder",contentValues);
    }
    public void sendClick(View view){
        //add typed messages from text box to list
        String msg = chatTextBox.getText().toString();
        messages.add(msg);
        messageAdapter.notifyDataSetChanged();

        //write message to database
        writeDb(database,msg);
        chatTextBox.setText("");//empty text  box
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount(){
            return messages.size();
        }

        public String getItem(int position){
            return messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;

        }


    }
}