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

import com.example.davchen.skibuddies.EventDetail;
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
public class EventTab extends Fragment implements AdapterView.OnItemClickListener {

    private final String TAG = EventTab.class.getSimpleName();

    private ListView eventListView;
    //private ArrayList<String> eventsList_ = new ArrayList<String>();
    private List<Event> eventList;

    public EventTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventList = new ArrayList<Event>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        eventListView = (ListView)rootView.findViewById(R.id.listView_event);
        Log.d(TAG, "I am in on createview");
        queryEventListFromParse();
        //eventListView.setOnItemClickListener(this);
       // ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, eventsList_);
        //eventListView.setAdapter(adapter);
        return rootView;
    }


    //query Events for current user
    private void queryEventListFromParse(){

        //Create query for objects of type "Event"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        // Restrict to cases where the author is the current user.
        //pass in a ParseUser and not String of that user

        query.whereEqualTo("author", ParseUser.getCurrentUser());
        Log.d(TAG, ParseUser.getCurrentUser().toString());
        query.orderByAscending("createAt");

        // Run the query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of event and notify the adapter
                    Log.d(TAG, "Im in background");
                    eventList.clear();
                    for (ParseObject event : objectList) {
                        eventList.add((Event)event);
                    }
                    Log.d(TAG, eventList.toString());

                    updateEventsList();

                } else {
                    Log.d(TAG, "Event retrieval error: " + e.getMessage());
                }
            }
        });
    }


    private void updateEventsList() {

        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(getActivity(),
                R.layout.listview_event_item, eventList){


            @Override
            public View getView(int position, View convertView, ViewGroup parent){

              //  if(convertView == null){
                    Log.d("L", "LL");
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_event_item, parent, false);
               // }


                Log.d(TAG, "I im in list adapter");


                TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_eventTitle);
                TextView tvStart = (TextView)convertView.findViewById(R.id.textView_start);
                TextView tvEnd = (TextView)convertView.findViewById(R.id.textView_end);

                Event event = eventList.get(position);

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
        Log.d(TAG, "I made it to updateEvent");
        eventListView.setAdapter(adapter);

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

        if (resultCode == Activity.RESULT_OK) {
            // If a new event has been added, update the list of events
            Log.d(TAG, "Hello");
            queryEventListFromParse();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), EventDetail.class);
        intent.putExtra("Event_Title", eventList.get(position).getEventTitle());
        startActivity(intent);
    }
}
