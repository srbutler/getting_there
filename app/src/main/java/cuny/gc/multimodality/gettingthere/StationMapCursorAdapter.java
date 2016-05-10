package cuny.gc.multimodality.gettingthere;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class StationMapCursorAdapter extends CursorAdapter {

    public StationMapCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_station_map, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView stationNameTV = (TextView) view.findViewById(R.id.stationName);
        TextView stationLinesTV = (TextView) view.findViewById(R.id.stationLines);

        // Extract properties from cursor
        String station_name = cursor.getString(cursor.getColumnIndexOrThrow("station_display_name"));
        String lines = cursor.getString(cursor.getColumnIndexOrThrow("lines"));

        // Populate fields with extracted properties
        stationNameTV.setText(station_name);
        stationLinesTV.setText(lines);
    }
}