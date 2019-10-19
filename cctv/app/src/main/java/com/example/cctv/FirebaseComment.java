package com.example.cctv;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseComment {

    public long getC_id() {
        return c_id;
    }

    public void setC_id(long c_id) {
        this.c_id = c_id;
    }

    public String getC_text() {
        return c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    private long c_id;

    private String c_text;

    public FirebaseComment(){

    }
    public FirebaseComment(long id,String text){
        this.c_id = id;
        this.c_text = text;
    }

    @Exclude
    public Map<String , Object> toMapC(){
        HashMap<String , Object> result = new HashMap<>();
        result.put("c_id",c_id);
        result.put("c_text",c_text);
        return result;
    }

}
