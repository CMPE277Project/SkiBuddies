package com.example.davchen.skibuddies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.davchen.skibuddies.Model.Session;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserBioDetails extends AppCompatActivity {

    private String name, userId, date;
    private ProfilePictureView profilePictureView;
    private TextView textView, textView1;
    private List<Session> sessionList;
    private Session session;
    private ListView recordslist;

    private static final String TAG = UserBioDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bio_details);

        profilePictureView = (ProfilePictureView)findViewById(R.id.bioprofilePic);
        textView = (TextView)findViewById(R.id.profilenn);
        textView1 = (TextView)findViewById(R.id.datebio);

        getFromIntent(); // gets infro from intent
        recordslist = (ListView)findViewById(R.id.listrecords);
        sessionList = new ArrayList<Session>();

        queryRecordsFromSession();
    }

    private void queryRecordsFromSession() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Session");
        parseQuery.whereEqualTo("UserId", ParseUser.getCurrentUser());
        //parseQuery.setCachePolicy()
        parseQuery.include("EventId");
        parseQuery.include("Distance");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    sessionList.clear();
                    for (ParseObject parseObject : objects) {
                        sessionList.add((Session) parseObject);
                    }
                    updateRecordsTable();
                } else {
                    Log.e(TAG, "Error: Could not retrieve record details " + e.getLocalizedMessage());
                }
            }
        });
    }

    private void updateRecordsTable() {

        ArrayAdapter<Session> arrayAdapter = new ArrayAdapter<Session>(this, R.layout.recordslist, sessionList) {


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.recordslist, parent, false);
                }
                TextView view = (TextView)convertView.findViewById(R.id.username);
                TextView view2 = (TextView)convertView.findViewById(R.id.othersRecord);

                session = sessionList.get(position);
               // view.setText(session.getEventId().getObjectId());
                view2.setText(session.getString("Distance"));

                return convertView;
            }
        };
        recordslist.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_bio_details, menu);
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

    private void getFromIntent(){
        Intent intent = getIntent();
        name = intent.getStringExtra("UserName");
        userId = intent.getStringExtra("Id");
        date = intent.getStringExtra("Date");

        profilePictureView.setProfileId(userId);
        textView.setText(name);
        textView1.setText("Member since: "+date);
    }


}
