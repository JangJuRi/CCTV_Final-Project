package com.example.cctv;

public class News{
    private String title;//기사 제목
    private String link;  // 기사 링크
    private String description; //본문내용
    private String pubDate;  // 날짜
    private String author; // 신문사
    private String category; // 카테고리

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){this.title =title;}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
}
