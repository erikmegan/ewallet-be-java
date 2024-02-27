package com.flip.assignment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;

@Slf4j
public class JSONHelper {
  private JSONHelper(){}

  private static final ObjectMapper mapper = new ObjectMapper();

  @Nullable
  public static String convertToJson(Object data){
    try {
      if(data instanceof String){
        return (String) data;
      }
      mapper.registerModule(new JavaTimeModule());
      return mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      log.error("Error on convertToJson.", e);
      return null;
    }
  }

  @Nullable
  public static <T> T convertJsonInStringToObject(String jsonInString, Class<T> clazz) {
    try {
      mapper.registerModule(new JavaTimeModule());
      return mapper.readValue(jsonInString, clazz);
    } catch (IOException e) {
      log.error("Error on convertJsonInStringToObject.", e);
      return null;
    }
  }

  @Nullable
  public static <T> T convertJsonInStringToObject(String jsonInString, TypeReference<T> typeRef) {
    try {
      mapper.registerModule(new JavaTimeModule());
      return mapper.readValue(jsonInString, typeRef);
    } catch (IOException e) {
      log.error("Error on convertJsonInStringToObject.", e);
      return null;
    }
  }
}
