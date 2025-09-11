package com.dawood.finance.mappers;

import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.entities.User;

public class UserMapper {

  public static RegisterResponseDTO toRegisterResponseDTO(User user) {
    return RegisterResponseDTO.builder()
        .email(user.getEmail())
        .fullname(user.getFullname())
        .id(user.getId())
        .photoUrl(user.getPhotoUrl())
        .build();
  }

}
