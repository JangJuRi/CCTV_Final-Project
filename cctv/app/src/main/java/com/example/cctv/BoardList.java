package com.example.cctv;

public class BoardList {
    private String rank;             //게시글 추천 수
    private String title;         // 게시글 제목
    private String description;    // 게시글 내용
    private String id;             // 게시글 방 번호
    private String writer;       //작성자 아이디

    public String getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {return id;}

    public String getWriter() {return writer;}

    public BoardList(String rank,String id, String title, String description, String writer) {
        this.rank = rank;               //추천 수
        this.id = id;                    //게시글 인덱스
        this.title = title;              //게시글 제목
        this.description = description;  //게시글 내용
        this.writer = writer;            //게시글 작성자
    }

}