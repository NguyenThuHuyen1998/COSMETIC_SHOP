package com.example.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tblFeedback")
public class FeedBack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long feedbackId;

    @NotNull
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "star")
    private int star;

    @Column(name = "date_post")
    private String datePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FeedBack() {
    }

    public FeedBack(String content, int star, Product product, String datePost, User user) {
        this.content = content;
        this.star= star;
        this.product = product;
        this.datePost= datePost;
        this.user= user;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
