package com.example.davchen.skibuddies;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.Date;

//import android.text.format.DateFormat;


public class SessionActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

//add for google part
    static final LatLng MyPoint = new LatLng(21 , 57);
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;   //use googleApiClient to get google service
    private boolean mResolvingError = false;    // flag for error
    private static final int REQUEST_RESOLVE_ERROR = 1001;  // Request code to use when launching the resolution activity
    private Location mLastLocation; //my current location
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private LocationRequest mLocationRequest;   //to update the current location
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates = true;
    private PolylineOptions track;  //draw the track line
    private Polyline poly;  //draw the track line

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "request_location_update_key";
    private static final String LOCATION_KEY = "location_key";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "last_update_time_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        //get map
        try {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //To avoid executing the code in onConnectionFailed() while a previous attempt to resolve an error is ongoing
        mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        //defines a buildGoogleApiClient()
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //create location Request
        createLocationRequest();

        //prepare for drawing the line
        track = new PolylineOptions();

        updateValuesFromBundle(savedInstanceState);



    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        System.out.println("in method onMapReady *************************************");
//        LatLng sydney = new LatLng(-33.867, 151.206);
        LatLng mapCenter = new LatLng(28.241019, 113.031012);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));



//add a marker
//        map.addMarker(new MarkerOptions()
////                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow))
//                .position(mapCenter)
//                .flat(true)
//                .rotation(245));
//
//        CameraPosition cameraPosition = CameraPosition.builder()
//                .target(mapCenter)
//                .zoom(13)
//                .bearing(90)
//                .build();
//
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
//                2000, null);

    }

    //connect the googleApiClient when onStart()
    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {  // if no error, connect the client
            mGoogleApiClient.connect();
        }
        else
        {
            System.out.println("There is an Error, cannot connect to ApiClient");
        }
    }

    //disconnect the googleApiClient when onStop()
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_session_activity, menu);
//        return true;
//    }



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


    //required by ConnectionCallback
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }


    //required by ConnectionCallback
    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // Get current location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            System.out.println("Current location: Latitude = "+mLastLocation.getLatitude());
            System.out.println("Current location: Longtitude = "+mLastLocation.getLongitude());
        }

        //next step: transfer a location to parselocation?

        //get location update
//        if (mRequestingLocationUpdates) {
              startLocationUpdates();
//        }

    }



    //get location update
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mRequestingLocationUpdates = true;
    }


    //required by LocationListener
    //update the current location
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;   //update the current location.
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());;
        System.out.println("onLocationChange**************** location: " + location.toString() + " time: " + mLastLocation);

        //move camera to current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
        googleMap.animateCamera(cameraUpdate);
        //locationManager.removeUpdates(this);


        // draw the track line
        //

        track.add(latLng);  //add the current location to track
//        if (poly != null) {
//            poly.remove();
//        }
        track.color(Color.GREEN).width(3);
        poly = googleMap.addPolyline(track);


    }

    //when pause, stop locationupdate
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    //
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    //required by OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        // More about this in the 'Handle Connection Failures' section.
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show error code using GoogleApiAvailability.getErrorDialog()
            System.out.println("*********************onConnectionFailed! the Error code is: "+result.getErrorCode());
            mResolvingError = true;
        }


    }


    //Once the user completes the resolution provided by startResolutionForResult() or getError,
    //activity receives the  onActivityResult() callback with the RESULT_OK result code.
    //called automatically?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }


    //To avoid executing the code in onConnectionFailed() while a previous attempt to resolve an error is ongoing
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);

        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, mLastLocation);
        outState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(outState);


    }


    //for save the state for receiving location updates
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);

            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(
                        LAST_UPDATED_TIME_STRING_KEY);
            }

        }
    }


}
