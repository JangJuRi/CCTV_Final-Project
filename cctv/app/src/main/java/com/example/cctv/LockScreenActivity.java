package com.example.cctv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LockScreenActivity extends Activity implements View.OnClickListener {

//    private final GestureDetector gestureDetector;
//    public boolean result = false;

    ImageView deco;
    ImageButton deco2;
    ImageButton callButton;
    ImageButton bellButton;
    public MediaPlayer mediaPlayer;

    @SuppressLint("WrongViewCast")
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);

        deco = findViewById(R.id.deco);
        deco2 = findViewById(R.id.deco2);
        callButton = findViewById(R.id.callButton);
        bellButton = findViewById(R.id.bellButton);

        callButton.setOnClickListener(this);
        bellButton.setOnClickListener(this);
        deco2.setOnClickListener(this);

        Bitmap DecoBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.deco);
        deco.setImageBitmap(DecoBitmap);
        Bitmap Deco2Bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.deco2);
        deco2.setImageBitmap(Deco2Bitmap);
        Bitmap CallBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.lock_call);
        callButton.setImageBitmap(CallBitmap);
        Bitmap BellBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.lock_noise);
        bellButton.setImageBitmap(BellBitmap);


        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#F7E756"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callButton:
                Context c = v.getContext();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:112"));

                try {
                    c.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bellButton:
                if(mediaPlayer != null) noiseOn();
                else
                {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.noise);
                    mediaPlayer.setLooping(true);
                    noiseOn();
                }
                break;
            case R.id.deco2:
                finish();
        }
    }

    @Override
    protected void onUserLeaveHint() {

        finish();
        super.onUserLeaveHint();
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