package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MapsMenu extends Activity {

    private static final String TAG = "MapsMenu.java";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_menu);

        // get the database
        MapsDBHandler dbHelper = new MapsDBHandler(this, null, null, 1);
        // Get access to the underlying writeable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor stationCursor = db.rawQuery("SELECT  * FROM stationmaps", null);

        // Find ListView to populate
        ListView mapsListView = (ListView) findViewById(R.id.mapsListView);
        // Setup cursor adapter using cursor from last step
        StationMapCursorAdapter stationAdapter = new StationMapCursorAdapter(this, stationCursor, 0);
        // Attach cursor adapter to the ListView
        mapsListView.setAdapter(stationAdapter);

        // this allows you to give instructions if a list item is clicked
        mapsListView.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // whenever you tap an item in a list, it's position, id, etc. get
                        // passed along and saved in a var

                        String list_info = String.valueOf(parent.getItemIdAtPosition(position));
                        // String selected = ((TextView) view.findViewById(R.id.stationName)).getText().toString();

                        Intent intent = new Intent(view.getContext(), StationMapDisplay.class);
                        intent.putExtra("position_id", list_info);

                        startActivity(intent);
                    }
                }
        );
    }
}
