package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class HomeScreen extends Activity {

    Button start_button_nav;
    Button start_button_maps;
    Button start_button_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        start_button_nav = (Button) findViewById(R.id.start_button_nav);
        start_button_maps = (Button) findViewById(R.id.start_button_maps);
        start_button_help = (Button) findViewById(R.id.start_button_help);
    }

    public void onClick(View v){

        if (v.getId() == R.id.start_button_nav) {
            Intent i = new Intent(this, NavMenu.class);
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
