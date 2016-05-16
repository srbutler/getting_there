package cuny.gc.multimodality.gettingthere;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextEntryCheck extends AppCompatActivity {

    Button acceptButton;
    Button clearButton;
    EditText textEntryForChecking;
    String destinationDirection;
    String oppositeDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry_check);

        acceptButton = (Button) findViewById(R.id.acceptButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        textEntryForChecking = (EditText) findViewById(R.id.textEntryForChecking);

        // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
        // get data from navigation listview intents
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destinationDirection = extras.getString("destination_direction");
        } else {
            destinationDirection = "";
        }
    }

    public void clearText(View v) {

        textEntryForChecking.setText("");
    }

    public void onClick(View v) {

        String inputText = textEntryForChecking.getText().toString();

        final boolean isCorrectDirection = checkCorrectDirection(destinationDirection, inputText);
        final boolean isOppositeDirection = checkOppositeDirection(oppositeDirection, inputText);

        if (isCorrectDirection) {

            // alertdialog for dealing with correct direction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("It looks like you're going the right way!");
            builder.setTitle("You're on the Right Path!");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // http://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                            // this is responding to an intent sent from previous menu
                            // needs to be implemented there!

                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        } else if (isOppositeDirection) {

            // http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog
            // alertdialog for dealing with opposite direction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("It looks like you're going in the wrong direction!" +
                    "You should get to the opposite platform.\n" +
                    "You can go back to the map, try re-entering the sign text, " +
                    "or get to the opposite side on your own.");
            builder.setTitle("Wrong Way!");
            builder.setCancelable(false);

            builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });

            builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    dialog.cancel();
                }
            });

            builder.setNegativeButton("To Maps", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    dialog.cancel();
                    // TODO: return to maps menu
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {

            // http://stackoverflow.com/questions/4671428/how-can-i-add-a-third-button-to-an-android-alert-dialog
            // alertdialog for dealing with opposite direction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("It's not clear if you are heading in the right direction or not." +
                    "You can go back to the map, try re-entering the sign text, " +
                    "or get to the opposite side on your own.");
            builder.setTitle("Wrong Way!");
            builder.setCancelable(false);

            builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });

            builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    dialog.cancel();
                }
            });

            builder.setNegativeButton("To Maps", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-on-android
                    // this is responding to an intent sent from previous menu
                    // needs to be implemented there!

                    dialog.cancel();
                    // TODO: return to maps menu
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean checkCorrectDirection(String destination, String inputText) {

        // this is a crude implementation; it only checks whether the destination
        // string appears in the user input text

        // TODO: implement a Levenstein-distance derived measure for checking

        return inputText.toLowerCase().contains(destination.toLowerCase());
    }

    private boolean checkOppositeDirection(String opposite, String inputText) {

        // this is a crude implementation; it only checks whether the opposite destination
        // string appears in the user input text

        // TODO: implement a Levenstein-distance derived measure for checking

        return inputText.toLowerCase().contains(opposite.toLowerCase());
    }

}
