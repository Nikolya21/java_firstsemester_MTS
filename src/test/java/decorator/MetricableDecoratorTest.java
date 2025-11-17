package decorator;

import com.mipt.nikolyakhachatryan.decorator.repository.DataService;
import com.mipt.nikolyakhachatryan.decorator.MetricableDecorator;
import com.mipt.nikolyakhachatryan.decorator.SimpleDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private DataService metricService;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    metricService = new MetricableDecorator(new SimpleDataService());
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void allMethods_emitMetricOutput() {
    metricService.saveData("k", "v");
    metricService.findDataByKey("k");
    metricService.deleteData("k");

    String output = outContent.toString();
    long count = output.lines()
      .filter(line -> line.startsWith("Метод выполнялся: PT"))
      .count();
    assertEquals(3, count, "Должно быть 3 записи метрик");
  }

  @Test
  void metricOutput_containsDurationPattern() {
    metricService.saveData("x", "y");
    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: PT"), "Должен содержать 'PT' (ISO-8601 duration)");
  }
}