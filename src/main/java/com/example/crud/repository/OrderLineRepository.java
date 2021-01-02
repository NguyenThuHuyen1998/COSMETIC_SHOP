package com.example.crud.repository;

import com.example.crud.entity.OrderLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends PagingAndSortingRepository<OrderLine, Long> {
    @Query("select t from OrderLine t left join fetch t.order tcc where tcc.orderId = :orderId")
    List<OrderLine> getListOrderLineInOrder(@Param("orderId") long orderId);
}