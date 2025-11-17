package decorator;

import com.mipt.nikolyakhachatryan.decorator.repository.DataService;
import com.mipt.nikolyakhachatryan.decorator.LoggingDecorator;
import com.mipt.nikolyakhachatryan.decorator.SimpleDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private DataService loggingService;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    loggingService = new LoggingDecorator(new SimpleDataService());
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void saveData_logsCorrectMessage() {
    loggingService.saveData("myKey", "myData");
    String output = outContent.toString();
    assertTrue(output.contains("[LOG] saveData: key=myKey, data=myData"));
  }

  @Test
  void findDataByKey_logsKey() {
    loggingService.findDataByKey("testKey");
    String output = outContent.toString();
    assertTrue(output.contains("[LOG] findDataByKey: testKey"));
  }

  @Test
  void deleteData_logsKey() {
    loggingService.deleteData("delKey");
    String output = outContent.toString();
    assertTrue(output.contains("[LOG] deleteData: delKey"));
  }
}