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
    private TextView textView;
    private String name;
    private String userId;
    private List<FriendShip> parseUserList;
    private ListView friendsList;
    private static final String TAG = ProfileTab.class.getSimpleName();
    private FriendShip friendShip;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getActivity().getIntent().getStringExtra("Name");
        userId = getActivity().getIntent().getStringExtra("Id");
        parseUserList = new ArrayList<FriendShip>();
        friendShip = new FriendShip();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.profile_tab_fragment, container, false);
        profilePictureView = (ProfilePictureView)rootView.findViewById(R.id.facebookProfileId);
        textView = (TextView)rootView.findViewById(R.id.profilePicName);
        friendsList = (ListView)rootView.findViewById(R.id.ListViewOfFriends);
        displayName();
        queryFriendShipRequest();
        return rootView;
    }

    private void queryFriendShipRequest() {

       // ParseRelation
       final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Friendship");
        //ParseRelation<FriendShip> parseQuery =
        parseQuery.whereEqualTo("accepterId", ParseUser.getCurrentUser());
        parseQuery.whereEqualTo("Status", "pending");
        parseQuery.include("requesterId");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    parseUserList.clear();
                    for (ParseObject parseObject : objects) {
                        parseUserList.add((FriendShip) parseObject);
                        //ParseObject obj1;
//                        parseObject.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
//                            @Override
//                            public void done(ParseObject object, ParseException e) {
//                                if(e==null){
//                                      obj1 = object.getParseUser("requesterId");
//                                }
//                            }
//
//                        });
//                        parseUserList.add((FriendShip) obj1);

                    }
                    //Log.d("Friend", parseUserList.get(0).getUsername());
                    Log.d("Messgae", "Inside");
                    updateFriendsList();

                } else {
                    Log.d(TAG, "Unable to retreive Invites: " + e.getMessage());
                }

            }
        });
    }

    private void updateFriendsList() {

         ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.friend_request_list, parseUserList) {

             @Override
             public View getView(final int position, View convertView, ViewGroup parent) {
                // return super.getView(position, convertView, parent);
                 if(convertView==null) {
                     convertView = getActivity().getLayoutInflater().inflate(R.layout.friend_request_list, parent, false);
                 }

                 ProfilePictureView profilePictureView1 = (ProfilePictureView)convertView.findViewById(R.id.userProfileName11);
                 TextView textView = (TextView)convertView.findViewById(R.id.IDNameOfPerson);
                 Button button = (Button)convertView.findViewById(R.id.accept);
                 Button button1 = (Button)convertView.findViewById(R.id.reject);

                profilePictureView1.setProfileId(parseUserList.get(position).getFrom().getString("UserId"));
                // textView.setText(parseUserList.get(position).getFrom().getUsername());
                 //textView.setText(p);

                 button.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         acceptFriendRequest(position);
                     }
                 });

                 button1.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         //rejectFriendRequest(position);
                     }
                 });

                 return convertView;
             }

         };
        friendsList.setAdapter(adapter);
    }

    private void displayName() {
        profilePictureView.setProfileId(userId);
        textView.setText(name);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryFriendShipRequest();
    }

    private void acceptFriendRequest(int position) {

        final FriendShip parseUser = parseUserList.get(position);

        ParseUser otherUser = ParseUser.getCurrentUser();

//        ParseObject parseObject = new ParseObject("FriendShip");
//        parseObject.put("From", ParseUser.getCurrentUser());
//        parseObject.put("To", otherUser);
//        parseObject.put("Status", "accepted");
//        parseObject.put("Date", new Date());
        parseUser.setAccepterId(otherUser);
        parseUser.setFrom(parseUser.getFrom());
        parseUser.setStatus("Accepted");
        parseUser.setDate(new Date());

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Accepted " + parseUser.getFrom() + "Friend request", Toast.LENGTH_LONG).show();
            }
        });

    }

    /*private void rejectFriendRequest(int position) {

        final FriendShip parseUser = parseUserList.get(position);

        ParseObject parseObject = new ParseObject("FriendShip");
        parseObject.put("From", ParseUser.getCurrentUser());
        parseObject.put("To", parseUser);
        parseObject.put("Status", "rejected");
        parseObject.put("Date", new Date());

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Rejected " + parseUser.getFrom() + "Friend request", Toast.LENGTH_LONG).show();
            }
        });

    }*/


}
