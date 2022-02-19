package com.nemtsov.dmitriy;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.scheduling.support.CronExpression;

public class DatesToCronConverterImpl implements DatesToCronConverter {
  private static final List<String> expressions = new ArrayList<>();

  static {
    File cron = new File("src\\main\\resources\\cron.txt");
    try (BufferedReader br = new BufferedReader(new FileReader(cron))) {
      while (br.ready()) {
        expressions.add(br.readLine());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String convert(List<String> list) throws DatesToCronConvertException {
    if (list == null || list.isEmpty()) {
      throw new DatesToCronConvertException();
    }

    List<LocalDateTime> dates = new ArrayList<>();

    try {
      list.forEach(item -> dates.add(LocalDateTime.parse(item)));
    } catch (DateTimeParseException | NullPointerException e) {
      throw new DatesToCronConvertException();
    }

    if (dates.size() == 1) {
      return String.join(" ",
              String.valueOf(dates.get(0).getSecond()),
              String.valueOf(dates.get(0).getMinute()),
              String.valueOf(dates.get(0).getHour()),
              String.valueOf(dates.get(0).getDayOfMonth()),
              String.valueOf(dates.get(0).getMonthValue()),
              dates.get(0).getDayOfWeek().toString().substring(0, 3));
    }
    return findExpression(dates);
  }

  @Override
  public String getImplementationInfo() {
    return String.join("\n",
            "Credentials: Nemtsov Dmitriy Sergeevich",
            "Class: " + this.getClass().getSimpleName(),
            this.getClass().getPackage().toString(),
            "GitHub: https://github.com/dsnemtsov/cron-converter");
  }

  private String findExpression(List<LocalDateTime> dates) throws DatesToCronConvertException {
    int validDates = 0;

    Collections.sort(dates);

    for (String expression : expressions) {
      List<LocalDateTime> datesForExpression = new ArrayList<>();
      CronExpression cronExpression = CronExpression.parse(expression);

      for (int i = 0; i < dates.size() / 2; i++) {
        datesForExpression.add(dates.get(i));

        for (int j = i + 1; j < dates.size(); j++) {
          datesForExpression.add(cronExpression.next(datesForExpression.get(j - i - 1)));
        }

        for (int j = i; j < dates.size(); j++) {
          if (dates.get(j).equals(datesForExpression.get(j - i))) {
            validDates++;
          }
          if (validDates > dates.size() / 2) {
            return expression;
          }
        }

        datesForExpression.clear();
        validDates = 0;
      }
    }
    throw new DatesToCronConvertException();
  }
}
