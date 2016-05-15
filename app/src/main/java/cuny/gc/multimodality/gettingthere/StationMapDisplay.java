package cuny.gc.multimodality.gettingthere;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class StationMapDisplay extends AppCompatActivity {

    private static final String TAG = "StationMapDisplay.java";

    String stationFileName;
    int stationLayoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the intent from MapsMenu
        Bundle stationMapData = getIntent().getExtras();

        // error checking in case the intent is empty
        if (stationMapData == null) {
            return;
        }

        String position_id = stationMapData.getString("position_id");

        if (position_id == null) {
            position_id = "0";
        }

        // get the appropriate resource ID for the passed-over list id
        switch (position_id) {
            case "6":
                stationFileName = "dnr_union_st";
                break;
            case "7":
                stationFileName = "dfgnr_4_av_9_st";
                break;
            case "8":
                stationFileName = "dnr_prospect_av";
                break;
            case "9":
                stationFileName = "dnr_25_st";
                break;
            case "10":
                stationFileName = "dnr_36_st";
                break;
            default:
                stationFileName = "error";
        }

        stationLayoutID = this.getResources().getIdentifier("map_" + stationFileName, "layout", this.getPackageName());
        Log.i(TAG, stationFileName + " ID: " + stationLayoutID);

        try {
            setContentView(stationLayoutID);
        } catch (Exception e) {
            Log.e(TAG, "Layout setting didn't work: " + e);
        }
    }

    public void onClick(View v) {

        // Note that I am fully aware of how insane this all is. I've gotta get this
        // done fast and in the dumb way.

        int buttonID;
        String signText;
        String signTitle;

        switch (v.getId()) {

            // UNION ST

            case R.id.imbtn_union_br_ext_n:
                buttonID = R.id.imbtn_union_br_ext_n;
                signTitle = "Union St: BK-Bound Entrance (North)";
                signText = "Union St Station R\n" +
                        "Bay Ridge";
                break;

            case R.id.imbtn_union_br_ext_s:
                buttonID = R.id.imbtn_union_br_ext_s;
                signTitle = "Union St: BK-Bound Entrance (South)";
                signText = "Union St Station R\n" +
                        "Bay Ridge";
                break;

            case R.id.imbtn_union_br_plat:
                buttonID = R.id.imbtn_union_br_plat;
                signTitle = "Union St: BK-Bound Platform";
                signText = "Bay Ridge\n" +
                        "via Local\n" +
                        "\n" +
                        "R To Bay Ridge-95 St\n" +
                        "Late nights take D or N\n" +
                        "to 36 St for R\n" +
                        "\n" +
                        "Late nights D N stops here:\n" +
                        "D to Coney Island via West End\n" +
                        "N to Coney Island via Sea Beach";
                break;

            case R.id.imbtn_union_mh_ext_n:
                buttonID = R.id.imbtn_union_mh_ext_n;
                signTitle = "Union St: MH-Bound Entrance (North)";
                signText = "Union St Station R\n" +
                        "Manhattan & Queens";
                break;

            case R.id.imbtn_union_mh_ext_s:
                buttonID = R.id.imbtn_union_mh_ext_s;
                signTitle = "Union St: MH-Bound Entrance (South)";
                signText = "Union St Station R\n" +
                        "Manhattan & Queens";
                break;

            case R.id.imbtn_union_mh_plat:
                buttonID = R.id.imbtn_union_mh_plat;
                signTitle = "Union St: MH-Bound Platform";
                signText = "Manhattan & Queens\n" +
                        "\n" +
                        "R Broadway Local\n" +
                        "To Forest Hills-71 Av\n" +
                        "except late nights\n" +
                        "\n" +
                        "Late nights D N stop here:\n" +
                        "D 6 Av Express via Bridge\n" +
                        "N Bwy Local via Lower Manhattan";
                break;

            // 4 AV 9 ST

            case R.id.imbtn_4_av_9_st_br_ext_s:
                buttonID = R.id.imbtn_4_av_9_st_br_ext_s;
                signTitle = "4 Av-9 St: BK-Bound Entrance";
                signText = "9 Street Station\n" +
                        "Bay Ridge\n" +
                        "R\n" +
                        "For Manhattan & Queens R \n" +
                        "enter across 4 Avenue";
                break;

            case R.id.imbtn_4_av_9_st_br_plat:
                buttonID = R.id.imbtn_4_av_9_st_br_plat;
                signTitle = "4 Av-9 St: BK-Bound Platform";
                signText = "Bay Ridge\n" +
                        "via Local\n" +
                        "\n" +
                        "R To Bay Ridge-95 St\n" +
                        "Late nights take D or N\n" +
                        "to 36 St for R\n" +
                        "\n" +
                        "Late nights D N stops here:\n" +
                        "D to Coney Island via West End\n" +
                        "N to Coney Island via Sea Beach";
                break;

            case R.id.imbtn_4_av_9_st_mh_ext_n:
                buttonID = R.id.imbtn_4_av_9_st_mh_ext_n;
                signTitle = "4 Av-9 St: MH-Bound Entrance (North)";
                signText = "9 Street Station\n" +
                        "Manhattan & Queens\n" +
                        "R\n" +
                        "For Bay Ridge R enter at 4 Av";
                break;

            case R.id.imbtn_4_av_9_st_mh_ext_s:
                buttonID = R.id.imbtn_4_av_9_st_mh_ext_s;
                signTitle = "4 Av-9 St: MH-Bound Entrance (South)";
                signText = "9 Street Station\n" +
                        "Manhattan & Queens\n" +
                        "R\n" +
                        "For Bay Ridge R enter at 4 Av";
                break;

            case R.id.imbtn_4_av_9_st_mh_plat:
                buttonID = R.id.imbtn_4_av_9_st_mh_plat;
                signTitle = "4 Av-9 St: MH-Bound Platform";
                signText = "Bay Ridge\n" +
                        "via Local\n" +
                        "\n" +
                        "R to Bay Ridge-95 St.\n" +
                        "Late nights take D or N\n" +
                        "to 36 St for R\n" +
                        "\n" +
                        "Late nights DN stop here:\n" +
                        "D to Coney Island via West End\n" +
                        "N to Coney Island via Sea Beach";
                break;

            // PROSPECT AV

            case R.id.imbtn_prospect_br_ext:
                buttonID = R.id.imbtn_prospect_br_ext;
                signTitle = "Prospect Av: BK-Bound Entrance";
                signText = "Prospect Av Station\n" +
                        "Bay Ridge\n" +
                        "R";
                break;

            case R.id.imbtn_prospect_br_plat:
                buttonID = R.id.imbtn_prospect_br_plat;
                signTitle = "Prospect Av: BK-Bound Platform";
                signText = "Bay Ridge\n" +
                        "via Local\n" +
                        "\n" +
                        "R To Bay Ridge-95 St\n" +
                        "Late nights take D or N\n" +
                        "to 36 St for R\n" +
                        "\n" +
                        "Late nights D N stops here:\n" +
                        "D to Coney Island via West End\n" +
                        "N to Coney Island via Sea Beach";
                break;

            case R.id.imbtn_prospect_mh_ext_n:
                buttonID = R.id.imbtn_prospect_mh_ext_n;
                signTitle = "Prospect Av: MH-Bound Exit (North)";
                signText = "Prospect Av Station\n" +
                        "Manhattan & Queens\n" +
                        "R";
                break;

            case R.id.imbtn_prospect_mh_ext_s:
                buttonID = R.id.imbtn_prospect_mh_ext_s;
                signTitle = "Prospect Av: MH-Bound Exit (South)";
                signText = "Prospect Av Station\n" +
                        "Manhattan & Queens\n" +
                        "R";
                break;

            case R.id.imbtn_prospect_mh_plat:
                buttonID = R.id.imbtn_prospect_mh_plat;
                signTitle = "Prospect Av: MH-Bound Platform";
                signText = "Manhattan & Queens\n" +
                        "\n" +
                        "R Broadway Local\n" +
                        "To Forest Hills-71 Av\n" +
                        "except late nights\n" +
                        "\n" +
                        "Late nights D N stop here:\n" +
                        "D 6 Av Express via Bridge\n" +
                        "N Bwy Local via Lower Manhattan";
                break;

            // 25 ST

            case R.id.imbtn_25_st_bk_plat:
                buttonID = R.id.imbtn_25_st_bk_plat;
                signTitle = "25 St: BK-Bound Platform";
                signText = "Bay Ridge\n" +
                        "via Local\n" +
                        "\n" +
                        "R To Bay Ridge-95 St\n" +
                        "Late nights take D or N\n" +
                        "to 36 St for A1:B21\n" +
                        "\n" +
                        "Late nights D N stops here:\n" +
                        "D to Coney Island via West End\n" +
                        "N to Coney Island via Sea Beach";
                break;

            case R.id.imbtn_25_st_br_ext:
                buttonID = R.id.imbtn_25_st_br_ext;
                signTitle = "25 St: BK-Bound Entrance";
                signText = "25 Street Station\n" +
                        "Bay Ridge\n" +
                        "R";
                break;

            case R.id.imbtn_25_st_mh_ext:
                buttonID = R.id.imbtn_25_st_mh_ext;
                signTitle = "25 St: MH-Bound Entrance";
                signText = "25 Street Station\n" +
                        "Manhattan & Queens\n" +
                        "R";
                break;

            case R.id.imbtn_25_st_mh_plat:
                buttonID = R.id.imbtn_25_st_mh_plat;
                signTitle = "25 St: MH-Bound Platform";
                signText = "Manhattan & Queens\n" +
                        "\n" +
                        "R Broadway Local\n" +
                        "To Forest Hills-71 Av\n" +
                        "except late nights\n" +
                        "\n" +
                        "Late nights D N stop here:\n" +
                        "D 6 Av Express via Bridge\n" +
                        "N Bwy Local via Lower Manhattan";
                break;
            default:
                buttonID = 0;
                signTitle = "ERROR";
                signText = "Oops! It looks like this text didn't" +
                        "get stored correctly.";

        }

        Log.i(TAG, Integer.toString(buttonID) + ": " + signText);

        // create a popup presenting the text of a selected sign
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(signText)
                .setTitle(signTitle)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
