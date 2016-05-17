package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GetDirections extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "GetDirections.java";
    final static int SLEEP_DURATION = 100;      //used in progress bar

    String headsign_name = "";
    String train_short_name = "";
    String instructions_name = "";
    String array_append1 = "";
    String array_append2 = "";
    String array_append3 = "";
    String num_stops = "";
    String dest = "";
    double lat = 0;
    double longs = 0;
    String origin = "";
    String userInput = "";
    String trip_desc = "";
    String town_desc = "";
    String currentLoc = "";
    ArrayList<String> direction_array = new ArrayList<String>(); //all directions
    ArrayList<String> names = new ArrayList<String>(); //names parsed from XML
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000; //1 min
    private long FASTEST_INTERVAL = 5000; //5 seconds
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Set display
    TextView etTo;
    Button btn;
    TextView tvResult;
    android.widget.CheckBox checkBox;
    android.widget.EditText textInputBox;
    android.widget.EditText manualInputBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);
        etTo = (TextView) findViewById(R.id.etTo);
        btn = (Button) findViewById(R.id.button);
        tvResult = (TextView) findViewById(R.id.tvResult);
        textInputBox = (android.widget.EditText) findViewById(R.id.textInput);
        manualInputBox = (android.widget.EditText) findViewById(R.id.findOrigin);
        checkBox = (android.widget.CheckBox) findViewById(R.id.checkBox);

        //starts copied part
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Internet Connection Failure - Map was not loadable.", Toast.LENGTH_SHORT).show();
        }
    }

    public void findPlace(View view) {
        dest = "";
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            tvResult.setText("Destination finder is not available at this time. Try moving to a location with internet service.");
        } catch (GooglePlayServicesNotAvailableException e) {
            tvResult.setText("Destination finder is not available at this time. Try moving to a location with internet service.");
        }
    }

    public void findOrigin(View view) {
        if (checkBox.isChecked() == true) {
            origin = "";
            manualInputBox.setVisibility(View.VISIBLE);
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, 1);
            } catch (GooglePlayServicesRepairableException e) {
                tvResult.setText("Location finder is not available at this time. Try moving to a location with internet service.");
            } catch (GooglePlayServicesNotAvailableException e) {
                tvResult.setText("Location finder is not available at this time. Try moving to a location with internet service.");
            }
        } else {
            manualInputBox.setVisibility(View.INVISIBLE);
            manualInputBox.setHint("Set Start Location");
            origin = currentLoc;
        }

    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber() + place.getLatLng().latitude);
                userInput = String.valueOf(place.getLatLng().latitude);
                userInput += ",";
                userInput += String.valueOf(place.getLatLng().longitude);
                if (dest == "" && checkBox.isChecked() == false) {
                    dest = userInput;
                    textInputBox.setHint("Destination: " + place.getName());
                    lat = place.getLatLng().latitude;
                    longs = place.getLatLng().longitude;
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, longs)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longs), 16));
                } else if (dest == "" && checkBox.isChecked() == true && origin != "") {
                    dest = userInput;
                    textInputBox.setHint("Destination: " + place.getName());
                    lat = place.getLatLng().latitude;
                    longs = place.getLatLng().longitude;
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, longs)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longs), 16));
                } else if (checkBox.isChecked() == true) {
                    origin = userInput;
                    manualInputBox.setHint("Start: " + place.getName());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                tvResult.setText("No known location. Please try another location.");
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    //After the user clicks "get there"
    public void getDirections(View v) {

        btn.setEnabled(false);
        if (manualInputBox.isShown() == false) {
            origin = currentLoc;
        }
        Log.e("Origin", "Place: " + origin + dest);

        direction_array.clear(); //clear directions

        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask(); //download info from Directions API
        downloadTask.execute(url);
    }

    //inputs are origin and destination
    //returns a string to hit the API
    private String getDirectionsUrl(String origin, String dest) {

        String str_origin = "origin=" + origin;
        String str_dest = "destination=" + dest;
        String sensor = "sensor=false"; //no longer necessary, but Google likes it
        String parameters = str_origin + "&" + str_dest + "&mode=transit" + "&" + sensor;
        String output = "xml";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("URL", "Place: " + url);
        return url;

    }

    //Input is the URL string built in getDirectionsURL
    // Hits the API and returns XML data
    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("XML Error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    //While the data is downloading...
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        int progress_status;
        ProgressBar pb; //displayed on-screen

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
            if (status != ConnectionResult.SUCCESS) { // Google Play failed

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, GetDirections.this, requestCode);
                dialog.show();//shows error

            } else { //display the progress bar

                pb = (ProgressBar) findViewById(R.id.progressBar);
                progress_status = 0;

                pb.setVisibility(View.VISIBLE);
                pb.setProgress(progress_status);
            }
        }

        //returns XML data
        @Override
        protected String doInBackground(String... url) {

            while (progress_status < 50) {

                progress_status += 2;
                publishProgress(progress_status);
                SystemClock.sleep(SLEEP_DURATION);
            }

            String s = null;
            try {
                s = downloadUrl(url[0]);//download XML
            } catch (IOException e) {
                Log.d("Background Task", e.toString());
            }
            return s;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            XMLTask xmlTask = new XMLTask();
            xmlTask.execute(result);
        }
    }

    //Parse XML to array of strings
    private ArrayList<String> parseXML(String is) throws
            XmlPullParserException, IOException, URISyntaxException {
        direction_array.clear();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(new StringReader(is));

        int eventType = parser.getEventType();

        String currentTag;

        String currentElement;

        if (lat < 40.711451) {
            town_desc = "Brooklyn-bound/Queens-bound ";
        } else if (lat > 40.805942) {
            town_desc = "Uptown/Manhattan-bound/Bronx-bound ";
        } else if (longs < -73.963081) {
            town_desc = "Uptown/Bronx-bound/Manhattan-bound ";
        } else {
            town_desc = "Downtown/Brooklyn-bound/Queens-bound ";
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {

                currentTag = parser.getName();
                if (currentTag.equals("short_name")) {
                    currentElement = parser.nextText();
                    train_short_name += currentElement;
                }
                if (currentTag.equals("headsign")) {
                    currentElement = parser.nextText();
                    headsign_name = currentElement;
                }
                if (currentTag.equals("num_stops")) {
                    currentElement = parser.nextText();
                    num_stops = currentElement += " Stops";
                }
                if (currentTag.equals("name")) {
                    currentElement = parser.nextText();
                    names.add(currentElement);
                }
                if (currentTag.equals("html_instructions")) {
                    currentElement = parser.nextText();

                    currentElement = currentElement.replace("<b>", "");
                    currentElement = currentElement.replace("</b>", "");

                    String s;
                    while (currentElement.contains("<")) {
                        if (currentElement.indexOf(">") == currentElement.lastIndexOf(">")) {
                            s = "";
                        } else {
                            s = " ";
                        }
                        currentElement = currentElement.replace(currentElement.substring(currentElement.indexOf("<"),
                                currentElement.indexOf(">") + 1), s);
                    }
                    instructions_name = currentElement += "";

                }

                if (headsign_name != "") {
                    direction_array.remove(direction_array.size() - 1);
                    array_append1 = "Station: ";
                    array_append1 += town_desc;
                    array_append1 += train_short_name;
                    array_append3 = "Train: ";
                    array_append3 += names.get(2);
                    array_append3 += " ";
                    array_append3 += train_short_name;
                    array_append3 += " to ";
                    array_append3 += headsign_name;
                    array_append2 = "Trip: ";
                    array_append2 += num_stops;
                    array_append2 += " to ";
                    array_append2 += names.get(1);
                    direction_array.add(array_append1);
                    direction_array.add(array_append3);
                    direction_array.add(array_append2);
                    train_short_name = "";
                    headsign_name = "";
                    array_append1 = "";
                    array_append2 = "";
                    num_stops = "";
                    names.clear();
                } else if (instructions_name != "") {
                    direction_array.add(instructions_name);
                    instructions_name = "";
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                currentTag = parser.getName();

                if (currentTag.equals("DirectionsResponse")) {
                    town_desc = "";
                    return direction_array;
                }
            }
            eventType = parser.next();
        }

        // return null if something went wrong
        return null;
    }

    private class XMLTask extends AsyncTask<String, Integer, ArrayList<String>> {

        int progress_status;
        ProgressBar pb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb = (ProgressBar) findViewById(R.id.progressBar);
            progress_status = 50;
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(progress_status);
        }

        protected ArrayList<String> doInBackground(String... is) {
            while (progress_status < 100) {
                progress_status += 2;
                publishProgress(progress_status);
                SystemClock.sleep(SLEEP_DURATION);
            }
            if (is[0] != null) {

                try {
                    return parseXML(is[0]);//parse the XML string to directions array

                } catch (Exception e) {
                    Log.e("XMLExample", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);
        }

        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            btn.setEnabled(true);

            if (result != null) {

                StringBuilder b = new StringBuilder(); //should be disabled for prod system; displays the string of directions
                for (String s : result) {
                    b.append(s + "\n");
                }
                Log.e("Text", "Place: " + b);
            }

            // pass data to NavList
            Intent i = new Intent(GetDirections.this, NavList.class);
            i.putExtra("directionsArrayList", result);
            startActivity(i);

            pb.setVisibility(View.GONE);
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            //Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            //needs to be updated to the name of the activity
            GetDirectionsPermissionsDispatcher.getMyLocationWithCheck(this);
        } else {
            Toast.makeText(this, "Could not load map.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //needs to be updated to the name of the activity
        GetDirectionsPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        if (map != null) {
            map.setMyLocationEnabled(true); //defaults to current location
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void connectClient() {
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    @Override
    protected void onStop() {
        // turns the services off
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (errorDialog != null) {// uses automatic Google Play error
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    //after location services connects
    @Override
    public void onConnected(Bundle dataBundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            currentLoc = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
            Toast.makeText(this, "Location found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            map.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(this, "Location unknown. Enable GPS updates to use Getting There.", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    //starts location services
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        currentLoc = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    //if location services stop
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    //uses a Google service to resolve the error
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,//try to fix the problem
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error--location services are not available.", Toast.LENGTH_LONG).show();
        }
    }

    // Errors
    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }



}
