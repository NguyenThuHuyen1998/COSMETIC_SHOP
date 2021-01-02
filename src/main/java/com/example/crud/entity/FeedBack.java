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
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public FeedBack() {
    }

    public FeedBack(String content, int star, Order order, String datePost) {
        this.content = content;
        this.star= star;
        this.order = order;
        this.datePost= datePost;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
