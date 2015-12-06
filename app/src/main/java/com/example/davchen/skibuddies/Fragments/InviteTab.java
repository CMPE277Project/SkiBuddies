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
import android.widget.ListView;
import android.widget.TextView;

import com.example.davchen.skibuddies.Model.Event;
import com.example.davchen.skibuddies.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emy on 12/4/15.
 */
public class InviteTab extends Fragment {

    private static final String TAG = InviteTab.class.getSimpleName();

    private List<Event> invites;
    private ListView inviteList;

    public InviteTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invites =  new ArrayList<Event>();
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereNotEqualTo("author", ParseUser.getCurrentUser());
        query.whereNotEqualTo("flag", 1);
        query.whereNotEqualTo("flag", 2);
        query.orderByDescending("createAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> Objects, ParseException e) {

                if (e == null) {
                    invites.clear();
                    for (ParseObject EventInvite : Objects) {
                        invites.add((Event) EventInvite);
                    }
                    updateList(); //update the list
                } else {
                    Log.d(TAG, "Unable to retreive Invites: " + e.getMessage());
                }

            }
        });
    }

    private void updateList() {

        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity(),
                R.layout.listview_event_item, invites) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Log.d("AS", "klk");
                //return super.getView(position, convertView, parent);
                if(convertView==null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_event_item, parent, false);
                }
                TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_eventTitle);
                TextView tvStart = (TextView)convertView.findViewById(R.id.textView_start);
                TextView tvEnd = (TextView)convertView.findViewById(R.id.textView_end);

                Event event = invites.get(position);

                tvTitle.setText(event.getEventTitle());
                tvStart.setText(event.getStartTime());
                tvEnd.setText(event.getEndTime());

                return convertView;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        Log.d(TAG, "KLKL");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
}
