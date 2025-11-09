package com.mipt.nikolyakhachatryan;
import com.mipt.nikolyakhachatryan.Homework1_8.classes.User;
import com.mipt.nikolyakhachatryan.Homework1_8.classes.ValidationResult;
import com.mipt.nikolyakhachatryan.Homework1_8.classes.Validator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class ValidatorTest {

  @Test
  public void validUserShouldPassValidation() {
    User user = new User();
    user.setName("Alice");
    user.setEmail("alice@example.com");
    user.setAge(30);
  }

  @Test
  public void invalidEmailShouldFail() {
    User user = new User();
    user.setEmail("not-an-email");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("email")));
  }
}