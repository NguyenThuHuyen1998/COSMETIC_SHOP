package com.example.crud.repository;

import com.example.crud.entity.UserVoucher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoucherRepository extends PagingAndSortingRepository<UserVoucher, Long> {
}
