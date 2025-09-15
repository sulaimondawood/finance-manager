package com.dawood.finance.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class ApiResponse<T> {

  private boolean success;

  private String message;

  private T data;

  @JsonInclude(Include.NON_NULL)
  private ApiMeta meta;

  public ApiResponse(String message, T data, boolean success, ApiMeta meta) {
    this.data = data;
    this.message = message;
    this.success = success;
    this.meta = meta;
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<T>(message, data, true, null);
  }

  public static <T> ApiResponse<T> success(String message, T data, ApiMeta meta) {
    return new ApiResponse<T>(message, data, true, meta);
  }

  public static <T> ApiResponse<T> success(String message) {
    return new ApiResponse<T>(message, null, true, null);
  }

}
