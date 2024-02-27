package com.flip.assignment.repository;

import com.flip.assignment.entity.TransactionHistory;
import com.flip.assignment.entity.dto.TransactionHistoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {
    public List<TransactionHistory> findAllTransactionHistoryByUsernameOrderByAmountDesc(String username, Pageable pageable);

    @Query(value = "select  th2.username, sum(th.amount) as transactedValue  from transaction_histories th \n" +
            "inner join transaction_histories th2 on th.id= th2.related_transaction_id\n" +
            "where th.username = (:username) and th.`type` = (:type)\n" +
            "group by th2.username\n" +
            "limit 10", nativeQuery = true)
    public List<TransactionHistoryDto> findAllByUsernameAndType(String username, String type);


}
