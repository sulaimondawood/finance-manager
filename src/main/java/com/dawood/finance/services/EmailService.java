package com.dawood.finance.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String from;

  public void sendSimpleMail(String to, String body, String subject) {

    try {

      SimpleMailMessage mailMessage = new SimpleMailMessage();

      mailMessage.setFrom("finance-manager@gmail.com");
      mailMessage.setSubject(subject);
      mailMessage.setTo(to);
      mailMessage.setText(body);

      javaMailSender.send(mailMessage);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

}
