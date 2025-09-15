package com.dawood.finance.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String from;

  public void sendSimpleMail(String to, String body, String subject) {

    try {

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

      helper.setFrom(from);
      helper.setSubject(subject);
      helper.setTo(to);
      helper.setText(body, true);

      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

}
