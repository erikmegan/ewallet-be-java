package com.flip.assignment.service.impl;

import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.entity.constant.ErrorCategory;
import com.flip.assignment.exception.BusinessException;
import com.flip.assignment.repository.UserRepository;
import com.flip.assignment.service.api.RedisService;
import com.flip.assignment.service.api.UserService;
import com.flip.assignment.service.model.BalanceServiceResponse;
import com.flip.assignment.service.model.RegisterUserRequest;
import com.flip.assignment.service.model.UserServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RedisService redisService;

  @Transactional(rollbackFor = Exception.class)
  public String registerUser(RegisterUserRequest registerUserRequest) {
    User user = userRepository.findByUsernameAndIsDeleted(registerUserRequest.getUsername(), false);
    if (Objects.nonNull(user)) {
      throw BusinessException.builder().errorCode(ErrorCategory.USER_EXIST).build();
    }

    User newUser = User.builder().username(registerUserRequest.getUsername()).build();
    userRepository.save(newUser);

    String token = UUID.randomUUID().toString();
    redisService.save(CommonConstant.REDIS_KEY, token, newUser);
    return token;
  }

  @Transactional
  public UserServiceResponse getUserFromToken(String token) {
    User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY, token);
    if (Objects.isNull(user)) {
      throw BusinessException.builder().errorCode(ErrorCategory.USER_NOT_FOUND).build();
    }
    return buildUserResponse(user);
  }

  private UserServiceResponse buildUserResponse(User user) {
    return UserServiceResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .createdDate(user.getCreatedDate())
        .updatedDate(user.getUpdatedDate())
        .build();
  }

  @Transactional
  public BalanceServiceResponse getBalance(String token) {
    User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY, token);
    if (Objects.isNull(user)) {
      return null;
    }

    return buildBalanceResponse(user);
  }

  private BalanceServiceResponse buildBalanceResponse(User user) {
    return BalanceServiceResponse.builder().balance(user.getBalance()).build();
  }

  public String getTokenFromUsername(String username) {
    String token = (String) redisService.getByKey(CommonConstant.REDIS_KEY, username);
    if (Objects.isNull(token)) {
      String newToken = UUID.randomUUID().toString();
      User user = userRepository.findByUsernameAndIsDeleted(username, false);

      redisService.save(CommonConstant.REDIS_KEY, username, newToken);
      redisService.save(CommonConstant.REDIS_KEY, newToken, user);
      return newToken;
    }
    return token;
  }
}
