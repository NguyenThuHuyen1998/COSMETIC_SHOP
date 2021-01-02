package com.example.crud.service.impl;

import com.example.crud.entity.FeedBack;
import com.example.crud.helper.TimeHelper;
import com.example.crud.predicate.PredicateFeedback;
import com.example.crud.repository.FeedbackRepository;
import com.example.crud.service.FeedbackService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository){
        this.feedbackRepository= feedbackRepository;
    }


    @Override
    public void save(FeedBack feedBack) {
        feedbackRepository.save(feedBack);
    }

    @Override
    public FeedBack getFeedback(long feedbackId) {
        Optional<FeedBack> optionalFeedBack= feedbackRepository.findById(feedbackId);
        return optionalFeedBack.get();
    }

    @Override
    public List<FeedBack> findAll() {
        return (List<FeedBack>) feedbackRepository.findAll();
    }

    @Override
    public void updateFeedback(FeedBack feedBack) {
        feedbackRepository.save(feedBack);
    }

    @Override
    public void deleteFeedback(FeedBack feedback) {
        feedbackRepository.delete(feedback);
    }

    @Override
    public List<FeedBack> sortByDatePost(List<FeedBack> feedBacks){
        Collections.sort(feedBacks, new Comparator<FeedBack>() {
            @SneakyThrows
            public int compare(FeedBack o1, FeedBack o2) {
                long time1= TimeHelper.getInstance().convertTimestamp(o1.getDatePost());
                long time2= TimeHelper.getInstance().convertTimestamp(o2.getDatePost());
                return time1 < time2 ? 1 : (o1 == o2 ? 0 : -1);
            }
        });
        return feedBacks;
    }

    @Override
    public List<FeedBack> getFeedbackByStar(int star) {
        Predicate<FeedBack> predicate= null;
        PredicateFeedback predicateFeedback= new PredicateFeedback();
        Predicate<FeedBack> checkStar= predicateFeedback.checkStar(star);
        predicate= checkStar;
        List<FeedBack> totalFeedback= findAll();
        List<FeedBack> result= filterFeedback(totalFeedback, predicate);
        return result;
    }

    public static List<FeedBack> filterFeedback (List<FeedBack> feedBacks,
                                               Predicate<FeedBack> predicate)
    {
        return feedBacks.stream()
                .filter( predicate )
                .collect(Collectors.<FeedBack>toList());
    }


}
