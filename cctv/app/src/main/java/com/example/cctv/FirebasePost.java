package com.example.cctv;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }



    public String getP_text() {
        return p_text;
    }

    public void setP_text(String p_text) {
        this.p_text = p_text;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getC_text() {
        return c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public long getGood() {
        return p_good;
    }

    public void setGood(int good) {
        this.p_good = good;
    }


    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public long getC_index() {
        return c_index;
    }

    public void setC_index(long c_index) {
        this.c_index = c_index;
    }
    private String p_id;
    private int p_good;
    private String p_text;
    private String p_title;
    private String p_writer;

    private String c_id;
    private long c_index;
    private String c_text;

    public FirebasePost(){

    }
    public FirebasePost(long c_index ,String c_id, String c_text){ // comment 생성자
        this.c_id = c_id;
        this.c_text = c_text;
        this.c_index = c_index;
    }
    public FirebasePost(String p_id ,String title, String text,int good, String writer){  // post 생성자
        this.p_id = p_id;
        this.p_text = text;
        this.p_title = title;
        this.p_good = good;
        this.p_writer = writer;
    }

    @Exclude
    public Map<String , Object> toMap(){
        HashMap<String , Object> result = new HashMap<>();
        result.put("p_id",p_id);
        result.put("p_title",p_title);
        result.put("p_text",p_text);
        result.put("p_good",p_good);
        result.put("p_writer",p_writer);
        return result;
    }
    public Map<String ,Object> toMapComment(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("c_id",c_id);
        //result.put("c_index",c_index);
        result.put("c_text",c_text);
        return result;
    }
}
