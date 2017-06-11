package org.androidtown.skkuruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by genie on 2017. 6. 11..
 */

public class jobEventAdapter extends BaseAdapter {

    // 아이템 데이터 리스트
    private ArrayList<jobEventItem> jobEventItemList = new ArrayList<jobEventItem>() ;

    public jobEventAdapter() {

    }
    @Override
    public int getCount() {
        return jobEventItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobEventItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;
        jobEventItem jobEventItem = jobEventItemList.get(position);

        if(convertView == null){ //새로 생성될 때만 업데이트
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

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
        
        return convertView;
    }

    public void clear() {
        jobEventItemList.clear();
    }

    //아이템 추가
    public void addItem(String eventTitle, String eventDate, String eventLocation) {
        jobEventItem item = new jobEventItem() ;
        item.setEventTitle(eventTitle);
        item.setEventDate(eventDate);
        item.setEventLocation(eventLocation);

        jobEventItemList.add(item);

//        this.notifyDataSetChanged(); // 그리고 notifyDataSetChanged를 호출해보세요.

    }
}


