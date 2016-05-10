package cuny.gc.multimodality.gettingthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HelpMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
    }

    public void onClick(View view) {

        // switch back to home screen
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }
}
