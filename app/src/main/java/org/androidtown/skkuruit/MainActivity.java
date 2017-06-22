package org.androidtown.skkuruit;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


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
    DatabaseReference userDatabase = firebaseDatabase.getReference("user");
    DatabaseReference targetUser = userDatabase.child(FirebaseInstanceId.getInstance().getToken());

    ListView mainViewList1, mainViewList2, allEventViewList, cmtViewList, companyViewList, myReplyViewList, myScrapViewList;
    jobEventAdapter adapter1, adapter2, adapterAll, adapterReply, adapterScrap;
    commentAdapter adapterCmt;
    companyAdapter adapterCom;

    ArrayList<Button> favComs = new ArrayList<Button>();
    ArrayList<ToggleButton> favBtns = new ArrayList<ToggleButton>();

    String currentUsername;

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

        //for listview
        mainViewList1 = (ListView) findViewById(R.id.mainViewList1);
        mainViewList2 = (ListView) findViewById(R.id.mainViewList2);
        allEventViewList = (ListView) findViewById(R.id.allEventViewList);
        cmtViewList = (ListView) findViewById(R.id.cmtViewList);
        companyViewList = (ListView) findViewById(R.id.companyViewList);
        myReplyViewList = (ListView) findViewById(R.id.myReplyViewList);
        myScrapViewList = (ListView) findViewById(R.id.myScrapViewList);

        //상세페이지 - 내용 부분 Header로 설정
        View header = getLayoutInflater().inflate(R.layout.spevent_header, null, false);
        cmtViewList.addHeaderView(header);

        //adpater 설정
        adapter1 = new jobEventAdapter();
        adapter2 = new jobEventAdapter();
        adapterAll = new jobEventAdapter();
        adapterCmt = new commentAdapter();
        adapterCom = new companyAdapter();
        adapterReply = new jobEventAdapter();
        adapterScrap = new jobEventAdapter();

        mainViewList1.setAdapter(adapter1);
        mainViewList2.setAdapter(adapter2);
        allEventViewList.setAdapter(adapterAll);
        cmtViewList.setAdapter(adapterCmt);
        companyViewList.setAdapter(adapterCom);
        myReplyViewList.setAdapter(adapterReply);
        myScrapViewList.setAdapter(adapterScrap);

        //로딩페이지 - 기존 user일 경우 바로 시작
        registerCheck();

        //로딩페이지 - 신규 user 닉네임 등록
        EditText nickText = (EditText) findViewById(R.id.nickText);
        Button nickCheckBtn = (Button) findViewById(R.id.nickCheckBtn);
        nickCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameCheck();
            }
        });

        readJobEvent();
    }

    //메인페이지-전체 공고 db 읽어와 출력
    private void readJobEventMain() {
        //중복 방지를 위한 list 초기화
        adapter2.clear();

        //listview의 각 event 클릭 시
        mainViewList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("click", "why");
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
    }

    //이미 가입한 user인지 체크
    private void registerCheck() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //users의 모든 자식들의 key값과 value 값들을 iterator로 참조
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                //찾고자 하는 토큰값은 key로 존재하는 값
                while(child.hasNext()) {
                    DataSnapshot tmp = child.next(); //현재 사용자 닉네임 저장
                    if(tmp.getKey().equals(FirebaseInstanceId.getInstance().getToken())) {
                        //이미 가입한 회원일 경우 메인 페이지로 이동
                        initStart();
                        currentUsername = tmp.child("nickname").getValue().toString();
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //닉네임 중복 체크
    private void nicknameCheck() {
        final EditText nickText = (EditText) findViewById(R.id.nickText);
        final TextView nickDescription = (TextView) findViewById(R.id.nickDescription);
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                Button startBtn = (Button) findViewById(R.id.startBtn);

                while(child.hasNext()) {
                    if (child.next().child("nickname").getValue().toString().equals(nickText.getText().toString())) {
                        nickDescription.setText("! 이미 있는 닉네임입니다!");
                        nickDescription.setTextColor(0xEEE40808);

                        //시작하기 버튼 unable 상태로 초기화(ok된 후 중복되는 닉네임 입력했을 경우 처리)
                        startBtn.setBackgroundResource(R.drawable.gray_box);
                        startBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });

                        databaseReference.removeEventListener(this);
                        return;
                    }
                }
                nickDescription.setText("ㅇ 사용 가능한 닉네임입니다");
                nickDescription.setTextColor(0xFF00B050);
                databaseReference.removeEventListener(this);

                //시작하기 버튼 활성화
                startBtn.setBackgroundResource(R.drawable.selected_button);
                startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentUsername = nickText.getText().toString();
                        registerUser();
                        initStart();
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //user 등록
    private void registerUser() {
        final EditText nickText = (EditText) findViewById(R.id.nickText);

        User user = new User();
        user.setFcmToken(FirebaseInstanceId.getInstance().getToken());
        user.setNickname(nickText.getText().toString());

//        databaseReference.child("user").push().setValue(user);
        databaseReference.child("user").child(FirebaseInstanceId.getInstance().getToken()).setValue(user);
//        firebaseDatabase.getReference("user").child("genie").setValue(user);
//        mFirebaseDatabase.getReference("users").child(userData.userEmailID).setValue(userData);

    }

    //전체공고 출력 - firebase에서 채용설명회 공고 읽어와 박스 출력
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
        setDefaultActionBarBack();

        TextView spEventTitle = (TextView) findViewById(R.id.spEventTitle);
        spEventTitle.setText(adapter.getList().get(position).getEventTitle());

        TextView spEventInfo= (TextView) findViewById(R.id.spEventInfo);
        spEventInfo.setText(adapter.getList().get(position).getEventLocation() + " : "
                + adapter.getList().get(position).getEventDate());

        TextView spEventContent = (TextView) findViewById(R.id.spEventContent);
        spEventContent.setText(adapter.getList().get(position).getEventContent());

        getCompany(adapter.getList().get(position).getEventCompanyNo());

        int eventNo = adapter.getList().get(position).getEventNo();
        getComment(eventNo);

        //star
        final ToggleButton scrapBtn2 = (ToggleButton) findViewById(R.id.scrapBtn2);
        scrapBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("scrap", "click");
                if (scrapBtn2.isChecked()) {
                    scrapBtn2.setBackgroundResource(R.drawable.star);
                } else {
                    scrapBtn2.setBackgroundResource(R.drawable.unstar);
                }
            }
        });

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

                Comment comment = new Comment(eventNo, currentUsername, commentInput.getText().toString(), getCurrentTime());
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

        databaseReference.child("comment").orderByChild("targetEvent").equalTo(eventNo).addChildEventListener(new ChildEventListener() {
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

                   //회사명/산업군/필수사항/우대사항 채우기
                   TextView spEventCompany = (TextView) findViewById(R.id.spEventCompany);
                   spEventCompany.setText(company.getCompanyName());

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
        actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        //액션바 아이콘을 업 네비게이션 형태로 표시
        actionBar.setDisplayHomeAsUpEnabled(false);
        //액션바에 표시되는 제목의 표시유무
        actionBar.setDisplayShowTitleEnabled(false);
        //홈 아이콘을 숨김처리
        actionBar.setDisplayShowHomeEnabled(false);


        //layout을 가지고 와서 actionbar에 포팅
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.default_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 타이틀 누르면 메인으로 돌아가도록
        Button actionBarTitle = (Button) findViewById(R.id.actionBarTitle);
        actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStart();
            }
        });

        //액션바 양쪽 공백 없애기
//        Toolbar parent = (Toolbar)actionbar.getParent();
//        parent.setContentInsetsAbsolute(0,0);

        //처음 로딩 화면에서는 액션바 숨김
        actionBar.hide();

        return true;
    }

    //기본 액션바 설정
    public void setDefaultActionBar() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.default_actionbar, null);
        actionBar.setCustomView(actionbar);

        Button actionBarTitle = (Button) findViewById(R.id.actionBarTitle);
        actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStart();
            }
        });
    }

    //기본 액션바 + back버튼 설정
    public void setDefaultActionBarBack() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.default_back_actionbar, null);
        actionBar.setCustomView(actionbar);

        Button actionBarTitle = (Button) findViewById(R.id.actionBarTitle);
        actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStart();
            }
        });
    }

    //페이지별 타이틀 나오는 액션바 설정
    public void setActionBar(String txt) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.page_actionbar, null);
        actionBar.setCustomView(actionbar);

        Button actionBar2Title = (Button) findViewById(R.id.actionBar2Title);
        actionBar2Title.setText(txt);
        actionBar2Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStart();
            }
        });
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
        setActionBar(currentUsername+"님의 마이페이지");
        showView(3);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        Button test = (Button) findViewById(R.id.actionBarTitle);
//        test.setText("지니님의 마이페이지");
//        Button actionBarLeft = (Button) findViewById(R.id.actionBarLeft);
//        actionBarLeft.setVisibility(View.GONE);
//        Button actionBarRight = (Button) findViewById(R.id.actionBarRight);
//        actionBarRight.setVisibility(View.GONE);
    }

    //마이페이지 -> '나의 스크랩' 페이지로 이동
    public void myScrap(View v) {
        setActionBar("나의 스크랩");
        showView(4);

        //공고 클릭할 때마다 초기화 필요
        adapterScrap.clear();
        //댓글 리스트뷰, 어댑터 만들고
        //댓글 사이즈 1보다 크면 댓글 없다는 text invisible 처리

        databaseReference.child("jobEvent").orderByChild("eventNo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final jobEventItem jobEventItem = dataSnapshot.getValue(jobEventItem.class);

                targetUser.child("userScrap").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(String.valueOf(jobEventItem.getEventNo()))){
                            adapterScrap.addItem(jobEventItem.getEventNo(), jobEventItem.getEventTitle(), jobEventItem.getEventDate(),
                                    jobEventItem.getEventCompanyNo(), jobEventItem.getEventLocation(), jobEventItem.getEventContent());
                            adapterScrap.notifyDataSetChanged();
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

    //마이페이지 -> '내가 댓글 단 공고' 페이지로 이동
    public void myReply(View v) {
        setActionBar("내가 댓글 단 공고");
        showView(5);

        //공고 클릭할 때마다 초기화 필요
        adapterReply.clear();
        //댓글 리스트뷰, 어댑터 만들고
        //댓글 사이즈 1보다 크면 댓글 없다는 text invisible 처리

        databaseReference.child("comment").orderByChild("cmtUser").equalTo(currentUsername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);

                databaseReference.child("jobEvent").orderByChild("eventNo").equalTo(comment.getTargetEvent()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        jobEventItem jobEventItem = dataSnapshot.getValue(jobEventItem.class);

                        adapterReply.addItem(jobEventItem.getEventNo(), jobEventItem.getEventTitle(), jobEventItem.getEventDate(),
                                jobEventItem.getEventCompanyNo(), jobEventItem.getEventLocation(), jobEventItem.getEventContent());
                        adapterReply.notifyDataSetChanged();
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

    //마이페이지 -> '알림 설정' 페이지로 이동
    public void pushSetting(View v) {
        setActionBar("알림 설정");
        showView(6);

        final Switch alramSwitch1 = (Switch) findViewById(R.id.alramSwitch1);
        final Switch alramSwitch2 = (Switch) findViewById(R.id.alramSwitch2);
        final Switch alramSwitch3 = (Switch) findViewById(R.id.alramSwitch3);

        alramSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alramSwitch2.setChecked(true);
                    alramSwitch3.setChecked(true);
                }
                else {
                    alramSwitch2.setChecked(false);
                    alramSwitch3.setChecked(false);
                }
            }
        });

        alramSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alramSwitch1.setChecked(true);
                }
                else if (alramSwitch3.isChecked() == false) {
                    alramSwitch1.setChecked(false);
                }
            }
        });

        alramSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alramSwitch1.setChecked(true);
                }
                else if (alramSwitch2.isChecked() == false) {
                    alramSwitch1.setChecked(false);
                }
            }
        });
    }

    //닉네임 설정 후 '시작하기' 버튼 누르면 메인페이지로 이동
    //액션바 클릭 -> 메인페이지로 이동
    public void initStart() {
        showView(1);
        readJobEventMain();
        actionBar.show();
        setDefaultActionBar();
    }

    //계열사 페이지 이동
    public void showCompany(int originCompanyNo) {
        setActionBar("세부 계열사 선택");

        //user 데이터 가져오기
        final ArrayList<Integer> favComArr = new ArrayList<>();
        targetUser.child("userFavCompany").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                favComArr.add(Integer.parseInt(dataSnapshot.getKey()));
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

        //중복 방지를 위한 list 초기화
        adapterCom.clear();

        databaseReference.child("company").orderByChild("originCompanyNo").equalTo(originCompanyNo).addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  company company = dataSnapshot.getValue(company.class);
                  if (favComArr.contains(company.getCompanyNo())) {

//                      Toast.makeText(MainActivity.this, company.getCompanyNo(), Toast.LENGTH_SHORT).show();
//                      adapterCom.getList().get(0);
                  }
                  adapterCom.addItem(company.getCompanyNo(), company.getCompanyName(), company.getOriginCompanyNo());


                  adapterCom.notifyDataSetChanged();
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

        showView(8);
    }

    //'나의 관심기업/산업군' 설정 페이지로 이동
    public void favoriteSetting(View v) {
        setActionBar("나의 관심 기업/산업군");

        //관심 기업 버튼 등록
        favComs.add((Button) findViewById(R.id.favCompany1));
        favComs.add((Button) findViewById(R.id.favCompany2));
        favComs.add((Button) findViewById(R.id.favCompany3));
        favComs.add((Button) findViewById(R.id.favCompany4));
        favComs.add((Button) findViewById(R.id.favCompany5));
        favComs.add((Button) findViewById(R.id.favCompany6));
        favComs.add((Button) findViewById(R.id.favCompany7));
        favComs.add((Button) findViewById(R.id.favCompany8));
        favComs.add((Button) findViewById(R.id.favCompany9));

        //관심 기업 버튼 onclick -> 각 기업 계열사 페이지로 이동
        favComs.get(0).setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  showCompany(1);
              }
        });
        favComs.get(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(2);
            }
        });
        favComs.get(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(3);
            }
        });
        favComs.get(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(4);
            }
        });
        favComs.get(4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(5);
            }
        });
        favComs.get(5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(6);
            }
        });
        favComs.get(6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(7);
            }
        });
        favComs.get(7).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(8);
            }
        });
        favComs.get(8).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCompany(9);
            }
        });

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

        targetUser.child("userFavCategory").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                boolean checked = true;
                ToggleButton targetBtn = favBtns.get(Integer.parseInt(dataSnapshot.getKey())-1);
                targetBtn.setChecked(checked);
                targetBtn.setTextColor(0xFFFFFFFF);
                targetBtn.setBackgroundResource(R.drawable.selected_button);
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

        //관심 산업군 버튼 onclick event 설정
        for (int i=0; i<favBtns.size(); i++) {
            final int index = i+1;
            final ToggleButton targetFavBtn = favBtns.get(i);
            targetFavBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (targetFavBtn.isChecked()) {
                        targetFavBtn.setTextColor(0xFFFFFFFF);
                        targetFavBtn.setBackgroundResource(R.drawable.selected_button);
                        targetUser.child("userFavCategory").child(String.valueOf(index)).setValue(true);
                    }
                    else {
                        targetFavBtn.setTextColor(0xFF151515);
                        targetFavBtn.setBackgroundResource(R.drawable.button);
                        targetUser.child("userFavCategory").child(String.valueOf(index)).removeValue();
                    }
                }
            });
        }

        showView(2);
    }


    //메인페이지 전체공고 더보기 -> 전체공고 출력 페이지로 이동
    public void allEvent(View v) {
        showView(9);
    }
}
