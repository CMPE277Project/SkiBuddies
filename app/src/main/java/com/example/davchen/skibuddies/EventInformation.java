package com.example.davchen.skibuddies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseObject;

public class EventInformation extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EventInformation.class.getSimpleName();

    private ParseObject object;
    private String name;
    private ProfilePictureView profilePictureView;
    private TextView textView1, textView2, textView3;
    private Button button;
    private ListView listView;


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

        updateProfile();

        button.setOnClickListener(this);


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
        profilePictureView.setProfileId("");
        textView1.setText("");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }
}
