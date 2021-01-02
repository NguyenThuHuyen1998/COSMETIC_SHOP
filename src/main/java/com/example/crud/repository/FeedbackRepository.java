package com.example.crud.repository;

import com.example.crud.entity.FeedBack;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends PagingAndSortingRepository<FeedBack, Long> {
}
