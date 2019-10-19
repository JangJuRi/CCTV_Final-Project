package com.example.cctv;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Mapform extends Fragment implements OnMapReadyCallback, Runnable, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    View v;
    Geocoder geocoder;
    private MapView mapView = null; //
    GoogleMap googleMap = null;

    private final int START_PROGRESSDIALOG = 100;
    private final int END_PROGRESSDIALOG = 101;
    ProgressDialog progressDialog = null;
    ProgressDialogHandler handler =null;

    Button Search;
    TextView Addr; // 지도 input info
    TextView bottom_Text;

    String get_data = "";
    String Addr_Point = "";
    String split_Addr;

    ThreadURL threadURL;
    ArrayList<MarkerItem> am = new ArrayList();
    int page = 1; // json 페이지 / 페이지당 요청수 1000개
    double mLat;
    double mLng; // GeoCoding 검색된 주소 좌표 값
    public Mapform() {
// required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mapform, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        geocoder = new Geocoder(getActivity());

        Search = (Button) v.findViewById(R.id.map_addressButton);
        Addr = (TextView) v.findViewById(R.id.map_addressSearch);
        handler = new ProgressDialogHandler(getActivity());
        mapView.getMapAsync(this);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addr_Point = Addr.getText().toString();

                //   GeoCoding 구글 10만회 넘으면 사용못함 10월 21되도 사용못함 -무료체험판-  //
                /*
                 */
                try {
                    List<Address> address;
                    address = geocoder.getFromLocationName(Addr_Point,10);

                    mLat = address.get(0).getLatitude();
                    mLng = address.get(0).getLongitude();
                    Log.e("mLat",""+String.valueOf(mLat));
                    Log.e("mLng",""+String.valueOf(mLng));
                    Addr_Point = address.get(0).getLocality();
                    Log.e("--시--",""+Addr_Point);
                    String str_Addr = address.get(0).getAddressLine(0);
                    Log.e("str_addr",""+str_Addr);
                    //Addr_Point = address.get(0).getLocality(); 구단위자르기
                 /*   String st = address.get(0).getCountryName()+" "+address.get(0).getAdminArea()+" "+address.get(0).getLocality()+" "+address.get(0).getSubLocality(); 동 자르기
                    Log.e("st"," "+st);

                    split_Addr = str_Addr.split(st)[1];
                   Log.e("split_Addr",""+split_Addr);
                    //split_Addr = split_Addr.split("동")[0]+"동";
                   split_Addr = split_Addr.split("동")[0]+"동";
                   Log.e("str",""+st);*/
                 /*   split_Addr = address.get(0).getSubLocality();
                    Log.e("split_Addr",""+split_Addr);*/

                    if(Addr_Point.equals("화성시")||Addr_Point.equals("하남시")||Addr_Point.equals("고양시") ){
                        Toast.makeText(getActivity(),"화성시 , 하남시 , 고양시는 cctv 위치정보가 없습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //   GeoCoding 구글 10만회 넘으면 사용못함 10월 21되도 사용못함 -무료체험판-  //
                // Progress(); // dialog function call
                /*pd = new ProgressDialog(getActivity());
                pd.setMessage("Preparing CCTV Location Service");
                pd.setCancelable(false);
                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
                pd.show();*/

                //  pd = ProgressDialog.show(getActivity(),"감시자들","Preparing CCTV Location Service");

                MapsApi(googleMap);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("map success", Addr_Point);
        this.googleMap = googleMap;
        // MapsApi(googleMap);

    }
    boolean check = false;
    public void MapsApi(GoogleMap Map) {


        int Sum = 0;
        int cnt = 0;
        boolean pass = false;
        page = 1;
        try {


            while(!pass) {
                handler.sendEmptyMessage(START_PROGRESSDIALOG);
                threadURL = new ThreadURL();
                threadURL.start();
                Thread.sleep(1500);
                    JSONObject jsonObject = new JSONObject(get_data);
                handler.sendEmptyMessage(END_PROGRESSDIALOG);
                check = true;
                JSONArray json_Arr = new JSONArray(jsonObject.getString("CCTV"));
                JSONObject jsontemp = json_Arr.getJSONObject(1); // CCTV 태그 버리기는 용

                JSONArray jsonArray = new JSONArray(jsontemp.getString("row"));


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject JSONob = jsonArray.getJSONObject(i);

                    String LAT = JSONob.getString("WGS84_LAT");
                    //Log.e("위도 = ",JSONob.getString("WGS84_LAT"));
                    String LOGT = JSONob.getString("WGS84_LOGT");
                    // Log.e("경도 = ",JSONob.getString("WGS84_LOGT"));
                    String Purpose = JSONob.getString("INSTL_PUPRS_DIV_NM");
                    // Log.e("CCTV 용도 = ",JSONob.getString("INSTL_PUPRS_DIV_NM"));
                    String Installation = JSONob.getString("INSTL_YM");
                    //   Log.e("설치년도 = ",JSONob.getString("INSTL_YM"));
                    String Storage = JSONob.getString("CUSTODY_DAY_CNT");
                    // Log.e("보관일수 = ",JSONob.getString("INSTL_YM"));
                    String DataDate = JSONob.getString("DATA_STD_DE");
                    // Log.e("데이터 기준일자 = ",JSONob.getString("DATA_STD_DE"));
                    String addr = JSONob.getString("REFINE_ROADNM_ADDR");
                    // Log.e("도로명 주소 = ",JSONob.getString("REFINE_ROADNM_ADDR"));
                    String tel = JSONob.getString("MANAGE_INST_TELNO");
                    //  Log.e("관리기 전화번호 = ",JSONob.getString("MANAGE_INST_TELNO"));
                    String s_CNT = JSONob.getString("CAMERA_CNT");
                    // Log.e("CCTV 갯수 = ",JSONob.getString("CAMERA_CNT"));

                    cnt++;
                    Sum++;


                    if (LAT!=null == LAT.equals("") == true){

                    }
                    else {
                        //if(addr.contains(split_Addr)){
                        MarkerItem item = new MarkerItem(LAT, LOGT, addr, tel, Purpose, Installation, Storage,s_CNT ,DataDate);
                        am.add(item);
                        //}
                    }
                }

                if (cnt >= 1000) {
                    cnt = 0;
                    page++;
                } else if (cnt <= 1000) {
                    pass = true;
                    page = 1;

                }
            }
            Log.e("페이지요청", String.valueOf(Sum));
            Sum = 0;
            for (int i = 0; i < am.size(); i++) {
                MarkerOptions mop = new MarkerOptions();
                LatLng location = new LatLng(Double.parseDouble(am.get(i).getLAT()), Double.parseDouble(am.get(i).getLOGT()));
                mop.position(location);
                mop.title(am.get(i).getAddr());


                //
                Map.addMarker(mop);
                Map.setOnMapClickListener(this);
                Map.setOnMarkerClickListener(this);
                    /*Map.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));*/
            }
            LatLng location = new LatLng(mLat,mLng);
            Map.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
            Log.e("ee","");
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            Log.e("dd","");
        }

    }

        @Override
        public void onMapClick(LatLng latLng) {

        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            String Marker_id = marker.getId();
            Marker_id = Marker_id.split("m")[1];

            String Marker_Info = "도로명 주소 : " +am.get(Integer.parseInt(Marker_id)).getAddr()+"\n"+
                    //"위도 , 경도 : "+ am.get(Integer.parseInt(Marker_id)).getLAT()+" , "+am.get(Integer.parseInt(Marker_id)).getLOGT()+"\n"+
                    "CCTV 용도 : "+am.get(Integer.parseInt(Marker_id)).getPurpose()+"\n"+
                    //"설치년도 : "+am.get(Integer.parseInt(Marker_id)).getInstallation()+"\n"+
                    //"보관일수 : "+am.get(Integer.parseInt(Marker_id)).getStorage()+"\n"+
                    //"데이터 기준일자 : "+am.get(Integer.parseInt(Marker_id)).getDataDate()+"\n"+
                    "CCTV 갯수 : "+am.get(Integer.parseInt(Marker_id)).getCNT()+"\n"+
                    "전화번호 : "+am.get(Integer.parseInt(Marker_id)).getTel();

            Toast.makeText(getActivity(), "" + Marker_Info, Toast.LENGTH_LONG).show();
            return false;
        }

    private String jsonReadAll(Reader reader) throws IOException {

        StringBuilder sb = new StringBuilder();

        int cp;

        while ((cp = reader.read()) != -1) {

            sb.append((char) cp);

        }

        return sb.toString();
    }

    @Override
    public void run() {

    }
    public class ThreadURL extends Thread {
        @Override
        public void run() {
            InputStream reader = null;
            String resultSet = null;
            InputStream is;
            String jsonText = "";

            try {
                URL url = new URL("https://openapi.gg.go.kr/CCTV?key=89e53a9d5f2e4e1aa56f51784b9d2a40&pSize=1000&type=json&SIGUN_NM=" + Addr_Point + "&pIndex=" + page);
                Log.e("페이지", String.valueOf(page));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1000);
                conn.setConnectTimeout(1000);
                try {
                    is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    jsonText = jsonReadAll(rd);
                    get_data = jsonText;
                } catch (Exception e) {
                    e.getStackTrace();
                    // Log.e("error", e.getMessage() );
                }

            } catch (Exception e) {
                e.getStackTrace();
                Log.e("catch", "is error");
            } finally {
                if (reader != null) try {
                    reader.close();
                } catch (IOException ie) {
                    ie.getStackTrace();
                }
            }
        }
    }


    public class ProgressDialogHandler extends Handler{
        private Context context = null;
        public ProgressDialogHandler(Context context) {this.context = context;}

        public void handleMessage(Message msg) {
            switch(msg.what) {
                case START_PROGRESSDIALOG:
                    if(progressDialog == null) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("불러오는중");
                        progressDialog.setMessage("잠시만 기다려주세요...");
                    }
                    progressDialog.show();
                    if(!check){
                    Mapform mp = new Mapform();
                    mp.MapsApi(googleMap);
                    }
                    else
                        check=false;
                    break;
                case END_PROGRESSDIALOG:
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;

            }
        }
    }
}