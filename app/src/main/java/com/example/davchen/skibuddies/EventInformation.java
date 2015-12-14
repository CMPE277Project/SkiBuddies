package com.example.davchen.skibuddies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.davchen.skibuddies.Model.Invitation;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

<<<<<<< HEAD
import java.text.SimpleDateFormat;
=======
>>>>>>> ae39b75f7f0e90447ca47ec9c24ecad5e924835e
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class EventInformation extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EventInformation.class.getSimpleName();

    private ParseObject object;
    private ProfilePictureView profilePictureView;
    private TextView textView1, textView2, textView3;
    private Button button;
    private ListView listView;
    private SessionActivity sessionActivity;
    private String name, userId, title, description;
    private List<Invitation> invitationList;
    private Invitation invitation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);

        button = (Button)findViewById(R.id.startSessionId);

        listView = (ListView)findViewById(R.id.listViewFriends);

        textView1 = (TextView)findViewById(R.id.IdinfoTextName);
        textView2 = (TextView)findViewById(R.id.hello);
        textView3 = (TextView)findViewById(R.id.descriptionText);
        profilePictureView = (ProfilePictureView)findViewById(R.id.Idinfo);

        invitationList = new ArrayList<Invitation>();
        invitation = new Invitation();

        sessionActivity = new SessionActivity();

        queryParseFromInvitation();

        updateProfile();

        button.setOnClickListener(this);


    }

    private void queryParseFromInvitation() {

        ParseQuery<ParseObject> parsequery = ParseQuery.getQuery("Invitation");
        parsequery.whereNotEqualTo("Participants", ParseUser.getCurrentUser());
        parsequery.whereEqualTo("Status", "1");
        parsequery.include("EventId");
        parsequery.include("Participants");
        parsequery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {
                    invitationList.clear();
                    for(ParseObject obj: objects) {
                        invitationList.add((Invitation)obj);
                    }
                    updateAttendeeList();
                }
                else{
                    Log.e(TAG, "Error: cannot retrieve participants from the list");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_information, menu);
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

    public void updateProfile() {
        Intent intent = getIntent();
        name = intent.getStringExtra("Username");
        userId = intent.getStringExtra("Id");
        title = intent.getStringExtra("Event_Title");
        description = intent.getStringExtra("Description");
        profilePictureView.setProfileId(userId);
        textView1.setText(name);
        textView2.setText(title);
        textView3.setText(description);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SessionActivity.class);
<<<<<<< HEAD
        intent.putExtra("EventId", title);
        intent.putExtra("Id", userId);
=======
        intent.putExtra("", "");
>>>>>>> ae39b75f7f0e90447ca47ec9c24ecad5e924835e

        //save eventId..
        //start session...
        startActivity(intent);
    }

    private void updateAttendeeList() {
        ArrayAdapter<Invitation> arrayAdapter = new ArrayAdapter<Invitation>(this, R.layout.list_attendees, invitationList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //return super.getView(position, convertView, parent);
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_attendees, parent, false);
                }
                ProfilePictureView profilePictureView = (ProfilePictureView)convertView.findViewById(R.id.bioName);
                TextView textView = (TextView)convertView.findViewById(R.id.bioTextName);

                ParseUser user = invitationList.get(position).getUserId();
              //  String bb = invitation.getUserId()
               // String name="";

//                if(invitation.getUserId().getUsername()!=null){
//                    name = invitation.getUserId().getUsername();
//                }
               // Log.d(TAG, invitationList.get(0).getUserId().getUsername());
                profilePictureView.setProfileId(user.get("UserId").toString());
                textView.setText(user.getUsername());
                return convertView;
            }
        };
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventInformation.this, UserBioDetails.class);
                intent.putExtra("UserName", invitationList.get(position).getUserId().getUsername());
                intent.putExtra("Id", invitationList.get(position).getUserId().get("UserId").toString());
<<<<<<< HEAD
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                intent.putExtra("Date", dateFormat.format(invitationList.get(position).getUserId().getCreatedAt()));
=======
>>>>>>> ae39b75f7f0e90447ca47ec9c24ecad5e924835e
                startActivity(intent);
            }
        });
    }
}
