package decorator;

import com.mipt.nikolyakhachatryan.decorator.repository.DataService;
import com.mipt.nikolyakhachatryan.decorator.SimpleDataService;
import com.mipt.nikolyakhachatryan.decorator.ValidationDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {
  private final DataService service = new ValidationDecorator(new SimpleDataService());

  @Test
  void findDataByKey_rejectsNullKey() {
    assertThrows(IllegalArgumentException.class, () -> service.findDataByKey(null));
  }

  @Test
  void findDataByKey_rejectsEmptyKey() {
    assertThrows(IllegalArgumentException.class, () -> service.findDataByKey(""));
    assertThrows(IllegalArgumentException.class, () -> service.findDataByKey("   "));
  }

  @Test
  void saveData_rejectsNullKey() {
    assertThrows(IllegalArgumentException.class, () -> service.saveData(null, "data"));
  }

  @Test
  void saveData_rejectsNullData() {
    assertThrows(IllegalArgumentException.class, () -> service.saveData("key", null));
  }

  @Test
  void saveData_acceptsValidInput() {
    assertDoesNotThrow(() -> service.saveData("key", "data"));
  }

  @Test
  void deleteData_rejectsBlankKey() {
    assertThrows(IllegalArgumentException.class, () -> service.deleteData("   "));
  }
}