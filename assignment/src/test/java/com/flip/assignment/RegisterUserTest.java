package com.flip.assignment;

import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.repository.UserRepository;
import com.flip.assignment.service.api.RedisService;
import com.flip.assignment.util.JSONHelper;
import com.flip.assignment.web.constant.ApiPath;
import com.flip.assignment.web.model.UserApiRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RegisterUserTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RedisService redisService;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @AfterEach
  public void clear() {
    redisTemplate.delete("*");
    userRepository.deleteAll();
  }

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void givenValidPayload_returnSuccess() throws Exception {
    UserApiRequest userApiRequest = UserApiRequest.builder().username("flip-test").build();
    mockMvc.perform(
            post(ApiPath.USER + ApiPath.CREATE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(userApiRequest))))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void givenNullUsername_returnBadRequest() throws Exception {
    UserApiRequest userApiRequest = UserApiRequest.builder().build();
    mockMvc.perform(
            post(ApiPath.USER + ApiPath.CREATE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(userApiRequest))))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void givenExistUsername_returnBadRequest() throws Exception {
    User user = setUser();
    UserApiRequest userApiRequest = UserApiRequest.builder().username(user.getUsername()).build();
    mockMvc.perform(
            post(ApiPath.USER + ApiPath.CREATE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JSONHelper.convertToJson(userApiRequest))))
        .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  public void givenExistUsername_returnValidBalance() throws Exception {

    User user = setUser();
    String token = setToken(user);
    mockMvc.perform(
            get(ApiPath.USER + ApiPath.BALANCE_READ)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstant.AUTHORIZED_HEADER, token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.balance").value(BigDecimal.TEN))
        .andReturn();
  }

  private String setToken(User user){
    String token = UUID.randomUUID().toString();
    redisService.save(CommonConstant.REDIS_KEY, token, user);
    redisService.save(CommonConstant.REDIS_KEY, user.getUsername(), token);
    return token;
  }

  private User setUser(){
    return userRepository.save(User.builder().username("flip-test").balance(BigDecimal.TEN).build());
  }
}
