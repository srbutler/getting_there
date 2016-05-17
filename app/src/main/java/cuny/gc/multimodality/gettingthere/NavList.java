package cuny.gc.multimodality.gettingthere;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NavList extends AppCompatActivity {

    private static final String TAG = "NavList.java";
    ListView directionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_list);

        // http://stackoverflow.com/questions/21250339/how-to-pass-arraylistcustomeobject-from-one-activity-to-another
        // get the ArrayList<String> via intent and convert to String[]

        ArrayList<String> directionsArrayList = getIntent().getStringArrayListExtra("directionsArrayList");

        String[] directionsArray = new String[directionsArrayList.size()];
        directionsArray = directionsArrayList.toArray(directionsArray);

        Log.i(TAG, "ArrayList: " + directionsArrayList.toString());
        Log.i(TAG, "String[]: " + directionsArrayList.toString());

//        String[] directionsArray = (String[]) directionsArrayList.toArray();

        // TODO: customize R.layout.row_directions and DirectionsAdapter.java to set this correctly
        ListAdapter directionsListAdapter = new DirectionsAdapter(this, directionsArray);
        directionsListView = (ListView) findViewById(R.id.directionsListView);
        directionsListView.setAdapter(directionsListAdapter);

        // this allows you to give instructions if a list item is clicked
        directionsListView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // whenever you tap an item in a list, it's position, id, etc. get
                        // passed along and saved in a var

                        // String itemPosition = String.valueOf(parent.getItemIdAtPosition(position));
                        // String selectedItemText = ((TextView) view.findViewById(R.id.SOMETHINGHERE)).getText().toString();

                        final CharSequence popupMenuOptions[] = new CharSequence[]
                                {"Confirm Step Completion", "See Map", "Type Sign Text", "OCR Sign Text"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(NavList.this);
                        builder.setTitle("Choose an Option");
                        builder.setItems(popupMenuOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (popupMenuOptions[which] == "Confirm Step Completion") {

                                    // TODO: mark some element of row_directions to show that its done

                                } else if (popupMenuOptions[which] == "See Map") {

                                    // TODO: use selectedItemText above to get station name for map

                                } else if (popupMenuOptions[which] == "Type Sign Text") {

                                    // go to the TextEntryCheck activity
                                    Intent i = new Intent(NavList.this, TextEntryCheck.class);
                                    startActivityForResult(i, 1);

                                } else if (popupMenuOptions[which] == "OCR Sign Text") {

                                    // go to the TakePhotoForOCR activity
                                    Intent i = new Intent(NavList.this, TakePhotoForOCR.class);
                                    startActivityForResult(i, 1);

                                } else {

                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Something didn't work.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            }
                        });
                        builder.show();
                    }

                }
        );



    }
}