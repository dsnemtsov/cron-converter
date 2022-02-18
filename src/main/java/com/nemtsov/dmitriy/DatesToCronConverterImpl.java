package com.nemtsov.dmitriy;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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

    return "Converted";
  }

  @Override
  public String getImplementationInfo() {
    return null;
  }
}
