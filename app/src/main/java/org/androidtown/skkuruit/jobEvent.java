package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class jobEvent {

    private String title;
    private String date;
    private int companyNo;
    private String location;

    public jobEvent() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public jobEvent(String title, String date, int companyNo, String location) {
        this.title = title;
        this.date = date;
        this.companyNo = companyNo;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getLocation() {
        return location;
    }
}
