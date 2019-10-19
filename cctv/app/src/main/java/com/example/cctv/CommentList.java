package com.example.cctv;

public class CommentList {
    private String Cwriter;         //댓글 작성자
    private String Cdescription;    //댓글 내용

    public String getCWriter() {
        return Cwriter;
    }

    public String getCDescription() {
        return Cdescription;
    }

    public CommentList(String Cwriter,String Cdescription) {
        this.Cwriter = Cwriter;
        this.Cdescription = Cdescription;
    }
}
