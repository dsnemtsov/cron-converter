package com.nemtsov.dmitriy;

import java.util.Set;
import java.util.stream.Collectors;

public class Cron {
  String cronSeconds;
  String cronMinutes;
  String cronHours;
  String cronDayOfMonth;
  String cronMonths;
  String cronDayOfWeek;

  public Cron(DataSorter dataSorter, int size) {
    cronSeconds = findValue(dataSorter.getSeconds(), size);
    cronMinutes = findValue(dataSorter.getMinutes(), size);
    cronHours = findValue(dataSorter.getHours(), size);
    cronDayOfMonth = findValue(dataSorter.getDaysOfMonth(), size);
    cronMonths = findValue(dataSorter.getMonths(), size);
    cronDayOfWeek = dataSorter.getDayOfWeek();
  }

  public String getCronSeconds() {
    return cronSeconds;
  }

  public String getCronMinutes() {
    return cronMinutes;
  }

  public String getCronHours() {
    return cronHours;
  }

  public String getCronDayOfMonth() {
    return cronDayOfMonth;
  }

  public String getCronMonths() {
    return cronMonths;
  }

  public String getCronDayOfWeek() {
    return cronDayOfWeek;
  }

  private String findValue(Set<Integer> input, int size) {
    String result = "*";

    if (input.size() == 1) {
      result = input.iterator().next().toString();
    } else if (input.size() == size) {
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

  public int getPeriod(Set<Integer> input) {
    Integer[] inputToArray = input.toArray(new Integer[0]);
    int period = inputToArray[1] - inputToArray[0];

    for (int i = 2; i < inputToArray.length; i++) {
      if (inputToArray[i] - inputToArray[i - 1] != period) {
        return -1;
      }
    }
    return period;
  }

  @Override
  public String toString() {
    return String.join(" ", cronSeconds, cronMinutes, cronHours, cronDayOfMonth, cronMonths, cronDayOfWeek);
  }
}
