package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MapsMenu extends Activity {

    private MapsDBHandler dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_menu);

        // get the database
        dbHelper = new MapsDBHandler(this, null, null, 1);
        // Get access to the underlying writeable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor stationCursor = db.rawQuery("SELECT  * FROM stationmaps", null);

        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.mapsListView);
        // Setup cursor adapter using cursor from last step
        StationMapCursorAdapter todoAdapter = new StationMapCursorAdapter(this, stationCursor, 0);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);
    }


}

