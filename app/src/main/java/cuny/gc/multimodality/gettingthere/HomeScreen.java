package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

    }

    public void onClick(View v){

        if (v.getId() == R.id.start_button_nav) {
            Intent i = new Intent(this, GetDirections.class);
            startActivity(i);

        } else if (v.getId() == R.id.start_button_maps) {
            Intent intent = new Intent(this, MapsMenu.class);
            startActivity(intent);

        } else if (v.getId() == R.id.start_button_help) {
            Intent i = new Intent(this, HelpMenu.class);
            startActivity(i);
        }

    }

}
