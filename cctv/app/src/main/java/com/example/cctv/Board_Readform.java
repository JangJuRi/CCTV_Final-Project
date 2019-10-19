package com.example.cctv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;

public class Board_Readform extends Activity {

    ImageView Logo;
    ImageButton GoodButton;
    boolean goodbutton = false;

    TextView commentText;
    TextView GoodCount;
    Button commentButton;

    DBHelper mydb;
    goodHelper goodDB;

    Map<String, Object> childUpdate = new HashMap<>();
    Map<String, Object> postValues = null;
    long number = 1;
    String p_id;

    ListView listView;
    ArrayList<CommentList> data = null;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_readform);

        mydb = new DBHelper(getApplicationContext());
        goodDB = new goodHelper(getApplicationContext());

        Intent intent = getIntent();

        TextView title = (TextView) findViewById(R.id.boardTitleText);
        TextView Ds = (TextView) findViewById(R.id.boardDsText);
        TextView writer = (TextView) findViewById(R.id.boardWriterText);
        GoodCount = (TextView) findViewById(R.id.GoodCountText);
        listView = (ListView)findViewById(R.id.CommentList);

        commentText = (TextView) findViewById(R.id.comment_write);   //댓글 입력창
        commentButton = (Button) findViewById(R.id.comment_submit);  //댓글 등록 버튼

        /* 따봉 */
        GoodButton = (ImageButton)findViewById(R.id.GoodButton);


        title.setText(intent.getStringExtra("title"));
        Ds.setText(intent.getStringExtra("Ds"));
        writer.setText(intent.getStringExtra("writer"));
        p_id = intent.getStringExtra("index");
        GoodCount.setText(intent.getStringExtra("good"));

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mContent = mDatabase.getReference();

        data = new ArrayList<>();

      /*  if(goodDB.getResult().toString() == "") {  // pid 와 같은 값이 있는지 체크 없으면 활성화 끄기
            goodbutton = false;
        }
        else if(goodDB.getResult().toString()!=""){ // ... 있으면 활성화 켜기
            goodbutton = true;
        }*/

        GoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContent.addChildEventListener(new ChildEventListener() {
                    String temp;
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(mydb.getResult().toString() == "[]") {
                            Toast.makeText(getApplicationContext(), "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else if(goodbutton == false)
                        {
                            GoodButton.setBackgroundColor(Color.parseColor("#F9E958"));
                            goodbutton = true;

                            temp = String.valueOf(Integer.parseInt(dataSnapshot.child(p_id).child("post").child("p_good").getValue().toString())+1);

                            mContent.child("id_list").child(p_id).child("post").child("p_good").setValue(temp);

                            //goodDB.insert
                            // 데이터 생성 -> good 데이터 +1 , 내부DB (pid 와 같은 값의 키를 생성)

                        }
                        else
                        {
                            GoodButton.setBackgroundColor(Color.parseColor("#EAEAEA"));
                            goodbutton = false;

                            temp = String.valueOf(Integer.parseInt(dataSnapshot.child(p_id).child("post").child("p_good").getValue().toString())-1);
                            mContent.child("id_list").child(p_id).child("post").child("p_good").setValue(temp);
                            //goodDB.delete();
                            // 데이터 삭제 -> good 데이터 -1 , 내부DB (pid 와 같은 값을 제거)
                            // pid == 게시판index(firebase 기준 p_id) , GoodCount.getText() == 게시판추천(firebase 기준 p_good)
                        }
                        GoodCount.setText(temp);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        // addValueEventListener 실행 후 변경시 실행, addListenerForSingleValueEvent 한번만 바로 실행
        mContent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number = 1;
                if(dataSnapshot.child("id_list").child("" + p_id).child("comment").child("c_index"+5).getValue()==null){
                }

                for(long i=1; i <= dataSnapshot.child("id_list").child("" + p_id).child("comment").getChildrenCount();i++){ // 댓글수 만큼
                    while(dataSnapshot.child("id_list").child("" + p_id).child("comment").child("c_index"+number).getValue()==null){ // 댓글 인덱스가 안맞으면
                        number++;
                    }
                    String c_id = String.valueOf(dataSnapshot.child("id_list").child("" + p_id).child("comment").child("c_index"+number).child("c_id").getValue());
                    String c_text = String.valueOf(dataSnapshot.child("id_list").child("" + p_id).child("comment").child("c_index"+number).child("c_text").getValue());
                    String c_index = String.valueOf(dataSnapshot.child("id_list").child("" + p_id).child("comment").child("c_index"+number).getValue());

                    CommentList commentList1 = new CommentList(c_id,c_text);
                    adapter = new CommentAdapter(getApplicationContext(),R.layout.comment_listview_layout,data);
                    data.add(commentList1);
                    listView.setAdapter(adapter);

                    number++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            String c_id;
            String c_text;
            @Override
            public void onClick(View v) {

                number=1;
                if(mydb.getResult().toString() == "[]") {
                    Toast.makeText(getApplicationContext(), "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else if (commentText.getText() == null || commentText.getText().length() == 0) {
                    Toast.makeText(Board_Readform.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    mContent.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            c_id = mydb.getResult().toString();
                            c_text = commentText.getText().toString();
                            if (dataSnapshot.child("" + p_id).getChildrenCount() == 1) {
                                number=1;
                                FirebasePost post = new FirebasePost(number, c_id, c_text);
                                postValues = post.toMapComment();

                                childUpdate.put("/id_list/" + p_id + "/comment/c_index" + (number), postValues);

                                mContent.updateChildren(childUpdate);


                                commentText.setText("");
                            } else {
                                number = dataSnapshot.child("" + p_id).child("comment").getChildrenCount() + 1;

                                FirebasePost post = new FirebasePost(number, c_id, c_text);
                                postValues = post.toMapComment();

                                childUpdate.put("/id_list/" + p_id + "/comment/c_index" + (number), postValues);

                                mContent.updateChildren(childUpdate);

                                commentText.setText("");
                            }
                            CommentList commentList1 = new CommentList(c_id,c_text);
                            adapter = new CommentAdapter(getApplicationContext(),R.layout.comment_listview_layout,data);
                            data.add(commentList1);
                            listView.setAdapter(adapter);
                        }


                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

        });


        /* 이미지 비트맵 변환*/
        Logo = (ImageView) findViewById(R.id.postLogo);
        GoodButton = (ImageButton) findViewById(R.id.GoodButton);

        Bitmap LogoBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
        Logo.setImageBitmap(LogoBitmap);
        Bitmap GoodBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.board_best);
        GoodButton.setImageBitmap(GoodBitmap);
    }

}