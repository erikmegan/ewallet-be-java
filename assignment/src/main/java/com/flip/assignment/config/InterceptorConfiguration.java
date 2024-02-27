package com.flip.assignment.config;

import com.flip.assignment.web.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
@RequiredArgsConstructor
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

  private final UserInterceptor userInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userInterceptor);
  }
}
