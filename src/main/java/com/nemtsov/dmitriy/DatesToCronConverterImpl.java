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
    return findCronExpression(dates);
  }

  @Override
  public String getImplementationInfo() {
    return String.join("\n",
            "Credentials: Nemtsov Dmitriy Sergeevich",
            "Class: " + this.getClass().getSimpleName(),
            this.getClass().getPackage().toString(),
            "GitHub: https://github.com/dsnemtsov/cron-converter");
  }

  private String findCronExpression(List<LocalDateTime> dates) throws DatesToCronConvertException {

    Collections.sort(dates);

    DataSorter dataSorter = new DataSorter(dates);

    Cron baseCron = new Cron(dataSorter, dates.size());

    String result = baseCron.toString();

    if (checkResult(result, dates)) {

      return getImprovedResult(baseCron, dates);
    } else {
      throw new DatesToCronConvertException();
    }
  }

  private String getImprovedResult(Cron cron, List<LocalDateTime> dates) {
    String improvedResult = cron.toString();
    String[] resultToArray = cron.toString().split(" ");

    for (String s : resultToArray) {
      if (s.contains(",")) {
        Integer[] numbers = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        Set<Integer> partOfTimes = new TreeSet<>(Arrays.asList(numbers));
        int period = cron.getPeriod(partOfTimes);
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
}
