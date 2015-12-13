package com.example.davchen.skibuddies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davchen.skibuddies.Model.Event;
import com.example.davchen.skibuddies.Model.Invitation;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1, textView2, textView3, textView4;
    private EditText editText, editText1;
    private Event event;
    private Button submit;
    private String title, description;
    private Invitation invitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        event = new Event();
        invitation = new Invitation();

        editText = (EditText)findViewById(R.id.CreateEventText);
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
        description = editText.getText().toString();
        if((title !=null && !title.isEmpty()) && (description!=null && !description.isEmpty())) {

            event.setAuthor(ParseUser.getCurrentUser());
            event.setEventTitle(title);
            event.setDescription(description);

            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null) {
                        event.setEventId(event.getObjectId());
                    }
                }
            });
//            invitation.setEventId(event.getEventId());
//            invitation.setUserId(ParseUser.getCurrentUser());
//            invitation.setFlag("");
            eventInvitation();

        }
        if(title==null || title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();
        }
        if(description==null || description.isEmpty()) {
            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_LONG).show();
        }



    }

    private void eventInvitation() {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("FriendShip");
        parseQuery.whereNotEqualTo("AccepterId", ParseUser.getCurrentUser());
        parseQuery.include("AccepterId");

    }
}
