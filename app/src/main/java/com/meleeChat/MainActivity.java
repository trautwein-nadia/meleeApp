package com.meleeChat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static String username;
    //private static String password;
    private static String APIkey;
    private static String responseMessage;
    private static String input;
    private static int responseCode;
    private static HttpsURLConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean getLoginInfo() {
        EditText editText = (EditText) findViewById(R.id.username);
        username = editText.getText().toString();

        editText = (EditText) findViewById(R.id.key);
        APIkey = editText.getText().toString();

        if (username.equals("") || APIkey.equals("")) {
            return false;
        }
        return true;
    }

    public void onClick(View v) {
        if (getLoginInfo()) {
            //disable button
            new Feedback().execute("https://api.challonge.com/v1/tournaments.json");
        }
        else {
            //make a toast saying try again and fill out shit
        }
    }


    private class Feedback extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection;
            String userpass = username + ":" + APIkey;
            String result = "";
            // do above Server call here
            try
            {
                URL url = new URL("https://api.challonge.com/v1/tournaments.json?subdomain=smashing121");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.encode(userpass.getBytes(), Base64.NO_WRAP)));
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.i("Code is:", ""+urlConnection.getResponseCode());
                if (in != null)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    Log.i("Information is", ""+bufferedReader.readLine());

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                }

                in.close();
                urlConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }
    /*
    public void onClick(View v) {
        if (!getLocationPermission()) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "Please enable location or wait until location is more accurate", duration);
            toast.show();
            return;
        }

        Intent intent = new Intent(this, MenuActivity.class);
        EditText editText = (EditText) findViewById(R.id.nickname);
        String nickname = editText.getText().toString();
        if (!nickname.equals("")) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("nickname", nickname);
            editor.commit();
            Bundle b = new Bundle();
            b.putFloat("LAT", (float) lat);
            b.putFloat("LON", (float) lon);
            intent.putExtras(b);
            startActivity(intent);
        }
        else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a nickname", duration);
            toast.show();
        }
    }
    */
}