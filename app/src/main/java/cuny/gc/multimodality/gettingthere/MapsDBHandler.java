package cuny.gc.multimodality.gettingthere;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MapsDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GettingThere.db";
    public static final String TABLE_STATIONMAPS = "stationmaps";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINES = "lines";
    public static final String COLUMN_LINES_DAY = "day_lines";
    public static final String COLUMN_LINES_NIGHT = "night_lines";
    public static final String COLUMN_STATION = "station_name";
    public static final String COLUMN_STATION_DISP = "station_display_name";

    //We need to pass database information along to superclass
    public MapsDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_STATIONMAPS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LINES + " TEXT " +
                COLUMN_LINES_DAY + " TEXT " +
                COLUMN_LINES_NIGHT + " TEXT " +
                COLUMN_STATION + " TEXT " +
                COLUMN_STATION_DISP + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONMAPS);
        onCreate(db);
    }

    //Add a new row to the database
    public void addStation(StationMap stationmap) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LINES, stationmap.getLines());
        values.put(COLUMN_LINES_DAY, stationmap.getDay_lines());
        values.put(COLUMN_LINES_NIGHT, stationmap.getNight_lines());
        values.put(COLUMN_STATION, stationmap.getStation_name());
        values.put(COLUMN_STATION_DISP, stationmap.getStation_display_name());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STATIONMAPS, null, values);
        db.close();
    }

    //Delete a product from the database
    public void deleteStation(String stationName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STATIONMAPS + " WHERE " + COLUMN_STATION_DISP
                + "=\"" + stationName + "\";");
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STATIONMAPS + " WHERE 1";

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("station_name")) != null) {
                dbString += c.getString(c.getColumnIndex("station_name"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

}