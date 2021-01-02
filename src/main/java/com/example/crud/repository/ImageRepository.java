package com.example.crud.repository;

import com.example.crud.entity.Voucher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Voucher, Long> {
}
