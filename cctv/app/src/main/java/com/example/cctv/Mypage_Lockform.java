package com.example.cctv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

public class Mypage_Lockform extends Activity implements View.OnClickListener {
    View v;

    Switch lockSwitch;
    Button lockCallButton;
    Button lockBellButton;
    boolean callOn;
    boolean bellOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_selectform);

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

        lockSwitch = (Switch)findViewById(R.id.lockSwitch);
        lockCallButton = (Button)findViewById(R.id.lockCallButton);
        lockBellButton = (Button)findViewById(R.id.lockBellButton);

        lockCallButton.setOnClickListener(this);
        lockBellButton.setOnClickListener(this);

        switchCheck();

        lockSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCheck();
            }
        });
    }

    @Override
    public void onResume() {
        switchCheck();
        super.onResume();
    }

    private void switchCheck(){
        LinearLayout Call_on = (LinearLayout)findViewById(R.id.Call_on);
        LinearLayout Bell_on = (LinearLayout)findViewById(R.id.Bell_on);

        if(lockSwitch.isChecked()) {
            ((MainActivity)MainActivity.mContext).checkPermission();
            Call_on.setVisibility(View.VISIBLE);
            Bell_on.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, ScreenService.class);
            startService(intent);
        }
        else{
            Call_on.setVisibility(View.GONE);
            Bell_on.setVisibility(View.GONE);
            Intent intent = new Intent(this, ScreenService.class);
            stopService(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lockCallButton:
                if(callOn==true){
                    lockCallButton.setText("ON");
                    lockCallButton.setTextColor(Color.rgb(153,204,0));
                    callOn=false;
                }
                else{
                    lockCallButton.setText("OFF");
                    lockCallButton.setTextColor(Color.rgb(255,68,68));
                    callOn=true;
                }
                break;

            case R.id.lockBellButton:
                if(bellOn==true){
                    lockBellButton.setText("ON");
                    lockBellButton.setTextColor(Color.rgb(153,204,0));
                    bellOn=false;
                }
                else{
                    lockBellButton.setText("OFF");
                    lockBellButton.setTextColor(Color.rgb(255,68,68));
                    bellOn=true;
                }
                break;
        }
    }

    @Override
    protected void onUserLeaveHint() {

        finish();

        //이벤트
        super.onUserLeaveHint();
    }
}
