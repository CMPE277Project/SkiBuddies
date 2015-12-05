package com.example.davchen.skibuddies;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
=======
import android.util.Log;
>>>>>>> master
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private String name;
    private ParseUser parseuser;

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity {
    private Button createEventBtn;
=======
>>>>>>> master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        createEventBtn = (Button)findViewById(R.id.create_event);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                startActivity(intent);
            }
        });
=======
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
>>>>>>> master
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
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    getUserDetailsFromFB();
                } else {
                    getUserDetailsFromParse();
                }
            }
        });
    }

    private void getUserDetailsFromFB() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                try{
                    name= graphResponse.getJSONObject().getString("name");
                    saveUserInformationToParse();
                }
                catch(JSONException e) {
                    e.getLocalizedMessage();
                }
            }
        });

    }

    private void getUserDetailsFromParse() {

        parseuser = ParseUser.getCurrentUser();

    }

    //stores information fetched from facebook to parse....
    private void saveUserInformationToParse() {
        parseuser = ParseUser.getCurrentUser();

        parseuser.setUsername(name);

        parseuser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }
}
