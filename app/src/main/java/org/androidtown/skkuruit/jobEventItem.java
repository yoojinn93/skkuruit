package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class jobEventItem {
    private String eventTitle;
    private String eventDate;
    private int eventCompanyNo;
    private String eventLocation;

    public jobEventItem() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public jobEventItem(String eventTitle, String eventDate, int eventCompanyNo, String eventLocation) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventCompanyNo = eventCompanyNo;
        this.eventLocation = eventLocation;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public String getEventDate() {
        return eventDate;
    }

    public void setEventCompanyNo(int eventCompanyNo) {
        this.eventCompanyNo =  eventCompanyNo;
    }
    public int getEventCompanyNo() {
        return eventCompanyNo;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    public String getEventLocation() {
        return eventLocation;
    }
}
