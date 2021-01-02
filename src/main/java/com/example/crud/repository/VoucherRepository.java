package com.example.crud.repository;

import com.example.crud.entity.User;
import com.example.crud.entity.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VoucherRepository extends PagingAndSortingRepository<Voucher, Long> {
//    @Query("select t from Voucher t where t.code = :userName")
//    User getUserByUserName(@Param("userName") String userName);
}
