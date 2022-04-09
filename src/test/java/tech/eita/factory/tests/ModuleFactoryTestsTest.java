package tech.eita.factory.tests;

import static org.junit.Assert.assertNotNull;

import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;
import org.junit.Test;

public class ModuleFactoryTestsTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    ModuleFactory factory = ModuleFactoryTests.getTestsFactory();
    assertNotNull(factory);
  }
}
