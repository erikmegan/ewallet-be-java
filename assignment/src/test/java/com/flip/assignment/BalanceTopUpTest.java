package com.flip.assignment;

import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.entity.constant.ErrorCategory;
import com.flip.assignment.repository.TransactionHistoryRepository;
import com.flip.assignment.repository.UserRepository;
import com.flip.assignment.service.api.RedisService;
import com.flip.assignment.util.JSONHelper;
import com.flip.assignment.web.constant.ApiPath;
import com.flip.assignment.web.model.TopUpBalanceApiRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class BalanceTopUpTest extends BaseIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RedisService redisService;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private TransactionHistoryRepository transactionHistoryRepository;

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  public void clear() {
    transactionHistoryRepository.deleteAll();
    userRepository.deleteAll();
    redisTemplate.delete("*");
  }

  @BeforeEach
  public void setup() {
    transactionHistoryRepository.deleteAll();
    userRepository.deleteAll();
    redisTemplate.delete("*");
  }

  @Test
  public void givenValidPayload_returnSuccess() throws Exception {
    String token = prepareUser();

    TopUpBalanceApiRequest topUpBalanceApiRequest = TopUpBalanceApiRequest.builder()
        .amount(BigDecimal.valueOf(1000))
        .build();

    mockMvc.perform(
            post(ApiPath.TRANSACTION + ApiPath.BALANCE_TOPUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(topUpBalanceApiRequest)))
                .header(CommonConstant.AUTHORIZED_HEADER, token))
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string("Topup successful"));
  }

  @Test
  public void givenInvalidAmountNegative_returnInvalidTopUpAmount() throws Exception {
    String token = prepareUser();

    TopUpBalanceApiRequest topUpBalanceApiRequest = TopUpBalanceApiRequest.builder()
        .amount(BigDecimal.valueOf(1000).negate())
        .build();

    mockMvc.perform(
            post(ApiPath.TRANSACTION + ApiPath.BALANCE_TOPUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(topUpBalanceApiRequest)))
                .header(CommonConstant.AUTHORIZED_HEADER, token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(ErrorCategory.INVALID_TOPUP_AMOUNT.getMessage()));
  }

  @Test
  public void givenInvalidAmountPositive_returnInvalidTopUpAmount() throws Exception {
    String token = prepareUser();

    TopUpBalanceApiRequest topUpBalanceApiRequest = TopUpBalanceApiRequest.builder()
        .amount(BigDecimal.valueOf(10000000).negate())
        .build();

    mockMvc.perform(
            post(ApiPath.TRANSACTION + ApiPath.BALANCE_TOPUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(topUpBalanceApiRequest)))
                .header(CommonConstant.AUTHORIZED_HEADER, token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(ErrorCategory.INVALID_TOPUP_AMOUNT.getMessage()));
  }

  private String prepareUser() {
    String tokenFrom = UUID.randomUUID().toString();
    String tokenTo = UUID.randomUUID().toString();
    User userFrom = User.builder().username("flip-test-from").balance(BigDecimal.valueOf(100000)).build();
    User userTo = User.builder().username("flip-test-to").balance(BigDecimal.valueOf(100000)).build();
    redisService.save(CommonConstant.REDIS_KEY, tokenFrom, userFrom);
    redisService.save(CommonConstant.REDIS_KEY, userFrom.getUsername(), tokenFrom);
    userRepository.save(userFrom);
    redisService.save(CommonConstant.REDIS_KEY, tokenTo, userTo);
    redisService.save(CommonConstant.REDIS_KEY, userTo.getUsername(), tokenTo);
    userRepository.save(userTo);
    return tokenFrom;
  }
}
