package com.example.davchen.skibuddies.Fragments;

import android.content.Context;
import android.content.Intent;
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

import com.example.davchen.skibuddies.Model.Invitation;
import com.example.davchen.skibuddies.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emy on 12/4/15.
 */
public class InviteTab extends Fragment {

    private static final String TAG = InviteTab.class.getSimpleName();

    private List<Invitation> invites;
    private ListView inviteList;
    private Invitation invitation;
    private Context context;

    public InviteTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invites =  new ArrayList<Invitation>();
        invitation = new Invitation();
    }

   // @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.invite_tab_fragment, container, false);
        inviteList = (ListView)rootView.findViewById(R.id.invitationList);
        queryEventListFromParse();
        return rootView;
    }

    private void queryEventListFromParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Invitation");
        query.whereNotEqualTo("Participants", ParseUser.getCurrentUser());
        query.whereEqualTo("Status", "0");
        query.include("EventId");
        //query.orderByDescending("createAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> Objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "hhuhuhuhu");
                    invites.clear();
                    for (ParseObject EventInvite : Objects) {
                        Log.d(TAG, "IN INVITATION");
                        invites.add((Invitation) EventInvite);
                        Log.d(TAG, invites.get(0).toString());
                    }
                    Log.d(TAG, String.valueOf(invites.size()));
                    updateList(); //update the list
                } else {
                    Log.d(TAG, "Unable to retreive Invites: " + e.getMessage());
                }

            }
        });
    }

    private void updateList() {

        ArrayAdapter<Invitation> adapter = new ArrayAdapter<Invitation>(context,
                R.layout.invitation_list, invites) {


            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                Log.d("AS", "going in");
                //return super.getView(position, convertView, parent);
                if(convertView==null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.invitation_list, parent, false);
                }
                if(getActivity()==null) {
                    Log.d(TAG, "activity null");
                }
                if(invites==null) {
                    Log.d(TAG, "invites null");
                }
                Log.d(TAG, "I im in list adapter");
                TextView Title = (TextView)convertView.findViewById(R.id.eventName);
                Button button1 =(Button)convertView.findViewById(R.id.acceptButton);
                Button button2 = (Button)convertView.findViewById(R.id.rejectButton);
                //TextView tvStart = (TextView)convertView.findViewById(R.id.textView_start);
               // TextView tvEnd = (TextView)convertView.findViewById(R.id.textView_end);

                invitation = invites.get(position);

                Title.setText(invitation.getEventId().get("EventTitle").toString());
//                tvStart.setText(event.getStartTime());
//                tvEnd.setText(event.getEndTime());

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus(position);
                    }
                });

                return convertView;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        Log.d(TAG, "KLKL");
        inviteList.setAdapter(adapter);

    }

    private void updateStatus(int position) {
        Invitation invite = invites.get(position);

        invite.setUserId(ParseUser.getCurrentUser());
        invite.setFlag("1");
        invite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Accepted Request", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryEventListFromParse();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            // If a new event has been added, update the list of events
            Log.d(TAG, "Hello");
            queryEventListFromParse();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryEventListFromParse();
    }

}
