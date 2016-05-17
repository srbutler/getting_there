package cuny.gc.multimodality.gettingthere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class DirectionsAdapter extends ArrayAdapter<String> {

    String[] directionsArray;

    DirectionsAdapter(Context context, String[] directionsArray) {
        super(context, R.layout.row_directions, directionsArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate generally = prep for rendering
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.row_directions, parent, false);

        // get the items in the custom_row layout
        String singleDirection = getItem(position);
        TextView directionTextView = (TextView) customView.findViewById(R.id.directionTextView);
        ImageView statusImage = (ImageView) customView.findViewById(R.id.statusImage);

        directionTextView.setText(singleDirection);
        statusImage.setImageResource(R.drawable.red_circle);

        return customView;
    }
}
