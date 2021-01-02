package com.example.crud.service;

import com.example.crud.entity.FeedBack;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FeedbackService {
    void save(FeedBack feedBack);
    FeedBack getFeedback(long feedbackId);
    List<FeedBack> findAll();
    void updateFeedback(FeedBack feedBack);
    void deleteFeedback(FeedBack feedback);
    List<FeedBack> sortByDatePost(List<FeedBack> feedBacks);
    List<FeedBack> getFeedbackByStar(int star);
    List<FeedBack> getFeedbackByProduct(long productId);
}

