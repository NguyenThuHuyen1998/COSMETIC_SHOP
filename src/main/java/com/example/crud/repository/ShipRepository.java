package com.example.crud.repository;

import com.example.crud.entity.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends PagingAndSortingRepository<Address, Long> {
    @Query("select t from Address t left join fetch t.user tcc where tcc.userId = :userId")
    List<Address> getAddressByUser_UserId(@Param("userId") long userId);
}
