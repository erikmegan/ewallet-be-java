package com.flip.assignment;

import com.flip.assignment.entity.TransactionHistory;
import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.repository.TransactionHistoryRepository;
import com.flip.assignment.repository.UserRepository;
import com.flip.assignment.service.api.RedisService;
import com.flip.assignment.web.constant.ApiPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TopOverallTransactionTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RedisService redisService;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private TransactionHistoryRepository transactionHistoryRepository;

  @AfterEach
  public void clear() {
    redisTemplate.delete("*");
    transactionHistoryRepository.deleteAll();
    userRepository.deleteAll();
  }

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
    transactionHistoryRepository.deleteAll();
  }


  @Test
  public void givenValidToken_returnOverallDebitTransaction() throws Exception {
    User userFrom = setUser("flip-test-from");
    User userTo = setUser("flip-test-to");
    String token = setToken(userFrom);
    prepareHistory(userFrom, userTo);

    mockMvc.perform(
            get(ApiPath.TRANSACTION + ApiPath.TOP_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstant.AUTHORIZED_HEADER, token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].username").value(userTo.getUsername()))
        .andExpect(jsonPath("$[0].transactedValue").value(new BigDecimal("20.0")));
  }

  private void prepareHistory(User from, User to) {
    String externalId = UUID.randomUUID().toString();
    TransactionHistory debit1 = TransactionHistory.builder()
        .amount(BigDecimal.TEN)
        .externalId(externalId)
        .type(CommonConstant.DEBIT)
        .username(from.getUsername())
        .build();

    TransactionHistory credit1 = TransactionHistory.builder()
        .amount(BigDecimal.TEN)
        .externalId(externalId)
        .type(CommonConstant.CREDIT)
        .username(to.getUsername())
        .build();

    debit1.setTransactionHistories(credit1);
    credit1.setRelatedTransactionId(debit1);


    transactionHistoryRepository.saveAll(Arrays.asList(debit1, credit1));

    TransactionHistory debit2 = TransactionHistory.builder()
        .amount(BigDecimal.TEN)
        .externalId(externalId)
        .type(CommonConstant.DEBIT)
        .username(from.getUsername())
        .build();

    TransactionHistory credit2 = TransactionHistory.builder()
        .amount(BigDecimal.TEN)
        .externalId(externalId)
        .type(CommonConstant.CREDIT)
        .username(to.getUsername())
        .build();
    debit2.setTransactionHistories(credit2);
    credit2.setRelatedTransactionId(debit2);

    transactionHistoryRepository.saveAll(Arrays.asList(debit2, credit2));
  }

  private User setUser(String username){
    return userRepository.save(User.builder().username(username).balance(BigDecimal.TEN).build());
  }

  private String setToken(User user){
    String token = UUID.randomUUID().toString();
    redisService.save(CommonConstant.REDIS_KEY, token, user);
    redisService.save(CommonConstant.REDIS_KEY, user.getUsername(), token);
    return token;
  }
}
