package org.androidtown.skkuruit;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<LinearLayout> views = new ArrayList<LinearLayout>();
    // 0 : loadView
    // 1 : mainView
    // 2 : favoriteView
    // 3 : mypageView
    // 4 : myScrapView;
    // 5 : myReplyView;
    // 6 : pushSettingView;
    // 7 : specificView
    // 8 : companyView
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        views.add((LinearLayout) findViewById(R.id.loadView));
        views.add((LinearLayout) findViewById(R.id.mainView));
        views.add((LinearLayout) findViewById(R.id.favoriteView));
        views.add((LinearLayout) findViewById(R.id.mypageView));
        views.add((LinearLayout) findViewById(R.id.myScrapView));
        views.add((LinearLayout) findViewById(R.id.myReplyView));
        views.add((LinearLayout) findViewById(R.id.pushSettingView));
        views.add((LinearLayout) findViewById(R.id.specificView));
        views.add((LinearLayout) findViewById(R.id.companyView));

        Button nickCheckBtn = (Button) findViewById(R.id.nickCheckBtn);
        nickCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nickDescription = (TextView) findViewById(R.id.nickDescription);
                nickDescription.setText("사용 가능한 닉네임입니다.");
//                nickDescription.setText("이미 있는 닉네임입니다.");
            }
        });

    }

    //액션바 menu 변경
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;

        actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.layout_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
//        Toolbar parent = (Toolbar)actionbar.getParent();
//        parent.setContentInsetsAbsolute(0,0);

        //처음 로딩 화면에서는 액션바 숨김
        actionBar.hide();

        return true;
    }

    //모든 frame 다 끄고 target만 visible
    public void showView(int target) {
        for (int i=0; i<views.size(); i++) {
            views.get(i).setVisibility(View.INVISIBLE);
        }
        views.get(target).setVisibility(View.VISIBLE);
    }

    //액션바 클릭 -> 마이페이지로 이동
    public void mypage(View v) {
        showView(3);
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        Button test = (Button) findViewById(R.id.actionBarTitle);
//        test.setText("지니님의 마이페이지");
//        Button actionBarLeft = (Button) findViewById(R.id.actionBarLeft);
//        actionBarLeft.setVisibility(View.INVISIBLE);
//        Button actionBarRight = (Button) findViewById(R.id.actionBarRight);
//        actionBarRight.setVisibility(View.INVISIBLE);
    }

    //마이페이지 -> '나의 스크랩' 페이지로 이동
    public void myScrap(View v) {
        showView(4);
    }

    //마이페이지 -> '내가 댓글 단 공고' 페이지로 이동
    public void myReply(View v) {
        showView(5);
    }

    //마이페이지 -> '알림 설정' 페이지로 이동
    public void pushSetting(View v) {
        showView(6);
    }

    //액션바 클릭 -> 메인페이지로 이동

//    menu select
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int curId = item.getItemId();
//        views.add((LinearLayout) findViewById(R.id.mypageView));
//
//        switch(curId) {
//            case R.id.menu_mypage:
//                invisibleAll();
//                views.get(3).setVisibility(View.VISIBLE);
//                Toast.makeText(this, "마이페이지 선택", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_search:
//                Toast.makeText(this, "검색 선택", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //닉네임 설정 후 '시작하기' 버튼 누르면 메인페이지로 이동
    //액션바 클릭 -> 메인페이지로 이동
    public void initStart(View v) {
        showView(1);
        actionBar.show();
    }

    //'나의 관심기업/산업군' 설정 페이지로 이동
    public void favoriteSetting(View v) {
        showView(2);
    }
}
