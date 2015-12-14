package com.example.davchen.skibuddies;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davchen.skibuddies.Model.Event;
import com.example.davchen.skibuddies.Model.FriendShip;
import com.example.davchen.skibuddies.Model.Invitation;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1, textView2, textView3, textView4;
    private EditText editText, editText1;
    private Event event;
    private Button submit;
    private String title, description;
    private Invitation invitation;
    private List<ParseUser> friendsList;
    private String ObjectId;

    private static final String TAG = CreateEvent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        event = new Event();
        friendsList = new ArrayList<ParseUser>();

        editText = (EditText)findViewById(R.id.CreateEventText);
        editText1 = (EditText)findViewById(R.id.descriptionsdetails);

        submit = (Button)findViewById(R.id.postbutton);
        submit.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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
    public void onClick(View v) {


        title = editText.getText().toString();
        description = editText1.getText().toString();

        if((title !=null && !title.isEmpty()) && (description!=null && !description.isEmpty())) {

            event.setAuthor(ParseUser.getCurrentUser());
            event.setEventTitle(title);
            event.setDescription(description);
            event.setStatus("1");

            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //  event.setEventId(event.getObjectId());
                        ObjectId = event.getObjectId();
                        Log.d(TAG, "Successfully created Event");
                        Log.d(TAG, ObjectId);
                        queryFriends(ObjectId);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to create Event", Toast.LENGTH_LONG).show();
                    }
                }
            });



        }
        if(title==null || title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();
        }
        if(description==null || description.isEmpty()) {
            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_LONG).show();
        }
        setResult(Activity.RESULT_OK);
       finish();

    }

    private void queryFriends(final String objectId) {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("_User");
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "IN FRIENDS");
                    friendsList.clear();
                    for (ParseObject parseObject : objects) {
                        friendsList.add((ParseUser) parseObject);
                    }
                    sendInvitations(objectId);

                } else {

                }
            }

        });


    }

    private void sendInvitations(String ObjectId) {

        for(ParseUser friends: friendsList) {

            invitation = new Invitation();
            invitation.setEventId(ParseObject.createWithoutData("Event", ObjectId));
            invitation.setUserId(friends);
            invitation.setFlag("0");

            invitation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Successfull in inviting");
                    } else {
                        Log.e(TAG, "Unsuccesful inviting friends");
                    }
                }
            });
        }
    }
}
