package com.macstudio.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText fromEdittext;
    Spinner fromSpinner, toSpinner;
    TextView resultTextview;
    Button convbtn;
    ImageView fromImage, toImage;
    String jsonrespstr;
    JSONObject countryjson;
    ArrayList countrylist, countrycode;
    Float fromval, result, fromcounval, tocounval;
    String fromcountry, tocountry;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fromEdittext = findViewById(R.id.fromEdittext);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        resultTextview = findViewById(R.id.resultTextview);
        convbtn = findViewById(R.id.convbtn);
        fromImage = findViewById(R.id.fromImage);
        toImage = findViewById(R.id.toImage);
        mainLayout = findViewById(R.id.mainLayout);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.exchangerate-api.com/v4/latest/USD")
                .header("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            jsonrespstr = response.body().string();
            JSONObject jsonresp = new JSONObject(jsonrespstr);
            countryjson = jsonresp.getJSONObject("rates");

        } catch (Exception e) {
            e.printStackTrace();
        }

        countrylist = new ArrayList<String>();
        countrycode = new ArrayList<String>();
        if (countryjson != null) {
            Iterator keysToCopyIterator = countryjson.keys();
            while (keysToCopyIterator.hasNext()) {
                String key = (String) keysToCopyIterator.next();
                countrycode.add(key.substring(0, 2));
                Locale loc = new Locale("", key.substring(0, 2));
                countrylist.add(loc.getDisplayCountry());
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please turn on internet! and restart application.", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
        }

        Log.d("info", "list " + countrylist);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countrylist);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(spinnerArrayAdapter);
        toSpinner.setAdapter(spinnerArrayAdapter);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fromImgcode = fromcountry = countrycode.get(countrylist.indexOf(fromSpinner.getSelectedItem().toString())).toString();
                fromImgcode = fromImgcode.substring(0, 2).toLowerCase();
                fromImage.setImageBitmap(getBitmapFromURL("https://www.countryflags.io/" + fromImgcode + "/flat/32.png"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String toImgcode = tocountry = countrycode.get(countrylist.indexOf(toSpinner.getSelectedItem().toString())).toString();
                toImgcode = toImgcode.substring(0, 2).toLowerCase();
                toImage.setImageBitmap(getBitmapFromURL("https://www.countryflags.io/" + toImgcode + "/flat/32.png"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        convbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(fromEdittext.getText().toString().equals(""))) {
                    fromval = Float.parseFloat(fromEdittext.getText().toString());
                    fromcountry = fromSpinner.getSelectedItem().toString();
                    tocountry = toSpinner.getSelectedItem().toString();
                    try {
                        fromcounval = Float.parseFloat(countryjson.getString(fromcountry));
                        tocounval = Float.parseFloat(countryjson.getString(tocountry));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = fromval * (tocounval / fromcounval);
                    resultTextview.setText("Converted value: " + result);
                }
                else{
                    resultTextview.setText("Please enter some value to convert!");
                }
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
