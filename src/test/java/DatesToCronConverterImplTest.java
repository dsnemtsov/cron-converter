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
  void convert_shouldConvert_whenInputCorrect() throws DatesToCronConvertException {
    List<String> list = Stream.of(
            "2022-01-24T19:53:00",
            "2022-01-24T19:54:00",
            "2022-01-24T19:55:00",
            "2022-01-24T19:56:00",
            "2022-01-24T19:57:00",
            "2022-01-24T19:58:00",
            "2022-01-24T19:59:00",
            "2022-01-24T20:00:00",
            "2022-01-24T20:01:00",
            "2022-01-24T20:02:00"
    ).collect(Collectors.toList());

    assertEquals("Converted", converter.convert(list));
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
}
