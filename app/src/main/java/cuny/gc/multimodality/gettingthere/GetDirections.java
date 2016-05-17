package cuny.gc.multimodality.gettingthere;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

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


public class GetDirections extends ActionBarActivity {

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
    String origin = "";
    String userInput = "";
    String trip_desc = "";
    String town_desc = "";
    ArrayList<Double> lats = new ArrayList<Double>(); //latitudes of locations
    ArrayList<Double> longs = new ArrayList<Double>(); //longitudes of locations
    ArrayList<String> direction_array = new ArrayList<String>(); //all directions
    ArrayList<String> names = new ArrayList<String>(); //names parsed from XML
    ArrayList<String> locs = new ArrayList<String>(); //all locations
    ArrayList<String> locs_names = new ArrayList<String>(); //location names

    // Set display
    TextView etTo;
    Button btn;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);
        etTo = (TextView) findViewById(R.id.etTo);
        btn = (Button) findViewById(R.id.button);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

    public void findPlace(View view) {
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
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                Log.i(TAG, "Place: " + place.getAddress() + place.getPhoneNumber() + place.getLatLng().latitude);

                userInput=String.valueOf(place.getLatLng().latitude);
                userInput+=",";
                userInput+=String.valueOf(place.getLatLng().longitude);
                locs.add(userInput);

                if (locs.size()==1) {
                    etTo.setText("Starting location: " + String.valueOf(place.getName()));

                } else if (locs.size()==2) {
                    etTo.setText("Destination: " + String.valueOf(place.getName()));

                } else {
                    etTo.setText("Location confirmed: " + String.valueOf(place.getName()));
                }

                locs_names.add(String.valueOf(place.getName()));
                lats.add(place.getLatLng().latitude);
                longs.add(place.getLatLng().longitude);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Log.i(TAG, "onActivityResult: result error");

                Status status = PlaceAutocomplete.getStatus(this, data);
                tvResult.setText("No known location. Please try another location.");

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: result cancelled");
            }
        }
    }

    //After the user clicks "get there"
    public void getDirections(View v) {

        btn.setEnabled(false);

        origin = locs.get(0); //first location
        dest = locs.get(1); //second location
        locs.clear(); //clear them in case user inputs second trip
        direction_array.clear(); //clear directions

        trip_desc="Trip confirmed: ";
        trip_desc+=locs_names.get(0);
        trip_desc+=" to ";
        trip_desc += locs_names.get(1);
        etTo.setText(trip_desc);//display trip to user

        locs_names.clear();

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
                tvResult.setText("Finding best route... 0%");
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
            tvResult.setText("Finding best route... " + values[0] + "%");
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

        if (lats.get(1)< 40.711451) {
            town_desc="Brooklyn-bound/Queens-bound ";
        }
        else if (lats.get(1) > 40.805942) {
            town_desc="Uptown/Manhattan-bound/Bronx-bound ";
        }
        else if (longs.get(1) < -73.963081) {
            town_desc="Uptown/Bronx-bound/Manhattan-bound ";
        }
        else {
            town_desc="Downtown/Brooklyn-bound/Queens-bound ";
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {

                currentTag = parser.getName();
                if (currentTag.equals("short_name")) {
                    currentElement = parser.nextText();
                    train_short_name+=currentElement;
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
                    array_append1="Station: ";
                    array_append1+=town_desc;
                    array_append1+=train_short_name;
                    array_append3="Train: ";
                    array_append3+=names.get(2);
                    array_append3+=" ";
                    array_append3+=train_short_name;
                    array_append3+=" to ";
                    array_append3+=headsign_name;
                    array_append2="Trip: ";
                    array_append2+=num_stops;
                    array_append2+=" to ";
                    array_append2+=names.get(1);
                    direction_array.add(array_append1);
                    direction_array.add(array_append3);
                    direction_array.add(array_append2);
                    train_short_name = "";
                    headsign_name = "";
                    array_append1="";
                    array_append2="";
                    num_stops = "";
                    names.clear();
                }
                else if (instructions_name !="") {
                    direction_array.add(instructions_name);
                    instructions_name = "";
                }

            }
            else if (eventType == XmlPullParser.END_TAG) {
                currentTag = parser.getName();

                if (currentTag.equals("DirectionsResponse")) {
                    town_desc="";
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
            tvResult.setText("Finding best route... " + values[0] + "%");   //displays progress
        }

        protected void onPostExecute(ArrayList<String> result) {

            super.onPostExecute(result);
            tvResult.setText("Route found!");
            btn.setEnabled(true);

            if (result != null) {

                StringBuilder b = new StringBuilder(); //should be disabled for prod system; displays the string of directions
                for (String s : result){
                    b.append(s+"\n");
                }
//                tvResult.setText(b);
//                lats.clear();
//                longs.clear();
                Log.i(TAG, b.toString());
            }

            // pass data to NavList
            Intent i = new Intent(GetDirections.this, NavList.class);
            i.putExtra("directionsArrayList", result);
            startActivity(i);

            pb.setVisibility(View.GONE);

        }
    }

}