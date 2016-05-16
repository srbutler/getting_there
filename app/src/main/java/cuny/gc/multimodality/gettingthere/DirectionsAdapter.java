package cuny.gc.multimodality.gettingthere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class DirectionsAdapter extends ArrayAdapter<String> {

    DirectionsAdapter(Context context, String[] directionsArray) {
        super(context, R.layout.row_directions, directionsArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate generally = prep for rendering
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.row_directions, parent, false);

        // get the items in the custom_row layout
        // TODO: fill in with actual details once we know them


        return customView;
    }
}
