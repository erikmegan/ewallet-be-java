package com.flip.assignment.service.impl;

import com.flip.assignment.entity.TransactionHistory;
import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.entity.constant.ErrorCategory;
import com.flip.assignment.entity.dto.TransactionHistoryDto;
import com.flip.assignment.exception.BusinessException;
import com.flip.assignment.repository.TransactionHistoryRepository;
import com.flip.assignment.repository.UserRepository;
import com.flip.assignment.service.api.RedisService;
import com.flip.assignment.service.api.TransactionHistoryService;
import com.flip.assignment.service.model.TopUpBalanceServiceRequest;
import com.flip.assignment.service.model.TopTransactionUserServiceResponse;
import com.flip.assignment.service.model.TopUserServiceResponse;
import com.flip.assignment.service.model.TransactionServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoriesServiceImpl implements TransactionHistoryService {

  private final TransactionHistoryRepository transactionHistoryRepository;
  private final UserRepository userRepository;
  private final RedisService redisService;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void transfer(TransactionServiceRequest transactionServiceRequest) {
    User from =
        (User) redisService.getByKey(CommonConstant.REDIS_KEY, transactionServiceRequest.getFrom());

    BigDecimal finalBalanceFrom = from.getBalance().subtract(transactionServiceRequest.getAmount());
    if (finalBalanceFrom.compareTo(BigDecimal.ZERO) < 0) {
      throw BusinessException.builder().errorCode(ErrorCategory.INSUFFICIENT_BALANCE).build();
    }

    User to = userRepository.findByUsernameAndIsDeleted(transactionServiceRequest.getTo(), false);
    if (Objects.isNull(to)) {
      throw BusinessException.builder().errorCode(ErrorCategory.DESTINATION_USER_NOT_FOUND).build();
    }

    BigDecimal finalBalanceTo = to.getBalance().add(transactionServiceRequest.getAmount());
    String externalId = UUID.randomUUID().toString();

    TransactionHistory transactionHistoriesDebit = this.buildTransactionHistory(
        from, externalId, transactionServiceRequest.getAmount(), CommonConstant.DEBIT
    );
    TransactionHistory transactionHistoriesCredit = this.buildTransactionHistory(
        to, externalId, transactionServiceRequest.getAmount(), CommonConstant.CREDIT
    );

    transactionHistoriesDebit.setTransactionHistories(transactionHistoriesCredit);
    transactionHistoriesCredit.setRelatedTransactionId(transactionHistoriesDebit);
    transactionHistoryRepository.saveAll(
        Arrays.asList(transactionHistoriesCredit, transactionHistoriesDebit));


    from.setBalance(finalBalanceFrom);
    to.setBalance(finalBalanceTo);
    userRepository.saveAll(Arrays.asList(from, to));
  }

  private TransactionHistory buildTransactionHistory(User user, String externalId, BigDecimal amount, String type) {
    return TransactionHistory.builder()
        .username(user.getUsername())
        .amount(amount)
        .externalId(externalId)
        .type(type)
        .build();
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void topUpBalance(TopUpBalanceServiceRequest topUpBalanceServiceRequest) {
    if (topUpBalanceServiceRequest.getAmount().compareTo(BigDecimal.ZERO) < 0
        || topUpBalanceServiceRequest.getAmount().compareTo(BigDecimal.valueOf(10000000)) > 0) {
      throw BusinessException.builder().errorCode(ErrorCategory.INVALID_TOPUP_AMOUNT).build();
    }

    User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY,
        topUpBalanceServiceRequest.getToken());

    String externalId = UUID.randomUUID().toString();

    TransactionHistory transactionHistory = this.buildTransactionHistory(
        user, externalId, topUpBalanceServiceRequest.getAmount(), CommonConstant.TOPUP
    );
    transactionHistoryRepository.save(transactionHistory);


    BigDecimal finalBalance = topUpBalanceServiceRequest.getAmount().add(user.getBalance());
    user.setBalance(finalBalance);
    userRepository.save(user);
  }

  @Transactional
  @Override
  public List<TopTransactionUserServiceResponse> topTransactionUsers(String token) {
    User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY, token);
    List<TransactionHistory> list =
        transactionHistoryRepository.findAllTransactionHistoryByUsernameOrderByAmountDesc(
            user.getUsername(), PageRequest.of(0, 10));

    return list.stream()
        .filter(transactionHistory -> !transactionHistory.getType().equals(CommonConstant.TOPUP))
        .map(transactionHistory -> TopTransactionUserServiceResponse.builder()
            .username(getUsername(transactionHistory))
            .amount(getAmount(transactionHistory))
            .type(transactionHistory.getType())
            .build())
        .collect(Collectors.toList());

  }

  private BigDecimal getAmount(TransactionHistory transactionHistory) {
    return isDebit(transactionHistory.getType()) ? transactionHistory.getTransactionHistories().getAmount()
        : transactionHistory.getRelatedTransactionId().getAmount();
  }

  private String getUsername(TransactionHistory transactionHistory) {
    return isDebit(transactionHistory.getType()) ? transactionHistory.getTransactionHistories().getUsername()
        : transactionHistory.getRelatedTransactionId().getUsername();
  }

  private boolean isDebit(String type) {
    return type.equals(CommonConstant.DEBIT);
  }

  @Transactional
  @Override
  public List<TopUserServiceResponse> topUsers(String token) {
    User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY, token);
    List<TransactionHistoryDto> list =
        transactionHistoryRepository.findAllByUsernameAndType(user.getUsername(),
            CommonConstant.DEBIT);

    return list.stream()
        .map(transactionHistoryDto -> TopUserServiceResponse.builder()
            .username(transactionHistoryDto.getUsername())
            .transactedValue(transactionHistoryDto.getTransactedValue())
            .build())
        .collect(Collectors.toList());

  }
}
