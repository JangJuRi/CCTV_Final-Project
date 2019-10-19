package com.example.cctv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Board_Writeform extends AppCompatActivity {
    ImageButton Subject;
    TextView Tv_title;
    TextView Tv_text;
    TextView Tv_writer;

    Map<String , Object> childUpdate = new HashMap<>();
    Map<String , Object> postValues = null;
    DBHelper mydb;
    long maxid=1;
    boolean check = false;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_writeform);
        check=true;
        Subject = (ImageButton) findViewById(R.id.imageButton3);
        Tv_title = (TextView) findViewById(R.id.board_title_text);
        Tv_text = (TextView) findViewById(R.id.board_description_text);
        Tv_writer = (TextView)findViewById(R.id.writerTextview);

        mydb = new DBHelper(getApplicationContext());

        Tv_writer.setText(mydb.getResult().toString());

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference mContent = mDatabase.getReference();


                if(Tv_title.getText()==null || Tv_title.getText().length()==0){
                    Toast.makeText(Board_Writeform.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Tv_text.getText()==null || Tv_text.getText().length()==0){
                    Toast.makeText(Board_Writeform.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mContent.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        }

                            @Override
                            public void onCancelled (@NonNull DatabaseError databaseError){

                            }
                    });
                    mContent.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (check) {
                                Log.e("Write", "");
                                if (dataSnapshot.exists()) {
                                    for (int i = 1; i <= dataSnapshot.getChildrenCount(); i++) {
                                        while (dataSnapshot.child("" + maxid).child("post").child("p_id").getValue() == null) {
                                            maxid++;
                                        }
                                        maxid++;
                                    }
                                }
                                String id = String.valueOf(maxid);
                                Log.e("error","check");
                                FirebasePost post = new FirebasePost(id, Tv_title.getText().toString(), Tv_text.getText().toString(), 0, Tv_writer.getText().toString());
                                postValues = post.toMap();

                                childUpdate.put("/id_list/" + id + "/post", postValues);

                                // childUpdate.put("/post_index/"+Tv_title.getText().toString(),postValues);

                                mContent.updateChildren(childUpdate);
                                Tv_title.setText("");
                                Tv_text.setText("");
                                Tv_writer.setText("");

                                Log.e("Write 클릭","_");
                                check=false;
                                Intent intent3 = new Intent(getApplicationContext(), Boardform.class);
                                startActivity(intent3);
                                finish();
                            }
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

    }
}