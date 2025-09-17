package com.dawood.finance.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.finance.dtos.ApiResponse;

import com.dawood.finance.services.OverviewService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/overview")
@RequiredArgsConstructor
public class OverviewController {

  private final OverviewService overviewService;

  @GetMapping
  public ResponseEntity<ApiResponse<Map<String, Object>>> getOverview() {
    return ResponseEntity.ok().body(ApiResponse.success(null, overviewService.getTransactionOverview()));
  }

}
