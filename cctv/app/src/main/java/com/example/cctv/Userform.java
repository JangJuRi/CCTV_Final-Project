package com.example.cctv;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class Userform extends Fragment implements View.OnClickListener{
    View v;
    Button LogoutButton;
    LinearLayout UserAddressLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.userform, container, false);

        LogoutButton = (Button)v.findViewById(R.id.LogoutButton);
        LogoutButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LogoutButton:
                ((MainActivity)getActivity()).Mypage();
//                ((Mypageform)).LogoutClick();
                break;
        }
    }
}
