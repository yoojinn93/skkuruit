package org.androidtown.skkuruit;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;


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
    // 9 : allEventView
    ActionBar actionBar;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ListView mainViewList1, mainViewList2, allEventViewList, cmtViewList;
    jobEventAdapter adapter1, adapter2, adapterAll;
    commentAdapter adapterCmt;

    ArrayList<ToggleButton> favBtns = new ArrayList<ToggleButton>();
    //0 : 삼성
    //1 : 현대차
    //2 : SK

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
        views.add((LinearLayout) findViewById(R.id.allEventView));

        EditText nickText = (EditText) findViewById(R.id.nickText);
        Button nickCheckBtn = (Button) findViewById(R.id.nickCheckBtn);
        nickCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nickDescription = (TextView) findViewById(R.id.nickDescription);
                nickDescription.setText("사용 가능한 닉네임입니다.");
//                nickDescription.setText("이미 있는 닉네임입니다.");
            }
        });



        //for listview - 메인, 전체공고
        mainViewList1 = (ListView) findViewById(R.id.mainViewList1);
        mainViewList2 = (ListView) findViewById(R.id.mainViewList2);
        allEventViewList = (ListView) findViewById(R.id.allEventViewList);
        cmtViewList = (ListView) findViewById(R.id.cmtViewList);

        //상세페이지 - 내용 부분 Header로 설정
        View header = getLayoutInflater().inflate(R.layout.spevent_header, null, false);
        cmtViewList.addHeaderView(header);

        //adpater 설정
        adapter1 = new jobEventAdapter();
        adapter2 = new jobEventAdapter();
        adapterAll = new jobEventAdapter();
        adapterCmt = new commentAdapter();

        mainViewList1.setAdapter(adapter1);
        mainViewList2.setAdapter(adapter2);
        allEventViewList.setAdapter(adapterAll);
        cmtViewList.setAdapter(adapterCmt);

        readJobEvent();
//        getCompany();
//        jobEvent t = new jobEvent("abbb", "a", 1, "a");
//        databaseReference.child("jobEvent").push().setValue(t);
//        adapter.addItem("abbb", "a", 1, "a");

    }

    //메인페이지-전체 공고 db 읽어와 출력
    private void readJobEventMain() {
        //중복 방지를 위한 list 초기화
        adapter2.clear();

        //listview의 각 event 클릭 시
        mainViewList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displaySpEvent(position, adapter2);
            }
        });

        databaseReference.child("jobEvent").orderByChild("date").limitToFirst(4).addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  jobEventItem jobEventItem = dataSnapshot.getValue(jobEventItem.class);
                  adapter2.addItem(jobEventItem.getEventNo(), jobEventItem.getEventTitle(), jobEventItem.getEventDate(),
                          jobEventItem.getEventCompanyNo(), jobEventItem.getEventLocation(), jobEventItem.getEventContent());
                  adapter2.notifyDataSetChanged();
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

        //star

//        final ToggleButton scrapBtn = (ToggleButton) findViewById(R.id.scrapBtn);
//        scrapBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (scrapBtn.isChecked()) {
//                    scrapBtn.setTextColor(0xFFFFFFFF);
//                    Toast.makeText(MainActivity.this, "ServerValue.TIMESTAMP", Toast.LENGTH_SHORT).show();
//                    scrapBtn.setBackgroundResource(R.drawable.star);
////                    scrapBtn.setBackground((getResources().getDrawable(R.drawable.star)));
//                } else {
////                    scrapBtn.setTextColor(0xFFFFFFFF);
//                    scrapBtn.setBackground((getResources().getDrawable(R.drawable.unstar)));
//                }
//            }
//        });
    }

    //firebase에서 채용설명회 공고 읽어와 박스 출력
    private void readJobEvent() {
        //listview의 각 event 클릭 시
        allEventViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displaySpEvent(position, adapterAll);
            }
        });

        databaseReference.child("jobEvent").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              jobEventItem jobEventItem = dataSnapshot.getValue(jobEventItem.class);
              adapterAll.addItem(jobEventItem.getEventNo(), jobEventItem.getEventTitle(), jobEventItem.getEventDate(),
                      jobEventItem.getEventCompanyNo(), jobEventItem.getEventLocation(), jobEventItem.getEventContent());
              adapterAll.notifyDataSetChanged();
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
        }
    );}

    //각 공고 박스 클릭 -> 상세 공고 페이지로 이동
    private void displaySpEvent(int position, jobEventAdapter adapter) {
        TextView spEventTitle = (TextView) findViewById(R.id.spEventTitle);
        spEventTitle.setText(adapter.getList().get(position).getEventTitle());

        TextView spEventCompany = (TextView) findViewById(R.id.spEventCompany);
        spEventCompany.setText(String.valueOf(adapter.getList().get(position).getEventCompanyNo()));

        TextView spEventInfo= (TextView) findViewById(R.id.spEventInfo);
        spEventInfo.setText(adapter.getList().get(position).getEventLocation() + " : "
                + adapter.getList().get(position).getEventDate());

        TextView spEventContent = (TextView) findViewById(R.id.spEventContent);
        spEventContent.setText(adapter.getList().get(position).getEventContent());

        getCompany(adapter.getList().get(position).getEventCompanyNo());

        int eventNo = adapter.getList().get(position).getEventNo();
        getComment(eventNo);

//        Toast.makeText(MainActivity.this, adapter.getList().get(position).getEventNo()+" ! ", Toast.LENGTH_LONG).show();

        showView(7);
        writeCmt(eventNo);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCurrentTime() {
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    //상세페이지에서 댓글 작성
    private void writeCmt(final int eventNo) {
        Button cmtSendBtn = (Button) findViewById(R.id.cmtSendBtn);
        cmtSendBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                EditText commentInput = (EditText) findViewById(R.id.commentInput);

                Comment comment = new Comment(eventNo, "genie", commentInput.getText().toString(), getCurrentTime());
                databaseReference.child("comment").push().setValue(comment);
                commentInput.setText("");
            }
        });
    }

    //해당 공고에 대한 댓글 읽어오기
    private void getComment(int eventNo) {
        //공고 클릭할 때마다 초기화 필요
        adapterCmt.clear();
        //댓글 리스트뷰, 어댑터 만들고
        //댓글 사이즈 1보다 크면 댓글 없다는 text invisible 처리

        databaseReference.child("comment").orderByChild("targetEvent").equalTo(eventNo).limitToFirst(2).addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  Comment comment = dataSnapshot.getValue(Comment.class);
//                  Toast.makeText(MainActivity.this, ServerValue.TIMESTAMP+"", Toast.LENGTH_SHORT).show();

//                  TextView cmttest = (TextView) findViewById(R.id.cmttest);
//                  cmttest.setText(comment.getCmtContent());

                  adapterCmt.addItem(comment.getTargetEvent(), comment.getCmtUser(), comment.getCmtContent(), comment.getCmtDate());

                  adapterCmt.notifyDataSetChanged();

                  LinearLayout noCommentMsg = (LinearLayout) findViewById(R.id.noCommentMsg);
                  noCommentMsg.setVisibility(View.GONE);
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
    }
    //카테고리 읽어오기
    private void getCategory() {
        databaseReference.child("companyCategory").orderByChild("categoryNo").equalTo(1).addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  companyCategory companyCategory = dataSnapshot.getValue(companyCategory.class);

//                  Toast.makeText(
//                        MainActivity.this, companyCategory.getCateName(),
//                        Toast.LENGTH_SHORT
//                    ).show();
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
          }
    );}

    //각 공고 클릭 시 해당하는 회사 정보 가져와 출력하기
    private void getCompany(int companyNo) {
        databaseReference.child("company").orderByChild("companyNo").equalTo(companyNo).addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   company company = dataSnapshot.getValue(company.class);

                   //산업군/필수사항/우대사항 채우기
                   TextView spEventCate = (TextView) findViewById(R.id.spEventCate);
                   spEventCate.setText(company.getCompanyCate());

                   TextView spMust = (TextView) findViewById(R.id.spMust);
                   spMust.setText(company.getMustQual());

                   TextView spOption = (TextView) findViewById(R.id.spOption);
                   spOption.setText(company.getOptionQual());
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
           }
        );
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
        readJobEventMain();
        actionBar.show();
    }

    //'나의 관심기업/산업군' 설정 페이지로 이동
    public void favoriteSetting(View v) {
        showView(2);

        //관심 산업군 버튼 등록
        favBtns.add((ToggleButton) findViewById(R.id.favBtn1));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn2));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn3));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn4));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn5));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn6));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn7));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn8));
        favBtns.add((ToggleButton) findViewById(R.id.favBtn9));

        //관심 산업군 버튼 onclick event 설정
        for (int i=0; i<favBtns.size(); i++) {
            final ToggleButton targetFavBtn = favBtns.get(i);
            targetFavBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (targetFavBtn.isChecked()) {
                        targetFavBtn.setTextColor(0xFFFFFFFF);
                        targetFavBtn.setBackgroundResource(R.drawable.selected_button);
                    }
                    else {
                        targetFavBtn.setTextColor(0xFF151515);
                        targetFavBtn.setBackgroundResource(R.drawable.button);
                    }
                }
            });
        }
    }


    //메인페이지 전체공고 더보기 -> 전체공고 출력 페이지로 이동
    public void allEvent(View v) {
        showView(9);
    }
}
