import com.digdes.school.DatesToCronConvertException;
import com.nemtsov.dmitriy.DatesToCronConverterImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatesToCronConverterImplTest {
  private DatesToCronConverterImpl converter;

  @BeforeEach
  void setUp() {
    converter = new DatesToCronConverterImpl();
  }

  @Test
  void convert_shouldThrowException_whenInputListIsNull() {
    assertThrows(DatesToCronConvertException.class, () -> converter.convert(null));
  }

  @Test
  void convert_shouldThrowException_whenInputListIsEmpty() {
    assertThrows(DatesToCronConvertException.class, () -> converter.convert(new ArrayList<>()));
  }

  @Test
  void convert_shouldConvert_whenInputCorrect() {
    List<String> list1 = Stream.of(
            "2022-01-24T19:53:00",
            "2022-01-24T19:55:00",
            "2022-01-24T19:54:00",
            "2022-01-24T20:02:00",
            "2022-01-24T19:56:00",
            "2022-01-24T19:57:00",
            "2022-01-24T19:58:00",
            "2022-01-24T19:59:00",
            "2022-01-24T20:00:00",
            "2022-01-24T20:01:00"
    ).collect(Collectors.toList());

    List<String> list2 = Stream.of(
            "2022-01-25T08:00:00",
            "2022-01-25T08:30:00",
            "2022-01-25T09:00:00",
            "2022-01-25T09:30:00",
            "2022-01-26T08:00:00",
            "2022-01-26T08:30:00",
            "2022-01-26T09:00:00",
            "2022-01-26T09:30:00"
    ).collect(Collectors.toList());

    List<String> list3 = Stream.of(
            "2022-01-01T00:00",
            "2022-02-01T00:00",
            "2022-03-01T00:00",
            "2022-04-01T00:00",
            "2022-05-01T00:00",
            "2022-06-01T00:00",
            "2022-07-01T00:00",
            "2022-08-01T00:00",
            "2022-09-01T00:00",
            "2022-10-01T00:00"
    ).collect(Collectors.toList());

    List<String> list4 = Stream.of(
            "2022-01-01T00:00",
            "2022-01-01T00:00:10",
            "2022-01-01T00:00:20",
            "2022-01-01T00:00:30",
            "2022-01-01T00:00:40",
            "2022-01-01T00:00:50",
            "2022-01-01T00:01:00",
            "2022-01-01T00:01:10",
            "2022-01-01T00:01:20",
            "2022-01-01T00:01:30"
    ).collect(Collectors.toList());

    assertAll(
            () -> assertEquals("0 * * 24 1 MON", converter.convert(list1)),
            () -> assertEquals("0 */30 8,9 * 1 *", converter.convert(list2)),
            () -> assertEquals("0 0 0 1 * *", converter.convert(list3)),
            () -> assertEquals("*/10 * 0 1 1 SAT", converter.convert(list4))
    );
  }

  @Test
  void convert_shouldThrowException_whenCronExpressionNotFound() {
    List<String> list = Stream.of(
            "2022-01-24T19:53:00",
            "2022-02-24T19:55:00",
            "2022-03-24T19:54:00",
            "2022-04-24T20:02:00",
            "2022-01-24T19:56:00",
            "2022-01-24T10:57:00",
            "2022-01-24T19:58:00",
            "2022-01-24T13:59:00",
            "2022-01-24T20:00:00",
            "2022-01-24T21:01:00"
    ).collect(Collectors.toList());
    assertThrows(DatesToCronConvertException.class, () -> converter.convert(list));
  }

  @Test
  void convert_shouldThrowException_whenInputIsNotCorrect() {
    assertAll(
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList(null))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList(" "))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-24 19:55:00"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-99-24T19:56:00"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-99T19:57:00"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-24T99:58:00"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-24T19:99:00"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-24T20:00:99"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("2022-01-24T"))),
            () -> assertThrows(DatesToCronConvertException.class, () -> converter.convert(Collections.singletonList("01-24T20:02:00")))
    );
  }

  @Test
  void convert_shouldConvert_whenInputIsOneDate() {
    assertAll(
            () -> assertEquals("0 55 19 3 1 MON", converter.convert(Collections.singletonList("2022-01-03T19:55:00"))),
            () -> assertEquals("0 55 19 4 1 TUE", converter.convert(Collections.singletonList("2022-01-04T19:55:00"))),
            () -> assertEquals("0 55 19 5 1 WED", converter.convert(Collections.singletonList("2022-01-05T19:55:00"))),
            () -> assertEquals("0 55 19 6 1 THU", converter.convert(Collections.singletonList("2022-01-06T19:55:00"))),
            () -> assertEquals("0 55 19 7 1 FRI", converter.convert(Collections.singletonList("2022-01-07T19:55:00"))),
            () -> assertEquals("0 55 19 8 1 SAT", converter.convert(Collections.singletonList("2022-01-08T19:55:00"))),
            () -> assertEquals("0 55 19 9 1 SUN", converter.convert(Collections.singletonList("2022-01-09T19:55:00")))
    );
  }

  @Test
  void getImplementationInfo_shouldGetInfo() {
    assertEquals("Credentials: Nemtsov Dmitriy Sergeevich\n" +
            "Class: DatesToCronConverterImpl\n" +
            "package com.nemtsov.dmitriy\n" +
            "GitHub: https://github.com/dsnemtsov/cron-converter", converter.getImplementationInfo());
  }
}
