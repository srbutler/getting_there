package cuny.gc.multimodality.gettingthere;


public class StationMap {

    // private variables
    int _id;
    String lines;
    String day_lines;
    String night_lines;
    String station_name;
    String station_display_name;

    // empty constructor
    public StationMap() {}

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getDay_lines() {
        return day_lines;
    }

    public void setDay_lines(String day_lines) {
        this.day_lines = day_lines;
    }

    public String getNight_lines() {
        return night_lines;
    }

    public void setNight_lines(String night_lines) {
        this.night_lines = night_lines;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_display_name() {
        return station_display_name;
    }

    public void setStation_display_name(String station_display_name) {
        this.station_display_name = station_display_name;
    }
}
