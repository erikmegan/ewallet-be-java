package com.flip.assignment.service.api;

public interface RedisService {
  Object getByKey(String key, String hash);

  void save(String key, String hash, Object object);
}
