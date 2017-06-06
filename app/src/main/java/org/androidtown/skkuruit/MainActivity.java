package org.androidtown.skkuruit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LinearLayout loadView, mainView, favoriteView;
    int viewIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadView = (LinearLayout) findViewById(R.id.loadView);
        mainView = (LinearLayout) findViewById(R.id.mainView);
        favoriteView = (LinearLayout) findViewById(R.id.favoriteView);

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

    //닉네임 설정 후 '시작하기' 버튼 누르면 메인페이지로 이동
    public void initStart(View v) {
        changeToMain();
    }
    private void changeToMain() {
        loadView.setVisibility(View.INVISIBLE);
        mainView.setVisibility(View.VISIBLE);
        viewIndex = 0;
    }

    //'나의 관심기업/산업군' 설정 페이지로 이동
    public void favoriteSetting(View v) {
        changeToFavorite();
    }
    private void changeToFavorite() {
        mainView.setVisibility(View.INVISIBLE);
        favoriteView.setVisibility(View.VISIBLE);
    }

    //for test start//
    public void change(View v) {
        changeView();
    }
    private void changeView() {
        if (viewIndex == 0) {
            loadView.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.INVISIBLE);
            viewIndex = 1;
        } else if (viewIndex == 1) {
            loadView.setVisibility(View.INVISIBLE);
            mainView.setVisibility(View.VISIBLE);
            viewIndex = 0;
        }
    }
    //for test end//
}
