package com.flip.assignment.service.api;

import com.flip.assignment.service.model.TopUpBalanceServiceRequest;
import com.flip.assignment.service.model.TopTransactionUserServiceResponse;
import com.flip.assignment.service.model.TopUserServiceResponse;
import com.flip.assignment.service.model.TransactionServiceRequest;

import java.util.List;

public interface TransactionHistoryService {
    void transfer(TransactionServiceRequest transactionServiceRequest);
    void topUpBalance(TopUpBalanceServiceRequest topUpBalanceServiceRequest);
    List<TopTransactionUserServiceResponse> topTransactionUsers(String token);
    List<TopUserServiceResponse> topUsers(String token);
}
