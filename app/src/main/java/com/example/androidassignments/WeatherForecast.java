package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.androidassignments.HttpUtils.getImage;

public class WeatherForecast extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    ProgressBar progressBar;
    TextView currentTempView;
    TextView maxTempView;
    TextView minTempView;
    ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //set progress bar to visible
        progressBar = (ProgressBar) findViewById(R.id.weatherProgress);
        currentTempView = (TextView) findViewById(R.id.curTempText);
        minTempView = (TextView) findViewById(R.id.minTempText);
        maxTempView = (TextView) findViewById(R.id.maxTempText);
        weatherImage = (ImageView) findViewById(R.id.weatherImage);



        //setup spinner for city selection
        Spinner citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.cities,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //get city name and pass to ForecastQuery
        String city = parent.getItemAtPosition(position).toString();
        Log.i(ACTIVITY_NAME, "onItemSelected: passing following city to ForecastQuery: "+city);
        progressBar.setVisibility(View.VISIBLE);
        new ForecastQuery(city).execute("this will go to background");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        private String curTemp;
        private String minTemp;
        private String maxTemp;
        //private String weatherIcon;
        private Bitmap picture;
        protected String city;

        ForecastQuery(String city){this.city=city;}

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            Log.d(ACTIVITY_NAME, "doInBackground: Running");
            try {

                url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+this.city+",ca&APPID=a219cfe65f3fb7e79ce2723442479156&mode=xml&units=metric");

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    try {
                        conn.setRequestMethod("GET");

                        conn.setDoInput(true);
                        // Starts the query
                        try {
                            conn.connect();

                            //get input stream
                            InputStream in = conn.getInputStream();
                            XmlPullParser parser = Xml.newPullParser();
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            parser.setInput(in, null);



                            //parse while not at the end of the xml
                            while (parser.next()!= XmlPullParser.END_DOCUMENT){


                                if (parser.getEventType() == XmlPullParser.START_TAG) {
                                    String name = parser.getName();
                                    //Log.d(ACTIVITY_NAME, "doInBackground: In parser loop tag is " + name);

                                    if (name.equals("temperature")) {
                                        //get temperature values
                                        curTemp = parser.getAttributeValue(null, "value");
                                        publishProgress(25);
                                        maxTemp = parser.getAttributeValue(null, "max");
                                        publishProgress(50);
                                        minTemp = parser.getAttributeValue(null, "min");
                                        publishProgress(75);
                                        Log.i(ACTIVITY_NAME, "doInBackground: current temp " + curTemp);
                                        Log.i(ACTIVITY_NAME, "doInBackground: min temp " + minTemp);
                                        Log.i(ACTIVITY_NAME, "doInBackground: max temp " + maxTemp);
                                    } else if (name.equals("weather")) {
                                        //get icon name
                                        String weatherIcon = parser.getAttributeValue(null, "icon");
                                        String fileName=weatherIcon+".png";

                                        Log.i(ACTIVITY_NAME, "doInBackground: Icon Id " + weatherIcon);

                                        Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);
                                        //only save file if not already downloaded and saved
                                        if (fileExistance(fileName)) {
                                            FileInputStream fis = null;
                                            try {
                                                fis = openFileInput(fileName);

                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            Log.i(ACTIVITY_NAME, "Found the file locally");
                                            picture = BitmapFactory.decodeStream(fis);
                                        } else {
                                            String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                            picture = getImage(new URL(iconUrl));

                                            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                            picture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                            Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                            outputStream.flush();
                                            outputStream.close();
                                        }

                                        publishProgress(100);

                                    }
                                }
                            }
                            Log.d(ACTIVITY_NAME, "doInBackground: After parser loop");




                        } catch (IOException e) {
                            Log.d(ACTIVITY_NAME, "doInBackground: e.printStackTrace();");
                        } catch (XmlPullParserException e) {
                            Log.d(ACTIVITY_NAME, "doInBackground: e.printStackTrace();");

                        }


                    } catch (ProtocolException e) {
                        Log.d(ACTIVITY_NAME, "doInBackground: e.printStackTrace();");

                    }

                } catch (IOException e) {
                    Log.d(ACTIVITY_NAME, "doInBackground: e.printStackTrace();");

                }


            } catch (MalformedURLException e) {
                Log.d(ACTIVITY_NAME, "doInBackground: e.printStackTrace();");

            }
            return " do background ended";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //set progressbar and show it
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            //set layout values
            weatherImage.setImageBitmap(picture);
            currentTempView.setText(getString(R.string.curTemp)+curTemp+"C\u00b0");
            maxTempView.setText(getString(R.string.maxTemp)+maxTemp+"C\u00b0");
            minTempView.setText(getString(R.string.minTemp)+minTemp+"C\u00b0");

            progressBar.setVisibility(View.INVISIBLE);

        }
    }
}