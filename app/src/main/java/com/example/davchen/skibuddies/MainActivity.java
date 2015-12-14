package com.example.davchen.skibuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private String name;
    private String userId;
    private String date;
    private ParseUser parseuser;
    private Button createEventBtn;

    private Button getEventBtn;
        //fortest
    private Button testButton;
    private Intent intent;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private static final String TAG = MainActivity.class.getSimpleName();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            button = (Button) findViewById(R.id.button);
            button.setOnClickListener(this);


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onClick(View v) {
            List<String> mPermission = Arrays.asList("public_profile");

            ParseFacebookUtils.logInWithReadPermissionsInBackground(MainActivity.this, mPermission, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user == null) {
                        Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                    } else if (user.isNew()) {
                        Log.d(TAG, "User signed up and logged in through Facebook!");
                        getUserDetailsFromFB();
                    } else {
                        getUserDetailsFromParse();
                    }
                }
            });
        }

        private void getUserDetailsFromFB() {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),"/me", null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    try {
                        name = graphResponse.getJSONObject().getString("name");
                        userId = graphResponse.getJSONObject().getString("id");
                        Log.d("Name: ", name);

                        saveUserInformationToParse();
                    } catch (JSONException e) {
                        e.getLocalizedMessage();
                    }
                }
            }).executeAsync();

        }

        private void getUserDetailsFromParse() {

            parseuser = ParseUser.getCurrentUser();
            name = parseuser.getUsername();
            userId = parseuser.getString("UserId");
            Date date1 = parseuser.getCreatedAt();
            date = simpleDateFormat.format(date1);

            Log.d("Name: ", name);
            Toast.makeText(getApplicationContext(), "Hello "+name, Toast.LENGTH_LONG).show();
            try {
                gotoNextActivity();
            }
            catch (Exception ex){
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }

        //stores information fetched from facebook to parse....
        private void saveUserInformationToParse() {
            parseuser = ParseUser.getCurrentUser();

            parseuser.setUsername(name);
            parseuser.put("UserId", userId);

            parseuser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    Toast.makeText(getApplicationContext(), name + "Successfully signed up", Toast.LENGTH_LONG).show();

                    try{
                        gotoNextActivity();
                    }
                    catch(Exception ex) {
                        Log.e(TAG, ex.getLocalizedMessage());
                    }
                }
            });
        }

        private void gotoNextActivity() {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("Id", userId);
            intent.putExtra("Date", date);
            startActivity(intent);
        }
    }

