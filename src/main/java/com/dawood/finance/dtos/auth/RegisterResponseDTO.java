package com.dawood.finance.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
// @AllArgsConstructor
// @NoArgsConstructor
public class RegisterResponseDTO {

  private Long id;

  private String fullname;

  private String email;

  private String photoUrl;

}
