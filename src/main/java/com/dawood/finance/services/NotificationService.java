package com.dawood.finance.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.Income;
import com.dawood.finance.entities.User;
import com.dawood.finance.repositories.ExpenseRepository;
import com.dawood.finance.repositories.IncomeRepository;
import com.dawood.finance.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final EmailService emailService;

  private final UserRepository userRepository;

  private final TemplateEngine templateEngine;

  private final OverviewService overviewService;

  private final ExpenseRepository expenseRepository;

  private final IncomeRepository incomeRepository;

  @Value("${app.client-url}")
  String frontendUrl;

  @Scheduled(cron = "0 0 8 * * *")
  public void sendReminder() {

    List<User> users = userRepository.findAll();

    for (User user : users) {
      String bodyContent = "Hi " + user.getFullname() + ", <br/><br/>" +
          "This is a reminder to add your income and expenses for today in Finance Manager.<br/><br/>" +
          "<a href=" + frontendUrl + " style='color:blue; text-decoration:none;'>Go to Finance Manager</a><br/><br/>"
          +
          "Best regards,<br/>" +
          "Finance Manager Team";

      emailService.sendSimpleMail(user.getEmail(), bodyContent, "Add your expenses and incomes today!");
    }

  }

  @Scheduled(cron = "0 0 20 * * *")
  public void sendDailyExpenseSummary() {

    Pageable pageable = PageRequest.of(0, 50);

    List<User> users = userRepository.findAll();

    LocalDate now = LocalDate.now();

    for (User user : users) {

      Map<String, Object> overview = overviewService.getTransactionOverview(user);
      List<Expense> todayExpenses = expenseRepository.findByUserAndDateBetween(user, now, now, pageable)
          .getContent();

      List<Income> todayIncomes = incomeRepository.findByUserAndDateBetween(user,
          now, now, pageable)
          .getContent();

      Context ctx = new Context();
      ctx.setVariable("totalBalance", overview.get("totalBalance"));
      ctx.setVariable("totalIncome", overview.get("totalIncome"));
      ctx.setVariable("totalExpense", overview.get("totalExpense"));

      if (!todayExpenses.isEmpty() || !todayIncomes.isEmpty()) {
        ctx.setVariable("expenses", todayExpenses);
        ctx.setVariable("incomes", todayIncomes);
        String html = templateEngine.process("daily-summary", ctx);
        emailService.sendSimpleMail(user.getEmail(), html, "Your Daily Finance Summary");
      }

    }

  }

}
