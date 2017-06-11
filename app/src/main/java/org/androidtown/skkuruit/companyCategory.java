package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class companyCategory {
    private String cateName;

    public companyCategory() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public companyCategory(String cateName) {
        this.cateName = cateName;
    }

//    public void setEventTitle(String eventTitle) {
//        this.eventTitle = eventTitle;
//    }
    public String getCateName() {
        return cateName;
    }
}
