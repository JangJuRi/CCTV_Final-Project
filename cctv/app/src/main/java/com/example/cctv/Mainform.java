package com.example.cctv;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.AUDIO_SERVICE;

public class Mainform extends Fragment implements View.OnClickListener{
    View v;

    LinearLayout boardLayout;
    LinearLayout newsLayout;
    LinearLayout callLayout;
    ImageButton bellButton;
    public MediaPlayer mediaPlayer;

    Button button_One;
    Button button_Two;
    Button button_three;

    String best_index[] ;
    String IntentPUSH[][];
    public static boolean main_check = false;
    long err=3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mainform, container, false);
        button_One = (Button)v.findViewById(R.id.button4);
        button_Two = (Button)v.findViewById(R.id.button3);
        button_three = (Button)v.findViewById(R.id.button);

        boardLayout = (LinearLayout)v.findViewById(R.id.boardButton);
        boardLayout.setOnClickListener(this);
        newsLayout = (LinearLayout)v.findViewById(R.id.newsButton);
        newsLayout.setOnClickListener(this);
        callLayout = (LinearLayout)v.findViewById(R.id.main2_112call);
        callLayout.setOnClickListener(this);

        ((MainActivity)MainActivity.mContext).checkPermission();

        bellButton = (ImageButton)v.findViewById(R.id.bellButton);
        bellButton.setOnClickListener(this);

        button_One.setOnClickListener(this);
        button_Two.setOnClickListener(this);
        button_three.setOnClickListener(this);
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mContent = mDatabase.getReference();

        IntentPUSH = new String[][]{{"", "", "","",""},{"","","","",""},{"","","","",""}};
        main_check=true;
        mContent.addValueEventListener(new ValueEventListener() { // addValueEventListener , addListenerForSingleValueEvent
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (true) {
                    Log.e("메인 폼 시작", "_"+main_check);
                    int p_count = (int) dataSnapshot.child("id_list").getChildrenCount();
                    int number = 1;
                    int best = -1;
                    int best1 = -1, best2 = -1, best3 = -1;

                    String temp = "";
                    best_index = new String[]{"", "", ""};
                    for (int i = 1; i <= p_count; i++) {
                        while (dataSnapshot.child("id_list").child("" + number).child("post").getValue() == null) {
                            number++;
                        }
                        best = Integer.parseInt(String.valueOf(dataSnapshot.child("id_list").child("" + number).child("post").child("p_good").getValue()));
                        if (best > best3) {
                            best_index[2] = dataSnapshot.child("id_list").child("" + number).child("post").child("p_id").getValue().toString();
                            best3 = best;
                            if (best3 > best2) {
                                temp = best_index[1];
                                best_index[1] = best_index[2];
                                best_index[2] = temp;

                                best = best2;
                                best2 = best3;
                                best3 = best;
                                if (best2 > best1) {
                                    temp = best_index[0];
                                    best_index[0] = best_index[1];
                                    best_index[1] = temp;

                                    best = best1;
                                    best1 = best2;
                                    best2 = best;
                                    best = -1;
                                }
                            }
                        }
                        number++;
                    }

                    if(dataSnapshot.child("id_list").getChildrenCount() > 0)
                    button_One.setText(""+dataSnapshot.child("id_list").child(best_index[0]).child("post").child("p_title").getValue().toString());
                    if(dataSnapshot.child("id_list").getChildrenCount() > 1)
                    button_Two.setText(""+dataSnapshot.child("id_list").child(best_index[1]).child("post").child("p_title").getValue().toString());
                    if(dataSnapshot.child("id_list").getChildrenCount() > 2)
                    button_three.setText(""+dataSnapshot.child("id_list").child(best_index[2]).child("post").child("p_title").getValue().toString());

                    if(dataSnapshot.child("id_list").getChildrenCount() <= 3) {
                        err = dataSnapshot.child("id_list").getChildrenCount();
                    }
                    for (int i = 0; i < err; i++) {
                        IntentPUSH[i][0] = dataSnapshot.child("id_list").child(best_index[i]).child("post").child("p_title").getValue().toString();
                        IntentPUSH[i][1] = dataSnapshot.child("id_list").child(best_index[i]).child("post").child("p_text").getValue().toString();
                        IntentPUSH[i][2] = dataSnapshot.child("id_list").child(best_index[i]).child("post").child("p_writer").getValue().toString();
                        IntentPUSH[i][3] = dataSnapshot.child("id_list").child(best_index[i]).child("post").child("p_id").getValue().toString();
                        IntentPUSH[i][4] = dataSnapshot.child("id_list").child(best_index[i]).child("post").child("p_good").getValue().toString();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.boardButton:
                main_check=false;
                Intent intent_board = new Intent(getActivity(),Boardform.class);
                startActivity(intent_board);
                break;
            case R.id.newsButton:
                main_check=false;
                Intent intent_news = new Intent(getActivity(),Newsform.class);
                startActivity(intent_news);
                break;
            case R.id.bellButton:
                if(mediaPlayer != null) noiseOn();
                else
                {
                    mediaPlayer = MediaPlayer.create(getActivity(),R.raw.noise);
                    mediaPlayer.setLooping(true);
                    noiseOn();
                }
                break;

            case R.id.main2_112call:
                Context c = v.getContext();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:112"));

                try {
                    c.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button4:
                if(err>=1) {
                    Intent intent_best1 = new Intent(getContext(), Board_Readform.class);

                    main_check = false;
                    intent_best1.putExtra("title", IntentPUSH[0][0]);
                    intent_best1.putExtra("Ds", IntentPUSH[0][1]);
                    intent_best1.putExtra("writer", IntentPUSH[0][2]);
                    intent_best1.putExtra("index", IntentPUSH[0][3]);
                    intent_best1.putExtra("good", IntentPUSH[0][4]);
                    intent_best1.putExtra("Pressed", "Mainform");
                    startActivity(intent_best1);
                }
                break;
            case R.id.button3:
                if(err>=2) {
                    Intent intent_best2 = new Intent(getContext(), Board_Readform.class);

                    main_check = false;
                    intent_best2.putExtra("title", IntentPUSH[1][0]);
                    intent_best2.putExtra("Ds", IntentPUSH[1][1]);
                    intent_best2.putExtra("writer", IntentPUSH[1][2]);
                    intent_best2.putExtra("index", IntentPUSH[1][3]);
                    intent_best2.putExtra("good", IntentPUSH[1][4]);
                    intent_best2.putExtra("Pressed", "Mainform");
                    startActivity(intent_best2);
                }
                break;
            case R.id.button:
                if(err>=3) {
                    Intent intent_best3 = new Intent(getContext(), Board_Readform.class);

                    main_check = false;
                    intent_best3.putExtra("title", IntentPUSH[2][0]);
                    intent_best3.putExtra("Ds", IntentPUSH[2][1]);
                    intent_best3.putExtra("writer", IntentPUSH[2][2]);
                    intent_best3.putExtra("index", IntentPUSH[2][3]);
                    intent_best3.putExtra("good", IntentPUSH[2][4]);
                    intent_best3.putExtra("Pressed", "Mainform");
                    startActivity(intent_best3);
                }
                break;
        }
    }


    public void noiseOn(){
        if(!mediaPlayer.isPlaying()) mediaPlayer.start();
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}