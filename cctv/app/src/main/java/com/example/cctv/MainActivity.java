package com.example.cctv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    String[] permission_list = {
            Manifest.permission.CALL_PHONE
    };

    Fragment loginfragment = null;
    Fragment mainfragment = null;
    Fragment mapfragment = null;
    Fragment my_addressfragment = null;
    Fragment mypagefragment = null;
    Fragment userfragment = null;
    Fragment votefragment = null;

    BottomNavigationView bottomNavigationView;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private String display="home";
    public static Context mContext;
    ScrollView mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainfragment = new Mainform();
        mapfragment = new Mapform();
        my_addressfragment = new Mypage_Addressform();
        mypagefragment = new Mypageform();
        userfragment = new Userform();

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

        mContext = this;

        if (mainfragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, mainfragment);
            ft.commit();
        }

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation);

        if(display == "naver"||display=="kakao"){
            Login_select();
        }
        else if(display=="login_select"||display=="vote"||display=="user_info"){
            Mypage();
        }
//        else if(display=="address"){
//            UserInfo();
//        }
        else if(display !="home"){
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            Mainpage();
        }
        else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mainfragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, mainfragment);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(id == R.id.navigation_home) {
            Mainpage();
        } else if (id == R.id.navigation_mypage){     //마이페이지
            Mypage();
        } else if (id == R.id.navigation_map) {       //지도 보기
            Map();
        }
        return true;
    }

//    @Override
//    protected void onUserLeaveHint() {
//
//        finish();
//
//        //이벤트
//        super.onUserLeaveHint();
//    }


    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용 여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < grantResults.length; i++) {
                //허용됐다면
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "권한 설정을 해주세요", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Mypageform mypageform = new Mypageform();
//        mypageform.forceLogout();
        super.onDestroy();
    }

    /* 프래그먼트 호출용 */

    public void Mainpage(){
        if (mainfragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(display=="mypage") ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            else if(display=="map") ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            else ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
            ft.replace(R.id.content_main, mainfragment);
            ft.commit();
            display = "home";
        }
    }

    public void Mypage(){
        if (mypagefragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(display=="home")  ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            else ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
            ft.replace(R.id.content_main, mypagefragment);
            ft.commit();
            display = "mypage";
        }
    }
    public void Login_select(){
        if (loginfragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
            ft.replace(R.id.content_main, loginfragment);
            ft.commit();
            display = "login_select";
        }
    }

//    public void UserInfo(){
//        if (userfragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
//            ft.replace(R.id.content_main, userfragment);
//            ft.commit();
//            display = "user_info";
//        }
//    }
    public void Map(){
        if (mapfragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(display=="home") ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            else ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
            ft.replace(R.id.content_main, mapfragment);
            ft.commit();
            display = "map";
        }
    }
}
