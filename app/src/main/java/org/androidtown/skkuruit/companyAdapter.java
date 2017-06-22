package org.androidtown.skkuruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by genie on 2017. 6. 11..
 */

public class companyAdapter extends BaseAdapter {

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
        company company = companyList.get(position);

        if(convertView == null){ //새로 생성될 때만 업데이트
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.spcompany_item, parent, false);
        }

        final ToggleButton spComName = (ToggleButton) convertView.findViewById(R.id.spComName);

        final String companyName = company.getCompanyName();
        spComName.setText(company.getCompanyName());
        spComName.setTag(position);
        spComName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spComName.isChecked()) {
                    spComName.setText(companyName);
                    spComName.setTextColor(0xFFFFFFFF);
                    spComName.setBackgroundResource(R.drawable.selected_button);
                }
                else {
                    spComName.setText(companyName);
                    spComName.setTextColor(0xFF151515);
                    spComName.setBackgroundResource(R.drawable.button);
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


