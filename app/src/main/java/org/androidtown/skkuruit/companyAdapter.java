package org.androidtown.skkuruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by genie on 2017. 6. 11..
 */

public class companyAdapter extends BaseAdapter {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference userDatabase = firebaseDatabase.getReference("user");
    DatabaseReference targetUser = userDatabase.child(FirebaseInstanceId.getInstance().getToken());


    // 아이템 데이터 리스트
    private ArrayList<company> companyList = new ArrayList<company>() ;
    private Context context;

    public void clear() {
        companyList.clear();
    }

    public companyAdapter() {

    }

    public ArrayList<company> getList() {
        return companyList;
    }

    @Override
    public int getCount() {
        return companyList.size();
    }

    // Adapter가 관리하는 Data의 Item 의 Position <객체> 형태로.
    @Override
    public Object getItem(int position) {
        return companyList.get(position);
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
        final company company = companyList.get(position);

        if(convertView == null){ //새로 생성될 때만 업데이트
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.spcompany_item, parent, false);
        }

        final ToggleButton spComName = (ToggleButton) convertView.findViewById(R.id.spComName);

        final String companyName = company.getCompanyName();
        spComName.setText(companyName);
        spComName.setTag(position);

        //이미 favorite 되어있는 기업
        targetUser.child("userFavCompany").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                boolean checked = true;
                if (dataSnapshot.getKey().equals(String.valueOf(company.getCompanyNo()))){
                    spComName.setChecked(checked);
                    spComName.setText(companyName);
                    spComName.setTextColor(0xFFFFFFFF);
                    spComName.setBackgroundResource(R.drawable.selected_button);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //onclick
        spComName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spComName.isChecked()) {
                    spComName.setText(companyName);
                    spComName.setTextColor(0xFFFFFFFF);
                    spComName.setBackgroundResource(R.drawable.selected_button);
                    targetUser.child("userFavCompany").child(String.valueOf(company.getCompanyNo())).setValue(true);
                }
                else {
                    spComName.setText(companyName);
                    spComName.setTextColor(0xFF151515);
                    spComName.setBackgroundResource(R.drawable.button);
                    targetUser.child("userFavCompany").child(String.valueOf(company.getCompanyNo())).removeValue();

                }
            }
        });

        return convertView;
    }

    //아이템 추가
    public void addItem(int companyNo, String companyName, int originCompanyNo) {
        company item = new company();
        item.setCompanyNo(companyNo);
        item.setCompanyName(companyName);
        item.setOriginCompanyNo(originCompanyNo);

        companyList.add(item);
    }
}


