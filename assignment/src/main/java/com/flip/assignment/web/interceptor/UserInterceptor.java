package com.flip.assignment.web.interceptor;

import com.flip.assignment.entity.User;
import com.flip.assignment.entity.constant.CommonConstant;
import com.flip.assignment.entity.constant.ErrorCategory;
import com.flip.assignment.exception.BusinessException;
import com.flip.assignment.exception.UnauthorizedException;
import com.flip.assignment.service.api.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

  private final RedisService redisService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization");
    if (StringUtils.isNotBlank(token)) {
      User user = (User) redisService.getByKey(CommonConstant.REDIS_KEY, token);

      if (Objects.isNull(user)) {
        throw BusinessException.builder().errorCode(ErrorCategory.UNAUTHORIZED).build();
      }
    }
    return true;
  }

}
