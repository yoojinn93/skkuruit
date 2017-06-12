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

public class commentAdapter extends BaseAdapter {

    // 아이템 데이터 리스트
    private ArrayList<Comment> commentList = new ArrayList<Comment>() ;
    private Context context;

    public void clear() {
        commentList.clear();
    }

    public commentAdapter() {

    }

    public ArrayList<Comment> getList() {
        return commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    // Adapter가 관리하는 Data의 Item 의 Position <객체> 형태로.
    @Override
    public Object getItem(int position) {
        return commentList.get(position);
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
        Comment comment = commentList.get(position);

        if(convertView == null){ //새로 생성될 때만 업데이트
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.comment_item, parent, false);
        }

        TextView cmtUser = (TextView) convertView.findViewById(R.id.cmtUser);
        TextView cmtContent= (TextView) convertView.findViewById(R.id.cmtContent);
        TextView cmtDate = (TextView) convertView.findViewById(R.id.cmtDate);

        cmtUser.setText(comment.getCmtUser());
        cmtContent.setText(comment.getCmtContent());
        cmtDate.setText(comment.getCmtDate());
        
        return convertView;
    }

    //아이템 추가
    public void addItem(int targetEvent, String cmtUser, String cmtContent, String cmtDate) {
        Comment item = new Comment();
        item.setTargetEvent(targetEvent);
        item.setCmtUser(cmtUser);
        item.setCmtContent(cmtContent);
        item.setCmtDate(cmtDate);

        commentList.add(item);
    }
}


