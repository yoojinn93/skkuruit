package org.androidtown.skkuruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by genie on 2017. 6. 11..
 */

public class jobEventAdapter extends BaseAdapter {

    // 아이템 데이터 리스트
    private ArrayList<jobEventItem> jobEventItemList = new ArrayList<jobEventItem>() ;
    private Context context;

    public void clear() {
        jobEventItemList.clear();
    }
    public jobEventAdapter() {

    }

    public ArrayList<jobEventItem> getList() {
        return jobEventItemList;
    }

    @Override
    public int getCount() {
        return jobEventItemList.size();
    }

    // Adapter가 관리하는 Data의 Item 의 Position <객체> 형태로.
    @Override
    public Object getItem(int position) {
        return jobEventItemList.get(position);
    }

    // Adapter가 관리하는 Data의 Item 의 position 값의 ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        int viewType = getItemViewType(position) ;
        jobEventItem jobEventItem = jobEventItemList.get(position);

        if(convertView == null){ //새로 생성될 때만 업데이트
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Data Set에서 position에 위치한 데이터 참조 획득
//            jobEventItem = jobEventItemList.get(position);

            convertView = inflater.inflate(R.layout.jobevent_item, parent, false);
        }

        TextView eventTitle = (TextView) convertView.findViewById(R.id.eventTitle);
        TextView eventDate = (TextView) convertView.findViewById(R.id.eventDate);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventLocation);

        eventTitle.setText(jobEventItem.getEventTitle());
        eventDate.setText(jobEventItem.getEventDate());
        eventLocation.setText(jobEventItem.getEventLocation());

        if (jobEventItem.getEventLocation().equalsIgnoreCase("자과캠")) {
            eventLocation.setTextColor(0xFF00B050);
        }

        //star
        final ToggleButton scrapBtn = (ToggleButton) convertView.findViewById(R.id.scrapBtn);
        scrapBtn.setTag(position);
        scrapBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (scrapBtn.isChecked()) {
                    scrapBtn.setBackgroundResource(R.drawable.star);
                } else {
                    scrapBtn.setBackgroundResource(R.drawable.unstar);
                }
            }
        });

        return convertView;
    }

    //아이템 추가
    public void addItem(int eventNo, String eventTitle, String eventDate, int eventCompanyNo, String eventLocation, String eventContent) {
        jobEventItem item = new jobEventItem() ;
        item.setEventNo(eventNo);
        item.setEventTitle(eventTitle);
        item.setEventDate(eventDate);
        item.setEventCompanyNo(eventCompanyNo);
        item.setEventLocation(eventLocation);
        item.setEventContent(eventContent);

        jobEventItemList.add(item);
    }
}


