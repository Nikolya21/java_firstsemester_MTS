package decorator;

import com.mipt.nikolyakhachatryan.decorator.CachingDecorator;
import com.mipt.nikolyakhachatryan.decorator.SimpleDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {
  private SimpleDataService baseService;
  private CachingDecorator cachingService;

  @BeforeEach
  void setUp() {
    baseService = new SimpleDataService();
    cachingService = new CachingDecorator(baseService);
  }

  @Test
  void findDataByKey_cachesResult() {
    baseService.saveData("key1", "value1");

    Optional<String> first = cachingService.findDataByKey("key1");
    Optional<String> second = cachingService.findDataByKey("key1");

    assertEquals("value1", first.orElse(null));
    assertEquals("value1", second.orElse(null));

    baseService.saveData("key1", "new_value");

    Optional<String> third = cachingService.findDataByKey("key1");
    assertEquals("value1", third.orElse(null));

    cachingService.saveData("key1", "updated");
    Optional<String> fourth = cachingService.findDataByKey("key1");
    assertEquals("updated", fourth.orElse(null));
  }

  @Test
  void deleteData_clearsCache() {
    cachingService.saveData("key", "data");
    assertEquals("data", cachingService.findDataByKey("key").orElse(null));

    cachingService.deleteData("key");
    assertFalse(cachingService.findDataByKey("key").isPresent());
  }
}