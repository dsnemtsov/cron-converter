package com.nemtsov.dmitriy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DataSorter {
  Set<Integer> seconds = new TreeSet<>();
  Set<Integer> minutes = new TreeSet<>();
  Set<Integer> hours = new TreeSet<>();
  Set<Integer> daysOfMonth = new TreeSet<>();
  Set<Integer> months = new TreeSet<>();
  String dayOfWeek;

  public DataSorter(List<LocalDateTime> dates) {
    dates.forEach(date -> {
      seconds.add(date.getSecond());
      minutes.add(date.getMinute());
      hours.add(date.getHour());
      daysOfMonth.add(date.getDayOfMonth());
      months.add(date.getMonthValue());
      dayOfWeek = getDayOfWeek(dates);
    });
  }

  public Set<Integer> getSeconds() {
    return seconds;
  }

  public Set<Integer> getMinutes() {
    return minutes;
  }

  public Set<Integer> getHours() {
    return hours;
  }

  public Set<Integer> getDaysOfMonth() {
    return daysOfMonth;
  }

  public Set<Integer> getMonths() {
    return months;
  }

  private String getDayOfWeek(List<LocalDateTime> dates) {
    for (int i = 1; i < dates.size(); i++) {
      if (!dates.get(i).getDayOfWeek().equals(dates.get(0).getDayOfWeek())) {
        return "*";
      }
    }
    return dates.get(0).getDayOfWeek().toString().substring(0, 3);
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }
}
