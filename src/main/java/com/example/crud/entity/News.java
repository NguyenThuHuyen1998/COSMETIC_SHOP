package com.example.crud.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tblNEWS")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long newsId;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "content")
    private String content;

    @Column(name = "date_post")
    private long datePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    public News(){

    }


    public News(long newsId, @NotNull String title, @NotNull String content, long datePost, User user) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.datePost = datePost;
        this.user= user;
    }

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDatePost() {
        return datePost;
    }

    public void setDatePost(long datePost) {
        this.datePost = datePost;
    }

    public String getDate() {
        Timestamp ts =new Timestamp(datePost);
        return ts.toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

