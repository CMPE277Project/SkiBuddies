package com.example.davchen.skibuddies.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davchen.skibuddies.Model.FriendShip;
import com.example.davchen.skibuddies.Model.Session;
import com.example.davchen.skibuddies.R;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by emy on 12/4/15.
 */
public class ProfileTab extends Fragment {

    private ProfilePictureView profilePictureView;
    private TextView textView, textView2;
    private String name, userId, date;
    private List<Session> parseUserList;
    private ListView recordsList;
    private static final String TAG = ProfileTab.class.getSimpleName();
    private Session session;
    private Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getActivity().getIntent().getStringExtra("Name");
        userId = getActivity().getIntent().getStringExtra("Id");
        date = getActivity().getIntent().getStringExtra("Date");

        parseUserList = new ArrayList<Session>();
        session = new Session();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_tab_fragment, container, false);
        profilePictureView = (ProfilePictureView)rootView.findViewById(R.id.facebookProfileId);
        textView = (TextView)rootView.findViewById(R.id.profilePicName);
        textView2 =(TextView)rootView.findViewById(R.id.dateMember);
        recordsList = (ListView)rootView.findViewById(R.id.ListViewOfFriends);

        displayName();
        queryUserRecord();
        return rootView;
    }


    private void queryUserRecord() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Session");
        parseQuery.whereEqualTo("UserId", ParseUser.getCurrentUser());
        parseQuery.include("Distance");
        //parseQuery.include("EventId");
        parseQuery.include("EventId");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //success
                    Toast.makeText(getActivity(), "Retrieving records", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Retrieving records", Toast.LENGTH_LONG).show();
                    parseUserList.clear();
                    for (ParseObject parseObject : objects) {
                        parseUserList.add((Session) parseObject);
                    }
                    fetchRecordList();
                } else {
                    //error
                    Log.e(TAG, "Error: Unable to retrieve records");
                }
            }
        });
    }

    //inflates the List View.....
    private void fetchRecordList() {

         ArrayAdapter<Session> adapter = new ArrayAdapter<Session>(getActivity(), R.layout.users_profile_record, parseUserList) {

             @Override
             public View getView(final int position, View convertView, ViewGroup parent) {
                // return super.getView(position, convertView, parent);
                 if(convertView==null) {
                     convertView = getActivity().getLayoutInflater().inflate(R.layout.users_profile_record, parent, false);
                 }

                 TextView textView = (TextView)convertView.findViewById(R.id.username);
                 TextView textView1 = (TextView)convertView.findViewById(R.id.usersdistance);

                 session = parseUserList.get(position);
                 //Important Note need to add Event after session is done

                 //textView.setText("Event title: "+session.);
                 textView1.setText("Distance Covered: "+session.getDistance());

                // textView.setText("Event title: "+session.getEventId().getString("EventTitle"));
                 textView1.setText("Distance Covered: "+session.getString("Distance"));

                 return convertView;
             }

         };
        recordsList.setAdapter(adapter);
    }

    private void displayName() {
        profilePictureView.setProfileId(userId);
        textView.setText(name);
        textView2.setText("Member since: "+date);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryUserRecord();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryUserRecord();
    }


}
