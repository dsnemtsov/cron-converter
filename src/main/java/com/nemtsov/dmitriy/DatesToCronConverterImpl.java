package com.nemtsov.dmitriy;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.scheduling.support.CronExpression;

public class DatesToCronConverterImpl implements DatesToCronConverter {

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

    Collections.sort(dates);

    Set<Integer> seconds = new TreeSet<>();
    Set<Integer> minutes = new TreeSet<>();
    Set<Integer> hours = new TreeSet<>();
    Set<Integer> daysOfMonth = new TreeSet<>();
    Set<Integer> months = new TreeSet<>();

    String cronSeconds;
    String cronMinutes;
    String cronHours;
    String cronDaysOfMonth;
    String cronMonths;
    String cronDayOfWeek;

    dates.forEach(date -> {
      seconds.add(date.getSecond());
      minutes.add(date.getMinute());
      hours.add(date.getHour());
      daysOfMonth.add(date.getDayOfMonth());
      months.add(date.getMonthValue());
    });

    cronSeconds = findValue(seconds, dates);
    cronMinutes = findValue(minutes, dates);
    cronHours = findValue(hours, dates);
    cronDaysOfMonth = findValue(daysOfMonth, dates);
    cronMonths = findValue(months, dates);
    cronDayOfWeek = getDayOfWeek(dates);

    String result = String.join(" ", cronSeconds, cronMinutes, cronHours, cronDaysOfMonth, cronMonths, cronDayOfWeek);

    if (checkResult(result, dates)) {

      return getImprovedResult(result, dates);
    } else {
      throw new DatesToCronConvertException();
    }
  }

  private String getImprovedResult(String result, List<LocalDateTime> dates) {
    String improvedResult = result;
    String[] resultToArray = result.split(" ");

    for (String s : resultToArray) {
      if (s.contains(",")) {
        Integer[] numbers = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        Set<Integer> partOfTimes = new TreeSet<>(Arrays.asList(numbers));
        int period = getPeriod(partOfTimes);
        if (period != -1) {
          String possibleResult;
          if (period == 1) {
            possibleResult = improvedResult.replace(s, "*");
          } else {
            possibleResult = improvedResult.replace(s, "*/" + period);
          }
          if (checkResult(possibleResult, dates)) {
            improvedResult = possibleResult;
          }
        }
      }
    }
    return improvedResult;
  }

  private boolean checkResult(String result, List<LocalDateTime> dates) {
    CronExpression cronExpression = CronExpression.parse(result);
    List<LocalDateTime> cronResult = new ArrayList<>();

    cronResult.add(dates.get(0));

    for (int i = 1; i < dates.size(); i++) {
      cronResult.add(cronExpression.next(cronResult.get(i - 1)));
    }
    return cronResult.equals(dates);
  }

  private String getDayOfWeek(List<LocalDateTime> dates) {
    for (int i = 1; i < dates.size(); i++) {
      if (!dates.get(i).getDayOfWeek().equals(dates.get(0).getDayOfWeek())) {
        return "*";
      }
    }
    return dates.get(0).getDayOfWeek().toString().substring(0, 3);
  }

  private String findValue(Set<Integer> input, List<LocalDateTime> dates) {
    String result = "*";

    if (input.size() == 1) {
      result = input.iterator().next().toString();
    } else if (input.size() == dates.size()) {
      int period = getPeriod(input);
      if (period != -1) {
        if (period == 1) {
          result = "*";
        } else {
          result = "*/" + period;
        }
      }
    } else {
      result = input.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    return result;
  }

  private int getPeriod(Set<Integer> input) {
    Integer[] inputToArray = input.toArray(new Integer[0]);
    int period = inputToArray[1] - inputToArray[0];

    for (int i = 2; i < inputToArray.length; i++) {
      if (inputToArray[i] - inputToArray[i - 1] != period) {
        return -1;
      }
    }
    return period;
  }
}
