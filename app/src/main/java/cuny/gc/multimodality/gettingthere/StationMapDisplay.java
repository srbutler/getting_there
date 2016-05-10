package cuny.gc.multimodality.gettingthere;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class StationMapDisplay extends AppCompatActivity {

    ImageView stationMapImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map_display);

        // get the intent from MapsMenu
        Bundle stationMapData = getIntent().getExtras();

        // error checking in case the intent is empty
        if (stationMapData == null) {
            return;
        }

        String position_id = stationMapData.getString("position_id");

        switch (position_id) {
            case 6:
                // TODO: put in cases for the position_id values here
        }


        stationMapImageView = (ImageView) findViewById(R.id.stationMapImageView);
    }
}
