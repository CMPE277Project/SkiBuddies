package com.example.davchen.skibuddies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davchen.skibuddies.Model.FriendShip;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private List<ParseUser> parseUserList;
    private ListView listView;
    private FriendShip friendShip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        parseUserList = new ArrayList<ParseUser>();
        listView = (ListView)findViewById(R.id.searchList);
        friendShip = new FriendShip();
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_search_results, menu);
//        return true;

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        // getMenuInflater().inflate(R.menu.menu_second, menu);
        menuInflater.inflate(R.menu.menu_second, menu);
        SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchBar1).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
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
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String search = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            queryUsersFromParse(search);
        }
    }

    private void queryUsersFromParse(String search) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", search);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null) {
                    parseUserList.clear();
                    for(ParseUser users: objects) {
                        // Log.d(TAG, users.getUsername());
                        parseUserList.add(users);
                    }
                    //Log.d(TAG, parseUserList.get(0));
                    updateSearchList();
                }
                else{
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }

    private void updateSearchList() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.search_user_profile, parseUserList) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // return super.getView(position, convertView, parent);
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.search_user_profile, parent, false);
                }
                ProfilePictureView profilePictureView = (ProfilePictureView)convertView.findViewById(R.id.userProfiles);
                TextView textView = (TextView)convertView.findViewById(R.id.searchUserId);
                Button button = (Button)convertView.findViewById(R.id.nextLink);

                profilePictureView.setProfileId(parseUserList.get(position).getString("UserId"));
                textView.setText(parseUserList.get(position).getUsername());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAFriend(position);
                    }
                });
                return convertView;

            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


    }

    private void addAFriend(int position) {
        final ParseUser otherUser = parseUserList.get(position);

        Log.d(TAG, otherUser.getUsername());

        friendShip.setFrom(ParseUser.getCurrentUser());
        friendShip.setAccepterId(otherUser);
        friendShip.setStatus("pending");
        friendShip.setDate(new Date());

        friendShip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Sent a Friend Request to "+otherUser.getUsername(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}
