package com.example.davchen.skibuddies.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.davchen.skibuddies.EventInformation;
import com.example.davchen.skibuddies.Model.Event;
import com.example.davchen.skibuddies.Model.Invitation;
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
public class EventTab extends Fragment implements AdapterView.OnItemClickListener {

    private final String TAG = EventTab.class.getSimpleName();

    private ListView eventListView;
    //private ArrayList<String> eventsList_ = new ArrayList<String>();
    private List<Event> eventList;
    private List<Invitation> invitationList;
    private Invitation invitation;
    private String name;
    private String userId;
    private Context context;
    private Event event;

    public EventTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventList = new ArrayList<Event>();
        invitationList = new ArrayList<Invitation>();
        invitation = new Invitation();
        event = new Event();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        eventListView = (ListView)rootView.findViewById(R.id.listView_event);
        queryEventListFromParse();
        //queryInvitation();
        eventListView.setOnItemClickListener(this);
        return rootView;
    }


    //query Events for current user
    private void queryEventListFromParse(){

//        //Create query for objects of type "Event"
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
//        //query.whereEqualTo("Status", "1");
//        // Restrict to cases where the author is the current user.
//        //pass in a ParseUser and not String of that user
//
//        query.whereEqualTo("author", ParseUser.getCurrentUser());
//        query.whereEqualTo("Status", "1");
//       // query.include("EventTitle");

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");

        //parseQuery.whereEqualTo("Participants", ParseUser.getCurrentUser());
        parseQuery.whereEqualTo("Status", "1");
        // parseQuery.include("EventId");
        parseQuery.include("author");
        Log.d(TAG, ParseUser.getCurrentUser().toString());


        //  query.orderByAscending("createAt");

        // Run the query
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of event and notify the adapter
                    Log.d(TAG, "Im in background");
                    //  eventList.clear();
                    eventList.clear();
                    for (ParseObject event : objectList) {
                        eventList.add((Event)event);
                    }

                    updateEventsList();

                } else {
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }

    private void queryInvitation() {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Invitation");
        parseQuery.whereEqualTo("username", ParseUser.getCurrentUser());
        parseQuery.whereEqualTo("status", "1");


        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    invitationList.clear();
                    for (ParseObject parseObject : objects) {
                        invitationList.add((Invitation) parseObject);
                    }
                }
                updateInvitationList();
            }
        });
    }


    private void updateEventsList() {

        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity(),
                R.layout.listview_event_item, eventList){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    Log.d("L", "LL");
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_event_item, parent, false);
                }

                TextView Title = (TextView)convertView.findViewById(R.id.textView_eventTitle);
                TextView tvStart = (TextView)convertView.findViewById(R.id.textView_start);
                TextView tvEnd = (TextView)convertView.findViewById(R.id.textView_end);

                //Event event = eventList.get(position);

                event = eventList.get(position);

                Title.setText(event.getString("EventTitle"));
                // tvStart.setText(event.getStartTime());
                // tvEnd.setText(event.getEndTime());

                return convertView;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        };
        Log.d(TAG, "I made it to updateEvent");
        eventListView.setAdapter(adapter);

    }


    private void updateInvitationList() {
        ArrayAdapter<Invitation> arrayAdapter = new ArrayAdapter<Invitation>(getActivity(), R.layout.listview_event_item, invitationList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_event_item, parent, false);
                }
                Log.d(TAG, "Hello");
                invitation = invitationList.get(position);
                TextView textView = (TextView)convertView.findViewById(R.id.textView_eventTitle);
                textView.setText(invitation.getEventId().get("EventTitle").toString());
                return convertView;
            }
        };
        eventListView.setAdapter(arrayAdapter);

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
        // queryInvitation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            // If a new event has been added, update the list of events
            Log.d(TAG, "Hello");
            queryEventListFromParse();
            // queryInvitation();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(), EventInformation.class);
        intent.putExtra("Event_Title", eventList.get(position).getEventTitle());
        intent.putExtra("Description", eventList.get(position).getDescription());
        intent.putExtra("Username", eventList.get(position).getAuthor().getUsername());
        intent.putExtra("Id", eventList.get(position).getAuthor().getString("UserId"));
        //intent.putExtra("UserId", in)
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryEventListFromParse();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            name = bundle.getString("Name");
            userId = bundle.getString("Id");
        }
    }


}
