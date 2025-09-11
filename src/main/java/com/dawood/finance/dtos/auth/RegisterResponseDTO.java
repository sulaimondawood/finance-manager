package com.dawood.finance.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponseDTO {

  private Long id;

  private String fullname;

  private String email;

  private String photoUrl;

}
